package com.biscuit.models.services;
import com.biscuit.Login;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class apiUtility {
    String authToken;
    {
        try {
            authToken = Login.getInstance().authToken;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while fetching authToken from Login instance.");
        }
    }
    String requestType;
    String requestDescription;
    HashMap<String,String> body = new HashMap<>();
    String urlPrefix = "https://api.taiga.io/api/v1/";
    String endpointPath;
    private final OkHttpClient httpClient = new OkHttpClient();

    public apiUtility(String requestType, String endpointPath , String requestDescription) {
        this.requestType = requestType;
        this.endpointPath = endpointPath;
        this.requestDescription = requestDescription;
    }
    public apiUtility(String requestType, String endpointPath, String requestDescription, HashMap<String ,String> body){
        this.requestType = requestType;
        this.endpointPath= endpointPath;
        this.body= body;
        this.requestDescription = requestDescription;
    }
    public JSONObject apiCall(){
        Request.Builder reqBuilder = new Request.Builder();
        reqBuilder.url(urlPrefix+endpointPath);
        reqBuilder.addHeader("Content-Type", "application/json");
        reqBuilder.addHeader("Authorization", "Bearer "+authToken);
        if(requestType == "GET") reqBuilder.get();
        else if (requestType == "POST") {
            FormBody.Builder builder = new FormBody.Builder();
            for(Map.Entry<String,String > mmap : body.entrySet())  builder.add(mmap.getKey(), mmap.getValue());
            RequestBody formBody = builder.build();
            reqBuilder.post(formBody);
        }
        Request request = reqBuilder.build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            JSONObject jsonObject= new JSONObject(response.body().string());
            System.out.println(requestDescription + " processed successfully");
            return  jsonObject;

        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Error while processing request " + requestDescription);
        }
    return new JSONObject();
    }
}
