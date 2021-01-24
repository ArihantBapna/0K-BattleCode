package OkZoomer2;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class PoliCircleMove extends Movement {
    public int dis;
    public PoliCircleMove(RobotController r) throws GameActionException {
        super(r);
        dis = rc.getType().actionRadiusSquared - 5;
    }

    public void DefensiveRotation() throws GameActionException {
        if(rc.canEmpower(-1)){
            rc.empower(-1);
        }
        if(rc.getLocation().isWithinDistanceSquared(mlEC,dis)){
            if(rc.getLocation().isWithinDistanceSquared(mlEC,9)){
                current = rc.getLocation().directionTo(mlEC).opposite();
            }else{
                current = current.rotateRight();
            }
        }else{
            current = rc.getLocation().directionTo(mlEC);
        }
        TryRandomMove();
    }
}
