package org.xabgesagtx.bot.main.commands;

import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.xabgesagtx.bot.main.CommandConstants;
import org.xabgesagtx.bot.scrapers.bm.model.Event;

@Component
public class TodayCommand extends AbstractNoArgCommandWithInline {
	
	@Override
	public String getDescription() {
		return "Die Termine von heute";
	}
	
	@Override
	public String getName() {
		return CommandConstants.TODAY;
	}
	
	@Override
	protected Stream<Event> getEvents(String text) {
		return facade.getEventsForToday().stream();
	}
	
	@Override
	public String createMessageText() {
		return createResponseTextEventList(facade.getEventsForToday());
	}

	@Override
	public boolean matches(String text) {
		return StringUtils.equals(text, getPrefixedName()) || Pattern.compile("^(heute|today|jetzt|gerade)$", Pattern.CASE_INSENSITIVE).matcher(text).matches();
	}
	
}
