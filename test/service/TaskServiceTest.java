package service;

import model.Task;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceTest {

    @Test
    void gorevOlusturuluyorMu(){
        TaskService taskService = new TaskService();

        Task task = taskService.createTask(
                "1","Test Deneme Task","Junit ogren"
        );


        assertNotNull(task);
        assertEquals("Test Deneme Task",task.getTitle());
        assertFalse(task.isCompleted());

    }



}
