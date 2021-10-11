package week10.assessment.encentral;

import org.apache.commons.collections4.IterableMap;
import org.apache.commons.collections4.list.TreeList;
import org.apache.commons.collections4.map.HashedMap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Driver class
 */
public class App {
    // Main Menu Options
    private static final int OPTION_CREATE_ACCOUNT = 1;
    private static final int OPTION_LOGIN          = 2;
    private static final int OPTION_QUIT           = 3;

    // Logged-In Menu Options
    private static final int OPTION_CHANGE_PASSWORD      = 1;
    private static final int OPTION_ADD_TODO             = 2;
    private static final int OPTION_SHOW_TODOS           = 3;
    private static final int OPTION_SHOW_ACTIVE_TODOS    = 4;
    private static final int OPTION_SHOW_COMPLETED_TODOS = 5;
    private static final int OPTION_REMOVE_TODO          = 6;
    private static final int OPTION_UPDATE_TODO          = 7;
    private static final int OPTION_SEARCH_TODO          = 8;
    private static final int OPTION_LOGOUT               = 9;

    // Update Todo Menu Options
    private static final int OPTION_UPDATE_TODO_NAME        = 1;
    private static final int OPTION_UPDATE_TODO_DESCRIPTION = 2;
    private static final int OPTION_UPDATE_TODO_STATUS      = 3;
    private static final int OPTION_UPDATE_TODO_CANCEL      = 4;

    // Update Todo Status Menu Options
    private static final int OPTION_UPDATE_TODO_STATUS_ACTIVE    = 1;
    private static final int OPTION_UPDATE_TODO_STATUS_COMPLETED = 2;
    private static final int OPTION_UPDATE_TODO_STATUS_CANCEL    = 3;

    // Search Todo Menu Options
    private static final int OPTION_SEARCH_TODO_BY_NAME        = 1;
    private static final int OPTION_SEARCH_TODO_BY_DESCRIPTION = 2;
    private static final int OPTION_SEARCH_TODO_BY_DATE        = 3;
    private static final int OPTION_SEARCH_TODO_CANCEL         = 4;

    // Search Todo By Date Menu Options
    private static final int OPTION_SEARCH_TODO_BY_DATE_BEFORE = 1;
    private static final int OPTION_SEARCH_TODO_BY_SAME_DATE   = 2;
    private static final int OPTION_SEARCH_TODO_BY_DATE_AFTER  = 3;
    private static final int OPTION_SEARCH_TODO_BY_DATE_CANCEL = 4;

    private static final Scanner scanner;
    private static final IterableMap<String, User> users;

    private static User loggedInUser;
    private static Todo currentTodo;

    static {
        scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

        users = new HashedMap<>();
    }

    public static void main( String[] args ) {
        boolean shouldQuit = false;

        while (!shouldQuit) {
            System.out.println();
            int option = getMainMenuOption();
            System.out.println();

            if (option == OPTION_QUIT) {
                shouldQuit = true;
                quit();
            }
            else if (option == OPTION_CREATE_ACCOUNT)
                createAccount();
            else if (option == OPTION_LOGIN)
                login();
            else
                System.out.println("Please choose a valid option");
        }

        scanner.close();
    }

    private static void quit() {
        System.out.println("Good bye!");
    }

    /**
     * Displays a menu where one option can be selected at a time.
     * The function automatically validates user inputs and guarantees that the provided value will always fall within
     * the range of provided options. This guarantee only holds for options that are sorted in ascending order!
     * This function won't return until a valid option is selected by the user.
     * @param options an array of options
     * @param descriptions an array of the description for each options
     * @return the selected option
     */
    private static int getMenuOption(int[] options, String[] descriptions) {
        if (options.length != descriptions.length)
            throw new IllegalArgumentException("The number of options must match the number of descriptions!");

        int option;

        while (true) {
            for (int i = 0; i < options.length; i++)
                System.out.printf("%d -> %s%n", options[i], descriptions[i]);

            String input = readInput("Please choose one of the options to continue: ");

            try {
                option = Integer.parseInt(input);

                if (option < options[0] || option > options[options.length-1])
                    System.out.println(input + " is not a valid option!");
                else
                    break;
            } catch (Exception e) {
                System.out.println(input + " is not a valid option!");
            }

            System.out.println();
        }

        return option;
    }

