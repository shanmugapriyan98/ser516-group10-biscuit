/*
	Author: Joseph Thomas;
 */

package com.biscuit.models;

import java.util.ArrayList;
import java.util.List;


public class Epic {

    public transient Project project;

    // info
    public String name;
    public String description;

    public List<String> userStories = new ArrayList<>();

    public static String[] fields;
    public static String[] fieldsAsHeader;

    static {
        fields = new String[]{"name", "description", "userStories"};
        fieldsAsHeader = new String[]{"Name", "Description", "User Stories"};
    }

    public void addUserStory(String userStory) {
        this.userStories.add(userStory);
    }

    public void save() {
        project.save();
    }

}
