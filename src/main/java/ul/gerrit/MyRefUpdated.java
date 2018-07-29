package ul.gerrit;

import com.sonymobile.tools.gerrit.gerritevents.dto.events.RefUpdated;
import net.sf.json.JSONObject;

public class MyRefUpdated extends RefUpdated {
    public MyRefUpdated(JSONObject json){
        super.fromJson(json);
    }
}
