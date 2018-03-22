package org.xabgesagtx.bot.main.commands;

import java.util.List;
import java.util.Optional;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResult;

import com.google.common.collect.Lists;

public abstract class AbstractNoArgCommand extends AbstractCommand {

	@Override
	public Optional<SendMessage> toResponse(String text, String chatId) {
		Optional<SendMessage> response = Optional.empty();
		if (matches(text)) {
			response = Optional.of(getMessage(chatId, createMessageText()));
		}
		return response;
	}
	
	@Override
	public List<InlineQueryResult> toInlineResult(String text) {
		return Lists.newArrayList();
	}
	
	public abstract boolean matches(String text);
	
	public abstract String createMessageText();

}
