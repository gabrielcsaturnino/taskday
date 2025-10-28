package com.example.jooby.controller;

import com.example.jooby.job.Job;
import com.example.jooby.job.dto.JobSearchDTO;
import com.example.jooby.user.Contractor;
import com.example.jooby.user.dto.ContractorSearchDTO;
import com.example.jooby.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {
    
    private final SearchService searchService;
    
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }
    
    @GetMapping("/jobs")
    public ResponseEntity<List<Job>> searchJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) String location) {
        
        JobSearchDTO searchDTO = new JobSearchDTO(title, description, minPrice, maxPrice, location, null, 0, 10, "createdAt", "desc");
        List<Job> jobs = searchService.searchJobs(searchDTO);
        return ResponseEntity.ok(jobs);
    }
    
    @GetMapping("/contractors")
    public ResponseEntity<List<Contractor>> searchContractors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxRating) {
        
        ContractorSearchDTO searchDTO = new ContractorSearchDTO(name, location, minRating, maxRating, null, 0, 10, "createdAt", "desc");
        List<Contractor> contractors = searchService.searchContractors(searchDTO);
        return ResponseEntity.ok(contractors);
    }
}
