package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventDateFormatter {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static LocalDateTime deserialize(String date) {
        return LocalDateTime.parse(date, formatter);
    }

    public static String serialize(LocalDateTime date) {
        return date.format(formatter);
    }
}
