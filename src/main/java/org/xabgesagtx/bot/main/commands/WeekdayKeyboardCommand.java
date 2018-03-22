package org.xabgesagtx.bot.main.commands;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.xabgesagtx.bot.main.CommandConstants;

@Component
public class WeekdayKeyboardCommand extends AbstractNoArgCommand {
	
	private static final TextStyle TEXT_STYLE = TextStyle.FULL;
	private static final Locale LOCALE = Locale.GERMAN;

	@Override
	public String getName() {
		return CommandConstants.WEEKDAYS;
	}
	
	@Override
	public boolean matches(String text) {
		return StringUtils.equals(text, getPrefixedName()) || StringUtils.equals(text, CommandConstants.WEEKDAYS_TEXT);
	}

	@Override
	public String getDescription() {
		return "Zeige Keyboard mit Wochentagen";
	}
	
	@Override
	protected void modifyMessage(SendMessage message) {
		ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
		keyboard.setResizeKeyboard(true);
		KeyboardRow firstRow = new KeyboardRow();
		firstRow.add(getDisplayName(DayOfWeek.MONDAY));
		firstRow.add(getDisplayName(DayOfWeek.TUESDAY));
		firstRow.add(getDisplayName(DayOfWeek.WEDNESDAY));
		KeyboardRow secondRow = new KeyboardRow();
		secondRow.add(getDisplayName(DayOfWeek.THURSDAY));
		secondRow.add(getDisplayName(DayOfWeek.FRIDAY));
		secondRow.add(getDisplayName(DayOfWeek.SATURDAY));
		KeyboardRow thirdRow = new KeyboardRow();
		thirdRow.add(getDisplayName(DayOfWeek.SUNDAY));
		thirdRow.add(CommandConstants.BACK_TEXT);
		keyboard.setKeyboard(Arrays.asList(firstRow, secondRow, thirdRow));
		message.setReplyMarkup(keyboard);
	}
	
	private String getDisplayName(DayOfWeek dayOfWeek) {
		return dayOfWeek.getDisplayName(TEXT_STYLE, LOCALE);
	}

	@Override
	public String createMessageText() {
		return "Wähle einen Wochentag";
	}

}
