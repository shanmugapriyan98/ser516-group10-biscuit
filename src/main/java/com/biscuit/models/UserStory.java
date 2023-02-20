package com.biscuit.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.biscuit.models.enums.BusinessValue;
import com.biscuit.models.enums.Status;

public class UserStory {

    /**
     * Project object to store current project details.
     */
    public transient Project project;

    /**
     * Title of the project.
     */
    public String title;

    /**
     * Description of the project.
     */
    public String description;

    /**
     * State of the user story.
     */
    public Status state;

    /**
     * Business value per user story.
     */
    public BusinessValue businessValue;

    /**
     * Initial date per user story.
     */
    public Date initiatedDate = null;

    /**
     * Planned date per user story.
     */
    public Date plannedDate = null;

    /**
     * Due Date per user story.
     */
    public Date dueDate = null;

    /**
     * Story points per user story.
     */
    public int points;

    /**
     * Array for fields.
     */
    public static String[] fields;

    public List<Task> tasks = new ArrayList<>();
    public List<Bug> bugs = new ArrayList<>();
    public List<Test> tests = new ArrayList<>();

    /**
     * List array to store comments.
     */
    public List<String> comments;

    static {
        fields = new String[]{"title", "description", "state", "business_value",
                "initiated_date", "planned_date", "due_date", "tasks", "points", "comments"};
    }


    /**
     * Method to save user story.
     */
    public void save() {
        project.save();
    }

}
