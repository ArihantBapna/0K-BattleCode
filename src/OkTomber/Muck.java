package OkTomber;

import battlecode.common.*;

public class Muck {
    RobotController rc;

    public Movement mover;

    public Muck(RobotController rc) throws GameActionException {
        this.rc = rc;
        mover = new Movement(rc);
    }

    public void doRun() throws GameActionException {
        TryExpose();
        DoMovement();
    }

    public void DoMovement() throws GameActionException{
        mover.DoMove();
    }

    public void TryExpose() throws GameActionException{
        Team enemy = rc.getTeam().opponent();
        int actionRadius = rc.getType().actionRadiusSquared;
        for (RobotInfo robot : rc.senseNearbyRobots(actionRadius, enemy)) {
            if (robot.type.canBeExposed()) {
               if (rc.canExpose(robot.location)) {
                    rc.expose(robot.location);
                    return;
                }
            }
        }
    }
}
