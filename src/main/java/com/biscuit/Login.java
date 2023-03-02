package com.biscuit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.biscuit.models.services.apiUtility;
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
     * Request object for http req.
     */
    private JSONArray jsonProjectsArray, jsonUserStoriesArray;
    public List<List<String>> userStoriesList = new ArrayList<>();
    public HashMap<String, Integer> projectMap = new HashMap<>();
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
        String requestDescription = "Display all projects of member";
        String endpointPath = "projects?member="+memberId;
        apiUtility utility = new apiUtility(endpointPath, requestDescription);
        jsonProjectsArray = utility.apiGET();
        System.out.println();
        System.out.println("Available projects");
        System.out.println("------------------");
        for(int i = 0; i< jsonProjectsArray.length(); i++){
            jsonObject = jsonProjectsArray.getJSONObject(i);
            System.out.println(jsonObject.getString("name"));
            projectMap.put(jsonObject.getString("name"), jsonObject.getInt("id"));
        }
        System.out.println();
    }


    public void getBackLogDataFromProject(String projectName){
        JSONObject jsonObject;
        userStoriesList = new ArrayList<>();
        int len;
        if(projectMap.get(projectName) == null) {
            System.out.println("Project Name Invalid");
            System.exit(1);
        }
        String requestDescription = "Get backlog data from project";
        String endpointPath = "userstories?project="+projectMap.get(projectName);
        apiUtility utility = new apiUtility(endpointPath, requestDescription);
        jsonUserStoriesArray = utility.apiGET();
        for(int i = 0; i< jsonUserStoriesArray.length(); i++){
            jsonObject = jsonUserStoriesArray.getJSONObject(i);
            if(jsonObject.has("subject") && String.valueOf(jsonObject.get("milestone_name")).equals("null")){
                userStoriesList.add(new ArrayList<>());
                len = userStoriesList.size()-1;
                userStoriesList.get(len).add(String.valueOf(jsonObject.get("ref")));
                userStoriesList.get(len).add((String) jsonObject.get("subject"));
                userStoriesList.get(len).add(String.valueOf(jsonObject.get("total_points")));
            }
        }
    }
}
