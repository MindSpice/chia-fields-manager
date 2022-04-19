package tailer;

import org.apache.commons.io.input.TailerListenerAdapter;

public class Listener extends TailerListenerAdapter {
    public void handle(String line) {
        System.out.println(line);
    }
}