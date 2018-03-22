package org.xabgesagtx.bot.main;

import static org.junit.Assert.*;
import static org.xabgesagtx.bot.main.DateUtils.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.collect.Lists;

public class DateUtilsTest {

	@Test
	public void testGetFullDate() {
		assertNull(getFullDate(null, LocalDate.now()));
		assertNull(getFullDate(10000l, null));
		assertEquals(LocalDateTime.of(2016, 1, 3, 3, 0), getFullDate(TimeUnit.SECONDS.convert(3, TimeUnit.HOURS), LocalDate.of(2016, 1, 2)));
		assertEquals(LocalDateTime.of(2016, 1, 2, 4, 0), getFullDate(TimeUnit.SECONDS.convert(4, TimeUnit.HOURS), LocalDate.of(2016, 1, 2)));
	}

	@Test
	public void testGetWeekend() {
		assertEquals(Lists.newArrayList(LocalDate.of(2016, 5, 20), LocalDate.of(2016, 5, 21), LocalDate.of(2016, 5, 22)),
				getWeekend(LocalDate.of(2016, 5, 18)));
		assertEquals(Lists.newArrayList(LocalDate.of(2016, 5, 20), LocalDate.of(2016, 5, 21), LocalDate.of(2016, 5, 22)),
				getWeekend(LocalDate.of(2016, 5, 20)));
		assertEquals(Lists.newArrayList(LocalDate.of(2016, 5, 21), LocalDate.of(2016, 5, 22)), getWeekend(LocalDate.of(2016, 5, 21)));
		assertEquals(Lists.newArrayList(LocalDate.of(2016, 5, 22)), getWeekend(LocalDate.of(2016, 5, 22)));
		assertEquals(Lists.newArrayList(LocalDate.of(2016, 5, 27), LocalDate.of(2016, 5, 28), LocalDate.of(2016, 5, 29)),
				getWeekend(LocalDate.of(2016, 5, 23)));
	}
	
	@Test
	public void testGetDayOfWeekFromString() {
		assertEquals(Optional.empty(),getDayOfWeekFromString(""));
		assertEquals(Optional.of(DayOfWeek.MONDAY),getDayOfWeekFromString("Monday"));
		assertEquals(Optional.of(DayOfWeek.MONDAY),getDayOfWeekFromString("Montag"));
		assertEquals(Optional.of(DayOfWeek.MONDAY),getDayOfWeekFromString("Mon"));
		assertEquals(Optional.of(DayOfWeek.TUESDAY),getDayOfWeekFromString("Dienstag"));
		assertEquals(Optional.of(DayOfWeek.WEDNESDAY),getDayOfWeekFromString("Mittwoch"));
		assertEquals(Optional.of(DayOfWeek.THURSDAY),getDayOfWeekFromString("Donnerstag"));
		assertEquals(Optional.of(DayOfWeek.FRIDAY),getDayOfWeekFromString("Freitag"));
		assertEquals(Optional.of(DayOfWeek.SATURDAY),getDayOfWeekFromString("Samstag"));
		assertEquals(Optional.of(DayOfWeek.SUNDAY),getDayOfWeekFromString("Sonntag"));
	}
	
	@Test
	public void testGetFittingDateForDayOfWeek() {
		assertEquals(LocalDate.of(2016,5,27), getFittingDateForDayOfWeek(DayOfWeek.FRIDAY, LocalDate.of(2016,5,21)));
		assertEquals(LocalDate.of(2016,5,21), getFittingDateForDayOfWeek(DayOfWeek.SATURDAY, LocalDate.of(2016,5,21)));
		assertEquals(LocalDate.of(2016,5,22), getFittingDateForDayOfWeek(DayOfWeek.SUNDAY, LocalDate.of(2016,5,21)));
	}
	
	@Test
	public void testGetLocalDateFromString() {
		assertEquals(Optional.of(LocalDate.of(2016, 5, 27)), getLocalDateFromString("27.5.",LocalDate.of(2016, 4, 20)));
		assertEquals(Optional.of(LocalDate.of(2016, 5, 27)), getLocalDateFromString("27.5.",LocalDate.of(2016, 5, 28)));
		assertEquals(Optional.of(LocalDate.of(2017, 5, 27)), getLocalDateFromString("27.5.",LocalDate.of(2016, 7, 28)));
		assertEquals(Optional.of(LocalDate.of(2016, 5, 27)), getLocalDateFromString("27.5.2016",LocalDate.of(2016, 7, 28)));
		assertEquals(Optional.of(LocalDate.of(2016, 6, 27)), getLocalDateFromString("27.Juni.2016",LocalDate.of(2016, 7, 28)));
		assertEquals(Optional.of(LocalDate.of(2016, 6, 27)), getLocalDateFromString("27.June.2016",LocalDate.of(2016, 7, 28)));
	}

}
