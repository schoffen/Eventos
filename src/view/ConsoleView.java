package view;

import java.util.List;
import java.util.Scanner;
import model.Event;
import model.EventCategory;

public class ConsoleView {
    private final Scanner scanner = new Scanner(System.in);
    private static final String LINE = "========================================";

    public String prompt(String message) {
        System.out.print("[user] " + message.trim() + ": ");
        return scanner.nextLine().trim();
    }

    public void showCreateUser() {
        System.out.println(LINE);
        System.out.println("    CADASTRO DE USUÁRIO");
        System.out.println(LINE);
    }

    public void showInitialMenu(String username) {
        System.out.println();
        System.out.println(LINE);
        System.out.printf(" Bem-vindo, %s!%n", username);
        System.out.println(LINE);
        System.out.println("1) Cadastrar evento");
        System.out.println("2) Listar todos os eventos");
        System.out.println("3) Listar meus eventos");
        System.out.println("4) Participar/Desistir de um evento");
        System.out.println("5) Listar eventos passados");
        System.out.println("6) Sair");
        System.out.println(LINE);
    }

    public void showMessage(String message) {
        System.out.println("[system] " + message);
    }

    public void showCategories(List<EventCategory> categories) {
        System.out.println(LINE);
        System.out.println(" CATEGORIAS DISPONÍVEIS");
        System.out.println(LINE);
        for (int i = 0; i < categories.size(); i++) {
            System.out.printf(" %d) %s%n", i + 1, categories.get(i).name());
        }
        System.out.println(LINE);
    }

    public void listAllEvents(List<Event> events) {
        System.out.println(LINE);
        if (events.isEmpty()) {
            System.out.println("Nenhum evento encontrado.");
        } else {
            System.out.println(" EVENTOS DISPONÍVEIS");
            System.out.println(LINE);
            for (Event event : events) {
                System.out.println(event.toString());
            }
        }
    }
}
