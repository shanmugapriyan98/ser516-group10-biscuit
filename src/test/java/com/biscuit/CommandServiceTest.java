import com.biscuit.models.services.CommandService;
import com.biscuit.views.BacklogView;
import com.biscuit.views.DashboardView;
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

}
