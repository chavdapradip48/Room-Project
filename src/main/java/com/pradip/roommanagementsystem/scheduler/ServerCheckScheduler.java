package com.pradip.roommanagementsystem.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Component
public class ServerCheckScheduler {
    private RestTemplate restTemplate = new RestTemplate();

    @Value("${server.url}")
    private String apiUrl; // replace with your API URL

    @Scheduled(fixedDelay = 1680000) // 28 minutes in milliseconds
    public void callApi() {
        String response = restTemplate.getForObject(apiUrl+"/keep-alive", String.class);
        System.out.println("\n=> Response from server : "+response+".");
        try{
            String[] timeDate = LocalDateTime.now().toString().split("T");
            System.out.println("=> Current Time         : "+timeDate[1].substring(0,timeDate[1].indexOf("."))+"");
            System.out.println("=> Current Date         : "+timeDate[0]+"\n");
        }
        catch (ArrayIndexOutOfBoundsException ex){
            System.out.println("Time and date is not available.");
        }

    }
}
