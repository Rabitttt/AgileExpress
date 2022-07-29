package com.obss.AgileExpress.service;

import com.obss.AgileExpress.domain.TaskDao;
import com.obss.AgileExpress.entity.Sprint;
import com.obss.AgileExpress.entity.Task;
import com.obss.AgileExpress.entity.User;
import com.obss.AgileExpress.repository.SprintRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SprintService {

    private final ProjectService projectService;
    private final SprintRepository sprintRepository;

    //TODO : Get projectId from request (client)
    public Sprint createSprint(Sprint sprint,String projectId) {
        Sprint createdSprint = sprintRepository.save(sprint);
        projectService.addSprintToProject(createdSprint, projectId);
        return createdSprint;
    }

    public List<Task> getSprintTasks(String sprintId) {
        return sprintRepository.findById(sprintId).get().getTasks();
    }

    public Sprint getSprint(String sprintId) {
        return sprintRepository.findById(sprintId).get();
    }

    public void addTaskToSprint(Task task, String sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId).get();
        sprint.getTasks().add(task);
        sprintRepository.save(sprint);
    }

    public void deleteTask(Task task, String sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId).get();
        sprint.getTasks().stream().findAny().ifPresent(t -> {
            if (t.getId().equals(task.getId())) {
                sprint.getTasks().remove(t);
            }
        });
        sprintRepository.save(sprint);
    }


    public Sprint changeState(String sprintId,String state) {
        Sprint sprint = sprintRepository.findById(sprintId).get();
        sprint.setSprintState(state);
        sprintRepository.save(sprint);
        return sprint;
    }
    public Sprint makeActiveSprint(String sprintId, String endDate) throws ParseException {
        Sprint sprint = sprintRepository.findById(sprintId).get();
        sprint.setSprintState("active");
        Date formattedEndDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
        sprint.setEndDate(formattedEndDate);
        sprint.setStartDate(new Date());
        sprintRepository.save(sprint);
        return sprint;
    }
}
