package org.xabgesagtx.bot.persistence;

import org.springframework.data.repository.CrudRepository;
import org.xabgesagtx.bot.scrapers.bm.model.Event;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends CrudRepository<Event, Integer> {

	List<Event> findByDateOrderByTimeStart(LocalDate date);

	Optional<Event> findByCleanId(String cleanId);
	
}
