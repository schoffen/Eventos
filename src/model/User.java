package model;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class User {
    private final String name;
    private final String email;
    private final String phone;
    private final List<Event> participatingEvents;

    public User(String name, String email, String phone) {
        this.name = validateString(name, "Nome");
        this.email = validateString(email, "Email");
        this.phone = validateString(phone, "Telefone");
        this.participatingEvents = new ArrayList<>();
    }

    private String validateString(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " não pode ser nulo");
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " não pode ser vazio");
        }
        return trimmed;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public List<Event> getEvents() {
        return Collections.unmodifiableList(participatingEvents);
    }

    public boolean addEvent(Event event) {
        Objects.requireNonNull(event, "Evento não pode ser nulo");
        if (!participatingEvents.contains(event)) {
            return participatingEvents.add(event);
        }
        return false;
    }

    public boolean removeEvent(Event event) {
        Objects.requireNonNull(event, "Evento não pode ser nulo");
        return participatingEvents.remove(event);
    }

    @Override
    public String toString() {
        return String.format("User[name=%s, email=%s, phone=%s, events=%d]",
            name, email, phone, participatingEvents.size());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User other = (User) o;
        return name.equalsIgnoreCase(other.name)
            && email.equalsIgnoreCase(other.email)
            && phone.equals(other.phone)
            && participatingEvents.equals(other.participatingEvents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase(), email.toLowerCase(), phone, participatingEvents);
    }
}
