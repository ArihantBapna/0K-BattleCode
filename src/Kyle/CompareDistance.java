package Kyle;

import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

import java.util.Comparator;

public class CompareDistance implements Comparator<RobotInfo> {
    private RobotController rc;

    public CompareDistance(RobotController r){
        rc = r;
    }

    /*
     * Sorts the list by distance of bot from source bot.
     * In ascending order
     */

    @Override
    public int compare(RobotInfo r1, RobotInfo r2){
        int x1 = rc.getLocation().distanceSquaredTo(r1.getLocation());
        int x2 = rc.getLocation().distanceSquaredTo(r2.getLocation());

        return x1 - x2;
    }

}
