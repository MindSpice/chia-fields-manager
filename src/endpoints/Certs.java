package endpoints;

public class Certs {

    private String nodeCert;
    private String nodeKey;
    private String harvesterCert;
    private String harvesterKey;
    private String farmerCert;
    private String farmerKey;
    private String walletCert;
    private String walletKey;

    public Certs() {
        this.nodeCert      = "C:\\ssl\\full_node\\private_full_node.crt";
        this.nodeKey       = "C:\\ssl\\full_node\\private_full_node.key";
        this.harvesterCert = "C:\\Users\\dwigh\\.chia\\mainnet\\config\\ssl\\harvester\\private_harvester.crt";
        this.harvesterKey  = "C:\\Users\\dwigh\\.chia\\mainnet\\config\\ssl\\harvester\\private_harvester.key";
        this.farmerCert    = "C:\\Users\\dwigh\\.chia\\mainnet\\config\\ssl\\farmer\\private_farmer.crt";
        this.farmerKey     = "C:\\Users\\dwigh\\.chia\\mainnet\\config\\ssl\\farmer\\private_farmer.key";
        this.walletCert    = "C:\\Users\\dwigh\\.chia\\mainnet\\config\\ssl\\wallet\\private_wallet.crt";
        this.walletKey     = "C:\\Users\\dwigh\\.chia\\mainnet\\config\\ssl\\wallet\\private_wallet.key";
    }


    private Certs(String nodeCert, String nodeKey, String harvesterCert, String harvesterKey,
                  String farmerCert, String farmerKey, String walletCert, String walletKey) {
        this.nodeCert = nodeCert;
        this.nodeKey = nodeKey;
        this.harvesterCert = harvesterCert;
        this.harvesterKey = harvesterKey;
        this.farmerCert = farmerCert;
        this.farmerKey = farmerKey;
        this.walletCert = walletCert;
        this.walletKey = walletKey;
    }

    public Certs(String sslDir) {
        this.nodeCert = sslDir + "\\full_node\\private_full_node.crt";
        this.nodeKey = sslDir + "\\full_node\\private_full_node.key";
        this.harvesterCert = sslDir + "\\harvester\\private_harvester.crt";
        this.harvesterKey = sslDir + "\\harvester\\private_harvester.key";
        this.farmerCert = sslDir + "\\farmer\\private_farmer.crt";
        this.farmerKey = sslDir + "\\farmer\\private_farmer.key";
        this.walletCert = sslDir + "\\wallet\\private_wallet.crt";
        this.walletKey = sslDir + "\\wallet\\private_wallet.key";
    }

    /*Getters*/

    public String getNodeCert() {
        return nodeCert;
    }

    public String getNodeKey() {
        return nodeKey;
    }

    public String getWalletCert() {
        return walletCert;
    }

    public String getWalletKey() {
        return walletKey;
    }

    public String getFarmerCert() {
        return farmerCert;
    }

    public String getFarmerKey() {
        return farmerKey;
    }

    public String getHarvesterCert() {
        return harvesterCert;
    }

    public String getHarvesterKey() {
        return this.harvesterKey;
    }


    /*Setters*/

    public void setNodeCert(String nodeCert) {
        this.nodeCert = nodeCert;
    }

    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }

    public void setWalletCert(String walletCert) {
        this.walletCert = walletCert;
    }

    public void setWalletKey(String walletKey) {
        this.walletKey = walletKey;
    }

    public void setFarmerCert(String farmerCert) {
        this.farmerCert = farmerCert;
    }

    public void setFarmerKey(String farmerKey) {
        this.farmerKey = farmerKey;
    }

    public void setHarvesterCert(String harvesterCert) {
        this.harvesterCert = harvesterCert;
    }

    public void setHarvesterKey(String harvesterKey) {
        this.harvesterKey = harvesterKey;
    }
}
