package OkTomber;

import battlecode.common.*;

public class Poli {
    RobotController rc;

    public Movement mover;

    public Poli(RobotController rc) throws GameActionException {
        this.rc = rc;
        mover = new Movement(rc);
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
}
