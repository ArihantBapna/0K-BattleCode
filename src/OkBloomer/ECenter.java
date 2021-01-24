package OkBloomer;

import battlecode.common.*;

public class ECenter {
    public RobotController rc;
    public ECenterSpawns spawn;


    public ECenter(RobotController r) {
        rc = r;
        spawn = new ECenterSpawns(rc);
    }

    public void doRun() throws GameActionException {
        spawn.DoRandomSpawns(spawn.GetRandomRobot());
    }
}
