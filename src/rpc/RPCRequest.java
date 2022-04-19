package rpc;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import endpoints.Certs;
import endpoints.Endpoints;
import endpoints.Paths;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class RPCRequest {


    Certs certs;
    Paths paths;
    RPC.Service service;
    private String port;

    private String[] commandList = {"cmd.exe", "/c",
            "curl",
            "-s",
            "--insecure",
            "--cert",
            null,      // Certificate File Path [6]
            "--key",
            null,      // Key File Path [8]
            "-d",
            null,      // Include Data [10]
            "-H",
            "\"Content-Type: application\\json\"",
            "-X",
            "POST",
            null,};    // Service Request [15] (https://xxx.xxx.x.xxx:port/rpc_call" Use requestBuilder


    public RPCRequest(RPC.Service service, String request, String data, Certs certs, Paths paths) {
        this.certs = certs;
        this.paths = paths;
        requestBuilder(service, request, data);
    }


    private void setService(RPC.Service service) {
        Endpoints endpoints = new Endpoints();

        switch (service) {
            case NODE -> {
                this.commandList[6] = certs.getNodeCert();
                this.commandList[8] = certs.getNodeKey();
                this.port = paths.getNodePort();
                this.service = service;
            }
            case FARMER -> {
                this.commandList[6] = certs.getFarmerCert();
                this.commandList[8] = certs.getFarmerKey();
                this.port = paths.getFarmerPort();
                this.service = service;
            }
            case HARVESTER -> {
                this.commandList[6] = certs.getHarvesterCert();
                this.commandList[8] = certs.getHarvesterKey();
                this.port = paths.getHarvesterPort();
                this.service = service;
            }
            case WALLET -> {
                this.commandList[6] = certs.getWalletCert();
                this.commandList[8] = certs.getWalletKey();
                this.port = paths.getWalletPort();
                this.service = service;
            }
        }
    }


    private void requestBuilder(RPC.Service service, String request, String data) {
        Endpoints endpoints = new Endpoints();
        setService(service);

        if (data == null) {
            this.commandList[10] = "{}";
        } else {
            this.commandList[10] = data;
        }

        StringBuilder serviceRequest = new StringBuilder();

        serviceRequest.append(paths.getHost());
        serviceRequest.append(":");
        serviceRequest.append(port);
        serviceRequest.append("/");
        serviceRequest.append(request);

        commandList[15] = serviceRequest.toString();
    }

    public JsonObject makeRequest() {


        /*       TEST TOGGLE
         *  OUTPUTS CURL REQUEST
         */
//        for (String s : commandList) {
//            System.out.print(s);
//            System.out.print(" ");
//        }
//        System.out.println("\n");
                /* TEST END*/


        ProcessBuilder processBuilder = new ProcessBuilder(commandList);
        processBuilder.directory(new File(paths.getCurlPath()));
        JsonObject returnJSON = null;

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));


            JsonElement element = JsonParser.parseReader(reader);

            if ((!(element instanceof JsonNull))) { //FIXME this is some hackish bullshit
                returnJSON = (JsonObject)(element);
            } else {
                return null;
            }

            int exitCode = process.waitFor();
            // DEBUG ERROR CODE
            if (exitCode != 0) {
                System.out.println("\nExited with error code : " + exitCode);
                System.out.println("Service Not Responding: " + service);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnJSON;
    }
}
