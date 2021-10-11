package week10.assessment.encentral;

import static org.junit.Assert.*;

import org.apache.commons.collections4.list.TreeList;
import org.junit.Test;

import java.time.LocalDateTime;

public class AppTest {

    @Test
    public void testCreateAndSearchTodos() {
        User user = new User("imran@encentral.com", "test");
        user.addTodo(new Todo("t1", "First todo", LocalDateTime.parse("2020-10-01T00:00:00")));
        user.addTodo(new Todo("t2", "Second todo", LocalDateTime.parse("2021-10-01T00:00:00")));
        user.addTodo(new Todo("t3", "Third one", LocalDateTime.parse("2022-10-01T00:00:00")));

        assertEquals(3, user.getTodos().size());

        TreeList<Todo> todos = user.searchTodosByName("t1");
        assertEquals(1, todos.size());

        todos = user.searchTodosByStatus(Todo.TodoStatus.ACTIVE);
        assertEquals(3, todos.size());

        todos = user.searchTodosByDescription("todo");
        assertEquals(2, todos.size());

        todos = user.searchTodosByDate(LocalDateTime.now(), Todo.TodoLookupDateStrategy.AFTER_DATE);
        assertEquals(1, todos.size());

        todos = user.searchTodosByDate(LocalDateTime.parse("2020-10-01T00:00:00"), Todo.TodoLookupDateStrategy.SAME_DATE);
        assertEquals(1, todos.size());

        todos = user.searchTodosByDate(LocalDateTime.parse("2022-10-01T00:00:00"), Todo.TodoLookupDateStrategy.BEFORE_DATE);
        assertEquals(2, todos.size());
    }
}
