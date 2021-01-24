package OkBloomer;

import battlecode.common.*;

public class Poli extends Bot {
    public Poli(RobotController r) throws GameActionException {
        super(r);
    }

    public void DoRun() throws GameActionException{
        TryGivingSpeech();
        if(scan.GetNearbyNeutral() > 0){
            move.FindPathToGoal(scan.neutral.get(0).getLocation());
            move.JustMove();
        }else if(scan.GetNearbyEC() > 0){
            move.FindPathToGoal(scan.ec.get(0).getLocation());
            move.JustMove();
        }else{
            move.TryRandomMove();
        }
    }

    public void TryGivingSpeech() throws GameActionException{
        Team enemy = rc.getTeam().opponent();
        int actionRadius = rc.getType().actionRadiusSquared;
        RobotInfo[] attack = rc.senseNearbyRobots(actionRadius-3, enemy);
        RobotInfo[] neut = rc.senseNearbyRobots(actionRadius, Team.NEUTRAL);
        if (attack.length != 0 && rc.canEmpower(actionRadius)) {
            rc.empower(actionRadius);
        }else if(neut.length != 0 && rc.canEmpower(actionRadius)){
            rc.empower(actionRadius);
        }
    }
}
