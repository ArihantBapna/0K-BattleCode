package OkZoomer;

import battlecode.common.*;

public class SlandMove extends Movement {

    public SlandScan scan;

    public static int dis;

    public SlandMove(RobotController r) throws GameActionException {
        super(r);
        scan  = new SlandScan(rc);
        dis = 9;
    }

    public void DoMove() throws GameActionException {
        CheckSurroundingsAndMove();
    }

    public void DoConvertedMove() throws GameActionException{
        if(scan.GetNearbyEnemy()>0){
            TryAndConvert();
            current = rc.getLocation().directionTo(scan.closest.getLocation());
            TryRandomMove();
        }else{
            TryRandomMove();
        }

    }

    public void TryAndConvert() throws GameActionException{
        if(scan.closest.getLocation().isWithinDistanceSquared(rc.getLocation(),rc.getType().actionRadiusSquared-4)){
            if(rc.canEmpower(rc.getType().actionRadiusSquared-4)){
                if(rc.getInfluence() != 1){
                    rc.empower(rc.getType().actionRadiusSquared-4);
                }
            }
        }
    }

    public void CheckSurroundingsAndMove() throws GameActionException {
        if(scan.GetNearbyMucks() != 0){
            current = scan.avoid;
        }else{
            if(rc.getLocation().isWithinDistanceSquared(mlEC,dis)){
                if(rc.getLocation().isWithinDistanceSquared(mlEC,4)){
                    current = rc.getLocation().directionTo(mlEC).opposite();
                }else{
                    current = current.rotateRight();
                }
            }else{
                current = rc.getLocation().directionTo(mlEC);
            }
        }
        TryRandomMove();
    }
}
