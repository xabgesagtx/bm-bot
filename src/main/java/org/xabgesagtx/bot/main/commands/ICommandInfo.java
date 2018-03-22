package org.xabgesagtx.bot.main.commands;

import java.util.List;
import java.util.Optional;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResult;

public interface ICommandInfo {
	
	String getName();
	String getDescription();
	Optional<SendMessage> toResponse(String text, String chatId);
	List<InlineQueryResult> toInlineResult(String text);

}
