package com.delivery.system.utils;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public final class UtcDateTimeUtils {

	private UtcDateTimeUtils() {
	}

	public static LocalDateTime toLocalDateTime(Long utcTimeStamp) {
		if (null == utcTimeStamp)
			return null;

		return LocalDateTime.ofInstant(Instant.ofEpochMilli(utcTimeStamp), ZoneOffset.UTC);
	}

	public static LocalDateTime toLocalDateTimeFromSeconds(Long utcTimeStampInSeconds) {
		if (null == utcTimeStampInSeconds)
			return null;

		return LocalDateTime.ofInstant(Instant.ofEpochSecond(utcTimeStampInSeconds), ZoneOffset.UTC);
	}

	public static Long toUtcTimeStamp(LocalDateTime localDateTime) {
		if (null == localDateTime)
			return null;

		return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
	}

	public static Long toUtcTimeStampInSeconds(LocalDateTime localDateTime) {
		if (null == localDateTime)
			return null;

		return localDateTime.toInstant(ZoneOffset.UTC).getEpochSecond();
	}

	public static LocalDateTime utcTimeNow() {
		return LocalDateTime.now(Clock.systemUTC());
	}

	public static LocalDate utcDateNow() {
		return LocalDate.now(Clock.systemUTC());
	}

	public static Long utcTimeNowInSeconds() {
		return Instant.now(Clock.systemUTC()).getEpochSecond();
	}

}
