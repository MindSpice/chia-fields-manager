package tailer;

import endpoints.Paths;
import org.apache.commons.io.input.TailerListener;
import settings.Settings;

import java.io.File;
import java.io.IOException;

public class Tailer {

    private Thread thread;
    public boolean stop;
    String pid = "Tailer Tread";
    org.apache.commons.io.input.Tailer tailer;

    public Tailer(TailerListener listener, Paths path) {
            File file = new File(path.getLogPath());
            tailer = new org.apache.commons.io.input.Tailer(file, listener, 2000, Settings.tailFromEndOfFile, true);
            thread = new Thread(tailer, pid);
    }


    public void stop() {
        tailer.stop();
        thread.interrupt();

    }

    public void start() {
        thread.start();
    }


}
