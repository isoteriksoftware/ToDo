package week10.assessment.encentral;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Todo {
    private String name;
    private String description;
    private final LocalDateTime dayCreated;
    private TodoStatus status;

    public enum TodoStatus {
        ACTIVE, COMPLETED
    }

    public enum TodoLookupDateStrategy {
        BEFORE_DATE, SAME_DATE, AFTER_DATE
    }

    public Todo(String name, String description, LocalDateTime dayCreated) {
        this.name = name;
        this.description = description;
        this.dayCreated = dayCreated;
        status = TodoStatus.ACTIVE;
    }

    public Todo(String name, String description) {
        this(name, description, LocalDateTime.now(ZoneId.systemDefault()));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDayCreated() {
        return dayCreated;
    }

    public TodoStatus getStatus() {
        return status;
    }

    public void setStatus(TodoStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dayCreated=" + dayCreated +
                ", status=" + status +
                '}';
    }
}
