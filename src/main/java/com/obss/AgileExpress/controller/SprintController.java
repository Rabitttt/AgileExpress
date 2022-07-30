package com.obss.AgileExpress.controller;


import com.obss.AgileExpress.entity.Sprint;
import com.obss.AgileExpress.service.SprintService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;

@RestController
@RequestMapping("sprint")
@RequiredArgsConstructor
public class SprintController {

    private final SprintService sprintService;

    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')")
    @PostMapping("/create/{projectId}")
    public Sprint createSprint(@RequestBody Sprint sprint,@PathVariable(value = "projectId") String projectId) {
        return sprintService.createSprint(sprint,projectId);
    }

    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')" + "|| hasRole('TeamLeader')")
    @GetMapping("/getSprint/{sprintId}")
    public Sprint getSprint(@PathVariable(value = "sprintId") String sprintId) {
        return sprintService.getSprint(sprintId);
    }

    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')" + "|| hasRole('TeamLeader')")
    @PostMapping("/makeActiveSprint")
    public Sprint changeActiveSprint(@RequestParam String sprintId,
                                     @RequestParam String endDate) throws ParseException {
        return sprintService.makeActiveSprint(sprintId,endDate);
    }
    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')" + "|| hasRole('TeamLeader')")
    @PostMapping("/changeSprintState")
    public Sprint changeSprintState(@RequestParam String sprintId,
                                     @RequestParam String state) throws ParseException {
        return sprintService.changeSprintState(sprintId,state);
    }
}
