package org.xabgesagtx.bot.main.commands;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xabgesagtx.bot.config.MainConfig;
import org.xabgesagtx.bot.main.CommandConstants;

@Component
public class StartCommand extends AbstractNoArgCommand {
	
	@Autowired
	private MainConfig config;
	
	@Override
	public String getName() {
		return CommandConstants.START;
	}
	
	@Override
	public boolean matches(String text) {
		return StringUtils.equals(text, getPrefixedName());
	}
	
	@Override
	public String getDescription() {
		return "Start der Konversation";
	}
	
	@Override
	public String createMessageText() {
		return config.botConfig.startMessage;
	}
		
}
