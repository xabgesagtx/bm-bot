package org.xabgesagtx.bot.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xabgesagtx.bot.persistence.EventRepository;
import org.xabgesagtx.bot.scrapers.bm.model.Event;

@RestController
@RequestMapping("/rest_debug")
public class DebugController {
	
	@Autowired
	private EventRepository repo;
	
	@RequestMapping(value = "events", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Event> getAllEvents() {
		List<Event> result = new ArrayList<>();
		for (Event event : repo.findAll()) {
			result.add(event);
		}
		return result;
	}

}
