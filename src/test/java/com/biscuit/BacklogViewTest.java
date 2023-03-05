import com.biscuit.views.*;

import org.junit.Assert;
import org.junit.Test;

public class BacklogViewTest{
    BacklogView backlogView = new BacklogView();

    @Test
    public void testCheckCommand(){
        String[] words= new String[3];
        Assert.assertTrue(backlogView.checkCommand(words));
    }

}