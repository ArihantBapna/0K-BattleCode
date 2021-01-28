package Final0k;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.Team;

public class Muck extends Bot {

    public Muck(RobotController r) throws GameActionException {
        super(r);
    }

    public void DoRun() throws GameActionException{

        TryExpose();
        if(DoChecks()){
            return;
        }else{
            if(RobotPlayer.turnCount > 50){
                if(scan.GetNearbyFriendlyMuck() > 0){
                    move.current = scan.mucks.get(0).getLocation().directionTo(rc.getLocation());
                    move.TryRandomMove();
                }
            }else{
                move.CheckAllPaths(rc.getLocation());
            }
        }
    }

    public boolean DoChecks() throws GameActionException{
        if(scan.GetNearbyNeutral() > 0){
            int val = Constants.EncodeLocation(scan.neutral.get(0).getLocation());
            int flag = Integer.parseInt("2" + String.valueOf(val));
            rc.setFlag(flag);
        }else if(scan.GetNearbyEC() > 0){
            int val = Constants.EncodeLocation(scan.ec.get(0).getLocation());
            int flag = Integer.parseInt("3" + String.valueOf(val));
            rc.setFlag(flag);
            if(!scan.IsECSurrounded() && !rc.getLocation().isAdjacentTo(scan.ec.get(0).getLocation())){
                move.current = rc.getLocation().directionTo(scan.ec.get(0).getLocation());
                move.TryRandomMove();
            }else{
                return rc.getLocation().isAdjacentTo(scan.ec.get(0).getLocation());
            }
        }
        else{
            rc.setFlag(0);
        }
        return false;
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
