package com.example.taskday.domain.model.auxiliary;

import java.time.LocalDate;
import java.time.Period;


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

    public DateOfBirthday(LocalDate dateOfBirthday) {
        period = Period.between(dateOfBirthday, currentDate);
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
