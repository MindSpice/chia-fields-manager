package data.json;

import java.util.ArrayList;
import java.util.List;

public class Signage_Points {

    List<String> proofs = new ArrayList<>();
    Signage_Point signage_point;

    public boolean getProofs() {
        if (!proofs.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public Signage_Point getSignage_Point() {
        return signage_point;
    }

    public class Signage_Point {

        String challengeChainSp = "";
        String challenge_hash = "";
        String reward_chain_sp = "";
        int signage_point_index = -1;
        long sub_slot_iters = -1;
        int difficulty = -1;

        public List<String> getProofs() {
            return proofs;
        }

        public String getChallengeChainSp() {
            return challengeChainSp;
        }

        public String getChallengeHash() {
            return challenge_hash;
        }

        public String getRewardChainSp() {
            return reward_chain_sp;
        }

        public int getIndex() {
            return signage_point_index;
        }

        public long getSubSlotIters() {
            return sub_slot_iters;
        }

        public int getDifficulty() {
            return difficulty;
        }
    }
}



