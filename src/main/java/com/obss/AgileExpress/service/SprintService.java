package com.obss.AgileExpress.service;

import com.obss.AgileExpress.documents.Sprint;
import com.obss.AgileExpress.documents.Task;
import com.obss.AgileExpress.repository.SprintRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class SprintService {

    private final ProjectService projectService;
    private final SprintRepository sprintRepository;

    //TODO : Get projectId from request (client)
    public Sprint createSprint(Sprint sprint,String projectId) {
        sprint.setSprintState("planned");
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

        int index = IntStream.range(0, sprint.getTasks().size())
                .filter(i -> Objects.equals(task.getId(), sprint.getTasks().get(i).getId()))
                .findFirst()
                .orElse(-1);

        sprint.getTasks().remove(index);
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

    public Sprint changeSprintState(String sprintId,String state) {
        Sprint sprint = sprintRepository.findById(sprintId).get();
        sprint.setSprintState(state);
        sprint.setStartDate(null);
        sprint.setEndDate(null);
        sprintRepository.save(sprint);
        return sprint;
    }
}
