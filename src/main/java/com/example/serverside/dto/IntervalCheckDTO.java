package com.example.serverside.dto;

import com.example.serverside.entity.IntervalCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Collections;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IntervalCheckDTO {

    private String zoneId;

    private Boolean isOn;

    private Instant time;

    public IntervalCheck toEntity() {
        return IntervalCheck.builder()
                .zoneId(this.zoneId)
                .isOn(this.isOn)
                .time(this.time)
                .lastUpdatedTime(ZonedDateTime.now().toInstant())
                .build();
    }
}
