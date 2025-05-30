package com.example.taskday.domain.model.auxiliary;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import org.springframework.format.annotation.DateTimeFormat;

public class DateOfBirthday {
    private LocalDate dateOfBirthday;
    private LocalDate currentDate = LocalDate.now();
    private Period period = Period.between(dateOfBirthday, currentDate);

    
    public DateOfBirthday() {}

    public DateOfBirthday(LocalDate dateOfBirthday) {
        checkYear(dateOfBirthday);
        this.dateOfBirthday = dateOfBirthday;
    }

    public void checkYear(LocalDate dateOfBirthday){
        if(dateOfBirthday == null) {
            throw new IllegalArgumentException("Date of birthday cannot be null");
        }

        if(period.getYears() < 18) {
            throw new IllegalArgumentException("You must be at least 18 years old");
        }
        

    }
}
