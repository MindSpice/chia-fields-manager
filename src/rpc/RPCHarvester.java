package rpc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import endpoints.Certs;
import endpoints.Paths;

public class RPCHarvester extends RPC {

    public RPCHarvester(Certs certs, Paths paths) {
        this.certs = certs;
        this.paths = paths;
    }

    public void getPlots() {
        String data = null;
        String request = "get_plots";
        RPCRequest rpc = new RPCRequest(Service.HARVESTER, request, data, certs, paths);

        JsonObject returnJSON = rpc.makeRequest();
        System.out.println(returnJSON.get("plots"));
    }

    public void refreshPlots() {
        String data = null;
        String request = "refresh_plots";
        RPCRequest rpc = new RPCRequest(Service.HARVESTER, request, data, certs, paths);

        JsonObject returnJSON = rpc.makeRequest();
        System.out.println(returnJSON.keySet());
    }

    public void addPlotDirectory() {
        String data = null;
        String request = "add_plot_directory";
        RPCRequest rpc = new RPCRequest(Service.HARVESTER, request, data, certs, paths);

        JsonObject returnJSON = rpc.makeRequest();
        System.out.println(returnJSON.keySet());
    }

    public void getPlotDirectories() {
        String data = null;
        String request = "get_plot_directories";
        RPCRequest rpc = new RPCRequest(Service.HARVESTER, request, data, certs, paths);

        JsonObject returnJSON = rpc.makeRequest();
        JsonArray dir = (JsonArray) returnJSON.get("directories");
        for (JsonElement d : dir) {
            System.out.println(d);
        }
    }

    public void removePlotDirectory() {
        String data = null;
        String request = "remove_plot_directory";
        RPCRequest rpc = new RPCRequest(Service.HARVESTER, request, data, certs, paths);

        JsonObject returnJSON = rpc.makeRequest();
        System.out.println(returnJSON.keySet());
    }
}
