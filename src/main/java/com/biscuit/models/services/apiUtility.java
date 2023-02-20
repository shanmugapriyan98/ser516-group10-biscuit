package com.biscuit.models.services;
import com.biscuit.Login;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;

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
            System.out.println("Error: Unable to fetch authToken from Login instance.");
        }
    }

    String requestDescription;
    HashMap<String,String> body = new HashMap<>();
    String urlPrefix = "https://api.taiga.io/api/v1/";
    String endpointPath;
    private final OkHttpClient httpClient = new OkHttpClient();
    Request.Builder reqBuilder = new Request.Builder();

    public apiUtility(String endpointPath , String requestDescription) {
        this.endpointPath = endpointPath;
        this.requestDescription = requestDescription;
        reqBuilder.url(urlPrefix+endpointPath);
        reqBuilder.addHeader("Content-Type", "application/json");
        reqBuilder.addHeader("Authorization", "Bearer "+authToken);
    }
    public apiUtility(String endpointPath, String requestDescription, HashMap<String ,String> body){
        this.endpointPath= endpointPath;
        this.body= body;
        this.requestDescription = requestDescription;
        reqBuilder.url(urlPrefix+endpointPath);
        reqBuilder.addHeader("Content-Type", "application/json");
        reqBuilder.addHeader("Authorization", "Bearer "+authToken);
    }
    public JSONArray apiGET(){
        reqBuilder.get();
        Request request = reqBuilder.build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            JSONArray jsonArray= new JSONArray(response.body().string());
            System.out.println(requestDescription + " processed successfully");
            return  jsonArray;

        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Error while processing request " + requestDescription);
        }
        return new JSONArray();
    }
    public void apiPOST(){
        FormBody.Builder builder = new FormBody.Builder();
        for(Map.Entry<String,String > mmap : body.entrySet())  builder.add(mmap.getKey(), mmap.getValue());
        RequestBody formBody = builder.build();
        reqBuilder.post(formBody);
        Request request = reqBuilder.build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            System.out.println(requestDescription + " processed successfully");

        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Error while processing request " + requestDescription);
        }
    }
    public void apiDELETE(){
        reqBuilder.delete();
        Request request = reqBuilder.build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            System.out.println(requestDescription + " processed successfully");
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Error while processing request " + requestDescription);
        }
    }
}
