package com.example.taskday.controller;

import com.example.taskday.job.Job;
import com.example.taskday.job.dto.JobSearchDTO;
import com.example.taskday.user.Contractor;
import com.example.taskday.user.dto.ContractorSearchDTO;
import com.example.taskday.service.SearchService;
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
    public ResponseEntity<List<Job>> searchJobs(JobSearchDTO searchDTO) {
        List<Job> jobs = searchService.searchJobs(searchDTO);
        return ResponseEntity.ok(jobs);
    }
    
    @GetMapping("/contractors")
    public ResponseEntity<List<Contractor>> searchContractors(ContractorSearchDTO searchDTO) {
        List<Contractor> contractors = searchService.searchContractors(searchDTO);
        return ResponseEntity.ok(contractors);
    }
}
