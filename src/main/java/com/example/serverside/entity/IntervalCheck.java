package com.example.serverside.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Data
@Builder
public class IntervalCheck {

    @Id
    public String id;

    private String zoneId;

    private Boolean isOn;

    private Instant time;

    private Instant lastUpdatedTime;

}

