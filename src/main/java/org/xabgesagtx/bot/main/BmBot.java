package org.xabgesagtx.bot.main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.xabgesagtx.bot.config.MainConfig;

@Component
@Slf4j
public class BmBot extends TelegramLongPollingBot {
	
	@Autowired
	private MainConfig config;
	
	@Autowired
	private MessageHandler handler;

	@Override
	public String getBotUsername() {
		return config.botConfig.name;
	}

	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage()) {
			log.debug("Got message: \"{}\"", update.getMessage());
			SendMessage response = handler.handleMessage(update.getMessage());
			try {
				log.info("Sending response to {}", update.getMessage().getChatId());
				sendApiMethod(response);
			} catch (TelegramApiException e) {
				log.error("Error sending message \"{}\": {}", response.getText(), e.getMessage());
			}
		} else if (update.hasInlineQuery()) {
			AnswerInlineQuery answer = handler.handleInlineQuery(update.getInlineQuery());
			try {
				sendApiMethod(answer);
			} catch (TelegramApiException e) {
				log.error("Error sending inline result \"{}\": {}", answer, e.getMessage());
			}
		}
	}

	@Override
	public String getBotToken() {
		return config.botConfig.token;
	}

}
