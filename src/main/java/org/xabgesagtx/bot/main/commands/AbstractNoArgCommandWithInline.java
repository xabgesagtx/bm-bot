package org.xabgesagtx.bot.main.commands;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResult;
import org.xabgesagtx.bot.scrapers.bm.model.Event;

import com.google.common.collect.Lists;

public abstract class AbstractNoArgCommandWithInline extends AbstractNoArgCommand {
	
	protected abstract Stream<Event> getEvents(String text);

	@Override
	public List<InlineQueryResult> toInlineResult(String text) {
		List<InlineQueryResult> result = Lists.newArrayList();
		if (matches(text)) {
			result = getEvents(text).map(this::toArticle).collect(Collectors.toList());
		}
		return result;
	}

}
