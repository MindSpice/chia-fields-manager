package data.tail;

import settings.Settings;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class SignagePoint extends DateTime {
    int index = 0;
    long timeSinceLast = 0;
    boolean warn = false;
    Warning warning;

    public boolean isWarn() {
        return warn;
    }

    public Warning getWarning() {
        return warning;
    }

    enum Warning {LOW, HIGH};

    public SignagePoint(String dateTime, int index) {
        super(dateTime);
        this.index = index;
    }


    // TODO Implement method to count and keep track of SP of a SubSlot
    // TODO Will need logic to be resistant to SP scrambling


    public SignagePoint(String dateTime, int index, LocalTime precedingSP) {
        super(dateTime);
        this.index = index;
        timeSinceLast = precedingSP.until(super.time, (ChronoUnit.MICROS));
        if (index == 1) {
            timeSinceLast /= 2;  // Hackish fix for oddity in some first signage points. Slot overflow maybe?
        }

        if (timeSinceLast > Settings.Alerts.SP.getSpWarnHigh()) {
            warn = true;
            warning = warning.HIGH;
        } else if (timeSinceLast > Settings.Alerts.SP.getSpWarnLow()) {
            warn = true;
            warning = warning.LOW;
        }
    }

    //private void calcTimeSince(long )

    public int getIndex() {
        return index;
    }

    public long getTimeSinceLast() {
        return timeSinceLast;
    }


}
