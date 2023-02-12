package com.biscuit;

import java.util.Scanner;

import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
public class Login {
    private static Login instance = null;
    private String userName;
    private String password;
    public String authToken;
    public String fullName;
    private final OkHttpClient httpClient = new OkHttpClient();

    public void sendPost(String uname, String pwd){
        RequestBody formBody = new FormBody.Builder()
                .add("username", uname)
                .add("password", pwd)
                .add("type", "normal")
                .build();

        Request request = new Request.Builder()
                .url("https://api.taiga.io/api/v1/auth")
                .addHeader("Content-Type", "application/json")
                .post(formBody)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            JSONObject jsonObject = new JSONObject(response.body().string());
            authToken = jsonObject.getString("auth_token");
            fullName = jsonObject.getString("full_name");

        } catch (IOException ioException){
            System.out.println("Authentication unsuccessful");
            System.exit(1);
        }

    }

    private Login() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Username:");
        userName = scanner.next();
        System.out.print("Enter Password:");
        password = scanner.next();
        sendPost(userName, password);
    }

    public static Login getInstance() throws Exception {
        if(instance == null)
            instance = new Login();
        return instance;
    }

    public static void setInstance(Login login) {
        instance = login;
    }

}