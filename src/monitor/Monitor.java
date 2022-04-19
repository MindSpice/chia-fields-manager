package monitor;

import connections.ConMonitor;
import data.json.Blockchain_State;
import data.json.Connection;
import data.json.Pool_State;
import data.tail.CachedPoint;
import data.tail.FilterPass;
import data.tail.SignagePoint;
import endpoints.Certs;
import endpoints.Paths;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import logic.Logic;
import rpc.*;
import tailer.Parser;
import tailer.Tailer;

import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static data.tail.FilterPass.Warning.*;

public class Monitor {

    DecimalFormat df = new DecimalFormat("#0.000");
    DecimalFormat dfp = new DecimalFormat("#0.00");
    private FarmState farmState = new FarmState();
    private Metrics metrics = new Metrics();
    private Pool pool = new Pool();
    private String name = "";
    private String service = "";
    private String address = "";
    private int epochDiff  = 0;
    private int proofCount = 0;
    private int spLastIter = 0;
    private int fpLastIter = 0;
    private boolean wallet;
    private boolean pooling;
    private boolean farming;

    ArrayList<SignagePoint> spList;
    ArrayList<FilterPass> fpList;
    ArrayList<CachedPoint> cpList;
    private ObservableList<String> farmActivity = FXCollections.observableArrayList("");

    Paths paths;
    Certs certs;
    MonitorType monitorType;
    RPCNode rpcNode;
    RPCFarmer rpcFarmer;
    RPCHarvester rpcHarvester;
    RPCWallet rpcWallet;
    MonitorType MonitorType;
    ConMonitor conMonitor;

    public enum MonitorType {
        NODE,
        HARVESTER,
        FARMER
    }

    public Monitor(String monitorName, MonitorType monitorType, Paths paths, Certs certs,boolean farming, boolean pooling, boolean wallet) {
        this.monitorType = monitorType;
        this.name = monitorName;
        this.paths = paths;
        this.certs = certs;
        this.service = monitorType.toString();
        this.address = paths.getAddress();
        this.pooling = pooling;
        this.wallet = wallet;
        this.farming = farming;
        setMonitorType(monitorType);
    }
    public Monitor() {
    }

    private void setMonitorType(MonitorType monitorType) {
        switch (monitorType) {

            case NODE -> {
                rpcFarmer = new RPCFarmer(certs, paths);
                rpcNode = new RPCNode(certs, paths);
                Parser parser = new Parser(true, false);
                spList = parser.getSignagePointList();
                cpList = parser.getCachedPointList();
                conMonitor = new ConMonitor(rpcNode);
                Tailer tailer = new Tailer(parser, paths);
                tailer.start();
            }
            case HARVESTER -> {
                Parser parser = new Parser(false, true);
                fpList = parser.getFilterPassList();
                Tailer tailer = new Tailer(parser, paths);
                tailer.start();
            }
            case FARMER -> {
                rpcHarvester = new RPCHarvester(certs, paths);
                rpcFarmer = new RPCFarmer(certs, paths);
                rpcNode = new RPCNode(certs, paths);
                Parser parser = new Parser(true, true);
                spList = parser.getSignagePointList();
                cpList = parser.getCachedPointList();
                fpList = parser.getFilterPassList();
                conMonitor = new ConMonitor(rpcNode);
                Tailer tailer = new Tailer(parser, paths);
                tailer.start();
            }
        }
    }


    public ObservableList<String> getFarmActivity() {
        return farmActivity;
    }
    public ArrayList<String> getSPTimeWarn() {
        ArrayList<String> spWarn = new ArrayList<>();
        int j = 9216;
        if (spList.size() < 9216 ) j = (spList.size() - 1);

        for (int i = j; i > 0; --i) {
            if(spList.get(i).isWarn() == true) {
                spWarn.add(spList.get(i).getTime() +
                        "\t\t" + spList.get(i).getWarning() +
                        "    SP: " + spList.get(i).getIndex() +
                        "\t\t" + spList.get(i).getTimeSinceLast() / 1000000.0 + "s");
            }
        }
        metrics.warnSpTime.set(spWarn.size());
        return spWarn;
    }

