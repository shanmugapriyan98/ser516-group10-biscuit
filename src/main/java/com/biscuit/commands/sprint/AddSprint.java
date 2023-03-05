package com.biscuit.commands.sprint;

import com.biscuit.ColorCodes;
import com.biscuit.commands.Command;
import com.biscuit.models.Project;
import com.biscuit.models.Sprint;
import com.biscuit.models.enums.Status;
import com.biscuit.models.services.apiUtility;
import jline.console.ConsoleReader;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class AddSprint implements Command {

    ConsoleReader reader = null;
    Project project = null;
    Sprint sprint = new Sprint();

    boolean local = false;
    boolean remote = false;


    public AddSprint(ConsoleReader reader, boolean local, boolean remote, String name, Project project) {
        super();
        this.reader = reader;
        this.project = project;
        this.local = local;
        this.remote = remote;
        sprint.name = name;
    }


    public boolean execute() throws IOException {
        StringBuilder description = new StringBuilder();

        if (!local) {
            String line;
            String prompt = reader.getPrompt();

            sprint.project = project;
            if (sprint.name == null) {
                setName();
                //setDescription(description);
                sprint.state = Status.NEW;
                sprint.startDate = new Date(0);
                sprint.dueDate = new Date(0);

                if (setStartDate()) {
                    if (!setDuration()) {
                        setDueDate();
                    }
                }

                sprint.assignedEffort = 0;
                //setVelocity();

                reader.setPrompt(prompt);

                project.addSprint(sprint);
                project.save();

                reader.println();
                reader.println(ColorCodes.GREEN + "Sprint \"" + sprint.name + "\" has been added!" + ColorCodes.RESET);

            }
        }

        if (!remote) {
            String requestDescription = "Create Sprint";
            HashMap<String, String> body = new HashMap<>();
            project.populateDetails();
            body.put("project", Integer.toString(project.id));
            body.put("name", sprint.name);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String startDate = formatter.format(sprint.startDate);
            String dueDate = formatter.format(sprint.dueDate);
            body.put("estimated_start", startDate);
            body.put("estimated_finish", dueDate);
            //body.put("estimated_finish", String.valueOf(sprint.dueDate));
            String endpoint = "milestones";
            apiUtility utility = new apiUtility(endpoint, requestDescription, body);
            utility.apiPOST();
            System.out.println(sprint.name + "created successfully on Taiga");
        }


        return false;
    }


    private void setDueDate() throws IOException {

        String line;

        reader.setPrompt(ColorCodes.BLUE + "\ndue date:\n" + ColorCodes.YELLOW + "Format example: yyyy-MM-dd (2023-01-01)\n"
                + ColorCodes.RESET);


        while ((line = reader.readLine()) != null) {
            line = line.trim();


            try {

                LocalDate date = LocalDate.parse(line, DateTimeFormatter.ISO_DATE);
                Date dt = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

                sprint.dueDate = dt;

            } catch (NumberFormatException | NullPointerException | ArrayIndexOutOfBoundsException |
                     DateTimeParseException e) {
                System.out.println(ColorCodes.RED + "invalid value" + ColorCodes.RESET);
                continue;
            }

            break;
        }


    }


    private boolean setDuration() throws IOException {
        String line;
        int duration;

//        reader.setPrompt(ColorCodes.BLUE + "duration: " + ColorCodes.YELLOW + "(no of days)\n" + ColorCodes.RESET);

        reader.setPrompt(ColorCodes.BLUE + "duration (no of days): " + ColorCodes.YELLOW + "(optional: leave it blank and hit enter)\n" + ColorCodes.RESET);

        while ((line = reader.readLine()) != null) {
            line = line.trim();

            if (line.isEmpty()) {
                return false;
            }

            try {
                duration = Integer.valueOf(line);
                if (duration <= 0) {
                    throw new IllegalArgumentException();
                }

                Calendar cal = new GregorianCalendar();
                cal.setTime(sprint.startDate);
                cal.add(Calendar.DAY_OF_YEAR, duration - 1);
                sprint.dueDate = cal.getTime();
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(ColorCodes.RED + "invalid value: must be a positive integer value!" + ColorCodes.RESET);
            }
        }

        return true;
    }


    private boolean setStartDate() throws IOException {
        String line;

        reader.setPrompt(ColorCodes.BLUE + "\nstart date:\n" + ColorCodes.YELLOW + "Format example: yyyy-MM-dd (2023-01-01)\n"
                + ColorCodes.RESET);


        while ((line = reader.readLine()) != null) {
            line = line.trim();


            try {

                LocalDate date = LocalDate.parse(line, DateTimeFormatter.ISO_DATE);
                Date dt = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

                sprint.startDate = dt;

            } catch (NumberFormatException | NullPointerException | ArrayIndexOutOfBoundsException |
                     DateTimeParseException e) {
                System.out.println(ColorCodes.RED + "invalid value" + ColorCodes.RESET);
                continue;
            }

            break;
        }
        return true;
    }


    private void setVelocity() throws IOException {
        String line;
        reader.setPrompt(ColorCodes.BLUE + "velocity: " + ColorCodes.RESET);

        while ((line = reader.readLine()) != null) {
            line = line.trim();

            try {
                sprint.velocity = Integer.valueOf(line);
                break;
            } catch (NumberFormatException e) {
                System.out.println(ColorCodes.RED + "invalid value: must be an integer value!" + ColorCodes.RESET);
            }
        }
    }


    private void setDescription(StringBuilder description) throws IOException {
        String line;
        reader.setPrompt(ColorCodes.BLUE + "\ndescription:\n" + ColorCodes.YELLOW + "(\\q to end writing)\n" + ColorCodes.RESET);

        while ((line = reader.readLine()) != null) {
            if (line.equals("\\q")) {
                break;
            }
            description.append(line).append("\n");
            reader.setPrompt("");
        }

        sprint.description = description.toString();
    }


    private void setName() throws IOException {
        reader.setPrompt(ColorCodes.BLUE + "name: " + ColorCodes.RESET);
        sprint.name = reader.readLine();
    }

}
