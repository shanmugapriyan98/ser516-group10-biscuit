import com.biscuit.models.services.CommandService;
import com.biscuit.views.*;
import  org.junit.Test;
import org.junit.Assert;
public class CommandServiceTest {

    @Test
    public void testBacklogCheckCommand(){
        BacklogView backlogView = new BacklogView();
        String[] words= new String[]{"list","user_stories"};
        Assert.assertTrue(CommandService.checkCommand(words, backlogView.backlogCmdArr));
    }

    @Test
    public void testDashboardCheckCommand(){
        DashboardView dashboardView = new DashboardView();
        String[] words= new String[]{"add","project"};
        Assert.assertTrue(CommandService.checkCommand(words, dashboardView.dashboardCmdArr));
    }

    @Test
    public void testEpicsCheckCommand(){
        EpicsView epicsView = new EpicsView();
        String[] words= new String[]{"add","epic"};
        Assert.assertTrue(CommandService.checkCommand(words, epicsView.epicsCmdArr));
    }

    @Test
    public void testEpicCheckCommand(){
        EpicView epicView = new EpicView();
        String[] words= new String[]{"show"};
        Assert.assertTrue(CommandService.checkCommand(words, epicView.epicCmdArr));
    }

    @Test
    public void testPlannerCheckCommand(){
        PlannerView plannerView = new PlannerView();
        String[] words= new String[]{"releases"};
        Assert.assertTrue(CommandService.checkCommand(words, plannerView.plannerCmdArr));
    }

    @Test
    public void testProjectCheckCommand(){
        ProjectView projectView = new ProjectView();
        String[] words= new String[]{"tasks"};
        Assert.assertTrue(CommandService.checkCommand(words, projectView.projectCmdArr));
    }

    @Test
    public void testReleasesCheckCommand(){
        ReleasesView releasesView = new ReleasesView();
        String[] words= new String[]{"help"};
        Assert.assertTrue(CommandService.checkCommand(words, releasesView.releasesCmdArr));
    }

    @Test
    public void testReleaseCheckCommand(){
        ReleaseView releaseView = new ReleaseView();
        String[] words= new String[]{"sprints"};
        Assert.assertTrue(CommandService.checkCommand(words, releaseView.releaseCmdArr));
    }

    @Test
    public void testSprintsCheckCommand(){
        SprintsView sprintsView = new SprintsView();
        String[] words= new String[]{"help"};
        Assert.assertTrue(CommandService.checkCommand(words, sprintsView.sprintsCmdArr));
    }

    @Test
    public void testSprintCheckCommand(){
        SprintView sprintView = new SprintView();
        String[] words= new String[]{"user_stories"};
        Assert.assertTrue(CommandService.checkCommand(words, sprintView.sprintCmdArr));
    }
    @Test
    public void testTaskCheckCommand(){
        TaskView taskView = new TaskView();
        String[] words= new String[]{"edit"};
        Assert.assertTrue(CommandService.checkCommand(words, taskView.taskCmdArr));
    }
    @Test
    public void testUserStoryCheckCommand(){
        UserStoryView userStoryView = new UserStoryView();
        String[] words= new String[]{"tasks"};
        Assert.assertTrue(CommandService.checkCommand(words, userStoryView.userStoryCmdArr));
    }

}