        public ArrayList getSPOrderWarn() {
        ArrayList<String> spWarn = new ArrayList<>();
        int j = 9216;
        if (spList.size() < 9216 ) j = (spList.size() - 1);

        for (int i = 1; i < j; ++i) {
            if (spList.get(i).getIndex() != 1) {
                if (spList.get(i).getIndex() != (spList.get(i - 1).getIndex() + 1)) {
                        spWarn.add( spList.get(i-1).getTime() + "\t\t" +
                                spList.get(i - 1).getIndex()  + "\t-->\t" +
                                        spList.get(i).getIndex() + "\t\t" +
                                        spList.get(i).getTime());
                    }
                } else {
                    if (spList.get(i).getIndex() + 62 != (spList.get(i - 1).getIndex())) {
                        spWarn.add( spList.get(i-1).getTime() + "\t\t" +
                                spList.get(i - 1).getIndex()  + "\t-->\t" +
                                spList.get(i).getIndex() + "\t\t" +
                                spList.get(i).getTime());
                    }
                }
            }
        metrics.warnSpIndex.set(spWarn.size());
        Collections.reverse(spWarn);
        return spWarn;
    }


    public void updateFarmEvents() {
        farmActivity.clear();
        String lastFilter;
        int f = fpList.size() - 1;
        if(farming == true) {
            int s = spList.size() - 1;
            int m = 64;
            if (spList.size() < 64 || fpList.size() < 64) {
                if (spList.size() >= fpList.size()) {
                    m = fpList.size();
                } else {
                    m = spList.size();
                }
            }
            for (int i = 0; i < m; ++i) {
                SignagePoint sp = spList.get(s - i);
                FilterPass fp = fpList.get(f - i);
                lastFilter = (sp.getTime()
                        + "   SP: " + sp.getIndex()
                        + "   " + fp.getEligiblePlots()
                        + "/" + fp.getTotalPlots()
                        + "   Time: " + fp.getLookUpTime()
                        + "s   " + "Proofs: " + fp.getProofs()
                );
                farmActivity.add(lastFilter);
            }
        } else {
            int m = 64;
            if (fpList.size() < 64) {
                    m = fpList.size();
            }
            for (int i = 0; i < m; ++i) {
                FilterPass fp = fpList.get(f - i);
                lastFilter = (fp.getTime() +
                        "   " + fp.getEligiblePlots()
                        + "/" + fp.getTotalPlots()
                        + "   Time: " + fp.getLookUpTime()
                        + "s   " + "Proofs: " + fp.getProofs()
                );
                farmActivity.add(lastFilter);
            }


        }


    }

    public void updateWallet() {

    }


    public ObservableList<Connection> getConnections() {
        if (farming == true && conMonitor.getConnectionList() != null) {
            conMonitor.getConnectionList().sort((o1, o2)
                    -> o1.getPeakHeight().compareTo(o2.getPeakHeight()));
        }

        return conMonitor.getConnectionList();
    }

    public ConMonitor getConMon() {
        return conMonitor;
    }


    public void updateConnectionsState() {

        ObservableList<Connection> connections = rpcNode.getConnections();
        if (connections == null ) {
            farmState.health.set("Bad");
            farming = false;
            return;
        } else {
            conMonitor.updateConnections(rpcNode.getConnections());
            farmState.health.set("good");
            farming = true;
            //TODO schedule watcher
            conMonitor.watcher();
        }
    }


    //TODO use bool for node health
    public boolean updateFarmState() {
        farmState.health.set("Good");
        farmState.status.set("Farming");
        Blockchain_State bs = null;
        //TODO separate farmer/node functions from harvester only ones
        if (this.service == "FARMER" || this.service == "NODE") {
             bs = rpcNode.getBlockchainState();

             if (bs == null) { //TODO You are here working on health stats
                 farmState.health.set("Bad");
                 return false;
             }
        }
        if (fpList.get(fpList.size() -1).getTime().isBefore(LocalTime.now().minusSeconds(30))) {
            farmState.health.set("Bad");
            farmState.status.set("Down");
        }



        farmState.filter.set(fpList.get(fpList.size() - 1).getEligiblePlots());
        farmState.plots.set(fpList.get(fpList.size() - 1).getTotalPlots());
        if (farming == true) {
            farmState.peers.set(conMonitor.getConnectionList().size() - 2);
            farmState.height.set(bs.getPeak().getHeight());
            farmState.weight.set(bs.getPeak().getWeight());
            farmState.sp.set(spList.get(spList.size() - 1).getIndex());
            farmState.memPool.set(bs.getMempoolSize());
            farmState.difficulty.set(bs.getDifficulty());
            farmState.netspace.set(String.valueOf(bs.getSpace()) + " EiB");

            if (bs.getDifficulty() != epochDiff) {
                if (epochDiff == 0) {
                    epochDiff = bs.getDifficulty();
                    return true;
                }
                farmState.epochDate.set(spList.get(spList.size() - 1).getDate().toString());
                farmState.epochTime.set(spList.get(spList.size() - 1).getTime().toString());
                epochDiff = bs.getDifficulty();
            }
        }

        return true;
    }

