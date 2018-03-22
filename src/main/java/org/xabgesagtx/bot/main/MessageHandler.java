package org.xabgesagtx.bot.main;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResult;
import org.xabgesagtx.bot.config.MainConfig;
import org.xabgesagtx.bot.main.commands.ICommandInfo;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class MessageHandler {
	
	@Autowired
	private MainConfig config;
	
	@Autowired
	private List<ICommandInfo> commandInfos;
	
	@Transactional
	public SendMessage handleMessage(Message message) {
		String text = StringUtils.trimToEmpty(message.getText());
		String chatId = message.getChatId().toString();
		Optional<SendMessage> responseOpt = commandInfos.stream().map(command -> command.toResponse(text, chatId)).filter(Optional::isPresent).map(Optional::get).findFirst();
		return responseOpt.orElseGet(() -> 
		{
			log.info("Failed to create response for {}", text);
			SendMessage backupResponse = new SendMessage();
			backupResponse.setChatId(chatId);
			backupResponse.setText("Hmm, ich weiß gerade nicht, was du willst. Probier es nochma!");
			return backupResponse;
		});
		
	}

	@Transactional
	public AnswerInlineQuery handleInlineQuery(InlineQuery inlineQuery) {
		String text = StringUtils.trimToEmpty(inlineQuery.getQuery());
		String id = inlineQuery.getId();
		Optional<List<InlineQueryResult>> resultsOpt = commandInfos.stream().map(command -> command.toInlineResult(text)).filter(list -> !list.isEmpty()).findFirst();
		List<InlineQueryResult> results = resultsOpt.orElseGet(() -> Lists.newArrayList());
		AnswerInlineQuery answer = new AnswerInlineQuery();
		answer.setInlineQueryId(id);
		answer.setResults(results);
		answer.setCacheTime(config.botConfig.inlineCacheTime);
		return answer;
	}
	
}
