package OkCoomers;

import battlecode.common.*;

public class Muck {
    public RobotController rc;
    public MuckMove move;

    public Muck(RobotController r) throws GameActionException {
        rc = r;
        move = new MuckMove(rc);
    }

    public void DoRun() throws GameActionException{
        move.DoMove();
    }
}
