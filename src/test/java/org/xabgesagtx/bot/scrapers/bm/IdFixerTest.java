package org.xabgesagtx.bot.scrapers.bm;

import static org.junit.Assert.*;

import java.util.List;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.xabgesagtx.bot.scrapers.bm.model.Event;

public class IdFixerTest {

	@Test
	public void test() {
		List<Event> input = Lists.newArrayList(Event.builder().id(1).build(),
				Event.builder().id(1).build(),
				Event.builder().id(2).build(),
				Event.builder().id(3).build(),
				Event.builder().id(3).build(),
				Event.builder().id(1).build());
		List<Event> expected = Lists.newArrayList(Event.builder().cleanId("1A").build(),
				Event.builder().cleanId("1B").build(),
				Event.builder().cleanId("2").build(),
				Event.builder().cleanId("3A").build(),
				Event.builder().cleanId("3B").build(),
				Event.builder().cleanId("1C").build());
		new IdFixer().fixIds(input);
		assertEquals(expected, input);
	}

}
