package connections;

import rpc.RPCNode;
import settings.Settings;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Peer {

    public enum InfractionType {
        HEAVY,
        MEDIUM,
        LIGHT
    };

    int InfractionPoints = 0;
    String node_id;
    String peer_host;
    private List<Infraction> infractionList = new ArrayList<>();
    Infraction infraction;

    public Peer(String node_id, String peer_host) {
        this.node_id = node_id;
        this.peer_host = peer_host;
    }

    /**
     * Adds infraction to peer and checks if ban threshold has been crossed yet.
     *
     * @param infractionType Weight of infraction
     * @param dateTime       Time of Infraction
     * @return True if peer triggers a ban
     */
    public boolean addInfraction(InfractionType infractionType, LocalDateTime dateTime) {
        rollOffCheck();
        switch (infractionType) {

            case HEAVY -> {
                Infraction infraction = new Infraction(Settings.PeerWatcher.getHeavyPoints(), dateTime);
                infractionList.add(infraction);
                return banCheck();
            }
            case MEDIUM -> {
                Infraction infraction = new Infraction(Settings.PeerWatcher.getMediumPoints(), dateTime);
                infractionList.add(infraction);
                return banCheck();
            }
            case LIGHT -> {
                Infraction infraction = new Infraction(Settings.PeerWatcher.getLightPoints(), dateTime);
                infractionList.add(infraction);
                return banCheck();
            }
        }
        return false;
    }

    /**
     * Calculates peers standing infraction points total
     * Bans if pass ban threshold
     *
     * @return true if banned
     */
    public boolean banCheck() {
        int totalPoints = 0;

        for (Infraction i : infractionList) {
            totalPoints += i.getPoints();
        }

        if (totalPoints > Settings.PeerWatcher.getBanThreshold()) {
            return true;
        } else {
            return false;
        }
    }

    public void rollOffCheck() {

        for(Iterator<Infraction> infractionIterator = infractionList.iterator(); infractionIterator.hasNext(); ) {
            Infraction infraction = infractionIterator.next();
            if (infraction.getDateTime().plusMinutes(Settings.PeerWatcher.getRollOffTime()).isBefore(LocalDateTime.now())) {
                infractionIterator.remove();
            }
        }
        //for (Infraction i : infractionList) {
            //if (i.getDateTime().plusMinutes(Settings.PeerWatcher.getRollOffTime()).isBefore(LocalDateTime.now())) {
                //infractionList.remove(i);
           // }
       // }
    }


    public String getNodeID() {
        return node_id;
    }

    private class Infraction {
        int points = 0;
        LocalDateTime dateTime;

        public Infraction(int points, LocalDateTime dateTime) {
            this.points = points;
            this.dateTime = dateTime;
        }

        public int getPoints() {
            return points;
        }

        public LocalDateTime getDateTime() {
            return dateTime;
        }
    }
}

