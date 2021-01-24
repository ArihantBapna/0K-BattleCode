package OkZoomer2;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

import java.util.Objects;

public class PoliMove extends Movement {

    static int dis;
    public PoliScan scan;

    public PoliMove(RobotController r) throws GameActionException {
        super(r);
        dis = 20;
        scan = new PoliScan(rc);
    }

    public void DoMove() throws GameActionException{
        if(rc.getInfluence() <= 20){
            if(scan.GetNearbyMucks() > 0){
                TryAndSubdue();
                if(!rc.getLocation().isAdjacentTo(scan.closest.getLocation())){
                    current = rc.getLocation().directionTo(scan.closest.getLocation());
                    OptimizeDirection(current);
                }
            }else{
                if(!Objects.isNull(mlEC)){
                    DefensiveRotation();
                }else{
                    OptimizeDirection(current);
                }
            }
        }else{
            if(scan.GetNearbyEnemies() > 0){
                TryAndSubdue();
                current = rc.getLocation().directionTo(scan.closest.getLocation());
            }else if(scan.GetNearbyNeutEC()){
                TryAndConvert();
                current = rc.getLocation().directionTo(scan.closest.getLocation());
            }else{
                CheckAllPaths(rc.getLocation());
            }
            OptimizeDirection(current);
        }
    }

    public void TryAndConvert() throws GameActionException{
        if(scan.closest.getLocation().isWithinDistanceSquared(rc.getLocation(),rc.getType().actionRadiusSquared-1)){
            if(rc.canEmpower(rc.getType().actionRadiusSquared-1)){
                if(rc.getInfluence() != 1){
                    rc.empower(rc.getType().actionRadiusSquared-1);
                }
            }
        }
    }

    public void TryAndSubdue() throws GameActionException{
        if(scan.closest.getLocation().isWithinDistanceSquared(rc.getLocation(),rc.getType().actionRadiusSquared-4)){
            if(rc.canEmpower(rc.getType().actionRadiusSquared-4)){
                if(rc.getInfluence() != 1){
                    rc.empower(rc.getType().actionRadiusSquared-4);
                }
            }
        }
    }

    public void DefensiveRotation() throws GameActionException {
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
