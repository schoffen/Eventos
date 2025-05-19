package app;

import java.util.Scanner;

import controller.EventController;
import controller.UserController;
import view.ConsoleView;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        ConsoleView consoleView = new ConsoleView();

        UserController userController = new UserController(consoleView, scanner);
        EventController eventController = new EventController(userController, scanner);

        eventController.start();
    }
}
