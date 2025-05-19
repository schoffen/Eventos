package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

import model.Event;

public class EventRepository {
    private final Path filePath;
    private final List<Event> events;

    public EventRepository() {
        this(Path.of("events.txt"));
    }

    public EventRepository(Path filePath) {
        this.filePath = filePath;
        this.events = new ArrayList<>();
        loadFromFile();
    }

    public List<Event> getEvents() {
        return Collections.unmodifiableList(events);
    }

    public Event findByName(String name) {
        for (Event ev : events) {
            if (ev.getName().equalsIgnoreCase(name)) {
                return ev;
            }
        }
        return null;
    }

    public void save(Event event) {
        Objects.requireNonNull(event, "Evento não pode ser nulo");
        try (BufferedWriter writer = Files.newBufferedWriter(
                filePath,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            writer.write(event.serialize());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao salvar evento: " + e.getMessage());
        }
        reload();
    }

    private void loadFromFile() {
        events.clear();
        if (!Files.exists(filePath)) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    events.add(Event.deserialize(line));
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar eventos: " + e.getMessage());
        }
    }

    private void reload() {
        loadFromFile();
    }
}
