package org.xabgesagtx.bot.main.commands;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand extends AbstractNoArgCommand {

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getDescription() {
		return "Hilfe zur Benutzung des Bots";
	}
	
	@Override
	public String createMessageText() {
		return "Alle momentan verfügbaren Kommandos sind im Kontextmenü oder auf der Tastatur angezeigt. Dazu kann man ein konkretes Datum eingeben. Z.B. 20.6 für den 20. Juni";
	}

	@Override
	public boolean matches(String text) {
		return StringUtils.equals(StringUtils.trim(text),getPrefixedName());
	}

}
