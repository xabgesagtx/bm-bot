package org.xabgesagtx.bot.persistence;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.xabgesagtx.bot.scrapers.bm.model.Event;

public interface EventRepository extends CrudRepository<Event, Integer> {

	List<Event> findByDateOrderByTimeStart(LocalDate date);
	
	Event findByCleanId(String cleanId);
	
}