    public boolean updateMetrics() {

        if (fpList.isEmpty()) {
            return false;
        }
        if (this.farming == true) {
            double spTenM = Logic.calcSpTimes(spList, 64);
            double spOneHr = Logic.calcSpTimes(spList, 384);
            double spFourHr = Logic.calcSpTimes(spList, 1536);
            double spTwelveHr = Logic.calcSpTimes(spList, 4608);
            int cachedOneHr = Logic.calcCachedCount(cpList, 1);
            int cachedFourHr = Logic.calcCachedCount(cpList, 4);
            int cachedTwelveHr = Logic.calcCachedCount(cpList, 12);

            metrics.spTenM.set(df.format(spTenM / 1000000.0));
            metrics.spOneHr.set(df.format(spOneHr / 1000000.0));
            metrics.spFourHr.set(df.format(spFourHr / 1000000.0));
            metrics.spTwelveHr.set(df.format(spTwelveHr / 1000000.0));
            metrics.cachedOneHr.set(cachedOneHr);
            metrics.cachedFourHr.set(cachedFourHr);
            metrics.cachedTwelveHr.set(cachedTwelveHr);
            metrics.spTenMPct.set(dfp.format((spTenM / 10000.0) / 9.375) + "%");
            metrics.spOneHrPct.set(dfp.format((spOneHr / 10000.0) / 9.375) + "%");
            metrics.spFourHrPct.set(dfp.format((spFourHr / 10000.0) / 9.375) + "%");
            metrics.spTwelveHrPct.set(dfp.format((spTwelveHr / 10000.0) / 9.375) + "%");
            metrics.cachedOneHrPct.set(df.format(((double) cachedOneHr / 384)) + "%");
            metrics.cachedFourHrPct.set(df.format(((double) cachedFourHr / 1536)) + "%");
            metrics.cachedTwelveHrPct.set(df.format(((double) cachedTwelveHr / 4608)) + "%");
        }

        Logic.FilterMetrics fMetrics = Logic.calcFilterMetrics(fpList);
        //TODO keep as string to avoid pointless cast
        metrics.fpFive.set((int) (fMetrics.getWarnMap().get(FIVE)));
        metrics.fpTen.set((int) (fMetrics.getWarnMap().get(TEN)));
        metrics.fpFifteen.set((int) (fMetrics.getWarnMap().get(FIFTEEN)));
        metrics.fpTwenty.set((int) (fMetrics.getWarnMap().get(TWENTY)));
        metrics.fpMissed.set((int) (fMetrics.getWarnMap().get(MISSED)));
        metrics.proofs.set(fMetrics.getProofs());
        metrics.fpMax.set(df.format(fMetrics.getFpMax()));
        metrics.fpAvg.set(df.format(fMetrics.getFpAvg()));
        metrics.filterLast.set(df.format(fpList.get(fpList.size() - 1).getLookUpTime()));

//        if (spLastIter < spList.size() && fpLastIter < fpList.size()) {
//            newFarmEvent = true;
//            spLastIter = spList.size();
//            fpLastIter = fpList.size();
//        }

        return true;
    }

    public boolean updatePool() {
        List<Pool_State> ps = farmer().getPoolState();

        System.out.println("pool Array Size: " + ps.size());


//        pool.pointsCurr.set(ps.getCurrentPoints());
//        pool.difficulty.set(ps.getCurrentDifficulty());
//
//        //pool.credit24Hr.set(ps.getPointsAcknowledged24h());
//        System.out.println(ps.getCurrentDifficulty());
//        for(Pool_State.Found24h f : ps.getPointsFound24h()) {
//            System.out.println(f.getCount());
//        }
        return true;
    }


    public RPCNode node(){
        return rpcNode;
    }

