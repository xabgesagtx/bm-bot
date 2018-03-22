package org.xabgesagtx.bot.persistence;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.xabgesagtx.bot.scrapers.bm.BmScraper;
import org.xabgesagtx.bot.scrapers.bm.model.Event;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Slf4j
public class BmUpdater {
	
	@Autowired
	private EventRepository repo;
	
	@Autowired
	private BmScraper scraper;
	
	@Scheduled(cron = "0 0 0/1 * * ?")
	@Transactional
	public void update() {
		try {
			log.info("Running update of events");
			List<Event> events = scraper.getEvents();
			log.info("Got {} events", events.size());
			if (!events.isEmpty()) {
				repo.deleteAll();
				repo.saveAll(events);
			}
		} catch (Exception e) {
			log.error("Failed to update events due to exception {}", e.getMessage());
		}
	}
	
	@PostConstruct
	@Transactional
	public void startUp() {
		update();
	}

}
