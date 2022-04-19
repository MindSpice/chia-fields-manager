package rpc;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import data.json.Pool_State;
import data.json.Signage_Points;
import endpoints.Certs;
import endpoints.Paths;


import java.util.ArrayList;
import java.util.List;

public class RPCFarmer extends RPC {

    public RPCFarmer(Certs certs, Paths paths) {
        this.certs = certs;
        this.paths = paths;
    }



//    public static void getSignagePoint() {
//        String data = null;
//        String request = "get_signage_point";
//        RPCRequest rpc = new RPCRequest(Service.FARMER, request, data, certs, paths);
//
//        JsonObject returnJSON = rpc.makeRequest();
//        System.out.println(returnJSON.keySet());
//    }

    public void getFarmInfo() {
        String data = null;
        String request = "farm_info";
        RPCRequest rpc = new RPCRequest(Service.FARMER, request, data, certs, paths);
        JsonObject returnJSON = rpc.makeRequest();
        System.out.println(returnJSON.keySet());
    }


    //TODO: Needs Class To Hold Array, Cant Test On Current PC To Implement
    public void getHarvesters() {
        String data = null;
        String request = "get_harvesters";
        RPCRequest rpc = new RPCRequest(Service.FARMER, request, data, certs, paths);
        JsonObject returnJSON = rpc.makeRequest();
        System.out.println(returnJSON.get("harvesters"));
    }

    public void getSignagePoints() {
        String data = null;
        String request = "get_signage_points";

        List<Signage_Points> signagePoints = new ArrayList<>();
        RPCRequest rpc = new RPCRequest(Service.FARMER, request, data, certs, paths);
        Gson gson = new Gson();

        JsonArray jArr = rpc.makeRequest().getAsJsonArray("signage_points");
        for (JsonElement j : jArr) {
            Signage_Points signage_points = gson.fromJson(j, Signage_Points.class);
            signagePoints.add(signage_points);
        }
        for (Signage_Points sp : signagePoints) {
            System.out.println(sp.getSignage_Point().getIndex());
        }
    }

    public void getSignagePoint() {
        String data = null;
        String request = "get_signage_points";

        List<Signage_Points> signagePoints = new ArrayList<>();
        RPCRequest rpc = new RPCRequest(Service.FARMER, request, data, certs, paths);
        Gson gson = new Gson();

        JsonArray jArr = rpc.makeRequest().getAsJsonArray("signage_points");
        Signage_Points signage_points = gson.fromJson(jArr.get(jArr.size() - 1), Signage_Points.class);
        signagePoints.add(signage_points);

        System.out.println(signagePoints.get(0).getSignage_Point().getIndex());

    }

    public List<Pool_State> getPoolState() {
        String data = null;
        String request = "get_pool_state";
        RPCRequest rpc = new RPCRequest(Service.FARMER, request, data, certs, paths);
        Gson gson = new Gson();

//        JsonArray jArr = rpc.makeRequest().getAsJsonArray("pool_state");
//        Pool_State pool_state = gson.fromJson(jArr.get(1), Pool_State.class);
//        return pool_state;
        List<Pool_State> poolStates = new ArrayList<>();
        JsonArray jArr = rpc.makeRequest().getAsJsonArray("pool_state");
        for (JsonElement j : jArr) {
            Pool_State pool_state = gson.fromJson(j, Pool_State.class);
            poolStates.add(pool_state);
        }
        return poolStates;
    }

    public void getPoolLoginLink() {
        String data = null;
        String request = "get_pool_login_link";
        RPCRequest rpc = new RPCRequest(Service.FARMER, request, data, certs, paths);
        JsonObject returnJSON = rpc.makeRequest();
        System.out.println(returnJSON.keySet());
    }

    /*
    Searching for private key bool(true) takes upwards of 10 seconds
    Likely quicker to just parse config yaml and compare if node is local
     */
    public  void getRewardTargets(boolean searchForPrivateKey) {
        String data = "\"{\\\"search_for_private_key\\\":" + searchForPrivateKey + "}\"";
        String request = "get_reward_targets";

        RPCRequest rpc = new RPCRequest(Service.FARMER, request, data, certs, paths);
        JsonObject returnJSON = rpc.makeRequest();

        System.out.println(returnJSON.keySet());
        System.out.println(returnJSON.get("farmer_target"));
        System.out.println(returnJSON.get("pool_target"));

        if (searchForPrivateKey == true) {
            System.out.println(returnJSON.get("have_farmer_sk"));
            System.out.println(returnJSON.get("have_pool_sk"));
        }
    }
}
