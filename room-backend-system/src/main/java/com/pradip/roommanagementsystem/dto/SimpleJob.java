package com.pradip.roommanagementsystem.dto;

import org.quartz.*;

import java.util.HashMap;
import java.util.Map;

public class SimpleJob implements Job {

    public static Map<String, Integer> jobCountMap = new HashMap<>();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        // Increase job excuting count time by one
        setJobNameAndConutByOneInMap(context.getJobDetail().getKey().getName());
        
        // Get Some data to print
        CronTrigger cronTrigger=null;
        try {
            cronTrigger = (CronTrigger) context.getScheduler().getTrigger(context.getTrigger().getKey());
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Executing "+ context.getJobDetail().getKey().getName()+" with "+
                cronTrigger.getCronExpression() +" cron Expression and next fire time is "+context.getTrigger().getNextFireTime()+".");
    }

    public static void setJobNameAndConutByOneInMap(String jobName) {
        jobCountMap.put(jobName, (jobCountMap.getOrDefault(jobName, 0)+1));
        System.out.println(jobCountMap.size()+"Executing SimpleJob with key: " + jobName + ", count: " + jobCountMap.get(jobName));
    }

    public static int getJobCount(String jobName) {
        return jobCountMap.getOrDefault(jobName, 0);
    }
}
