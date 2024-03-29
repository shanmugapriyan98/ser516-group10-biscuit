/*
	Author: Hamad Al Marri;
 */

package com.biscuit.views;

import com.biscuit.commands.epic.AddEpic;
import com.biscuit.commands.epic.ListEpics;
import com.biscuit.commands.help.ProjectHelp;
import com.biscuit.commands.planner.ShowPlan;
import com.biscuit.commands.planner.ShowPlanDetails;
import com.biscuit.commands.project.ShowProject;
import com.biscuit.commands.release.AddRelease;
import com.biscuit.commands.release.ListReleases;
import com.biscuit.commands.sprint.AddSprint;
import com.biscuit.commands.sprint.ListSprints;
import com.biscuit.commands.task.ListTasks;
import com.biscuit.commands.userStory.AddUserStoryToBacklog;
import com.biscuit.commands.userStory.ListUserStories;
import com.biscuit.factories.ProjectCompleterFactory;
import com.biscuit.models.*;
import com.biscuit.models.services.CommandService;
import com.biscuit.models.services.Finder.Epics;
import com.biscuit.models.services.Finder.Releases;
import com.biscuit.models.services.Finder.Sprints;
import com.biscuit.models.services.Finder.UserStories;
import jline.console.completer.Completer;

import java.io.IOException;
import java.util.List;

public class ProjectView extends View {

    Project project = null;

    public String []projectCmdArr= new String[]
            {"info", "releases", "sprints", "epics", "backlog", "user_stories", "tasks", "plan", "show", "help"};


    public ProjectView(){}

    public ProjectView(View previousView, Project p) {
        super(previousView, p.name);
        this.project = p;
    }


    @Override
    void addSpecificCompleters(List<Completer> completers) {
        completers.addAll(ProjectCompleterFactory.getProjectCompleters(project));
    }


    @Override
    boolean executeCommand(String[] words) throws IOException {
        if (words.length == 1) {
            return execute1Keyword(words);
        } else if (words.length == 2) {
            return execute2Keywords(words);
        } else if (words.length == 3) {
            return execute3Keywords(words);
        } else if (words.length == 4) {
            return execute4Keywords(words);
        }
        return false;
    }


    private boolean execute4Keywords(String[] words) throws IOException {
        if (words[0].equals("show")) {
            if (words[1].equals("backlog")) {
                if (words[2].equals("filter")) {
                    (new ListUserStories(project.backlog, "", true, words[3], false, "")).execute();
                    return true;
                } else if (words[2].equals("sort")) {
                    (new ListUserStories(project.backlog, "", false, "", true, words[3])).execute();
                    return true;
                }
            }
        } else if (words[0].equals("list")) {
            if (words[1].equals("releases")) {
                if (words[2].equals("filter")) {
                    (new ListReleases(project, "Releases", true, words[3], false, "")).execute();
                    return true;
                } else if (words[2].equals("sort")) {
                    (new ListReleases(project, "Releases", false, "", true, words[3])).execute();
                    return true;
                }
            } else if (words[1].equals("sprints")) {
                if (words[2].equals("filter")) {
                    (new ListSprints(project, "Sprints", true, words[3], false, "")).execute();
                    return true;
                } else if (words[2].equals("sort")) {
                    (new ListSprints(project, "Sprints", false, "", true, words[3])).execute();
                    return true;
                }
            } else if (words[1].equals("epics")) {
                if (words[2].equals("filter")) {
                    (new ListEpics(project, "Epics", true, words[3], false, "")).execute();
                    return true;
                } else if (words[2].equals("sort")) {
                    (new ListEpics(project, "Epics", false, "", true, words[3])).execute();
                    return true;
                }
            } else if (words[1].equals("user_stories")) {
                if (words[2].equals("filter")) {
                    (new ListUserStories(UserStories.getAll(project), "User Stories (Filtered)", true, words[3], false, "")).execute();
                    return true;
                } else if (words[2].equals("sort")) {
                    (new ListUserStories(UserStories.getAll(project), "All User Stories", false, "", true, words[3])).execute();
                    return true;
                }
            }
        }
        return false;
    }


