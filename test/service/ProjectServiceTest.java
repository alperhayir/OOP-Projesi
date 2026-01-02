package service;

import model.Project;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProjectServiceTest {

    @Test
    void projeOlusturuluyorMu(){
        ProjectService projectService = new ProjectService();
        Project project =projectService.createProject("p1","OOP Projesi");

        assertNotNull(project);
        assertEquals("p1",project.getId());
        assertEquals(1,projectService.getAllProjects().size());

    }


}
