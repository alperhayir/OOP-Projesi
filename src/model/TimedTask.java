package model;

import java.time.LocalDate;

public class TimedTask extends Task {

    private Deadline deadline;

    public TimedTask(Deadline deadline) {
        this.deadline = deadline;
    }

    public Deadline getDeadline() {
        return deadline;
    }
    }
