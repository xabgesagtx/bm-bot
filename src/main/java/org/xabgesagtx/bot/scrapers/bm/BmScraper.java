package org.xabgesagtx.bot.scrapers.bm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.xabgesagtx.bot.config.MainConfig;
import org.xabgesagtx.bot.scrapers.bm.model.Event;

@Component
public class BmScraper {
	
	@Autowired
	private MainConfig config;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private IdFixer fixer;
	
	public List<Event> getEvents() {
		ResponseEntity<List<Event>> entity = restTemplate.exchange(config.scrapers.bm.url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Event>>() {});
		if (entity.getStatusCode().is2xxSuccessful()) {
			List<Event> events = entity.getBody();
			fixer.fixIds(events);
			events.parallelStream().forEach(event -> event.init());
			return events;
		} else {
			return new ArrayList<>();
		}
	}

}
