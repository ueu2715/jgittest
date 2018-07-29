package ul.gerrit;

import com.sonymobile.tools.gerrit.gerritevents.GerritEventListener;
import com.sonymobile.tools.gerrit.gerritevents.dto.GerritEvent;
import com.sonymobile.tools.gerrit.gerritevents.dto.events.RefUpdated;

public class MyEventListener implements GerritEventListener {
    @Override
    public void gerritEvent(GerritEvent event) {
        System.out.println(event);
    }

    public void gerritEvent(RefUpdated event) {
        System.out.println("1"+event);
    }
}
