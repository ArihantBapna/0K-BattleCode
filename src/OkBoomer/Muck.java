package OkBoomer;

import battlecode.common.*;

import java.awt.*;

public class Muck {
    RobotController rc;
    private RobotInfo rEC;

    public int idEC = 0;
    public MapLocation adjLoc;
    public Movement mover;

    public Muck(RobotController rc) throws GameActionException {
        this.rc = rc;
        FirstTimeSense();
        mover = new Movement(rc,adjLoc, idEC,rEC);
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

    public void FirstTimeSense() throws GameActionException {
        for(RobotInfo r : rc.senseNearbyRobots(-1,rc.getTeam())){
            if(r.getType().equals(RobotType.ENLIGHTENMENT_CENTER)){
                idEC = r.getID();
                rEC = r;
                adjLoc = new MapLocation(64,64).add(r.getLocation().directionTo(rc.getLocation()));
                rc.setFlag(FlagHandle.adjLocWrite(adjLoc));
            }
        }
    }
}
