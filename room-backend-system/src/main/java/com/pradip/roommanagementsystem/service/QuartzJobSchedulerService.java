package com.pradip.roommanagementsystem.service;

import com.pradip.roommanagementsystem.dto.JobDTO;
import com.pradip.roommanagementsystem.dto.SchedulerJobNotification;
import com.pradip.roommanagementsystem.dto.SimpleJob;
import com.pradip.roommanagementsystem.exception.ResourceNotFoundException;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

import static com.pradip.roommanagementsystem.dto.SimpleJob.jobCountMap;

@Service
public class QuartzJobSchedulerService {

    @Autowired
    private Scheduler scheduler;
    private static final String JOB_PACKAGE_PATH = "com.pradip.roommanagementsystem.dto.";

    public List<JobDTO> getAllJobs() throws SchedulerException {
        List<JobDTO> jobDTOs = new ArrayList<>();

        for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.anyJobGroup())) {
            jobDTOs.add(getJobDTOBuJobKey(jobKey));
        }
        return jobDTOs;
    }

    private JobDTO getJobDTOBuJobKey(JobKey jobKey) throws SchedulerException {

        Date jobStartTime = null, lastExecutionTime=null, nextExecutionTime;

        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);

        JobDTO jobDTO = new JobDTO();
        jobDTO.setNumberOfJobExecutingCount(SimpleJob.getJobCount(jobKey.getName()));
        jobDTO.setJobName(jobDetail.getKey().getName());
        jobDTO.setJobClass(jobDetail.getJobClass().getName().replace(JOB_PACKAGE_PATH, ""));

        Trigger trigger = null;
        if (triggers != null && !triggers.isEmpty()){
            trigger=triggers.get(0);
            jobDTO.setTriggerName(trigger.getKey().getName());
            jobDTO.setCronExpression(((CronTrigger) trigger).getCronExpression());
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            jobDTO.setStatus(triggerState.toString());
            if (triggerState.equals(Trigger.TriggerState.NORMAL)) {
                jobStartTime = trigger.getStartTime();
                jobDTO.setStatus("IN_PROGRESS");
            }
            lastExecutionTime = trigger.getPreviousFireTime();

            if (lastExecutionTime == null)
                jobDTO.setStatus("SCHEDULED");

            nextExecutionTime = trigger.getNextFireTime();
            if (!triggerState.equals(Trigger.TriggerState.PAUSED)) {
                jobDTO.setNextFireTime(nextExecutionTime);
            }

            jobDTO.setJobData(jobDetail.getJobDataMap().getWrappedMap());

            ArrayList<Map.Entry<String, Object>> entries = new ArrayList<>(jobDTO.getJobData().entrySet());
            if(entries != null && !entries.isEmpty()){
                int currentTurnIndex = jobDTO.getNumberOfJobExecutingCount() % jobDTO.getJobData().size();
                jobDTO.setNextTurn((String) entries.get(currentTurnIndex).getValue());

                if (jobDTO.getNumberOfJobExecutingCount() != 0)
                    if (currentTurnIndex == 0) jobDTO.setCurrentTurn((String) entries.get(entries.size()-1).getValue());
                    else jobDTO.setCurrentTurn((String) entries.get(currentTurnIndex-1).getValue());
            }
        }

        jobDTO.setJobStartTime(jobStartTime);
        jobDTO.setLastFireTime(lastExecutionTime);
        return jobDTO;
    }


    public JobDTO createJob(JobDTO jobDTO, JobDataMap jobDataMap) throws SchedulerException, ClassNotFoundException {
        if (scheduler.checkExists(JobKey.jobKey(jobDTO.getJobName()))) {
            throw new ResourceNotFoundException(jobDTO.getJobName()+" Job is already exist.");
        }
        JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) Class.forName(JOB_PACKAGE_PATH+jobDTO.getJobClass()))
                .withIdentity(jobDTO.getJobName())
                .usingJobData(jobDataMap)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobDTO.getTriggerName())
                .withSchedule(CronScheduleBuilder.cronSchedule(jobDTO.getCronExpression()))
                .build();

        Date nextFireTime = scheduler.scheduleJob(jobDetail, trigger);
        Date startTime = trigger.getStartTime();
        jobDTO.setJobStartTime(startTime);
        jobDTO.setNextFireTime(nextFireTime);
        jobDTO.setStatus("SCHEDULED");
        return jobDTO;
    }

    public JobDTO updateJob(String jobName, JobDTO jobDTO, JobDataMap jobDataMap) throws SchedulerException, ClassNotFoundException {
        if (!scheduler.checkExists(JobKey.jobKey(jobName))) {
            throw new ResourceNotFoundException(jobName+" Job does not exist.");
        }
        TriggerKey triggerKey = TriggerKey.triggerKey(jobDTO.getTriggerName());
        Trigger newTrigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .withSchedule(CronScheduleBuilder.cronSchedule(jobDTO.getCronExpression()))
                .build();

        JobKey jobKey = JobKey.jobKey(jobName);
        JobDetail newJobDetail = JobBuilder.newJob((Class<? extends Job>) Class.forName(JOB_PACKAGE_PATH+jobDTO.getJobClass()))
                .withIdentity(jobKey)
                .usingJobData(jobDataMap)
                .storeDurably() // Make the job durable
                .build();

        scheduler.addJob(newJobDetail, true);
        scheduler.rescheduleJob(triggerKey, newTrigger);
        Date startTime = newTrigger.getStartTime();
        jobDTO.setJobStartTime(startTime);
        jobDTO.setNextFireTime(null);
        jobDTO.setStatus("SCHEDULED");
        return jobDTO;
    }

    public String deleteJob(String jobName) throws SchedulerException {
        jobCountMap.remove(jobName);
        if (!scheduler.deleteJob(JobKey.jobKey(jobName)))
            throw new ResourceNotFoundException(jobName+" Job does not deleted.");
        return jobName+" Job Deleted Successfully.";
    }

    public String pauseJob(String jobName) throws SchedulerException {
        scheduler.pauseJob(JobKey.jobKey(jobName));
        return jobName+" Job Paused Successfully.";
    }

    public String resumeJob(String jobName) throws SchedulerException {
        scheduler.resumeJob(JobKey.jobKey(jobName));
        return jobName+" Job Resumed Successfully.";
    }

    public JobDTO getJobByJobName(String jobName) throws SchedulerException {
        try{
            return getJobDTOBuJobKey(JobKey.jobKey(jobName));
        }catch (NullPointerException e){throw new ResourceNotFoundException(jobName+" Job does not exist.");}
    }

    public List<SchedulerJobNotification> getSchedulerNotifications() throws SchedulerException {
        List<SchedulerJobNotification> schedulerJobNotifications=new ArrayList<>();
        Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyJobGroup());

        if (jobKeys != null && jobKeys.isEmpty())
            throw new ResourceNotFoundException("Job Notifications are not available.");

        for (JobKey jobKey : jobKeys) {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            Trigger trigger = (scheduler.getTriggersOfJob(jobDetail.getKey())).get(0);
            long delay = trigger.getNextFireTime().getTime() - System.currentTimeMillis();


            String jobTurn = "";
            ArrayList<Map.Entry<String, Object>> entries = new ArrayList<>(jobDetail.getJobDataMap().entrySet());
            if(entries != null && !entries.isEmpty()){
                int jobCount = SimpleJob.getJobCount(jobKey.getName());
                int currentTurnIndex = jobCount  % entries.size();

                if (jobCount != 0)
                    if (currentTurnIndex == 0) jobTurn = (String) entries.get(entries.size()-1).getValue();
                    else jobTurn = (String) entries.get(currentTurnIndex-1).getValue();
            }

            schedulerJobNotifications.add(new SchedulerJobNotification(jobKey.getName(), jobTurn, delay));
        }

        return schedulerJobNotifications;
    }
}
