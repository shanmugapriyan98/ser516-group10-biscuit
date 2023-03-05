import com.biscuit.models.services.CommandService;
import com.biscuit.views.BacklogView;
import  org.junit.Test;
import org.junit.Assert;
public class CommandServiceTest {

    @Test
    public void testCheckCommand(){
        BacklogView backlogView = new BacklogView();
        String[] words= new String[]{"list","user_stories"};
        Assert.assertTrue(CommandService.checkCommand(words, backlogView.backlogCmdArr));
    }

}
