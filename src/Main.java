import data.json.Blockchain_State;
import endpoints.Endpoints;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import logic.Logic;
import monitor.Monitor;
import connections.ConMonitor;

import java.text.DecimalFormat;
import java.time.LocalDateTime;


public class Main extends Application {

    //TODO try .clear on connections table so it doesn't null out

//
//    public void start(Stage stage) throws Exception {

        @Override
        public void start(Stage stage) throws Exception {
//            Parent root = FXMLLoader.load(getClass().getResource("gui/FarmOverview.fxml"));
//            stage.setScene(new Scene(root,1600 , 900));
//            stage.setResizable(false);
//            stage.show();
//        }


        final int initWidth = 1600;      //initial width
        final int initHeight = 900;    //initial height
        final Pane root = new Pane();   //necessary evil

        Pane controller = FXMLLoader.load(getClass().getResource("gui/FarmOverview.fxml"));   //initial view
        controller.setPrefWidth(initWidth);     //if not initialized
        controller.setPrefHeight(initHeight);   //if not initialized
        root.getChildren().add(controller);     //necessary evil


        Scale scale = new Scale(1, 1, 0, 0);
        scale.xProperty().bind(root.widthProperty().divide(initWidth));     //must match with the one in the controller
        scale.yProperty().bind(root.heightProperty().divide(initHeight));   //must match with the one in the controller
        root.getTransforms().add(scale);

        final Scene scene = new Scene(root, initWidth, initHeight);
        stage.setScene(scene);
        stage.setResizable(true);

        stage.show();
//        add listener for the use of scene.setRoot()
        scene.rootProperty().addListener(new ChangeListener<Parent>() {
            @Override
            public void changed(ObservableValue<? extends Parent> arg0, Parent oldValue, Parent newValue) {
                scene.rootProperty().removeListener(this);
                scene.setRoot(root);
                ((Region) newValue).setPrefWidth(initWidth);     //make sure is a Region!
                ((Region) newValue).setPrefHeight(initHeight);   //make sure is a Region!
                root.getChildren().clear();
                root.getChildren().add(newValue);
                scene.rootProperty().addListener(this);
            }
        });
    }


    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }
}

