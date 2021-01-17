package OkZoomer;

import battlecode.common.*;

import java.util.Objects;

public class MuckMove extends Movement {

    public MuckScan scan;

    public MuckMove(RobotController r) throws GameActionException {
        super(r);
        scan = new MuckScan(rc);
    }

    public void DoMove() throws GameActionException{
        TryAndExpose();
        if(scan.NearbyEC()){
            current = scan.closest;
            if(!rc.getLocation().isAdjacentTo(scan.TargetEC.getLocation())){
                TryRandomMove();
            }
        }else{
            if(!Objects.isNull(scan.TargetEC)){
                if(!scan.TargetEC.getLocation().isAdjacentTo(rc.getLocation())){
                    TryRandomMove();
                }
            }else{
                TryRandomMove();
            }
        }

    }

    public void TryAndExpose() throws GameActionException{
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
