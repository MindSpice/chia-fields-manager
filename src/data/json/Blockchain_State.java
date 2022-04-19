package data.json;

import java.math.BigDecimal;
import java.math.MathContext;

public class Blockchain_State {

    int mempool_size = -1;
    long weight = -1;
    int height = -1;
    int fees = -1;
    int difficulty = -1;

    String overflow = "";

    BigDecimal space;
    //TODO hardcode value instead of using math library for the whole calculation you derp.
    BigDecimal EiB = new BigDecimal("1024").pow(6);
    Peak peak;
    Sync sync;

    public int getMempoolSize() {
        return mempool_size;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public BigDecimal getSpace() {
        return space.divide(EiB).round(new MathContext(5));
    }

    public Peak getPeak() {
        return peak;
    }

    public Sync getSync() {
        return sync;
    }


    public class Peak {
        long weight;
        int signage_point_index;
        int height;
        int deficit;
        boolean overflow;

        public int getSingagePointIndex() {
            return signage_point_index;
        }

        public int getHeight() {
            return height;
        }

        public long getWeight() {
            return weight;
        }

        public boolean getOverflow() {
            return overflow;
        }

        public int getDeficit() {
            return deficit;
        }
    }


    public class Sync {

        boolean sync_mode;
        boolean synced;
        int sync_progress_height = -1;
        int sync_tip_height = -1;

        public int getSyncProgressHeight() {
            return sync_progress_height;
        }

        public int getSyncTipHeight() {
            return sync_tip_height;
        }

        public boolean isSyncMode() {
            return sync_mode;
        }

        public boolean isSynced() {
            return synced;
        }
    }
}


