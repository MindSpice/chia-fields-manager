package rpc;//import org.json.simple.JsonObject;

import com.google.gson.JsonObject;
import endpoints.Certs;
import endpoints.Paths;

public class RPCWallet extends RPC {

    public RPCWallet(Certs certs, Paths paths) {
        this.certs = certs;
        this.paths = paths;
    }


    public void logIn(String fingerprint) {
        String data = "\"{\\\"fingerprint\\\":" + fingerprint + "}\"";
        String request = "log_in";
        RPCRequest rpc = new RPCRequest(Service.WALLET, request, data, certs, paths);
        JsonObject returnJSON = rpc.makeRequest();
        System.out.println(returnJSON.keySet());

    }

    public void getWallets() {
        String data = null;
        String request = "get_wallets";
        RPCRequest rpc = new RPCRequest(Service.WALLET, request, data, certs, paths);

        JsonObject returnJSON = rpc.makeRequest();
        System.out.println(returnJSON.keySet());
        System.out.println(returnJSON.get("wallets"));
    }

    public void getFarmedAmount(String walletID) {
        String data = "\"{\\\"wallet_id\\\":" + walletID + "}\"";
        String request = "get_farmed_amount";
        RPCRequest rpc = new RPCRequest(Service.WALLET, request, data, certs, paths);

        JsonObject returnJSON = rpc.makeRequest();
        System.out.println(returnJSON.keySet());
    }

    public void getWalletBalance(String walletID) {
        String data = "\"{\\\"wallet_id\\\":" + walletID + "}\"";
        String request = "get_wallet_balance";
        RPCRequest rpc = new RPCRequest(Service.WALLET, request, data, certs, paths);

        JsonObject returnJSON = rpc.makeRequest();
        System.out.println(returnJSON.keySet());
    }

    public void getPrivateKey(String fingerprint) {
        String data = "\"{\\\"fingerprint\\\":" + fingerprint + "}\"";
        String request = "get_private_key";
        RPCRequest rpc = new RPCRequest(Service.WALLET, request, data, certs, paths);

        JsonObject returnJSON = rpc.makeRequest();
        System.out.println(returnJSON.get("private_key"));
    }

    public void getPublicKeys() {
        String data = null;
        String request = "get_public_keys";
        RPCRequest rpc = new RPCRequest(Service.WALLET, request, data, certs, paths);

        JsonObject returnJSON = rpc.makeRequest();
        System.out.println(returnJSON.get("public_key_fingerprints"));
    }

    public void getSyncStatus() {
        String data = null;
        String request = "get_sync_status";
        RPCRequest rpc = new RPCRequest(Service.WALLET, request, data, certs, paths);

        JsonObject returnJSON = rpc.makeRequest();
        System.out.println(returnJSON.keySet());
    }

    public void getTransactions(String walletID) {
        String data = "\"{\\\"wallet_id\\\":" + walletID + "}\"";
        String request = "get_transactions";
        RPCRequest rpc = new RPCRequest(Service.WALLET, request, data, certs, paths);

        JsonObject returnJSON = rpc.makeRequest();
        System.out.println(returnJSON.keySet());
    }

   //TODO Implement fetch by txID
    public void getTransaction(String txID) {
        String data = null;
        //FIXME String data ="\"{\\\"wallet_id\\\":" + walletID + "}\"";
        String request = "get_transaction";
        RPCRequest rpc = new RPCRequest(Service.WALLET, request, data, certs, paths);

        JsonObject returnJSON = rpc.makeRequest();
        System.out.println(returnJSON.keySet());
    }


}
