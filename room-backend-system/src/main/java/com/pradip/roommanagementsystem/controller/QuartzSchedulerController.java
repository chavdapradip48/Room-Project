package com.pradip.roommanagementsystem.controller;

import com.pradip.roommanagementsystem.dto.ApiResponse;
import com.pradip.roommanagementsystem.dto.JobDTO;
import com.pradip.roommanagementsystem.service.QuartzJobSchedulerService;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jobs")
@CrossOrigin(origins = "*")
public class QuartzSchedulerController {

    @Autowired
    private QuartzJobSchedulerService jobService;

    @GetMapping
    public ResponseEntity getAllJobs() throws SchedulerException {
        List<JobDTO> allJobs = jobService.getAllJobs();
        if (allJobs == null || allJobs.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Jobs are not Available."));
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "All Jobs are Fetched", allJobs));
    }

    @PostMapping
    public ResponseEntity createJob(@RequestBody JobDTO jobDTO) throws SchedulerException, ClassNotFoundException {
        JobDataMap jobDataMap = createJobDataMap(jobDTO.getJobData());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),"Job Created Successfully.",jobService.createJob(jobDTO, jobDataMap)));
    }

    @PutMapping("/{jobName}")
    public ResponseEntity updateJob(@PathVariable String jobName, @RequestBody JobDTO jobDTO) throws SchedulerException, ClassNotFoundException {
        JobDataMap jobDataMap = createJobDataMap(jobDTO.getJobData());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),"Job Updated Successfully.", jobService.updateJob(jobName, jobDTO, jobDataMap)));
    }

    @GetMapping("/{jobName}")
    public ResponseEntity GetJobById(@PathVariable String jobName) throws SchedulerException {

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),"Job Fetched Successfully", jobService.getJobByJobName(jobName)));
    }


    @DeleteMapping("/{jobName}")
    public ResponseEntity deleteJob(@PathVariable String jobName) throws SchedulerException {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),jobService.deleteJob(jobName)));
    }

    @PostMapping("/{jobName}/pause")
    public ResponseEntity pauseJob(@PathVariable String jobName) throws SchedulerException {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),jobService.pauseJob(jobName)));
    }

    @PostMapping("/{jobName}/resume")
    public ResponseEntity resumeJob(@PathVariable String jobName) throws SchedulerException {

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),jobService.resumeJob(jobName)));
    }

    @GetMapping("/scheduler-job-notifications")
    public ResponseEntity getSchedulerNotifications() throws SchedulerException {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Job Notifications Fetched Successfully.", jobService.getSchedulerNotifications()));
    }

    private JobDataMap createJobDataMap(Map<String, Object> jobData) {
        JobDataMap jobDataMap = new JobDataMap();
        if (jobData != null) {
            jobDataMap.putAll(jobData);
        }
        return jobDataMap;
    }
}
