package Final0k;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.Team;

public class Poli extends Bot {

    public Poli(RobotController r) throws GameActionException {
        super(r);
    }

    public void DoRun() throws GameActionException{
        if(RobotPlayer.turnCount % 50 == 0) move.base = rc.getLocation();
        TryGivingSpeech();
        if(scan.GetNearbyFriendlyMuck() > 0){
            for(RobotInfo r : scan.mucks){
                if(rc.canGetFlag(r.getID())){
                    int flag = 0;
                    try{
                        flag = rc.getFlag(r.getID());
                    }catch(GameActionException e){
                        System.out.println("Saved u from an error. I gotchu bro.");
                    }
                    if(String.valueOf(flag).substring(0,1).equals("2")){
                        move.current = Constants.AdjustLocation(rc.getLocation()).directionTo(Constants.DecodeLocation(flag));
                        break;
                    }
                }
            }
        }else{
            if(scan.GetNearbyNeutral() > 0){
                move.current = rc.getLocation().directionTo(scan.neutral.get(0).getLocation());
            }else{
                move.CheckAllPaths(rc.getLocation());
            }
        }
        move.TryRandomMove();
    }

    public void TryGivingSpeech() throws GameActionException{
        Team enemy = rc.getTeam().opponent();
        int actionRadius = rc.getType().actionRadiusSquared;
        RobotInfo[] attack = rc.senseNearbyRobots(actionRadius, enemy);
        RobotInfo[] neut = rc.senseNearbyRobots(actionRadius-3, Team.NEUTRAL);
        if (attack.length != 0 && rc.canEmpower(actionRadius)) {
            rc.empower(actionRadius);
        }else if(neut.length != 0 && rc.canEmpower(actionRadius)){
            rc.empower(actionRadius);
        }
    }
}
