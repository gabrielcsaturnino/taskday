package com.example.taskday.auxiliary;

import java.time.LocalDate;
import java.time.Period;

import com.example.taskday.exception.InvalidFormatException;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class DateOfBirthday {
    
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirthday;
    @jakarta.persistence.Transient
    private LocalDate currentDate = LocalDate.now();
    @jakarta.persistence.Transient
    private Period period;

    
    public DateOfBirthday() {}

    public DateOfBirthday(String value) {
        LocalDate dateOfBirthday = stringTLocalDate(value);
        period = Period.between(dateOfBirthday, currentDate);
        checkYear(dateOfBirthday);
        this.dateOfBirthday = dateOfBirthday;
    }

    public void checkYear(LocalDate dateOfBirthday){
        if(dateOfBirthday == null) {
            throw new InvalidFormatException("Date of birthday cannot be null");
        }

        if(period.getYears() < 18) {
            throw new InvalidFormatException("You must be at least 18 years old");
        }
    }

    public LocalDate stringTLocalDate(String date) {
        return LocalDate.parse(date);
    }
    
}
