package org.xabgesagtx.bot.main.commands;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.xabgesagtx.bot.main.CommandConstants;

@Component
public class MainMenuCommand extends AbstractNoArgCommand {

	@Override
	public String getName() {
		return CommandConstants.MAIN_MENU;
	}
	
	@Override
	public boolean matches(String text) {
		return StringUtils.equals(text, getPrefixedName()) || StringUtils.equals(text, CommandConstants.BACK_TEXT);
	}

	@Override
	public String getDescription() {
		return "Zeigt das Hauptmenü an";
	}

	@Override
	public String createMessageText() {
		return "Triff eine Auswahl oder gib einen Wochentag oder Datum an";
	}

}
