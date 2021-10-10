package week10.assessment.encentral;

import org.apache.commons.collections4.list.TreeList;

import java.time.LocalDateTime;

public class User {
    private final String email;
    private String password;
    private final TreeList<Todo> todos;
    private final TreeList<Todo> tempList;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        todos = new TreeList<>();
        tempList = new TreeList<>();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPasswordMatched(String match) {
        return password.equals(match);
    }

    public TreeList<Todo> getTodos() {
        return todos;
    }

    public void addTodo(Todo todo) {
        todos.add(todo);
    }

    public boolean removeTodo(Todo todo) {
        return todos.remove(todo);
    }

    public TreeList<Todo> searchTodosByName(String search) {
        tempList.clear();

        todos.forEach(todo -> {
            if (todo.getName().contains(search))
                tempList.add(todo);
        });

        return tempList;
    }

    public TreeList<Todo> searchTodosByDescription(String search) {
        tempList.clear();

        todos.forEach(todo -> {
            if (todo.getDescription().contains(search))
                tempList.add(todo);
        });

        return tempList;
    }

    public TreeList<Todo> searchTodosByDate(LocalDateTime dateTime, Todo.TodoLookupDateStrategy lookupDateStrategy) {
        tempList.clear();

        todos.forEach(todo -> {
            switch (lookupDateStrategy) {
                case SAME_DATE:
                    if (dateTime.isEqual(todo.getDayCreated()))
                        tempList.add(todo);
                    break;
                case AFTER_DATE:
                    if (todo.getDayCreated().isAfter(dateTime))
                        tempList.add(todo);
                    break;
                case BEFORE_DATE:
                    if (todo.getDayCreated().isBefore(dateTime))
                        tempList.add(todo);
            }
        });

        return tempList;
    }

    public TreeList<Todo> searchTodosByStatus(Todo.TodoStatus status) {
        tempList.clear();

        todos.forEach(todo -> {
            if (todo.getStatus().equals(status))
                tempList.add(todo);
        });

        return tempList;
    }
}
