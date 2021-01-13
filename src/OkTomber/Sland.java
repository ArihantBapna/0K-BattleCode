package OkTomber;

import battlecode.common.*;

public class Sland {
    RobotController rc;

    public Movement mover;

    public Sland(RobotController rc) throws GameActionException {
        this.rc = rc;
        mover = new Movement(rc);
    }

    public void doRun() throws GameActionException {
        DoMovement();
    }

    public void DoMovement() throws GameActionException{
        mover.DoMove();
    }
}
