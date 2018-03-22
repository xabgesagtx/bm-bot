package org.xabgesagtx.bot.main.commands;

import static org.xabgesagtx.bot.main.DateUtils.*;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResult;
import org.xabgesagtx.bot.main.CommandConstants;

@Component
public class WeekendCommand extends AbstractNoArgCommand {

	@Override
	public String getName() {
		return CommandConstants.WEEKEND;
	}

	@Override
	public String getDescription() {
		return "Termine vom anliegenden oder aktuellem Wochenende";
	}

	@Override
	public boolean matches(String text) {
		return StringUtils.equals(text, getPrefixedName()) || Pattern.compile("^(wochenende|weekend)$",Pattern.CASE_INSENSITIVE).matcher(text).matches(); 
	}
	
	@Override
	public String createMessageText() {
		return getWeekend().stream().map(this::createTextForDay).collect(Collectors.joining("\n\n"));
	}
	
	String createTextForDay(LocalDate day) {
		String date = DATE_FORMATTER.format(day);
		String eventsText = createResponseTextEventList(facade.getEventsForDate(day));
		return createBoldText(date) + "\n\n" + eventsText;
	}
	
	@Override
	public List<InlineQueryResult> toInlineResult(String text) {
		List<InlineQueryResult> result = Lists.newArrayList();
		if (matches(text)) {
			result = getWeekend().stream().flatMap(day -> facade.getEventsForDate(day).stream()).map(event -> toArticle(event)).collect(Collectors.toList());
		}
		return result;
	}

}