    public RPCFarmer farmer(){
        return rpcFarmer;
    }

    public RPCHarvester harvester(){
        return rpcHarvester;
    }

    public RPCWallet wallet() {
        return  rpcWallet;
    }
    public ArrayList<SignagePoint> getSpList() {
        return spList;
    }
    public List<FilterPass> getFpList() {
        return fpList;
    }

    /*
    FIXME?  *SIGH* AFAIK all these getters are needed for javafx to observe the property values.
                   So far no alternative has worked. :(
                   JavaFx seems to require them in the top class.
     */


    public String getName() {
        return name;
    }
    public String getService() {
        return service;
    }
    public String getAddress() {
        return address;
    }

    public boolean isWallet() {
        return wallet;
    }

    public boolean isPooling() {
        return pooling;
    }
    public boolean isFarming(){
        return farming;
    }

    public SimpleStringProperty getHealth() {
        return farmState.health;
    }
    public SimpleStringProperty getStatus() {
        return farmState.status;
    }
    public SimpleIntegerProperty getPeers() {
        return farmState.peers;
    }
    public SimpleIntegerProperty getHeight() {
        return farmState.height;
    }
    public SimpleLongProperty getWeight() {
        return farmState.weight;
    }
    public SimpleIntegerProperty getSp() {
        return farmState.sp;
    }
    public SimpleIntegerProperty getFilter() {
        return farmState.filter;
    }
    public SimpleIntegerProperty getPlots() {
        return farmState.plots;
    }
    public SimpleIntegerProperty getMemPool() {
        return farmState.memPool;
    }
    public SimpleIntegerProperty getDifficulty() {
        return farmState.difficulty;
    }
    public SimpleStringProperty getNetspace() {
        return farmState.netspace;
    }
    public SimpleIntegerProperty getETW() {
        return farmState.etw;
    }
    public SimpleStringProperty getEpochDate() {
        return farmState.epochDate;
    }
    public SimpleStringProperty getEpochTime() {
        return farmState.epochTime;
    }
    public SimpleIntegerProperty getFpFive() {
        return metrics.fpFive;
    }
    public SimpleIntegerProperty getFpTen() {
        return metrics.fpTen;
    }
    public SimpleIntegerProperty getFpFifteen() {
        return metrics.fpFifteen;
    }
    public SimpleIntegerProperty getFpTwenty() {
        return metrics.fpTwenty;
    }
    public SimpleIntegerProperty getFpMissed() {
        return metrics.fpMissed;
    }
    public SimpleStringProperty getFpAvg() {
        return metrics.fpAvg;
    }
    public SimpleStringProperty getFpMax() {
        return metrics.fpMax;
    }
    public SimpleStringProperty getSpTenM() {
        return metrics.spTenM;
    }
    public SimpleStringProperty getSpOneHr() {
        return metrics.spOneHr;
    }
    public SimpleStringProperty getSpFourHr() {
        return metrics.spFourHr;
    }
    public SimpleStringProperty getSpTwelveHr() {
        return metrics.spTwelveHr;
    }
    public SimpleIntegerProperty getCachedOneHr() {
        return metrics.cachedOneHr;
    }
    public SimpleIntegerProperty getCachedFourHr() {
        return metrics.cachedFourHr;
    }
    public SimpleIntegerProperty getCachedTwelveHr() {
        return metrics.cachedTwelveHr;
    }
    public SimpleIntegerProperty getWarnSpTime() {
        return metrics.warnSpTime;
    }
    public SimpleIntegerProperty getWarnSpIndex() {
        return metrics.warnSpIndex;
    }
    public SimpleIntegerProperty getProofs() {
        return metrics.proofs;
    }
    public SimpleStringProperty getFilterLast() {
        return metrics.filterLast;
    }
    public SimpleStringProperty getSpTenMPct() {
        return metrics.spTenMPct;
    }
    public SimpleStringProperty getSpOneHrPct() {
        return metrics.spOneHrPct;
    }
    public SimpleStringProperty getSpFourHrPct() {
        return metrics.spFourHrPct;
    }
    public SimpleStringProperty getSpTwelveHrPct() {
        return metrics.spTwelveHrPct;
    }
    public SimpleStringProperty getCachedOneHrPct() {
        return metrics.cachedOneHrPct;
    }

