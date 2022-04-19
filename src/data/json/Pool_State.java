package data.json;

import java.util.ArrayList;
import java.util.List;

public class Pool_State {

    int current_difficulty = -1;
    int current_points = -1;
    int next_farmer_update = -1;
    int next_pool_info_update = -1;

    String p2_singleton_puzzle_hash = "";
    String authentication_token_timeout = "";

    int points_acknowledged_since_start;
    int points_found_since_start;

    List<Ack24h> points_acknowledged_24h = new ArrayList<>();
    List<Error24h> pool_errors_24h = new ArrayList<>();
    List<Found24h> points_found_24h = new ArrayList<>();
    List<PoolConfig> pool_config = new ArrayList<>();


    public String getAuthenticationTokenTimeout() {
        return authentication_token_timeout;
    }

    public int getCurrentDifficulty() {
        return current_difficulty;
    }

    public int getCurrentPoints() {
        return current_points;
    }

    public int getNextFarmerUpdate() {
        return next_farmer_update;
    }

    public int getNextPoolInfoUpdate() {
        return next_pool_info_update;
    }

    public String getP2SingletonPuzzleHash() {
        return p2_singleton_puzzle_hash;
    }

    public List<Ack24h> getPointsAcknowledged24h() {
        return points_acknowledged_24h;
    }

    public int getPointsAcknowledgedSinceStart() {
        return points_acknowledged_since_start;
    }

    public List<Found24h> getPointsFound24h() {
        return points_found_24h;
    }

    public List<Error24h> getPoolErrors24h() {
        return pool_errors_24h;
    }

    public int getPointsFoundSinceStart() {
        return points_found_since_start;
    }

    public List<PoolConfig> getPoolConfig() {
        return pool_config;
    }

    public class Pool_Config {

        String launcher_id = "";
        String owner_public_key = "";
        String p2_singleton_puzzle_hash = "";
        String payout_instructions = "";
        String pool_url = "";
        String target_puzzle_hash = "";
        String authentication_public_key = "";

        public String getAuthenticationPublicKey() {
            return authentication_public_key;
        }

        public String getLauncher_ID() {
            return launcher_id;
        }

        public String getOwnerPublicKey() {
            return owner_public_key;
        }

        public String getP2SingletonPuzzleHash() {
            return p2_singleton_puzzle_hash;
        }

        public String getPayoutInstructions() {
            return payout_instructions;
        }

        public String getPoolURL() {
            return pool_url;
        }

        public String getTargetPuzzleHash() {
            return target_puzzle_hash;
        }


    }

    public class PoolConfig {
        String authentication_public_key;
        String launcher_id;
        String owner_public_key;
        String p2_singleton_puzzle_hash;
        String payout_instructions;
        String pool_url;
        String target_puzzle_hash;

    }

    public class Error24h {
        double time;
        int count;

        public int getCount() {
            return count;
        }
    }
    public class Ack24h {
        double time;
        int count;

        public int getCount() {
            return count;
        }
    }
    public class Found24h {
        double time;
        int count;

        public int getCount() {
            return count;
        }
    }


}
