package com.example.taskday.auxiliary;

import com.example.taskday.exception.InvalidFormatException;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Rating {
    
    @Column(name = "avarage_rating", nullable = false, columnDefinition = "DECIMAL(2,1)")
    private double value;

    public Rating () {}

    public Rating(double value) {
        if (value < 0 || value > 5) {
            throw new InvalidFormatException("Rating must be between 0 and 5");
        }
        this.value = Math.round(value * 10.0) / 10.0;
    }

    public double getValue() {return value;}

    public Rating setValue(Rating oldRating, double weightNew) {
        double weightOld = 1 - weightNew;
        return new Rating((oldRating.getValue() * weightOld) + (this.value * weightNew));

    }
    

}
