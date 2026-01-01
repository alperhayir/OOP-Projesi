package model;

import java.time.LocalDate;

public class TimedTask extends Task {

    private Deadline deadline;

    public TimedTask(String id , String title , String description ,Deadline deadline) {
        super(id, title, description);
        this.deadline = deadline;
    }

    public Deadline getDeadline() {
        return deadline;
    }
    }
