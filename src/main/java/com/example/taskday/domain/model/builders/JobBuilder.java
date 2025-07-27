package com.example.taskday.domain.model.builders;


import com.example.taskday.domain.model.Client;
import com.example.taskday.domain.model.Job;
import com.example.taskday.domain.model.dtos.CreateJobRequestDTO;

public class JobBuilder {
    String title;
    String description;
    int pricePerHour;
    Client client;

    public JobBuilder fromDTO(CreateJobRequestDTO dto, Client client) {
        this.title = dto.title();
        this.description = dto.description();
        this.pricePerHour = dto.pricePerHour();
        this.client = client;
        return this;
    }

    public Job build() {
        return new Job(title, description, pricePerHour, client);
 }
}
