package org.xabgesagtx.bot.main.commands;

import com.google.common.collect.Lists;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.xabgesagtx.bot.main.CommandConstants;
import org.xabgesagtx.bot.persistence.EventFacade;
import org.xabgesagtx.bot.scrapers.bm.model.Band;
import org.xabgesagtx.bot.scrapers.bm.model.Event;
import org.xabgesagtx.bot.scrapers.bm.model.Link;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@ToString
@Slf4j
public abstract class AbstractCommand implements ICommandInfo {
	
	protected static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE, d.M.").withLocale(Locale.GERMAN);

	@Autowired
	protected EventFacade facade;

	@Override
	public abstract String getName();
	
	public String getPrefixedName() {
		return CommandConstants.PREFIX + getName();
	}

	@Override
	public abstract String getDescription();
	
	protected SendMessage getMessage(String chatId, String text) {
		SendMessage message = new SendMessage();
		message.setChatId(chatId);
		message.enableHtml(true);
		message.setText(text);
		modifyMessage(message);
		return message;
	}

	protected void modifyMessage(SendMessage message) {
		setBasicKeyboard(message);
	}
	
	protected String createBoldText(String text) {
		return String.format("<b>%s</b>", replaceWithHTMLEntities(text));
	}
	
	protected String createLink(String text, String url) {
		return String.format("<a href=\"%s\">%s</a>", url, escape(text));
	}
	
	protected static String createBoldTextMarkdown(String text) {
		return "*" + StringUtils.defaultString(text).replaceAll("([#/\\(\\)\\[\\]<>_])","\\\\$1").replaceAll("\\*", "*\\\\**") + "*";
	}

	public static String escapeMarkdown(String text) {
		return StringUtils.defaultString(text).replaceAll("([\\*#\\[\\]<>_])","\\\\$1");
	}
	
	public static String escape(String text) {
		return replaceWithHTMLEntities(text);
	}
	
	public static String replaceWithHTMLEntities(String text) {
		return StringUtils.defaultString(text).replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
	}

	protected String createResponseTextEventList(List<Event> events) {
		return events.stream().map(event -> createSimpleEventInfo(event)).collect(Collectors.joining("\n\n"));
	}

	String createSimpleEventInfo(Event event) {
		String startTime = StringUtils.isNotBlank(event.getStartTimeString()) ? event.getStartTimeString() + " " : StringUtils.EMPTY;
		String locationTitle = escape(event.getLocationTitle());
		return String.format("%s%s\n%s %s%s ", startTime, locationTitle, createBoldText(event.getTitle()), CommandConstants.PREFIX + CommandConstants.DETAILS, event.getCleanId());
	}

	public void setBasicKeyboard(SendMessage message) {
		log.debug("Setting basic keyboard");
		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
		keyboardMarkup.setResizeKeyboard(true);
		KeyboardButton todayButton = new KeyboardButton("Heute");
		KeyboardButton tomorrowButton = new KeyboardButton("Morgen");
		KeyboardRow firstRow = new KeyboardRow();
		firstRow.add(todayButton);
		firstRow.add(tomorrowButton);
		KeyboardRow secondRow = new KeyboardRow();
		KeyboardButton weekendButton = new KeyboardButton("Wochenende");
		secondRow.add(weekendButton);
		secondRow.add(CommandConstants.WEEKDAYS_TEXT);
		keyboardMarkup.setKeyboard(Lists.newArrayList(firstRow, secondRow));
		message.setReplyMarkup(keyboardMarkup);
	}
	
	protected String getContent(String text) {
		String result = text;
		if (StringUtils.startsWith(text, getPrefixedName())) {
			result = StringUtils.trimToEmpty(StringUtils.substringAfter(result, getPrefixedName())); 
		} 
		return StringUtils.trimToEmpty(result);
	}
	
	protected InlineQueryResultArticle toArticle(Event event) {
		InlineQueryResultArticle article = new InlineQueryResultArticle();
		String title = StringUtils.isNotBlank(event.getStartTimeString()) ? event.getStartTimeString() + " - " + event.getTitle() : event.getTitle();
		article.setTitle(title);
		article.setId(event.getCleanId());
		String description = StringUtils.isBlank(event.getDescription()) ? event.getLocationTitle() : event.getLocationTitle() + " - " + event.getDescription();
		article.setDescription(description);
		InputTextMessageContent content = new InputTextMessageContent();
		content.enableHtml(true);
		content.setMessageText(createEventDetails(event, true));
		article.setInputMessageContent(content);
		return article;
	}
	
	protected String createEventDetails(Event event, boolean withDate) {
		StringBuilder result = new StringBuilder();
		result.append(createBoldText(event.getTitle())).append("\n");
		if (withDate) {
			result.append(DATE_FORMATTER.format(event.getDate())).append(" ");
		}
		if (StringUtils.isNotBlank(event.getStartTimeString())) {
			result.append(event.getStartTimeString()).append(" ");
		}
		result.append(escape(event.getLocationTitle()));
		
		result.append("\n");
		if (StringUtils.isNotBlank(event.getPrice())) {
			result.append("Preis: ").append(escape(event.getPrice())).append("\n");
		}
		if (StringUtils.isNotBlank(event.getDescription())) {
			result.append("\n").append(escape(event.getDescription())).append("\n");
		}
		if (!event.getLinks().isEmpty()) {
			result.append("\n").append(createBoldText("Links:")).append("\n");
			event.getLinks().stream().filter(Link::isValid).forEach(link -> result.append(createLink(link.getTitle(),link.getUrl())).append("\n"));
		}
		if (!event.getBands().isEmpty()) {
			result.append("\n").append(createBoldText("Bands:")).append("\n");
			result.append(event.getBands().stream().map(this::getBandString).collect(Collectors.joining("\n\n")));
		}
		return result.toString();
	}
	
	String getBandString(Band band) {
		StringBuilder result = new StringBuilder(escape(band.getTitle()));
		if (StringUtils.isNotBlank(band.getDescription())) {
			result.append(" - ");
			result.append(escape(band.getDescription()));
		}
		result.append("\n");
		band.getLinks().stream().filter(Link::isValid).forEach(link -> result.append(createLink(link.getTitle(),link.getUrl())).append(" "));
		return result.toString();
	}
}