    private static int getMainMenuOption() {
        int[] options = {OPTION_CREATE_ACCOUNT, OPTION_LOGIN, OPTION_QUIT};
        String[] descriptions = {"Create an Account", "Login", "Quit"};

        return getMenuOption(options, descriptions);
    }

    private static int getLoggedInMenuOption() {
        int[] options = {
                OPTION_CHANGE_PASSWORD, OPTION_ADD_TODO, OPTION_SHOW_TODOS, OPTION_SHOW_ACTIVE_TODOS,
                OPTION_SHOW_COMPLETED_TODOS, OPTION_REMOVE_TODO, OPTION_UPDATE_TODO, OPTION_SEARCH_TODO,
                OPTION_LOGOUT
        };
        String[] descriptions = {
                "Change Password", "Add Todo", "Show All Todos", "Show Active Todos", "Show Completed Todos",
                "Remove Todo", "Update Todo", "Search Todos", "Logout"
        };

        return getMenuOption(options, descriptions);
    }

    private static int getUpdateTodoMenuOption() {
        int[] options = {
                OPTION_UPDATE_TODO_NAME, OPTION_UPDATE_TODO_DESCRIPTION, OPTION_UPDATE_TODO_STATUS,
                OPTION_UPDATE_TODO_CANCEL
        };
        String[] descriptions = {
                "Update Todo Name", "Update Todo Description", "Update Todo Status", "Go Back"
        };

        return getMenuOption(options, descriptions);
    }

    private static int getUpdateTodoStatusMenuOption() {
        int[] options = {
                OPTION_UPDATE_TODO_STATUS_ACTIVE, OPTION_UPDATE_TODO_STATUS_COMPLETED, OPTION_UPDATE_TODO_STATUS_CANCEL
        };
        String[] descriptions = {"Set Active", "Set Completed", "Cancel"};

        return getMenuOption(options, descriptions);
    }

    private static int getSearchTodoMenuOption() {
        int[] options = {
                OPTION_SEARCH_TODO_BY_NAME, OPTION_SEARCH_TODO_BY_DESCRIPTION, OPTION_SEARCH_TODO_BY_DATE,
                OPTION_SEARCH_TODO_CANCEL
        };
        String[] descriptions = {
                "Search Todos by Name", "Search Todos by Description", "Search Todos by Date", "Back"
        };

        return getMenuOption(options, descriptions);
    }

    private static int getSearchTodoByDateMenuOption() {
        int[] options = {
                OPTION_SEARCH_TODO_BY_DATE_BEFORE, OPTION_SEARCH_TODO_BY_SAME_DATE, OPTION_SEARCH_TODO_BY_DATE_AFTER,
                OPTION_SEARCH_TODO_BY_DATE_CANCEL
        };
        String[] descriptions = {
                "Search Todos Before This Date", "Search Todos On This Date", "Search Todos After This Date", "Back"
        };

        return getMenuOption(options, descriptions);
    }

    /**
     * Display a menu where a given list of todos are listed for selection.
     * An option to Cancel the operation is automatically added.
     * @param todos the list of todos
     * @return the index of the selected todo or the size of the list if the user chose to cancel the operation
     */
    private static int getTodoListOption(TreeList<Todo> todos) {
        int[] options = new int[todos.size() + 1];
        String[] descriptions = new String[options.length];

        for (int i = 0; i < options.length - 1; i++) {
            options[i] = i + 1;
            descriptions[i] = todos.get(i).toString();
        }

        options[options.length - 1] = options[options.length - 2] + 1;
        descriptions[options.length - 1] = "Cancel";

        int option = getMenuOption(options, descriptions);
        return option - 1;
    }

    /**
     * Reads from standard input and returns it
     * @param message the message to display (should be descriptive)
     * @return the captured input
     */
    private static String readInput(String message) {
        System.out.print(message);
        return scanner.next();
    }

