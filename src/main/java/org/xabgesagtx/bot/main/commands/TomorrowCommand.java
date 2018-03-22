package org.xabgesagtx.bot.main.commands;

import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.xabgesagtx.bot.main.CommandConstants;
import org.xabgesagtx.bot.scrapers.bm.model.Event;

@Component
public class TomorrowCommand extends AbstractNoArgCommandWithInline {

	@Override
	public String getName() {
		return CommandConstants.TOMORROW;
	}

	@Override
	public String getDescription() {
		return "Die Events von morgen";
	}
	
	@Override
	protected Stream<Event> getEvents(String text) {
		return facade.getEventsForTomorrow().stream();
	}
	
	@Override
	public boolean matches(String text) {
		return StringUtils.equals(text, getPrefixedName()) || Pattern.compile("^(tomorr?ow|morgen)$", Pattern.CASE_INSENSITIVE).matcher(text).matches();
	}
	
	@Override
	public String createMessageText() {
		return createResponseTextEventList(facade.getEventsForTomorrow());
	}

}
