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

    private LocalDate getMiddleDate(LocalDate startDate, LocalDate endDate) {
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        return startDate.plusDays(daysBetween / 2);
    }

    @Override
    public List<LocalDate> generateTimeStamps(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> res = new ArrayList<>();

        res.add(startDate);
        res.add(getMiddleDate(startDate, endDate));
        res.add(endDate);

        return res;
    }
}