    /**
     * Validates an email address
     * @param email the email to validate
     * @return true if the email is valid. false otherwise
     */
    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    /**
     * Checks if a user is already registered with a given email address
     * @param email the email address to check
     * @return true if such user exists. false otherwise
     */
    private static boolean isUserExists(String email) {
        return users.containsKey(email);
    }

    /**
     * Creates a new user account.
     */
    private static void createAccount() {
        String email = readInput("Enter an email address: ");
        while (!isValidEmail(email)) {
            System.out.println();
            System.out.println("Please enter a valid email address");
            email = readInput("Enter a valid email address: ");
        }

        while (isUserExists(email)) {
            System.out.println();
            System.out.println("This email is already registered, please choose another one");
            email = readInput("Enter a unique email address: ");
        }

        String password = readInput("Choose a password: ");
        while (password.length() < 6) {
            System.out.println();
            System.out.println("Password should be at least 6 characters long!");
            password = readInput("Choose a longer password: ");
        }

        User user = new User(email, password);
        users.put(email, user);

        System.out.println("\nAccount created successfully!");
    }

    /**
     * Logs in a user
     */
    private static void login() {
        String email = readInput("Enter your email address: ");
        String password = readInput("Enter your password: ");

        if (!isUserExists(email)) {
            System.out.println("Invalid email or password");
            return;
        }

        User user = users.get(email);
        if (!user.isPasswordMatched(password)) {
            System.out.println("Invalid email or password");
            return;
        }

        loggedInUser = user;
        System.out.println("\nWelcome back!");
        showLoggedInMenu();
    }

    private static void showLoggedInMenu() {
        while (loggedInUser != null) {
            System.out.println();
            int option = getLoggedInMenuOption();
            System.out.println();

            if (option == OPTION_LOGOUT) {
                loggedInUser = null;
                System.out.println("You're logged out!");
            }
            else if (option == OPTION_CHANGE_PASSWORD)
                changePassword();
            else if (option == OPTION_ADD_TODO)
                addTodo();
            else if (option == OPTION_SHOW_TODOS)
                showTodos();
            else if (option == OPTION_SHOW_ACTIVE_TODOS)
                showActiveTodos();
            else if (option == OPTION_SHOW_COMPLETED_TODOS)
                showCompletedTodos();
            else if (option == OPTION_REMOVE_TODO)
                removeTodo();
            else if (option == OPTION_UPDATE_TODO)
                updateTodo();
            else if (option == OPTION_SEARCH_TODO)
                showSearchTodoMenu();
        }
    }

    /**
     * Changes the password of the currently logged-in user.
     */
    private static void changePassword() {
        String oldPassword = readInput("Enter your current password: ");
        while (!loggedInUser.isPasswordMatched(oldPassword)) {
            System.out.println();
            System.out.println("Invalid old password!");
            oldPassword = readInput("Enter the valid password: ");
        }

        String newPassword = readInput("Enter a new password: ");
        while (newPassword.length() < 6) {
            System.out.println();
            System.out.println("Password should be at least 6 characters long!");
            newPassword = readInput("Choose a longer password: ");
        }

        loggedInUser.setPassword(newPassword);
        System.out.println("\nPassword changed successfully!");
    }

    /**
     * Adds a todo for the currently logged-in user.
     */
    private static void addTodo() {
        String name = readInput("Enter the todo name: ").trim();
        while (name.isEmpty()) {
            System.out.println();
            System.out.println("Todo name is required!");
            name = readInput("Enter the todo name: ").trim();
        }

        String description = readInput("Enter the todo description: ").trim();

        Todo todo = new Todo(name, description);
        loggedInUser.addTodo(todo);

        System.out.println("\nTodo added successfully!");
    }

    /**
     * Displays all the todos for the currently logged-in user.
     */
    private static void showTodos() {
        TreeList<Todo> todos = loggedInUser.getTodos();
        if (todos.isEmpty())
            System.out.println("You have no Todos!");
        else {
            System.out.println("Your Todos:");
            todos.forEach(todo -> System.out.printf("\t%s%n", todo));
        }
    }

