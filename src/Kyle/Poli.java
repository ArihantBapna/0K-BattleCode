package Kyle;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.Team;

public class Poli extends Bot {
    public Poli(RobotController r) throws GameActionException {
        super(r);
    }

    public void DoRun() throws GameActionException{
        TryGivingSpeech();
        if(RobotPlayer.turnCount % 51 == 0) move.base = rc.getLocation();
        if(scan.GetNearbyNeutral() > 0){
            move.FindPathToGoal(scan.neutral.get(0).getLocation());
            move.OptimizeDirection();
        }else if(scan.GetNearbyEC() > 0){
            move.FindPathToGoal(scan.ec.get(0).getLocation());
            move.OptimizeDirection();
        }else{
            move.CheckAllPaths(rc.getLocation());
            move.OptimizeDirection();
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
