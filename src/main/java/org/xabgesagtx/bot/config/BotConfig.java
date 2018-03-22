package org.xabgesagtx.bot.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BotConfig {
	
	public String name;
	public String token;
	public String notificationUrl;
	public String startMessage;
	public int inlineCacheTime;

}
