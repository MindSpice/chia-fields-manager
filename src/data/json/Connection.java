package data.json;



import java.math.BigDecimal;

public class Connection {

    int peer_port = -1;
    int peer_server_port = -1;
    int type = -1;
    int local_port = -1;
    int bytes_read = -1;
    int bytes_written = -1;
    int peak_height = -1;
    long peak_weight = -1;
    double creation_time = -1;
    double last_message_time = -1;
    String node_id = "";
    String peak_hash = "";
    String peer_host = "";

    public int getPort() {
        return peer_port;
    }

    public int getServerPort() {
        return peer_server_port;
    }

    public int getType() {
        return type;
    }

    public int getLocalPort() {
        return local_port;
    }

    public long getRead() {
        return bytes_read;
    }

    public long getWrite() {
        return bytes_written;
    }

    public Integer getPeakHeight() {
        return peak_height;
    }

    public long getPeakWeight() {
        return peak_weight;
    }

    public double getCreationTime() {
        return creation_time;
    }

    public double getLastMessage() {
        return last_message_time;
    }

    public String getNodeID() {
        return node_id;
    }

    public String getPeakHash() {
        return peak_hash;
    }

    public String getHost() {
        return peer_host;
    }
}
