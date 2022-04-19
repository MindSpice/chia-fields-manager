package rpc;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import data.json.Blockchain_State;
import data.json.Connection;
import endpoints.Certs;
import endpoints.Paths;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class RPCNode extends RPC {

   public RPCNode(Certs certs, Paths paths) {
       this.certs = certs;
       this.paths = paths;
    }

    public  Blockchain_State getBlockchainState() {
       try {
           String data = null;
           String request = "get_blockchain_state";
           RPCRequest rpc = new RPCRequest(Service.NODE, request, data, certs, paths);
           Gson gson = new Gson();

           JsonObject requestReturn = rpc.makeRequest();

           if (requestReturn == null) return null;

           Blockchain_State blockchain_state = gson.fromJson(requestReturn.get("blockchain_state"), Blockchain_State.class);
           return blockchain_state;

       } catch (Exception e) {
           e.printStackTrace();
           return null;
       }
    }


    public ObservableList<Connection> getConnections() {
        String data = null;
        String request = "get_connections";
        ObservableList<Connection> connectionList = FXCollections.observableArrayList();
        RPCRequest rpc = new RPCRequest(Service.NODE, request, data, certs, paths);
        Gson gson = new Gson();

        JsonObject requestReturn = rpc.makeRequest();


        if (requestReturn == null) {
            System.out.println("null was found");
            return null;
        }

        JsonArray jArr = requestReturn.getAsJsonArray("connections");

        for (JsonElement j : jArr) {
            Connection connection = gson.fromJson(j, Connection.class);
            connectionList.add(connection);

        }
        return connectionList;

    }

    public void closeConnection(String nodeID) {
        String data = "\"{\\\"node_id\\\": \\\"" + nodeID + "\\\"}\"";
        String request = "close_connection";
        RPCRequest rpc = new RPCRequest(Service.NODE, request, data, certs, paths);
        JsonObject returnJSON = rpc.makeRequest();
        if (returnJSON.get("success").toString().equals("true")) {
            System.out.println("Connection Successfully Removed");
        }
        ;


    }


    //    public static void getNetworkSpace() {
//        String data = null;
//        String request = "get_network_space";
//        RPC rpc = new RPC(Service.NODE, request, data);
//
//       JsonObject returnJSON = rpc.makeRequest();
//       System.out.println(returnJSON.get("error"));
//    }


    //    public static void getNetworkInfo() {
//        String data = null;
//        String request = "get_network_info";
//        RPC rpc = new RPC(Service.NODE, request, data);
//
//        JsonObject returnJSON = rpc.makeRequest();
//        System.out.println(returnJSON.keySet());
//    }

}