    /**
     * Displays all the {@link Todo.TodoStatus#ACTIVE} todos of the currently logged-in user.
     */
    private static void showActiveTodos() {
        TreeList<Todo> todos = loggedInUser.searchTodosByStatus(Todo.TodoStatus.ACTIVE);
        if (todos.isEmpty())
            System.out.println("You have no Active Todos");
        else {
            System.out.println("Your Active Todos:");
            todos.forEach(todo -> System.out.printf("\t%s%n", todo));
        }
    }

    /**
     * Displays all the {@link Todo.TodoStatus#COMPLETED} todos of the currently logged-in user.
     */
    private static void showCompletedTodos() {
        TreeList<Todo> todos = loggedInUser.searchTodosByStatus(Todo.TodoStatus.COMPLETED);
        if (todos.isEmpty())
            System.out.println("You have no Completed Todos");
        else {
            System.out.println("Your Completed Todos:");
            todos.forEach(todo -> System.out.printf("\t%s%n", todo));
        }
    }

    /**
     * Removes a todo for the currently logged-in user.
     */
    private static void removeTodo() {
        TreeList<Todo> todos = loggedInUser.getTodos();
        if (todos.isEmpty())
            System.out.println("You have no Todos!");
        else {
            int index = getTodoListOption(todos);
            if (index != todos.size()) {
                Todo todo = todos.get(index);
                loggedInUser.removeTodo(todo);
                System.out.println("\nTodo removed successfully!");
            }
        }
    }

    /**
     * Updates a todo for the currently logged-in user.
     */
    private static void updateTodo() {
        TreeList<Todo> todos = loggedInUser.getTodos();
        if (todos.isEmpty())
            System.out.println("You have no Todos!");
        else {
            int index = 0;
            while (index != todos.size()) {
                index = getTodoListOption(todos);
                if (index != todos.size()) {
                    currentTodo = todos.get(index);
                    showUpdateTodoMenu();
                }
            }
        }
    }

    private static void showUpdateTodoMenu() {
        while (currentTodo != null) {
            System.out.println();
            int option = getUpdateTodoMenuOption();
            System.out.println();

            if (option == OPTION_UPDATE_TODO_CANCEL)
                currentTodo = null;
            else if (option == OPTION_UPDATE_TODO_NAME)
                updateTodoName();
            else if (option == OPTION_UPDATE_TODO_DESCRIPTION)
                updateTodoDescription();
            else if (option == OPTION_UPDATE_TODO_STATUS)
                updateTodoStatus();
        }
    }

    /**
     * Updates a todo name
     */
    private static void updateTodoName() {
        String name = readInput("Enter the new todo name: ").trim();
        while (name.isEmpty()) {
            System.out.println();
            System.out.println("Todo name is required!");
            name = readInput("Enter the new todo name again: ").trim();
        }

        currentTodo.setName(name);
        System.out.println("\nTodo name updated");
    }

    /**
     * Updates a todo description
     */
    private static void updateTodoDescription() {
        String description = readInput("Enter the new todo description: ").trim();
        currentTodo.setDescription(description);
        System.out.println("\nTodo description updated");
    }

    /**
     * Updates a todo status({@link Todo.TodoStatus})
     */
    private static void updateTodoStatus() {
        System.out.println();
        int option = getUpdateTodoStatusMenuOption();
        System.out.println();

        if (option == OPTION_UPDATE_TODO_STATUS_ACTIVE)
            currentTodo.setStatus(Todo.TodoStatus.ACTIVE);
        else
            currentTodo.setStatus(Todo.TodoStatus.COMPLETED);

        System.out.println("\nTodo status updated!");
    }

    private static void showSearchTodoMenu() {
        int option = 0;
        while (option != OPTION_SEARCH_TODO_CANCEL) {
            System.out.println();
            option = getSearchTodoMenuOption();
            System.out.println();

            if (option == OPTION_SEARCH_TODO_BY_NAME)
                searchTodoByName();
            else if (option == OPTION_SEARCH_TODO_BY_DESCRIPTION)
                searchTodoByDescription();
            else if (option == OPTION_SEARCH_TODO_BY_DATE)
                searchTodoByDate();
        }
    }

