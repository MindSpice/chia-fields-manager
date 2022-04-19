package endpoints;

public class Paths {
    private static final String protocol = "https://";
    private String nodePort;
    private String farmerPort;
    private String harvesterPort;
    private String walletPort;
    private String hostAddress;
    private static String curlPath = "c:\\curl";
    private String logPath;

    Paths(String nodePort, String farmerPort, String harvesterPort, String walletPort, String hostAddress, String logPath) {
        this.nodePort = nodePort;
        this.farmerPort = farmerPort;
        this.harvesterPort = harvesterPort;
        this.walletPort = walletPort;
        this.hostAddress = hostAddress;
        this.logPath = logPath;
    }

    Paths() {
        this.nodePort = "8555";
        this.farmerPort = "8559";
        this.harvesterPort = "8560";
        this.walletPort = "9256";
        this.hostAddress = "192.168.0.103";
        this.logPath = "\\\\192.168.0.103\\log\\debug.log";
    }

    public Paths(String logPath, String address) {
        this.nodePort = "8555";
        this.farmerPort = "8559";
        this.harvesterPort = "8560";
        this.walletPort = "9256";
        this.hostAddress = address;
        this.logPath = logPath;
    }

    public String getNodePort() {
        return nodePort;
    }

    public String getFarmerPort() {
        return farmerPort;
    }

    public String getHarvesterPort() {
        return harvesterPort;
    }

    public String getWalletPort() {
        return walletPort;
    }

    public String getHost() {
        return (protocol + hostAddress);
    }
    public String getAddress() {
        return hostAddress;
    }


    public String getCurlPath() {
        return curlPath;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public String getLogPath() {
        return logPath;
    }
}
