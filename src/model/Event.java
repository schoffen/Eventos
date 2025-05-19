package model;

import java.time.LocalDateTime;
import java.util.Objects;
import util.EventDateFormatter;

public class Event {
    private final String name;
    private final String address;
    private final EventCategory category;
    private final LocalDateTime dateTime;
    private final String description;

    public Event(String name, String address, EventCategory category, LocalDateTime dateTime, String description) {
        this.name = Objects.requireNonNull(name, "Nome não pode ser nulo");
        this.address = Objects.requireNonNull(address, "Endereço não pode ser nulo");
        this.category = Objects.requireNonNull(category, "Categoria não pode ser nula");
        this.dateTime = Objects.requireNonNull(dateTime, "Data/hora não pode ser nula");
        this.description = Objects.requireNonNull(description, "Descrição não pode ser nula");
    }

    public Event(String name, String address, EventCategory category, String dateTimeStr, String description) {
        this(name, address, category, EventDateFormatter.deserialize(dateTimeStr), description);
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public EventCategory getCategory() {
        return category;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPastEvent() {
        return dateTime.isBefore(LocalDateTime.now());
    }

    public String serialize() {
        return String.join(" | ",
            name,
            address,
            category.name(),
            EventDateFormatter.serialize(dateTime),
            description
        );
    }

    public static Event deserialize(String line) {
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Linha inválida para desserialização: " + line);
        }
        return new Event(
            parts[0].trim(),
            parts[1].trim(),
            EventCategory.valueOf(parts[2].trim()),
            parts[3].trim(),
            parts[4].trim()
        );
    }

    @Override
    public String toString() {
        return String.format(
            "%s [categoria=%s, data=%s, endereço=%s] - %s",
            name,
            category,
            EventDateFormatter.serialize(dateTime),
            address,
            description
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event other = (Event) o;
        return name.equalsIgnoreCase(other.name)
            && address.equalsIgnoreCase(other.address)
            && category == other.category
            && dateTime.equals(other.dateTime)
            && description.equals(other.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase(), address.toLowerCase(), category, dateTime, description);
    }
}
