package controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import model.Event;
import model.EventCategory;
import repository.EventRepository;
import view.ConsoleView;

public class EventController {
    private final UserController userController;
    private final ConsoleView consoleView;
    private final EventRepository repository;
    private final Scanner scanner;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public EventController(UserController userController, Scanner scanner) {
        this.userController = userController;
        this.consoleView = new ConsoleView();
        this.repository = new EventRepository();
        this.scanner = scanner;
    }

    public void start() {
        String option;
        do {
            consoleView.showInitialMenu(userController.getUser().getName());
            option = consoleView.prompt("Escolha uma opção: ");
            handleOption(option);
        } while (!"6".equals(option));
        consoleView.showMessage("Encerrando sessão. Até logo!");
    }

    private void handleOption(String option) {
        switch (option) {
            case "1" -> createEvent();
            case "2" -> listAllEvents();
            case "3" -> listUsersEvents();
            case "4" -> manageParticipation();
            case "5" -> listPastEvents();
            case "6" -> {
                // Exit
            }
            default -> consoleView.showMessage("Opção inválida. Tente novamente.");
        }
    }

    private void createEvent() {
        String name = consoleView.prompt("Nome: ");
        String address = consoleView.prompt("Endereço: ");
        EventCategory category = selectCategory();
        String dateStr = selectValidDate();
        LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
        String description = consoleView.prompt("Descrição: ");

        Event event = new Event(name, address, category, dateTime, description);
        repository.save(event);
        consoleView.showMessage("Evento criado com sucesso: " + name);
    }

    private String selectValidDate() {
        while (true) {
            String dateStr = consoleView.prompt("Data (dd/MM/yyyy HH:mm): ");
            try {
                LocalDateTime.parse(dateStr, formatter);
                return dateStr;
            } catch (DateTimeParseException e) {
                consoleView.showMessage("Formato inválido! Use dd/MM/yyyy HH:mm. Tente novamente.");
            }
        }
    }

    private EventCategory selectCategory() {
        List<EventCategory> categories = List.of(EventCategory.values());
        consoleView.showCategories(categories);
        while (true) {
            try {
                consoleView.showMessage("Digite o número da categoria: ");
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= 1 && choice <= categories.size()) {
                    return categories.get(choice - 1);
                }
            } catch (NumberFormatException e) {
                // ignore and retry
            }
            consoleView.showMessage("Categoria inválida. Tente novamente.");
        }
    }

    private void listAllEvents() {
        LocalDateTime now = LocalDateTime.now();
        List<Event> sorted = repository.getEvents().stream()
            .sorted(Comparator.comparing(Event::getDateTime))
            .collect(Collectors.toList());

        consoleView.showMessage("\n== Eventos (próximos e atuais) ==");
        for (Event event : sorted) {
            LocalDateTime eventTime = event.getDateTime();
            String status;
            if (eventTime.isEqual(now)) {
                status = "(Ocorrendo agora)";
            } else if (eventTime.isBefore(now)) {
                status = "(Já ocorreu)";
            } else {
                status = "(Acontecerá)";
            }
            consoleView.showMessage(String.format("%s - %s %s", eventTime.format(formatter), event.getName(), status));
        }
    }

    private void listPastEvents() {
        LocalDateTime now = LocalDateTime.now();
        List<Event> past = repository.getEvents().stream()
            .filter(e -> e.getDateTime().isBefore(now))
            .sorted(Comparator.comparing(Event::getDateTime))
            .collect(Collectors.toList());

        consoleView.showMessage("\n== Eventos Passados ==");
        if (past.isEmpty()) {
            consoleView.showMessage("Nenhum evento já ocorreu.");
        } else {
            for (Event event : past) {
                consoleView.showMessage(String.format("%s - %s", event.getDateTime().format(formatter), event.getName()));
            }
        }
    }

    private void listUsersEvents() {
        consoleView.listAllEvents(userController.getUser().getEvents());
    }

    private void manageParticipation() {
        String eventName = consoleView.prompt("Nome do evento: ");
        Event event = repository.findByName(eventName);
        if (event == null) {
            return;
        }

        if (userController.getUser().getEvents().contains(event)) {
            userController.removeEvent(event);
        } else {
            userController.addEvent(event);
        }
    }
}
