package com.biscuit;

import java.util.Scanner;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
public class Login {
    /**
     * Login instance variable.
     */
    private static Login instance = null;

    /**
     * Username of user.
     */
    private String userName;

    /**
     * Password for username.
     */
    private String password;

    /**
     * Json array.
     */
    private JSONArray jsonProjectsArray;

    /**
     * Request object for http req.
     */
    private Request request;

    /**
     * RequestBody for hhtp request.
     */
    private RequestBody formBody;

    /**
     * Authentication token.
     */
    public String authToken;

    /**
     * Fetched full name of user.
     */
    public String fullName;

    /**
     * Member ID of user.
     */
    public int memberId;
    private final OkHttpClient httpClient = new OkHttpClient();

    public void sendPost(String uname, String pwd){
        JSONObject jsonObject;
        formBody = new FormBody.Builder()
                .add("username", uname)
                .add("password", pwd)
                .add("type", "normal")
                .build();

        request = new Request.Builder()
                .url("https://api.taiga.io/api/v1/auth")
                .addHeader("Content-Type", "application/json")
                .post(formBody)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            jsonObject = new JSONObject(response.body().string());
            authToken = jsonObject.getString("auth_token");
            fullName = jsonObject.getString("full_name");
            memberId = jsonObject.getInt("id");

        } catch (IOException ioException){
            System.out.println("Authentication unsuccessful");
            System.exit(1);
        }

    }

    public Login(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Username:");
        userName = scanner.next();
        System.out.print("Enter Password:");
        password = scanner.next();
        sendPost(userName, password);
    }

    public static Login getInstance(){
        if(instance == null)
            instance = new Login();
        return instance;
    }

    public static void setInstance(Login login) {
        instance = login;
    }

    public void displayProjects(){
        JSONObject jsonObject;
        System.out.println();
        request = new Request.Builder()
                .url("https://api.taiga.io/api/v1/projects?member="+memberId)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer "+authToken)
                .get()
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            jsonProjectsArray = new JSONArray(response.body().string());
            System.out.println("Project Name : Slug Name");
            System.out.println("-------------------------");
            for(int i = 0; i< jsonProjectsArray.length(); i++){
                jsonObject = jsonProjectsArray.getJSONObject(i);
                System.out.println(jsonObject.getString("name")+" : "+jsonObject.getString("slug"));
            }
            System.out.println();
        } catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

}
