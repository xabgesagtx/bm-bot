package org.xabgesagtx.bot.persistence;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xabgesagtx.bot.scrapers.bm.model.Event;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.xabgesagtx.bot.main.DateUtils.getToday;
import static org.xabgesagtx.bot.main.DateUtils.getTomorrow;

@Component
@Slf4j
public class EventFacade {
	
	@Autowired
	private EventRepository repo;
	
	public List<Event> getEventsForToday() {
		return getEventsForDate(getToday());
	}
	
	public List<Event> getEventsForTomorrow() {
		return getEventsForDate(getTomorrow());
	}
	
	public List<Event> getEventsForDate(LocalDate date) {
		return repo.findByDateOrderByTimeStart(date);
	}
	
	public Optional<Event> getEvent(String cleanId) {
		Optional<Event> result = Optional.empty();
		try {
			result = Optional.ofNullable(repo.findByCleanId(cleanId));
		} catch (NumberFormatException e) {
			log.warn("Failed to get event because wrong cleanId: \"{}\"", cleanId);
		}
		return result;
	}
	
}
