package Final0k;

import battlecode.common.*;

import java.util.Objects;

public class Bot {
    public RobotController rc;

    public Movement move;
    public Scan scan;

    public boolean knowEC;
    public MapLocation mlEC;
    public int EC;

    public Bot(RobotController r) throws GameActionException {

        rc = r;
        scan = new Scan(rc);
        move = new Movement(rc);
        knowEC = false;

        DoInitialSetup();
        if(!Objects.isNull(mlEC)) knowEC = true;

    }

    public void DoInitialSetup() {
        int id = 0;
        for(RobotInfo r : rc.senseNearbyRobots(2,rc.getTeam())){
            if(r.getType().equals(RobotType.ENLIGHTENMENT_CENTER)){
                id = r.getID();
                mlEC = r.getLocation();
            }
        }
        EC = id;
    }
}
