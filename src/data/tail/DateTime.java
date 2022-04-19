package data.tail;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTime {
    LocalDateTime dateTime;
    LocalDate date;
    LocalTime time;

    //TODO HH?

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("H:mm:ss.SSS");

    //TODO CLEAN UP
    public DateTime(String dateTime) {
        //this.dateTime = dateTime;

        String tmpTime = dateTime.substring(11, 23);
        String tmpDate = dateTime.substring(0, 10);
        this.dateTime = LocalDateTime.parse(dateTime);
        date = LocalDate.parse(tmpDate);
        time = LocalTime.parse(tmpTime, dtf);
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }


}
