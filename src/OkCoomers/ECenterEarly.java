package OkCoomers;

import battlecode.common.*;

import java.util.Arrays;

public class ECenterEarly extends ECenterSpawns {

    public RobotController rc;

    public ECenterEarly(RobotController r) {
        super(r);
        rc = r;
    }

    public void RunSpawns() throws GameActionException{
        probDist = Arrays.asList(0.4,0.4,0.2);
        switch (rc.getRoundNum()) {
            case 1:
                DoRandomSpawns(RobotType.SLANDERER,130);
                break;
            case 3:
                DoRandomSpawns(RobotType.SLANDERER,25);
                break;
            case 5:
                DoRandomSpawns(RobotType.MUCKRAKER,1);
                break;
            default:
                DoRandomSpawns(GetRandomRobot());
                break;
        }

    }
}
