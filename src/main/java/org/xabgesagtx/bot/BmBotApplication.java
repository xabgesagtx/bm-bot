package org.xabgesagtx.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EntityScan(basePackageClasses = { BmBotApplication.class, Jsr310JpaConverters.class })
public class BmBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(BmBotApplication.class, args);
	}
}
