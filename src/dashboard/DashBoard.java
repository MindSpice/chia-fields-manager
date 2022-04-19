package dashboard;

import data.json.Connection;
import endpoints.Endpoints;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import monitor.Monitor;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;


// TODO fix the pesky mis focus error and add reporting for if an sp hasnt occurred in n time
// TODO add a config file

public class DashBoard implements Initializable {


    @FXML
    public TableView<Monitor> main_table;
    @FXML
    public TableColumn<Monitor, String> main_table_name, main_table_health, main_table_service, main_table_address;
    @FXML
    public TableColumn<Monitor, Integer> main_table_peers, main_table_height, main_table_sp, main_table_filter, main_table_plots, main_table_proofs;
    @FXML
    public TableView<Connection> con_table;
    @FXML
    public TableColumn<Connection, Integer> con_height, con_port, con_read, con_write;
    @FXML
    public TableColumn<Connection, String> con_address, con_node_id;
    @FXML
    public TextField dash_overview_health, dash_overview_height, dash_overview_weight, dash_overview_netspace, dash_overview_difficulty,
            dash_overview_epoch_date, dash_overview_epoch_time, dash_node_sync, dash_node_sp_index, dash_node_mempool, dash_node_filter_last,
            dash_node_filter_max, dash_node_filter_avg, dash_node_sp_var, dash_node_filter_last_pct, dash_node_sp_var_pct, dash_pool_points_curr,
            dash_pool_points_24h, dash_pool_credit_24h, dash_pool_difficulty, dash_wallet_hot, dash_wallet_cold, dash_wallet_cold2, dash_wallet_pool;
    @FXML
    public TextField farm_status, farm_health, farm_proofs, farm_mempool, farm_filter_avg, farm_filter_max, farm_filter_5, farm_filter_10,
            farm_filter_15, farm_filter_20, farm_filter_missed, farm_sp_index, farm_sp_reorders, farm_sp_delayed, farm_sp_var, farm_sp_10m,
            farm_sp_1h, farm_sp_4h, farm_sp_12h, farm_cached_1h, farm_cached_4h, farm_cached_12h, farm_sp_10m_pct, farm_sp_1h_pct,
            farm_sp_4h_pct, farm_sp_12h_pct, farm_cached_12h_pct, farm_cached_4h_pct, farm_cached_1h_pct;
    @FXML
    public TextField network_weight, network_difficulty, network_netspace, network_height, network_epoch_date, network_epoch_time;
    @FXML
    public ListView con_watcher_activity, farm_fp_warnings, farm_sp_warnings, farm_node_warnings, farm_activity;
    ;
    @FXML
    public TextField con_addr_box, con_addr_box_id;
    public Button remove_peer;


    //TODO HH?
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("H:mm:ss.SSS");
    Endpoints endpoints = new Endpoints();
    Monitor farmInFocus;
    Connection selectedPeer;


    private static ObservableList<Monitor> monitors = FXCollections.observableArrayList();
    private static ObservableList<String> farmActivity = FXCollections.observableArrayList();
    private static ObservableList<String> peerWatchLog = FXCollections.observableArrayList();
    private static ObservableList<String> spIndex = FXCollections.observableArrayList();
    private static ObservableList<String> spTime = FXCollections.observableArrayList();



    private void monLoop() {
        for (Monitor m : monitors) {  //todo FIx hangup on rpc crash
            m.updateFarmState();
        }


    }

    private void metLoop() {
        for (Monitor m : monitors) {
            m.updateMetrics();
        }
        farmActivity();
    }


    private void conMon() {
            for (Monitor m : monitors) {
                if(m.isFarming() == true) {
                    m.updateConnectionsState();
                }
                if (farmInFocus.isFarming() == true) {
                    con_table.getItems().setAll(farmInFocus.getConnections());
                    con_table.refresh();
                }

            }
            peerWatcherActivity();
            warnings(); //Placed here to due to longer delay

        }


    private void poolLoop() {
        for (Monitor m : monitors) {
            if (m.isPooling()) m.updatePool();
        }
    }

    private void farmActivity() {
                farmInFocus.updateFarmEvents();
                farmActivity.setAll(farmInFocus.getFarmActivity());
                farm_activity.refresh();
    }

    private void peerWatcherActivity() {
                if (farmInFocus.isFarming() == true && farmInFocus.getConMon().newWatcherLog()) {
                    peerWatchLog.setAll(farmInFocus.getConMon().getWatcherLog());
                    con_watcher_activity.refresh();
                }
    }

