package Kyle;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

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
