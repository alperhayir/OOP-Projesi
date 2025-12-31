package service;

import model.Project;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public class ProjectService {

    private List<Project> projects;

    public ProjectService() {
        this.projects = new ArrayList<>();
    }

    public Project createProject(String id ,String name){
        Project project = new Project(id,name);
        projects.add(project);
        return project;
    }
    public List<Project> getAllProjects() {
        return projects;
    }

    public Project findProjectById(String  projectId){
        for(Project project : projects){
            if(project.getId().equals(projectId)){
                return project;
            }
        }
        return null;
    }

    public List<Task> getProjectTasks(String projectId){
        Project project = findProjectById(projectId);
        if (project !=null){
            return project.getTasks();
        }
        return null;
    }
}
