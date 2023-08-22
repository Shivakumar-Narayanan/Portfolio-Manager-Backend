package com.hackathon.portfoliomanagerapi.util;

import java.time.LocalDate;
import java.util.List;

public interface TimeStampGenerator {

    public List<LocalDate> generateTimeStamps(LocalDate startDate, LocalDate endDate);
}
