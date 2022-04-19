package endpoints;

import java.util.ArrayList;
import java.util.List;

public class Endpoints {

    private static List<Endpoint> endpointsList = new ArrayList<>();


    public void addEndpoint(String name, String certs, String paths, String address) {
        new Endpoint(name, certs, paths, address);
    }

    public Certs getCerts(int index) {
        return endpointsList.get(index).getCerts();
    }

    public Paths getPaths(int index) {
        return endpointsList.get(index).getPaths();
    }
//    public Paths getName(int index) {
//        return endpointsList.get(index).getName();
//    }



    public  class Endpoint {

        String name;
        Certs certs;
        Paths paths;

        public Endpoint(String name, String certDir, String path, String address) {
            this.name = name;
            certs = new Certs(certDir);
            paths = new Paths(path, address);
            endpointsList.add(this);
        }

        public String getName() {
            return name;
        }

        public Certs getCerts() {
            return this.certs;
        }

        public Paths getPaths() {
            return this.paths;
        }


    }
}



