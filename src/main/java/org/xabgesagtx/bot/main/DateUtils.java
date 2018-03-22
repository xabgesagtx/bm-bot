package org.xabgesagtx.bot.main;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public final class DateUtils {
	
	public static final int END_OF_DAY_IN_HOURS = 3;
	
	public static LocalDate getToday() {
		LocalDateTime result = LocalDateTime.now();
		LocalDateTime limit = LocalDateTime.of(LocalDate.now(), LocalTime.of(3, 0));
		if (result.isBefore(limit)) {
			result = result.minusDays(1);
		}
		return result.toLocalDate();
	}
	
	public static LocalDate getTomorrow() {
		return getToday().plusDays(1);
	}

	public static LocalDateTime getFullDate(Long seconds, LocalDate date) {
		LocalDateTime result = null;
		if (date != null && seconds != null) {
			LocalDateTime dateTime;
			if (seconds <= TimeUnit.SECONDS.convert(END_OF_DAY_IN_HOURS, TimeUnit.HOURS)) {
				dateTime = date.plusDays(1).atStartOfDay();
			} else {
				dateTime = date.atStartOfDay();
			}
			result = dateTime.plusSeconds(seconds);
		}
		return result;
	}
	
	public static List<LocalDate> getWeekend() {
		LocalDate now = LocalDate.now();
		return getWeekend(now);
	}
	
	static List<LocalDate> getWeekend(LocalDate now) {
		List<LocalDate> result = Lists.newArrayList();
		int dayOfWeek = now.getDayOfWeek().getValue();
		if (dayOfWeek > 5) {
			int toGet = 7 - dayOfWeek;
			result.add(now);
			for(int i = 1; toGet >= i; i++) {
				result.add(now.plusDays(i));
			}
		} else {
			int toAdd = 5 - dayOfWeek;
			result.add(now.plusDays(toAdd));
			result.add(now.plusDays(toAdd + 1));
			result.add(now.plusDays(toAdd + 2));
		}
		return result;
	}
	
	public static Optional<LocalDate> getLocalDateFromDayOfWeekString(String text) {
		return getDayOfWeekFromString(text).map(dayOfWeek -> getFittingDateForDayOfWeek(dayOfWeek, getToday()));
	}
	
	static LocalDate getFittingDateForDayOfWeek(DayOfWeek dayOfWeek, LocalDate date) {
		LocalDate result;
		DayOfWeek currentDayOfWeek = date.getDayOfWeek();
		int diff = dayOfWeek.getValue() - currentDayOfWeek.getValue();
		if (diff >= 0) {
			result = date.plusDays(diff);
		} else {
			result = date.plusWeeks(1).plusDays(diff);
		}
		return result;
	}
	
	public static Optional<DayOfWeek> getDayOfWeekFromString(String text) {
		String safeText = StringUtils.defaultString(text);
		return Arrays.asList(DayOfWeek.values()).stream()
				.filter(dayOfWeek -> matchesDayOfWeek(dayOfWeek, safeText))
				.findFirst();
	}
	
	private static boolean matchesDayOfWeek(DayOfWeek dayOfWeek, String text) {
		return Arrays.asList(Locale.GERMAN,Locale.US).stream().anyMatch(locale -> matchesDayOfWeek(dayOfWeek, text, locale));
	}
	
	private static boolean matchesDayOfWeek(DayOfWeek dayOfWeek, String text, Locale locale) {
		return Arrays.asList(TextStyle.FULL,TextStyle.SHORT).stream().anyMatch(style -> StringUtils.equalsIgnoreCase(dayOfWeek.getDisplayName(style,locale),text));
	}
	
	private static final DateTimeFormatter FULL_DATE_FORMATTER = DateTimeFormatter.ofPattern("d.M.yyyy");
	private static final DateTimeFormatter FULL_DATE_FORMATTER_WITH_MONTH_NAME = DateTimeFormatter.ofPattern("d.MMMM.yyyy",Locale.GERMAN);
	private static final DateTimeFormatter FULL_DATE_FORMATTER_WITH_MONTH_NAME_US = DateTimeFormatter.ofPattern("d.MMMM.yyyy",Locale.US);
	
	public static Optional<LocalDate> getLocalDateFromString(String text) {
		return getLocalDateFromString(text, LocalDate.now());
	}
	
	static Optional<LocalDate> getLocalDateFromString(String text, LocalDate now) {
		boolean appendedYear = false;
		if (Pattern.matches("^(\\d{1,2})\\.\\s?(\\d{1,2}\\.?)|(\\w+)$",text)) {
			if (!StringUtils.endsWith(text, ".")) {
				text = text + ".";
			}
			appendedYear = true;
			text = text + now.getYear();
		}
		String searchText = text.replaceAll("\\s+", StringUtils.EMPTY);
		Optional<LocalDate> result = Arrays.asList(FULL_DATE_FORMATTER, FULL_DATE_FORMATTER_WITH_MONTH_NAME, FULL_DATE_FORMATTER_WITH_MONTH_NAME_US).stream().flatMap(formatter -> parseSafely(formatter, searchText)).findFirst();
		if (appendedYear && result.isPresent() && result.get().plusMonths(1).isBefore(now)) {
			result = Optional.of(result.get().plusYears(1));
		}
		return result;
	}
	
	static Stream<LocalDate> parseSafely(DateTimeFormatter formatter, String text) {
		Stream<LocalDate> result = Stream.empty();
		try {
			result = Stream.of(LocalDate.parse(text, formatter));
		} catch (DateTimeParseException e) {
			// nothing to do
		}
		return result;
	}
}