    private void warnings() {
                if (farmInFocus.isFarming() == true) {
                    spIndex.setAll(farmInFocus.getSPOrderWarn());
                    farm_sp_warnings.refresh();
                    spTime.setAll(farmInFocus.getSPTimeWarn());
                    farm_fp_warnings.refresh();
                }
    }


    //FIXME Scene Resize from main needs delay on initializations

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        endpoints.addEndpoint("og_node",
                "C:\\ssl",
                "\\\\192.168.0.103\\log\\debug.log",
                "192.168.0.103");

        endpoints.addEndpoint("nft_node",
                "C:\\ssl1",
                "\\\\192.168.0.111\\log\\debug.log",
                "192.168.0.111");

        endpoints.addEndpoint("nft_node",
                "C:\\ssl1",
                "\\\\192.168.0.193\\log\\debug.log",
                "192.168.0.193");


        monitors.add(new Monitor("NODE", Monitor.MonitorType.FARMER, endpoints.getPaths(0),
                endpoints.getCerts(0),true, false, false));
        monitors.add(new Monitor("NODE NFT", Monitor.MonitorType.FARMER, endpoints.getPaths(1),
                endpoints.getCerts(1), true, true, false));
        monitors.add(new Monitor("HARVESTER", Monitor.MonitorType.HARVESTER, endpoints.getPaths(2),
                endpoints.getCerts(1), false, true, false));


        setFarmFocus(monitors.get(0));

