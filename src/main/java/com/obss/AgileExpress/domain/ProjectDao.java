package com.obss.AgileExpress.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDao {

    private String id;
    private String name;
    private String description;

    private Date createdDate;

    private String creator;

    private String teamLeader;

    private String projectManager;

    private List<String> members = new ArrayList<>();

    private List<String> backlogTasks = new ArrayList<>();
    private List<String> taskStatus = new ArrayList<>();

    private List<String> sprints = new ArrayList<>();
}