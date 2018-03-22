package org.xabgesagtx.bot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="config")
@Getter
@Setter
public class MainConfig {
	
	public BotConfig botConfig;
	public ScraperConfig scrapers;

}
