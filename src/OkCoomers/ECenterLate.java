package OkCoomers;

import battlecode.common.*;

import java.util.Arrays;

public class ECenterLate extends ECenterSpawns {

    public ECenterLate(RobotController r) {
        super(r);
    }

    public void RunSpawns() throws GameActionException{
        probDist = Arrays.asList(0.36,0.2,0.44);
        DoRandomSpawns(GetRandomRobot());
    }
}
