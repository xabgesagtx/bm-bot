package org.xabgesagtx.bot.scrapers.bm;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;
import org.xabgesagtx.bot.scrapers.bm.model.Event;

import com.google.common.collect.Lists;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
public class IdFixer {
	
	@RequiredArgsConstructor
	class IdProvider {
		@NonNull
		private final Map<Integer,Long> occurences;
		private final List<String> providedIds = Lists.newArrayList();
		private final List<String> alphabet = IntStream.rangeClosed('A', 'Z').mapToObj(c -> String.valueOf((char) c)).collect(toList());
		
		public void setIdFor(Event event) {
			String id;
			if (occurences.get(event.getId()) > 1) {
				id = getFixedIdFor(event.getId().toString());
			} else {
				id = event.getId().toString();
			}
			providedIds.add(id);
			event.setCleanId(id);
			event.setId(null);
		}
		
		public String getFixedIdFor(String id) {
			Optional<String> firstId = alphabet.stream().map(letter -> id + letter).filter(cleanedId -> !providedIds.contains(cleanedId)).findFirst();
			return firstId.orElseGet(() -> getFixedIdFor(id + alphabet.get(0)));
		}
		
		
	}
	
	public void fixIds(List<Event> events) {
		Map<Integer, Long> occurences = events.stream().collect(groupingBy(event -> event.getId(), counting()));
		IdProvider idProvider = new IdProvider(occurences);
		events.stream().forEach(idProvider::setIdFor);
				
	}

}
