package connections;

import data.json.Connection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rpc.RPCNode;
import settings.Settings;


import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.*;

public class ConMonitor {


    private ObservableList<Connection> connectionList = FXCollections.observableArrayList();
    private List<Peer> watchedPeers = new ArrayList<>();
    private List<String> watcherLog = new ArrayList<>();
    int wlSize = 0;

    RPCNode node;

    public ConMonitor(RPCNode node){
        this.node = node;
        this.connectionList = node.getConnections();
    }

    public ObservableList<Connection> getConnectionList() {
        return connectionList;
    }

    public boolean newWatcherLog() {
        if (watcherLog.size() > wlSize) {
            wlSize = watcherLog.size();
            return true;
        } else {
            return false;
        }
    }
    //TODO : add resize to addWatcher lock get
    public List<String> getWatcherLog() {
        if ( watcherLog.size() > 200) {
            watcherLog.subList(0, 100);
        }
        return watcherLog;
    }

    private void addLog(Peer.InfractionType type, String ip){
        watcherLog.add(type + " Infraction On: " + ip);

    }
    private void addLog(String ip){
        watcherLog.add("Removed: " + ip);

    }

    /**
     * Updates existing object with new peer connections
     *
     * @param connectionList
     */
    public void updateConnections(ObservableList<Connection> connectionList) {
        this.connectionList = connectionList;
    }


    /**
     * @returns long of most frequent occurring block height among peers
     */
    private long getMostFreqHeight() {  //FIXME Long
        List<Integer> heights = new ArrayList<>();

        for (Connection c : connectionList) {
            if (c.getPeakHeight() > 0) {                    //skips localhost with -1 height
                heights.add(c.getPeakHeight());
            }
        }
        Map<Integer, Integer> map = new HashMap<>();
        for (Integer h : heights) {
            Integer height = map.get(h);
            map.put(h, height != null ? height + 1 : 1);

        }
        Integer mostFrequent = Collections.max(map.entrySet(),
                new Comparator<Map.Entry<Integer, Integer>>() {
                    @Override
                    public int compare(Map.Entry<Integer, Integer> height0, Map.Entry<Integer, Integer> height1) {
                        return height0.getValue().compareTo(height1.getValue());
                    }
                }).getKey();
        return mostFrequent;
    }

//    private long getMostFreqHeight() {  //FIXME Long
//        List<Long> heights = new ArrayList<>();
//
//        for (Connection c : connectionList) {
//            if (c.getPeakHeight() > 0) {                    //skips localhost with -1 height
//                heights.add(c.getPeakHeight());
//            }
//        }
//        Map<Long, Long> map = new HashMap<>();
//        for (Long h : heights) {
//            Long height = map.get(h);
//            map.put(h, height != null ? height + 1 : 1);
//
//        }
//        Long mostFrequent = Collections.max(map.entrySet(),
//                new Comparator<Map.Entry<Long, Long>>() {
//                    @Override
//                    public int compare(Map.Entry<Long, Long> height0, Map.Entry<Long, Long> height1) {
//                        return height0.getValue().compareTo(height1.getValue());
//                    }
//                }).getKey();
//        return mostFrequent;
//    }

    public void closeConnection(String nodeID) {
        node.closeConnection(nodeID);
    }


    /**
     * @param node_id
     * @return true if peer is already on watchlist
     */
    private Peer isWatchedPeer(String node_id) {
        for (Peer p : watchedPeers) {

            if (p.getNodeID().equals(node_id)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Run a single pass of the watcher on instanced objects current connection list
     * Calls Peer Infraction if needed
     * References' threshold for infractions from settings
     */
    public void watcher() {

        Peer p;
        long mostFreqHeight = getMostFreqHeight();


        for (Connection c : connectionList) {
            if (c.getPeakHeight() <= (mostFreqHeight - Settings.PeerWatcher.getLightThreshold())
                    && ((mostFreqHeight - c.getPeakHeight()) < Settings.PeerWatcher.getIgnoreSyncThreshold())) {
                if (isWatchedPeer(c.getNodeID()) != null) {
                    p = isWatchedPeer(c.getNodeID());
                    watchedPeers.add(p);
                }
                else {
                    p = new Peer(c.getNodeID(), c.getHost());
                    watchedPeers.add(p);
                }
                if ((mostFreqHeight - c.getPeakHeight()) >= Settings.PeerWatcher.getHeavyThreshold()) {
                    if (p.addInfraction(Peer.InfractionType.HEAVY, LocalDateTime.now())) {
                        node.closeConnection(p.getNodeID());
                        addLog(c.getHost());
                    }
                    else {
                        addLog(Peer.InfractionType.HEAVY,  c.getHost());
                        System.out.println(1);
                    }
                } else if ((mostFreqHeight - c.getPeakHeight()) >= Settings.PeerWatcher.getMediumThreshold()) {
                    if (p.addInfraction(Peer.InfractionType.MEDIUM, LocalDateTime.now())) {
                        node.closeConnection(p.getNodeID());
                        addLog(c.getHost());
                    }
                    else {
                        addLog(Peer.InfractionType.MEDIUM,  c.getHost());
                        System.out.println(2);
                    }
                } else if ((mostFreqHeight - c.getPeakHeight()) >= Settings.PeerWatcher.getLightThreshold()) {
                    if (p.addInfraction(Peer.InfractionType.LIGHT, LocalDateTime.now())) {
                        node.closeConnection(p.getNodeID());
                        addLog(c.getHost());
                    }
                    else {
                        addLog(Peer.InfractionType.LIGHT,  c.getHost());
                        System.out.println(3);
                    }
                }
            }
        }
    }
}




