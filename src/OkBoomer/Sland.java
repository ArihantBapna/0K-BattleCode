package OkBoomer;

import battlecode.common.*;

public class Sland {
    RobotController rc;
    private RobotInfo rEC;

    public int idEC = 0;
    public MapLocation adjLoc;
    public Movement mover;

    public Sland(RobotController rc) throws GameActionException {
        this.rc = rc;
        FirstTimeSense();
        mover = new Movement(rc,adjLoc, idEC,rEC);
    }

    public void doRun() throws GameActionException {
        DoMovement();
    }

    public void DoMovement() throws GameActionException{
        mover.DoMove();
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
