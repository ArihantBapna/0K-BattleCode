package Final0k;

import battlecode.common.*;

public class Sland extends Bot {

    public Sland(RobotController r) throws GameActionException {
        super(r);
    }

    public void DoRun() throws GameActionException{
        if(rc.getType().equals(RobotType.SLANDERER)){
            if(EnsureSafety()){
                move.TryRandomMove();
            }else{
                if (scan.GetNearbyFriendlyEC() <= 0) {
                    move.current = rc.getLocation().directionTo(mlEC);
                }
            }
            move.TryRandomMove();
        }else{
            TryGivingSpeech();
            if(RobotPlayer.turnCount % 50 == 0) move.base = rc.getLocation();
            move.CheckAllPaths(rc.getLocation());
        }

    }

    public boolean EnsureSafety() throws GameActionException{
        if(scan.GetNearbyMucks() > 0){
            move.current = scan.mucks.get(0).getLocation().directionTo(rc.getLocation());
            return true;
        }
        return false;
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
