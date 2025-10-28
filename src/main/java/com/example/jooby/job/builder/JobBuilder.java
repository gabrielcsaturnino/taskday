package com.example.jooby.job.builder;


import com.example.jooby.job.Job;
import com.example.jooby.job.dto.CreateJobRequestDTO;
import com.example.jooby.user.Client;

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
