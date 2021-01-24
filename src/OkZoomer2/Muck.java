package OkZoomer2;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class Muck {
    public RobotController rc;
    public MuckMove move;

    public Muck(RobotController r) throws GameActionException {
        rc = r;
        move = new MuckMove(rc);
    }

    public void doRun() throws GameActionException {
        move.DoMove();
    }
}
