package com.example.serverside.controller;

import com.example.serverside.entity.IntervalCheck;
import com.example.serverside.service.IntervalCheckService;
import com.example.serverside.dto.IntervalCheckDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("interval/data")
@Slf4j
public class IntervalCheckController {

    private final IntervalCheckService intervalCheckService;

    @PostMapping
    public void create(@RequestBody IntervalCheckDTO intervalCheck) {
        intervalCheckService.addCheck(intervalCheck);
    }

    @PostMapping("list")
    public void createAll(@RequestBody(required = false) List<IntervalCheckDTO> intervalChecks) {
        intervalCheckService.addAllChecks(intervalChecks);
    }

    @GetMapping("zones")
    public List<String> getAll() {
        return intervalCheckService.findUniqueZones();
    }

    @GetMapping("zone/information")
    public List<IntervalCheck> getAll(String zoneId) {
        return intervalCheckService.findZoneInformation(zoneId);
    }

}
