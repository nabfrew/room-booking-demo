package com.example.demo.model;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Objects;

public final class DateRange {
    private final long start;
    private final long end;

    public DateRange(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public DateRange(LocalDate start, LocalDate end) {
        // If null assume ongoing.
        if (end == null) {
            end = LocalDate.MAX;
        }
        if (start.isAfter(end)) {
            throw new DateTimeException("start date cannot be after end date");
        }
        this.start = start.toEpochDay();
        this.end = end.toEpochDay();
    }

    public long getDays() {
        return end - start;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DateRange dateRange = (DateRange) o;
        return Objects.equals(start, dateRange.start) && Objects.equals(end, dateRange.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    public long start() {
        return start;
    }

    public long end() {
        return end;
    }

    @Override
    public String toString() {
        return "DateRange[" +
                "start=" + start + ", " +
                "end=" + end + ']';
    }

}
