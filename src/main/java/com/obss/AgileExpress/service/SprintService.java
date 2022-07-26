package com.obss.AgileExpress.service;

import com.obss.AgileExpress.domain.TaskDao;
import com.obss.AgileExpress.entity.Sprint;
import com.obss.AgileExpress.entity.Task;
import com.obss.AgileExpress.entity.User;
import com.obss.AgileExpress.repository.SprintRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SprintService {

    private final ProjectService projectService;
    private final SprintRepository sprintRepository;

    //TODO : Get projectId from request (client)
    public Sprint createSprint(Sprint sprint) {
        Sprint createdSprint = sprintRepository.save(sprint);
        projectService.addSprintToProject(createdSprint, "62db079402d1390f0b58f513");
        return createdSprint;
    }
}
