package OkBloomer;

import OkCoomers.Scan;
import battlecode.common.*;

import java.util.Objects;

public class Bot {
    public RobotController rc;

    public Movement move;
    public Scanner scan;


    public boolean knowEC;
    public MapLocation mlEC;
    public int EC;

    public Bot(RobotController r) throws GameActionException {

        rc = r;
        scan = new Scanner(rc);
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
