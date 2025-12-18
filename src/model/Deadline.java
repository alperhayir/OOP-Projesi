package model;

import java.time.LocalDate;

public class Deadline {
    private LocalDate dueDate;

    public Deadline(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }
}
