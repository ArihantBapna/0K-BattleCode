package OkBoomer;

import battlecode.common.*;

import java.util.Arrays;

public class Poli {
    RobotController rc;
    private RobotInfo rEC;

    public int idEC = 0;
    public MapLocation adjLoc;
    public Movement mover;

    public Poli(RobotController rc) throws GameActionException {
        this.rc = rc;
        adjLoc = new MapLocation(64,64);
        FirstTimeSense();
        mover = new Movement(rc,adjLoc, idEC,rEC);

    }

    public void doRun() throws GameActionException {
        TryGivingSpeech();
        DoMovement();
    }

    public void TryGivingSpeech() throws GameActionException{
        Team enemy = rc.getTeam().opponent();
        int actionRadius = rc.getType().actionRadiusSquared;
        RobotInfo[] attack = rc.senseNearbyRobots(actionRadius, enemy);
        RobotInfo[] neut = rc.senseNearbyRobots(actionRadius, Team.NEUTRAL);
        if (attack.length != 0 && rc.canEmpower(actionRadius)) {
            rc.empower(actionRadius);
        }else if(neut.length != 0 && rc.canEmpower(actionRadius)){
            rc.empower(actionRadius);
        }
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
