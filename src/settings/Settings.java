package settings;

import java.util.ArrayList;
import java.util.List;

public class Settings {

    List<MonitorSettings> Monitors = new ArrayList<>();
    public static boolean tailFromEndOfFile = false;


    public class Alerts {

        public static class SP {
            static int spWarnLow = 12500000; ///FIXME Do these need so much precision?
            static int spWarnHigh = 15000000;
            static boolean ignoreFirstSp = true;

            public static int getSpWarnLow() {
                return spWarnLow;
            }

            public static int getSpWarnHigh() {
                return spWarnHigh;
            }

            public static boolean isIgnoreFirstSp() {
                return ignoreFirstSp;
            }
        }
    }

    public static class PeerWatcher {

        static int heavyThreshold = 8;
        static int mediumThreshold = 5;
        static int lightThreshold = 3;
        static int heavyPoints = 5;
        static int mediumPoints = 3;
        static int lightPoints = 2;
        static int banThreshold = 19;
        static int ignoreSyncThreshold = 200;
        static int rollOffMinutes = 30;

        public static int getHeavyThreshold() {
            return heavyThreshold;
        }

        public static int getMediumThreshold() {
            return mediumThreshold;
        }

        public static int getLightThreshold() {
            return lightThreshold;
        }

        public static int getHeavyPoints() {
            return heavyPoints;
        }

        public static int getMediumPoints() {
            return mediumPoints;
        }

        public static int getLightPoints() {
            return lightPoints;
        }

        public static int getBanThreshold() {
            return banThreshold;
        }

        public static int getIgnoreSyncThreshold() {
            return ignoreSyncThreshold;
        }

        public static int getRollOffTime() {
            return rollOffMinutes;
        }
    }


}