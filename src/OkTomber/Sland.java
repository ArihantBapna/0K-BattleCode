package OkTomber;

import battlecode.common.*;

public class Sland {
    RobotController rc;

    public SlandMove mover;
    public Movement move;

    public Sland(RobotController rc) throws GameActionException {
        this.rc = rc;
        mover = new SlandMove(rc);
        move = new Movement(rc);
    }

    public void doRun() throws GameActionException {
        if(rc.getType().equals(RobotType.SLANDERER)) DoMovement();
        else move.DoMove();
    }

    public void DoMovement() throws GameActionException{
        mover.DoSlandMove();
    }
}
