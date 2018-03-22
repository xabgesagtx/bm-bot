package org.xabgesagtx.bot.main.commands;

import static org.xabgesagtx.bot.main.CommandConstants.*;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResult;
import org.xabgesagtx.bot.scrapers.bm.model.Event;

import com.google.common.collect.Lists;

@Component
public class EventDetailsCommand extends AbstractCommand {
	
	private static final Logger logger = LoggerFactory.getLogger(LoggerFactory.class);
	
	private static final Pattern DETAILS_PATTERN = Pattern.compile(PREFIX + DETAILS + "(\\S+)");
	
	@Override
	public String getName() {
		return "e";
	}
	
	private Optional<String> getId(String text) {
		Optional<String> result = Optional.empty();
		Matcher matcher = DETAILS_PATTERN.matcher(text);
		if (matcher.matches()) {
			result = Optional.of(matcher.group(1));
		}
		return result;
	}
	
	@Override
	public String getDescription() {
		return "Details zu einem Event";
	}
	
	@Override
	public Optional<SendMessage> toResponse(String text, String chatId) {
		Optional<SendMessage> response = Optional.empty();
		Optional<String> id = getId(text);
		if (id.isPresent()) {
			response = Optional.of(getMessage(chatId, createMessageText(id.get())));
		}
		return response;
	}


	private String createMessageText(String id) {
		Optional<Event> event = facade.getEvent(id);
		if (!event.isPresent()) {
			return "Sorry, konnte keinen entsprechenden Termin finden";
		} else {
			return createEventDetails(event.get(), false);
		}	
	}
	
	@Override
	public List<InlineQueryResult> toInlineResult(String text) {
		return Lists.newArrayList();
	}

}
