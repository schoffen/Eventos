package controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import model.Event;
import model.User;
import view.ConsoleView;

public class UserController {
    private final User user;
    private final ConsoleView consoleView;

    public UserController(ConsoleView consoleView, Scanner scanner) {
        this.consoleView = consoleView;
        this.user = initializeUser();
    }

    private User initializeUser() {
        consoleView.showCreateUser();
        String name = promptNonEmpty("Nome: ");
        String email = promptNonEmpty("Email: ");
        String phone = promptPhone("Telefone (apenas números): ");
        consoleView.showMessage(String.format("Usuário criado: %s (%s) - %s", name, email, phone));
        return new User(name, email, phone);
    }

    private String promptNonEmpty(String prompt) {
        String input;
        do {
            input = consoleView.prompt(prompt).trim();
            if (input.isEmpty()) {
                consoleView.showMessage("Entrada inválida. O valor não pode ser vazio.");
            }
        } while (input.isEmpty());
        return input;
    }

    private String promptPhone(String prompt) {
        String input;
        do {
            input = consoleView.prompt(prompt).trim();
            if (!input.matches("\\d{8,15}")) {
                consoleView.showMessage("Telefone inválido. Informe apenas dígitos (8 a 15 caracteres). Tente novamente.");
                input = "";
            }
        } while (input.isEmpty());
        return input;
    }

    public User getUser() {
        return user;
    }

    public void addEvent(Event event) {
        LocalDateTime now = LocalDateTime.now();
        if (event.getDateTime().isBefore(now)) {
            consoleView.showMessage("Não é possível adicionar o evento '" 
                + event.getName() + "' pois ele já ocorreu em " 
                + event.getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + ".");
            return;
        }
        user.addEvent(event);
        consoleView.showMessage("Evento '" + event.getName() + "' adicionado ao usuário.");
    }

    public void removeEvent(Event event) {
        LocalDateTime now = LocalDateTime.now();
        if (event.getDateTime().isBefore(now)) {
            consoleView.showMessage("Não é possível remover o evento '" 
                + event.getName() + "' pois ele já ocorreu em " 
                + event.getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + ".");
            return;
        }
        user.removeEvent(event);
        consoleView.showMessage("Evento '" + event.getName() + "' removido do usuário.");
    }
}