    /**
     * Search for todos using their names
     */
    private static void searchTodoByName() {
        String search = readInput("Enter the keyword(s) to search in names: ").trim();
        while (search.isEmpty()) {
            System.out.println("\nSearch keyword cannot be empty!");
            search = readInput("Enter the keyword(s) to search again: ").trim();
        }

        TreeList<Todo> todos = loggedInUser.searchTodosByName(search);
        if (todos.isEmpty())
            System.out.println("No Todo matched your query!");
        else {
            System.out.println("Todos Found:");
            todos.forEach(todo -> System.out.printf("\t%s%n", todo));
        }
    }

    /**
     * Searches for todos using their description
     */
    private static void searchTodoByDescription() {
        String search = readInput("Enter the keyword(s) to search in descriptions: ").trim();
        while (search.isEmpty()) {
            System.out.println("\nSearch keyword cannot be empty!");
            search = readInput("Enter the keyword(s) to search again: ").trim();
        }

        TreeList<Todo> todos = loggedInUser.searchTodosByDescription(search);
        if (todos.isEmpty())
            System.out.println("No Todo matched your query!");
        else {
            System.out.println("Todos Found:");
            todos.forEach(todo -> System.out.printf("\t%s%n", todo));
        }
    }

    /**
     * Searches for todos using their creation date
     */
    private static void searchTodoByDate() {
        String date = readInput("Enter the date using the format 'Year-Month-Day' (eg 2021-10-08). Enter -1 to use the current date: ").trim();
        while (!date.equals("-1")) {
            try {
                LocalDate.parse(date);
                break;
            } catch (Exception e) {
                System.out.println("\nPlease enter a valid date!");
                date = readInput("Enter the date using the format 'Year-Month-Day' (eg 2021-10-08). Enter -1 to use the current date: ").trim();
            }
        }

        if (date.equals("-1")) {
            date = LocalDate.now(ZoneId.systemDefault()).toString();
            System.out.println("Using current date: " + date + "\n");
        }

        String time = readInput("Enter the time using the format 'Hour-Minute-Second' (eg 12:35:00). Enter -1 to use the current time: ").trim();
        while (!time.equals("-1")) {
            try {
                LocalDateTime.parse(LocalDate.now() + "T" + time);
                break;
            } catch (Exception e) {
                System.out.println("\nPlease enter a valid time!");
                time = readInput("Enter the time using the format 'Hour-Minute-Second' (eg 12:35:00). Enter -1 to use the current time: ").trim();
            }
        }

        if (time.equals("-1")) {
            time = LocalDateTime.now(ZoneId.systemDefault()).toString().split("T")[1];
            System.out.println("Using current time: " + time + "\n");
        }

        String queryDate = date + "T" + time;
        LocalDateTime dateTime = LocalDateTime.parse(queryDate);
        Todo.TodoLookupDateStrategy lookupDateStrategy =  null;

        int option = 0;
        while (option != OPTION_SEARCH_TODO_BY_DATE_CANCEL) {
            System.out.println("\nUsing Date: " + dateTime);
            option = getSearchTodoByDateMenuOption();
            System.out.println();

            if (option == OPTION_SEARCH_TODO_BY_DATE_BEFORE)
                lookupDateStrategy = Todo.TodoLookupDateStrategy.BEFORE_DATE;
            else if (option == OPTION_SEARCH_TODO_BY_SAME_DATE)
                lookupDateStrategy = Todo.TodoLookupDateStrategy.SAME_DATE;
            else if (option == OPTION_SEARCH_TODO_BY_DATE_AFTER)
                lookupDateStrategy = Todo.TodoLookupDateStrategy.AFTER_DATE;

            if (lookupDateStrategy != null && option != OPTION_SEARCH_TODO_BY_DATE_CANCEL ) {
                TreeList<Todo> todos = loggedInUser.searchTodosByDate(dateTime, lookupDateStrategy);
                if (todos.isEmpty())
                    System.out.println("No Todo matched your query!");
                else {
                    System.out.println("Todos Found:");
                    todos.forEach(todo -> System.out.printf("\t%s%n", todo));
                }
            }
        }
    }
}