package rpc;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import endpoints.Certs;
import endpoints.Endpoints;
import endpoints.Paths;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class RPC {

    Certs certs;
    Paths paths;


    RPC() {
    };




    enum Service {
        NODE,
        FARMER,
        HARVESTER,
        WALLET
    }
}