//        Endpoints endpoints = new Endpoints();
//
//
//
//        endpoints.addEndpoint("og_node",
//                "C:\\ssl",
//                "\\\\192.168.0.103\\log\\debug.log",
//                "192.168.0.103");
//
//        endpoints.addEndpoint("nft_node",
//                "C:\\ssl1",
//                "\\\\192.168.0.111\\log\\debug.log",
//                "192.168.0.111");
//
//
//
//        Monitor ogMonitor = new Monitor("NODE", Monitor.MonitorType.FARMER, endpoints.getPaths(0), endpoints.getCerts(0),false, false);
//        Monitor nftMonitor = new Monitor("NODE", Monitor.MonitorType.FARMER, endpoints.getPaths(1), endpoints.getCerts(1),true, false);
////        monitors.add(new Monitor("NODE", Monitor.MonitorType.FARMER, endpoints.getPaths(0),
////                endpoints.getCerts(0),false,false));
////        monitors.add(new Monitor("NODE NFT", Monitor.MonitorType.FARMER, endpoints.getPaths(1),
////                endpoints.getCerts(1),true,false));
//
//
//
//
//
//        ConMonitor c = new ConMonitor(ogMonitor.node());
//        ConMonitor c2 = new ConMonitor(nftMonitor.node());
//        c.watcher();
//        c2.watcher();
//        DecimalFormat df = new DecimalFormat("#.###");
//
//        int filterLength = 0;
//        int filterLength2 = 0;
//
//
//        while (true) {
//
//            c.updateConnections(ogMonitor.node().getConnections());
//            c.watcher();
//
//
//            //  RPCFarmer.getSignagePoints();
//
//
//
//
//
//
//
//
//            /*
//             *Test for duplicates and mis-ordered ones then come up with algo for
//             */
//            System.out.println("\n### SP Issues 12 Hours###\n");
//            for (int i = 1; i < ogMonitor.getSpList().size(); ++i) {
//                if (ogMonitor.getSpList().get(i).getIndex() != 1) {
//                    if (ogMonitor.getSpList().get(i).getIndex() != (ogMonitor.getSpList().get(i - 1).getIndex() + 1) &&
//                            !ogMonitor.getSpList().get(i).getDateTime().plusMinutes(720).isBefore((LocalDateTime.now()))) {
//
//                        System.out.println(
//                                ogMonitor.getSpList().get(i - 1).getIndex() + "\t" +
//                                ogMonitor.getSpList().get(i-1).getTime() + "\t-->\t" +
//                                ogMonitor.getSpList().get(i).getIndex() + "\t\t" +
//                                ogMonitor.getSpList().get(i).getTime());
//                    }
//                } else {
//                    if (ogMonitor.getSpList().get(i).getIndex() + 62 != (ogMonitor.getSpList().get(i - 1).getIndex()) &&
//                            !ogMonitor.getSpList().get(i).getDateTime().plusMinutes(720).isBefore((LocalDateTime.now()))) {
//
//                        System.out.println(
//                                ogMonitor.getSpList().get(i - 1).getIndex() + "\t" +
//                                ogMonitor.getSpList().get(i-1).getTime() + "\t-->\t" +
//                                ogMonitor.getSpList().get(i).getIndex() + "\t\t" +
//                                ogMonitor.getSpList().get(i).getTime());
//                    }
//                }
//            }
//
//            System.out.println("\n### SP Warning Past 2 Hours ###\n");
//            for (int i = 1; i < ogMonitor.getSpList().size(); ++i) {
//                if (ogMonitor.getSpList().get(i).isWarn() == true && !ogMonitor.getSpList().get(i).getDateTime().plusMinutes(120).isBefore((LocalDateTime.now()))) {
//
//                    System.out.println(
//                            ogMonitor.getSpList().get(i).getWarning() +
//                            " " + ogMonitor.getSpList().get(i).getIndex() +
//                            " " + (ogMonitor.getSpList().get(i).getTimeSinceLast() / 1000000.0) +
//                            " " + ogMonitor.getSpList().get(i).getTime());
//                }
//            }
//
//
//            System.out.println("\n### Long Filter Passes ###\n");
//            for (int i = 0; i < ogMonitor.getFpList().size(); ++i) {
//                if (ogMonitor.getFpList().get(i).getLookUpTime() > 2) {
//
//                    System.out.println(
//                            ogMonitor.getFpList().get(i).getTime() + "\t" +
//                            ogMonitor.getFpList().get(i).getLookUpTime());
//                }
//
//                if (ogMonitor.getFpList().get(i).getProofs() == 1) {
//
//                    System.out.println("\n   !!!!!!!!!!!!!!!  ");
//                    System.out.println("!!!! PROOF FOUND !!!!");
//                    System.out.println("   !!!!!!!!!!!!!!!  ");
//                }
//            }
//
//
//            System.out.println("\n### Filter Passes ###\n");
//            for (int i = filterLength; i < ogMonitor.getFpList().size(); ++i) {
//
//                System.out.println(
//                        ogMonitor.getFpList().get(i).getTime() + "\t" +
//                        ogMonitor.getFpList().get(i).getEligiblePlots() + "/" +
//                        ogMonitor.getFpList().get(i).getTotalPlots() + "\t\t" +
//                        ogMonitor.getFpList().get(i).getLookUpTime());
//
//                filterLength++;
//
//            }
//
//
//
//
//
//            System.out.println("\n### SP Iters " + ogMonitor.getSpList().size()+ " ### \n");
//            System.out.println("12 Hour AVG\t" + df.format(Logic.calcSpTimes(ogMonitor.getSpList(), 4608) / 1000000.0)+  " \t " + df.format((Logic.calcSpTimes(ogMonitor.getSpList(), 4608) / 10000.0) /9.375 ) + "%");
//            System.out.println("6 Hour AVG \t" + df.format(Logic.calcSpTimes(ogMonitor.getSpList(), 2304) / 1000000.0)+  " \t " + df.format((Logic.calcSpTimes(ogMonitor.getSpList(), 2304) / 10000.0) /9.375 ) + "%");
//            System.out.println("1 Hour AVG \t" + df.format(Logic.calcSpTimes(ogMonitor.getSpList(), 384) / 1000000.0)+  " \t " + df.format((Logic.calcSpTimes(ogMonitor.getSpList(), 384) / 10000.0) /9.375 ) + "%");
//            System.out.println("S-Slot AVG \t" + df.format(Logic.calcSpTimes(ogMonitor.getSpList(), 64) / 1000000.0) +  " \t " + df.format((Logic.calcSpTimes(ogMonitor.getSpList(), 64) / 10000.0) /9.375 ) + "%");
//            System.out.println("2 Min AVG \t" + df.format(Logic.calcSpTimes(ogMonitor.getSpList(), 13) / 1000000.0) + " \t " + df.format((Logic.calcSpTimes(ogMonitor.getSpList(), 13) / 10000.0) /9.375 ) + "%");
//
//
//            Blockchain_State bs = ogMonitor.node().getBlockchainState();
//
//            System.out.println("\nOG Monitor: \tNetspace: " + bs.getSpace() + " EiB" + "\t" +
//                    "Chain Height: " + bs.getPeak().getHeight() + "\t" +
//                    "Mem Pool Size: " +  bs.getMempoolSize() + "\n");
//
//
////        System.out.println(monitor.getSignagePointList().get(i).getWarning() + "   \t" + monitor.getSignagePointList().get(i).getSignagePointIndex() +
////        "   " + (monitor.getSignagePointList().get(i).getTimeSinceLast() / 1000000.0) + "   " + monitor.getSignagePointList().get(i).getTime());
////
////        }
//            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
//            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
//            Thread.sleep(20000);
//
//
//            /*
//
//
//************************************************BREAK***********************************************************************************
//
// */
//            c2.updateConnections(nftMonitor.node().getConnections());
//            c2.watcher();
//
//
//            /*
//             *Test for duplicates and mis-ordered ones then come up with algo for
//             */
//            System.out.println("\n### SP Issues 12 Hours###\n");
//            for (int i = 1; i < nftMonitor.getSpList().size(); ++i) {
//                if (nftMonitor.getSpList().get(i).getIndex() != 1) {
//                    if (nftMonitor.getSpList().get(i).getIndex() != (nftMonitor.getSpList().get(i - 1).getIndex() + 1)
//                            && !nftMonitor.getSpList().get(i).getDateTime().plusMinutes(720).isBefore((LocalDateTime.now()))) {
//
//                        System.out.println(
//                                nftMonitor.getSpList().get(i - 1).getIndex() + "\t" +
//                                nftMonitor.getSpList().get(i-1).getTime() + "\t-->\t" +
//                                nftMonitor.getSpList().get(i).getIndex() + "\t\t" +
//                                nftMonitor.getSpList().get(i).getTime());
//                    }
//                } else {
//                    if (nftMonitor.getSpList().get(i).getIndex() + 62 != (nftMonitor.getSpList().get(i - 1).getIndex())
//                            && !nftMonitor.getSpList().get(i).getDateTime().plusMinutes(720).isBefore((LocalDateTime.now()))) {
//
//                        System.out.println(
//                                nftMonitor.getSpList().get(i - 1).getIndex() + "\t" +
//                                nftMonitor.getSpList().get(i-1).getTime() + "\t-->\t" +
//                                nftMonitor.getSpList().get(i).getIndex() + "\t\t" +
//                                nftMonitor.getSpList().get(i).getTime());
//                    }
//                }
//            }
//
//            System.out.println("\n### SP Warning Past 2 Hours ###\n");
//            for (int i = 1; i < nftMonitor.getSpList().size(); ++i) {
//                if (nftMonitor.getSpList().get(i).isWarn() == true && !nftMonitor.getSpList().get(i).getDateTime().plusMinutes(120).isBefore((LocalDateTime.now()))) {
//
//                    System.out.println(
//                            nftMonitor.getSpList().get(i).getWarning() +
//                            " " + nftMonitor.getSpList().get(i).getIndex() +
//                            " " + (nftMonitor.getSpList().get(i).getTimeSinceLast() / 1000000.0) +
//                            " " + nftMonitor.getSpList().get(i).getTime());
//                }
//            }
//
//
//            System.out.println("\n### Long Filter Passes ###\n");
//            for (int i = 0; i < nftMonitor.getFpList().size(); ++i) {
//                if (nftMonitor.getFpList().get(i).getLookUpTime() > 2) {
//
//                    System.out.println(nftMonitor.getFpList().get(i).getTime() + "\t" +
//                            nftMonitor.getFpList().get(i).getLookUpTime());
//                }
//
//                if (nftMonitor.getFpList().get(i).getProofs() == 1) {
//                    System.out.println("\n   !!!!!!!!!!!!!!!  ");
//                    System.out.println("!!!! PROOF FOUND !!!!");
//                    System.out.println("\n   !!!!!!!!!!!!!!!  ");
//                }
//            }
//
//
//            System.out.println("\n### Filter Passes ###\n");
//            for (int i = filterLength2; i < nftMonitor.getFpList().size(); ++i) {
//
//                System.out.println(
//                        nftMonitor.getFpList().get(i).getTime() + "\t" +
//                        nftMonitor.getFpList().get(i).getEligiblePlots() + "/" +
//                        nftMonitor.getFpList().get(i).getTotalPlots() + "\t\t" +
//                        nftMonitor.getFpList().get(i).getLookUpTime());
//                filterLength2++;
//            }
//
//
//
//
//            //TODO make logic return results all in one pass
//            System.out.println("\n### SP Iters " + nftMonitor.getSpList().size()+ " ### \n");
//            System.out.println("12 Hour AVG\t" + df.format(Logic.calcSpTimes(nftMonitor.getSpList(), 4608) / 1000000.0)+  " \t " + df.format((Logic.calcSpTimes(nftMonitor.getSpList(), 4608) / 10000.0) /9.375 ) + "%");
//            System.out.println("6 Hour AVG \t" + df.format(Logic.calcSpTimes(nftMonitor.getSpList(), 2304) / 1000000.0)+  " \t " + df.format((Logic.calcSpTimes(nftMonitor.getSpList(), 2304) / 10000.0) /9.375 ) + "%");
//            System.out.println("1 Hour AVG \t" + df.format(Logic.calcSpTimes(nftMonitor.getSpList(), 384) / 1000000.0)+  " \t " + df.format((Logic.calcSpTimes(nftMonitor.getSpList(), 384) / 10000.0) /9.375 ) + "%");
//            System.out.println("S-Slot AVG \t" + df.format(Logic.calcSpTimes(nftMonitor.getSpList(), 64) / 1000000.0) +  " \t " + df.format((Logic.calcSpTimes(nftMonitor.getSpList(), 64) / 10000.0) /9.375 ) + "%");
//            System.out.println("2 Min AVG \t" + df.format(Logic.calcSpTimes(nftMonitor.getSpList(), 13) / 1000000.0) + " \t " + df.format((Logic.calcSpTimes(nftMonitor.getSpList(), 13) / 10000.0) /9.375 ) + "%");
//
//
//            Blockchain_State bs2 = nftMonitor.node().getBlockchainState();
//
//            System.out.println("\nNFT Monitor: \tNetspace: " + bs2.getSpace() + " EiB" + "\t" +
//                    "Chain Height: " + bs2.getPeak().getHeight() + "\t" +
//                    "Mem Pool Size: " +  bs2.getMempoolSize() + "\n");
//
//
////        System.out.println(monitor.getSignagePointList().get(i).getWarning() + "   \t" + monitor.getSignagePointList().get(i).getSignagePointIndex() +
////        "   " + (monitor.getSignagePointList().get(i).getTimeSinceLast() / 1000000.0) + "   " + monitor.getSignagePointList().get(i).getTime());
////
//   //     }
//            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
//            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
//            Thread.sleep(20000);
//
//
//
//
//
//
//
//
//
//        }
//    }
//}
//
//
