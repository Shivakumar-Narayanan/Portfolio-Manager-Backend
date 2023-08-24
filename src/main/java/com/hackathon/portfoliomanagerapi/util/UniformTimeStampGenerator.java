package com.hackathon.portfoliomanagerapi.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class UniformTimeStampGenerator implements TimeStampGenerator {

    @Value("${numtimestamps}")
    private int numTimeStamps;

    @Override
    public List<LocalDate> generateTimeStamps(LocalDate startDate, LocalDate endDate) {
        return generateEvenlyDistributedDates(startDate, endDate, numTimeStamps);
    }

    public static List<LocalDate> generateEvenlyDistributedDates(LocalDate startDate, LocalDate endDate, int count) {
        List<LocalDate> dates = new ArrayList<>();
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        long interval = daysBetween / (count - 1);

        for (long i = 0; i < count; i++) {
            LocalDate newDate = startDate.plusDays(interval * i);
            dates.add(newDate);
        }

        return dates;
    }
}