    public SimpleStringProperty getCachedFourHrPct() {
        return metrics.cachedFourHrPct;
    }
    public SimpleStringProperty getCachedTwelveHrPct() {
        return metrics.cachedTwelveHrPct;
    }
    public SimpleIntegerProperty getPointsCurr() {
        return pool.pointsCurr;
    }
    public SimpleIntegerProperty getPoints24Hr() {
        return pool.points24Hr;
    }
    public SimpleIntegerProperty getCredit24Hr() {
        return pool.credit24Hr;
    }
    public SimpleIntegerProperty getPoolDifficulty() {
        return pool.difficulty;
    }
}




    class FarmState {

        SimpleStringProperty health = new SimpleStringProperty("");
        SimpleStringProperty status = new SimpleStringProperty("");
        SimpleIntegerProperty peers = new SimpleIntegerProperty(-1);
        SimpleIntegerProperty height = new SimpleIntegerProperty(-1);
        SimpleLongProperty weight = new SimpleLongProperty(-1);
        SimpleIntegerProperty sp = new SimpleIntegerProperty(-1);
        SimpleIntegerProperty filter = new SimpleIntegerProperty(-1);
        SimpleIntegerProperty plots = new SimpleIntegerProperty(-1);
        SimpleIntegerProperty memPool = new SimpleIntegerProperty(-1);
        SimpleIntegerProperty difficulty = new SimpleIntegerProperty(-1);
        SimpleStringProperty netspace = new SimpleStringProperty("");
        SimpleIntegerProperty etw = new SimpleIntegerProperty(-1);
        SimpleStringProperty epochDate = new SimpleStringProperty("null");
        SimpleStringProperty epochTime = new SimpleStringProperty("null");
    }

     class Metrics {
         SimpleIntegerProperty fpFive = new SimpleIntegerProperty(-1);
         SimpleIntegerProperty fpTen = new SimpleIntegerProperty(-1);
         SimpleIntegerProperty fpFifteen = new SimpleIntegerProperty(-1);
         SimpleIntegerProperty fpTwenty = new SimpleIntegerProperty(-1);
         SimpleIntegerProperty fpMissed = new SimpleIntegerProperty(-1);
         SimpleStringProperty fpAvg = new SimpleStringProperty("-1");
         SimpleStringProperty fpMax = new SimpleStringProperty("-1");
         SimpleStringProperty spTenM = new SimpleStringProperty("-1");
         SimpleStringProperty spOneHr = new SimpleStringProperty("-1");
         SimpleStringProperty spFourHr = new SimpleStringProperty("-1");
         SimpleStringProperty spTwelveHr = new SimpleStringProperty("-1");
         SimpleIntegerProperty cachedOneHr = new SimpleIntegerProperty(-1);
         SimpleIntegerProperty cachedFourHr = new SimpleIntegerProperty(-1);
         SimpleIntegerProperty cachedTwelveHr = new SimpleIntegerProperty(-1);
         SimpleStringProperty spTenMPct = new SimpleStringProperty("-1");
         SimpleStringProperty spOneHrPct = new SimpleStringProperty("-1");
         SimpleStringProperty spFourHrPct = new SimpleStringProperty("-1");
         SimpleStringProperty spTwelveHrPct = new SimpleStringProperty("-1");
         SimpleStringProperty cachedOneHrPct = new SimpleStringProperty("-1");
         SimpleStringProperty cachedFourHrPct = new SimpleStringProperty("-1");
         SimpleStringProperty cachedTwelveHrPct = new SimpleStringProperty("-1");
         SimpleIntegerProperty warnSpTime = new SimpleIntegerProperty(-1);
         SimpleIntegerProperty warnSpIndex = new SimpleIntegerProperty(-1);
         SimpleIntegerProperty proofs = new SimpleIntegerProperty(-1);
         SimpleStringProperty filterLast = new SimpleStringProperty("-1");
     }
     class Pool {
         SimpleIntegerProperty pointsCurr = new SimpleIntegerProperty(-1);
         SimpleIntegerProperty points24Hr = new SimpleIntegerProperty(-1);
         SimpleIntegerProperty credit24Hr = new SimpleIntegerProperty(-1);
         SimpleIntegerProperty difficulty = new SimpleIntegerProperty(-1);
     }

//
//    public void startTail() {
//        tailer.start();
//    }
//
//    public void stopTail() {
//        tailer.stop();
//    }
