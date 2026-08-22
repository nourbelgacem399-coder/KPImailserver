package com.kpimailserver;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/kpi")
@CrossOrigin
public class KpiController {

    @GetMapping("/date/{date}")
    public ArrayList<KPI> getByDate(
            @PathVariable String date) {

        return DatabaseManager.getKPIByDate(date);
    }

    @GetMapping("/hours")
    public ArrayList<KPI> getBetweenHours(
            @RequestParam String date,
            @RequestParam int startHour,
            @RequestParam int endHour) {

        return DatabaseManager.getKPIBetweenHours(
                date,
                startHour,
                endHour
        );
    }

    @GetMapping("/dates")
    public ArrayList<KPI> getBetweenDates(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        return DatabaseManager.getKPIBetweenDates(
                startDate,
                endDate
        );
    }
}