        Task metricMonitor = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                while (true) {
                    Platform.runLater(() -> metLoop());
                    Platform.runLater(() -> monLoop());
                    Thread.sleep(4000);
                }
            }
        };
        Thread metMon = new Thread(metricMonitor);
        metMon.setDaemon(true);
        metMon.start();

        Task conMonitor = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                while (true) {
                    Platform.runLater(() -> conMon());
                    Thread.sleep(40000);
                }
            }
        };
        Thread conMon = new Thread(conMonitor);
        conMon.setDaemon(true);
        conMon.start();



        main_table.setItems(monitors);
        main_table_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        main_table_health.setCellValueFactory(cell -> cell.getValue().getHealth());
        main_table_service.setCellValueFactory(new PropertyValueFactory<>("service"));
        main_table_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        main_table_peers.setCellValueFactory(cell -> cell.getValue().getPeers().asObject());
        main_table_height.setCellValueFactory(cell -> cell.getValue().getHeight().asObject());
        main_table_sp.setCellValueFactory(cell -> cell.getValue().getSp().asObject());
        main_table_filter.setCellValueFactory(cell -> cell.getValue().getFilter().asObject());
        main_table_plots.setCellValueFactory(cell -> cell.getValue().getPlots().asObject());
        main_table_proofs.setCellValueFactory(cell -> cell.getValue().getProofs().asObject());

        con_table.setItems(monitors.get(0).getConnections());
        con_address.setCellValueFactory(new PropertyValueFactory<>("Host"));
        con_port.setCellValueFactory(new PropertyValueFactory<>("Port"));
        con_height.setCellValueFactory(new PropertyValueFactory<>("PeakHeight"));
        con_read.setCellValueFactory(new PropertyValueFactory<>("Write"));
        con_write.setCellValueFactory(new PropertyValueFactory<>("Read"));
        con_node_id.setCellValueFactory(new PropertyValueFactory<>("NodeID"));
        con_watcher_activity.setItems(peerWatchLog);


        dash_overview_health.textProperty().bind(monitors.get(0).getHealth());
        dash_overview_height.textProperty().bind(monitors.get(0).getHeight().asString());
        dash_overview_weight.textProperty().bind(monitors.get(0).getWeight().asString());
        dash_overview_difficulty.textProperty().bind(monitors.get(0).getDifficulty().asString());
        dash_overview_netspace.textProperty().bind(monitors.get(0).getNetspace());
        dash_overview_epoch_date.textProperty().bind(monitors.get(0).getEpochDate());
        dash_overview_epoch_time.textProperty().bind(monitors.get(0).getEpochTime());

        //dash_node_sync
         dash_node_sp_index.textProperty().bind(monitors.get(0).getSp().asString());
         dash_node_mempool.textProperty().bind(monitors.get(0).getMemPool().asString());
         dash_node_filter_last.textProperty().bind(monitors.get(0).getFilterLast());
         dash_node_filter_max.textProperty().bind(monitors.get(0).getFpMax());
         dash_node_filter_avg.textProperty().bind(monitors.get(0).getFpAvg());
         dash_node_sp_var.textProperty().bind(monitors.get(0).getSpTenM());
         dash_pool_points_curr.textProperty().bind(monitors.get(0).getPointsCurr().asString());
         dash_pool_difficulty.textProperty().bind(monitors.get(0).getPoolDifficulty().asString());

         farm_activity.setItems(farmActivity);
         farm_sp_warnings.setItems(spIndex);
         farm_fp_warnings.setItems(spTime);
    }


    private void setFarmFocus(Monitor mon) {
        farmInFocus = mon;
        farm_filter_avg.textProperty().bind(mon.getFpAvg());
        farm_filter_max.textProperty().bind(mon.getFpMax());
        farm_filter_5.textProperty().bind(mon.getFpFive().asString());
        farm_filter_10.textProperty().bind(mon.getFpTen().asString());
        farm_filter_15.textProperty().bind(mon.getFpFifteen().asString());
        farm_filter_20.textProperty().bind(mon.getFpTwenty().asString());
        farm_filter_missed.textProperty().bind(mon.getFpMissed().asString());
        farm_health.textProperty().bind(mon.getHealth());
        farm_proofs.textProperty().bind(mon.getProofs().asString());
        if (mon.isFarming() == true) {
            farm_status.textProperty().set("tGood");
            farm_mempool.textProperty().bind(mon.getMemPool().asString());
            farm_sp_index.textProperty().bind(mon.getSp().asString());
            farm_sp_10m.textProperty().bind(mon.getSpTenM());
            farm_sp_1h.textProperty().bind(mon.getSpOneHr());
            farm_sp_4h.textProperty().bind(mon.getSpFourHr());
            farm_sp_12h.textProperty().bind(mon.getSpTwelveHr());
            farm_cached_1h.textProperty().bind(mon.getCachedOneHr().asString());
            farm_cached_4h.textProperty().bind(mon.getCachedFourHr().asString());
            farm_cached_12h.textProperty().bind(mon.getCachedTwelveHr().asString());
            farm_sp_10m_pct.textProperty().bind(mon.getSpTenMPct());
            farm_sp_1h_pct.textProperty().bind(mon.getSpOneHrPct());
            farm_sp_4h_pct.textProperty().bind(mon.getSpFourHrPct());
            farm_sp_12h_pct.textProperty().bind(mon.getSpTwelveHrPct());
            farm_cached_1h_pct.textProperty().bind(mon.getCachedOneHrPct());
            farm_cached_4h_pct.textProperty().bind(mon.getCachedFourHrPct());
            farm_cached_12h_pct.textProperty().bind(mon.getCachedTwelveHrPct());
            network_height.textProperty().bind(mon.getHeight().asString());
            network_weight.textProperty().bind(mon.getWeight().asString());
            network_difficulty.textProperty().bind(mon.getDifficulty().asString());
            network_netspace.textProperty().bind(mon.getNetspace());
            network_epoch_date.textProperty().bind(mon.getEpochDate());
            network_epoch_time.textProperty().bind(mon.getEpochTime());
        }
    }

    public void changeInFocus(MouseEvent mouseEvent) {
        setFarmFocus(main_table.getSelectionModel().getSelectedItem());
        farmInFocus = main_table.getSelectionModel().getSelectedItem();
        farm_fp_warnings.refresh();
        farmInFocus.updateFarmEvents();
        farmActivity.setAll(farmInFocus.getFarmActivity());
        farm_activity.refresh();
        con_addr_box.clear();
        con_addr_box_id.clear();
        selectedPeer = null;
        if ( farmInFocus.isFarming() == true) {
            con_table.setItems(farmInFocus.getConnections());
            con_table.refresh();
            peerWatchLog.setAll(farmInFocus.getConMon().getWatcherLog());
            con_watcher_activity.refresh();
            spIndex.setAll(farmInFocus.getSPOrderWarn());
            farm_sp_warnings.refresh();
            spTime.setAll(farmInFocus.getSPTimeWarn());
            farm_activity.refresh();
            con_addr_box.clear();
            con_addr_box_id.clear();
            selectedPeer = null;
        } else {
            con_table.getItems().clear();
            con_table.refresh();
        }

    }

    public void conSelectedNode(MouseEvent mouseEvent) {
        if (con_table.getSelectionModel().getSelectedItem() != null) {
            con_addr_box.setText(con_table.getSelectionModel().getSelectedItem().getHost());
            con_addr_box_id.setText(con_table.getSelectionModel().getSelectedItem().getNodeID());
            selectedPeer = con_table.getSelectionModel().getSelectedItem();
        }
        con_table.getSelectionModel().clearSelection();
    }

    public void addPeer(ActionEvent actionEvent) {
    }

    public void removePeer(ActionEvent actionEvent) {
        if (selectedPeer != null) {
            farmInFocus.getConMon().closeConnection(selectedPeer.getNodeID());
            con_table.getItems().remove(selectedPeer);
        }
    }
}