    private boolean execute3Keywords(String[] words) throws IOException {
        if (words[0].equals("go_to")) {
            if (words[1].equals("release")) {
                if (Releases.getAllNames(project).contains(words[2])) {
                    Release r = Releases.find(project, words[2]);
                    if (r == null) {
                        return false;
                    }

                    // r.project = project;

                    ReleaseView rv = new ReleaseView(this, r);
                    rv.view();
                    return true;
                }
            } else if (words[1].equals("sprint")) {

                project.populateDetails();

                if (project.sprintDetails.containsKey(words[2])) {

                    Sprint s = Sprints.find(project, words[2]);
                    if (s != null) {
                        String sprintId = String.valueOf(project.sprintDetails.get(s.name));
                        s.sprintId = sprintId;
                        SprintView sv = new SprintView(this, s);
                        sv.view();
                        return true;
                    } else {

                        if (project.sprintDetails.containsKey(words[2])) {
                            System.out.println("Caching sprint name " + words[1] + " to local");
                            boolean local = false;
                            boolean taiga = true;
                            new AddSprint(reader, local, taiga, words[2], project).execute();
                            //new AddProject(reader,local,taiga,words[1]).execute();
                            s = Sprints.find(project, words[2]);
                            String sprintId = String.valueOf(s.project.sprintDetails.get(s.name));
                            s.sprintId = sprintId;
                            SprintView sv = new SprintView(this, s);
                            sv.view();
                            return true;
                        }
                    }
                    return false;
                }

                if (Sprints.getAllNames(project).contains(words[2])) {
                    Sprint s = Sprints.find(project, words[2]);
                    if (s == null) {
                        return false;
                    }

                    //s.project = project;

                    SprintView sv = new SprintView(this, s);
                    sv.view();
                    return true;
                }

            } else if (words[1].equals("epic")) {
                if (Epics.getAllNames(project).contains(words[2])) {
                    Epic e = Epics.find(project, words[2]);
                    if (e == null) {
                        return false;
                    }


                    EpicView ev = new EpicView(this, e);
                    ev.view();
                    return true;
                }

            } else if (words[1].equals("user_story")) {
                if (UserStories.getAllNames(project).contains(words[2])) {
                    UserStory us = UserStories.find(project, words[2]);
                    if (us == null) {
                        return false;
                    }

                    UserStoryView usv = new UserStoryView(this, us);
                    usv.view();
                    return true;
                }
            }
        }

        return false;
    }


    private boolean execute2Keywords(String[] words) throws IOException {
        if (words[0].equals("add")) {
            if (words[1].equals("user_story")) {
                (new AddUserStoryToBacklog(reader, project)).execute();
                return true;
            } else if (words[1].equals("release")) {
                (new AddRelease(reader, project)).execute();
                resetCompleters();

                return true;

            } else if (words[1].equals("sprint")) {
                (new AddSprint(reader, false, false, null, project)).execute();
                resetCompleters();

                return true;

            } else if (words[1].equals("epic")) {
                (new AddEpic(reader, project)).execute();
                resetCompleters();

                return true;
            }


        } else if (words[0].equals("go_to")) {
            if (words[1].equals("backlog")) {
                this.project.backlog.project = this.project;
                BacklogView bv = new BacklogView(this, this.project.backlog);
                bv.view();
                return true;
            } else if (words[1].equals("releases")) {

                ReleasesView rsv = new ReleasesView(this, project);
                rsv.view();

                return true;
            } else if (words[1].equals("sprints")) {

                SprintsView ssv = new SprintsView(this, project);
                ssv.view();

                return true;
            } else if (words[1].equals("epics")) {

                EpicsView esv = new EpicsView(this, project);
                esv.view();

                return true;
            } else if (words[1].equals("planner")) {

                PlannerView pv = new PlannerView(this, project);
                pv.view();

                return true;
            }
        } else if (words[0].equals("show")) {
            if (words[1].equals("backlog")) {
                (new ListUserStories(project.backlog, "Backlog (User Stories)")).execute();
                return true;
            }
        } else if (words[0].equals("list")) {
            if (words[1].equals("releases")) {
                (new ListReleases(project, "Releases")).execute();
                return true;
            } else if (words[1].equals("sprints")) {
                (new ListSprints(project, "Sprints")).execute();
                return true;
            } else if (words[1].equals("epics")) {
                (new ListEpics(project, "Epics")).execute();
                return true;
            } else if (words[1].equals("user_stories")) {
                (new ListUserStories(UserStories.getAll(project), "All User Stories")).execute();
                return true;
            }
        } else if (words[0].equals("plan")) {
            if (words[1].equals("details")) {
                (new ShowPlanDetails(project)).execute();
                return true;
            }
        }

        return false;
    }


    private boolean execute1Keyword(String[] words) throws IOException {
        if(!(CommandService.checkCommand(words, projectCmdArr))) return true;
        if (words[0].equals("info")) {
            reader.println(project.toString());
            return true;
        } else if (words[0].equals("backlog")) {
            (new ListUserStories(project.backlog, "Backlog (User Stories)")).execute();
            return true;
        } else if (words[0].equals("releases")) {
            (new ListReleases(project, "Releases")).execute();
            return true;
        } else if (words[0].equals("sprints")) {

            for (Release r : project.releases) {
                if (!r.sprints.isEmpty()) {
                    (new ListSprints(r, "Release: " + r.name + " -> Sprints")).execute();
                }
            }

            (new ListSprints(project, "Unplanned Sprints")).execute();
            return true;
        } else if (words[0].equals("epics")) {
            (new ListEpics(project, "Epics")).execute();
            return true;
        } else if (words[0].equals("user_stories")) {
            (new ListUserStories(UserStories.getAll(project), "All User Stories")).execute();
            return true;
        } else if (words[0].equals("plan")) {
            (new ShowPlan(project)).execute();
            return true;
        } else if (words[0].equals("tasks")) {
            List<UserStory> userStories = UserStories.getAll(project);
            for (UserStory us : userStories) {
                if (!us.tasks.isEmpty()) {
                    (new ListTasks(us, "User Story: " + us.title)).execute();
                }
            }
            return true;
        } else if (words[0].equals("show")) {
            return (new ShowProject(project).execute());
        } else if (words[0].equals("help")) {
            return (new ProjectHelp().execute());
        }

        return false;
    }

}
