package OkCoomers;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

import java.util.Arrays;

public class ECenterMid extends ECenterSpawns {

    public RobotController rc;

    public ECenterMid(RobotController r) {
        super(r);
        rc = r;
    }

    public void RunSpawns() throws GameActionException{
        probDist = Arrays.asList(0.56,0.2,0.24);
        DoRandomSpawns(GetRandomRobot());
    }
}
