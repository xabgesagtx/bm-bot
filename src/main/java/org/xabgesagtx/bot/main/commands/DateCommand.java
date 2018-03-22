package org.xabgesagtx.bot.main.commands;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResult;
import org.xabgesagtx.bot.main.CommandConstants;
import org.xabgesagtx.bot.main.DateUtils;

import com.google.common.collect.Lists;

@Component
public class DateCommand extends AbstractCommand {

	@Override
	public String getName() {
		return CommandConstants.DATE;
	}

	@Override
	public String getDescription() {
		return "Ein genaues Datum wie z.B. 28.6.";
	}


	@Override
	public Optional<SendMessage> toResponse(String text, String chatId) {
		Optional<SendMessage> response = Optional.empty();
		Optional<LocalDate> date = DateUtils.getLocalDateFromString(text);
		if (date.isPresent()) {
			String responseText = createBoldText(DATE_FORMATTER.format(date.get())) + "\n\n" + createResponseTextEventList(facade.getEventsForDate(date.get()));
			response = Optional.of(getMessage(chatId, responseText));
		}
		return response;
	}
	
	@Override
	public List<InlineQueryResult> toInlineResult(String text) {
		List<InlineQueryResult> result = Lists.newArrayList();
		Optional<LocalDate> date = DateUtils.getLocalDateFromString(text);
		if (date.isPresent()) {
			result = facade.getEventsForDate(date.get()).stream().map(event -> toArticle(event)).collect(Collectors.toList());
		}
		return result;
	}
	
	
	
}
