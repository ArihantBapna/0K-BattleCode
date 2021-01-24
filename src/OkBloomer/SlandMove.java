package OkBloomer;

import battlecode.common.*;

public class SlandMove extends Movement {
    public SlandMove(RobotController r) throws GameActionException {
        super(r);
    }

    public void CheckSurroundings() throws GameActionException {
        if(scan.GetNearbyMucks() != 0){
            current = scan.mucks.get(0).getLocation().directionTo(rc.getLocation());
        }else{
            if(scan.GetNearbyFPoli() != 0){
                current = scan.poli.get(0).getLocation().directionTo(rc.getLocation());
            }else{
                current = current.rotateRight();
            }
        }
    }

    public boolean DoConvertedMove() throws GameActionException{
        if(scan.GetNearbyEnemies()>0){
            current = rc.getLocation().directionTo(scan.enemies.get(0).getLocation());
            return true;
        }
        return false;
    }
}
