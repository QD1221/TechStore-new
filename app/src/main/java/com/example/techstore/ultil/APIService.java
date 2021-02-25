package com.example.techstore.ultil;

public class APIService {
    private static String base_url = "https://qdstore.000webhostapp.com/server/";

    public static DataService getService(){
        return com.example.techstore.ultil.APIRetrofitClient.getClient(base_url).create(DataService.class);
    }
}
