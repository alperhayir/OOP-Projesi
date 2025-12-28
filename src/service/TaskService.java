package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskService {
    private List<Task> tasks;


    public TaskService(){
        this.tasks=new ArrayList<>();
    }

    public Task createTask(String id , String title , String description ){
        Task task = new Task(id,title,description);
        tasks.add(task);
        return task;
    }

    public List<Task> getAllTasks(){
        return tasks;
    }

    public void completeTask(String taskId){
        for(Task task : tasks){
            if(task.getId().equals(taskId)){
                task.complete();
                break;
            }
        }
    }
}
