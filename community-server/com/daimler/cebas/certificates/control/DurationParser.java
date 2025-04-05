/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.certificates.control;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationParser {
    private static final Pattern PATTERN = Pattern.compile("([-+]?)P(?:([-+]?[0-9]+)Y)?(?:([-+]?[0-9]+)M)?(?:([-+]?[0-9]+)W)?(?:([-+]?[0-9]+)D)?(T(?:([-+]?[0-9]+)H)?(?:([-+]?[0-9]+)M)?(?:([-+]?[0-9]+)(?:[.,]([0-9]{0,9}))?S)?)?", 2);
    private Period period;
    private Duration duration;

    private DurationParser(Period period, Duration duration) {
        this.period = period;
        this.duration = duration;
    }

    public Period getPeriod() {
        return this.period;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public static DurationParser parse(String text) {
        Period period;
        Matcher matcher = PATTERN.matcher(text);
        if (!matcher.matches()) throw new DateTimeParseException("Text cannot be parsed to a DurationParser", text, 0);
        Duration duration = Duration.ZERO;
        int timeStart = text.indexOf("T");
        if (timeStart > 0) {
            String periodText = text.substring(0, timeStart);
            period = periodText.length() > 1 ? Period.parse(periodText) : Period.ZERO;
            duration = Duration.parse("P" + text.substring(timeStart));
        } else {
            period = Period.parse(text);
        }
        return new DurationParser(period, duration);
    }

    public LocalDateTime plusTo(LocalDateTime dateTime) {
        return dateTime.plus(this.period).plus(this.duration);
    }

    public LocalDateTime minusFrom(LocalDateTime dateTime) {
        return dateTime.minus(this.period).minus(this.duration);
    }

    public String toString() {
        return this.period.toString() + this.duration.toString().substring(1);
    }
}
