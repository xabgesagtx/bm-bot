package org.xabgesagtx.bot.main.commands;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResult;
import org.xabgesagtx.bot.main.CommandConstants;
import org.xabgesagtx.bot.main.DateUtils;

@Component
public class WeekdayCommand extends AbstractCommand {
	
	@Override
	public String getName() {
		return CommandConstants.WEEKDAY;
	}

	@Override
	public String getDescription() {
		return "Freier Text zur Beschreibung eines Tages";
	}
	
	@Override
	public Optional<SendMessage> toResponse(String text, String chatId) {
		Optional<SendMessage> response = Optional.empty();
		Optional<LocalDate> localDate = DateUtils.getLocalDateFromDayOfWeekString(getContent(text));
		if (localDate.isPresent()) {
			response = Optional.of(getMessage(chatId, createMessageText(localDate.get())));
		}
		return response;
	}

	public String createMessageText(LocalDate date) {
		return String.format("%s\n\n%s", createBoldText(DATE_FORMATTER.format(date)), createResponseTextEventList(facade.getEventsForDate(date)));
	}
	
	
	@Override
	public List<InlineQueryResult> toInlineResult(String text) {
		List<InlineQueryResult> result = Lists.newArrayList();
		Optional<LocalDate> localDate = DateUtils.getLocalDateFromDayOfWeekString(getContent(text));
		if (localDate.isPresent()) {
			result = facade.getEventsForDate(localDate.get()).stream().map(this::toArticle).collect(Collectors.toList());
		}
		return result;
	}
}
