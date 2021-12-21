package com.delivery.system.utils;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;

public final class UtcDateTimeUtils {

	private UtcDateTimeUtils() {
	}

	public static LocalDateTime utcTimeNow() {
		return LocalDateTime.now(Clock.systemUTC());
	}

	public static Long utcTimeNowInSeconds() {
		return Instant.now(Clock.systemUTC()).getEpochSecond();
	}

}
