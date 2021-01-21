package OkCoomers;

import battlecode.common.*;

public class SlandMoveEarly extends Movement {

    public int dis;
    public SlandMoveEarly(RobotController r) throws GameActionException {
        super(r);
        super.DoInitialSetup();
        dis = 9;
    }

    public void DoMove() throws GameActionException{
        CheckSurroundingsAndMove();
        TryRandomMove();
    }

    public void CheckSurroundingsAndMove() throws GameActionException {
        if(scan.GetNearbyMucks() != 0){
            current = scan.mucks.get(0).getLocation().directionTo(rc.getLocation());
        }else{
            if(rc.getLocation().isWithinDistanceSquared(mlEC,dis)){
                if(rc.getLocation().isWithinDistanceSquared(mlEC,4)){
                    current = rc.getLocation().directionTo(mlEC).opposite();
                }else{
                    current = Direction.CENTER;
                }
            }else{
                current = rc.getLocation().directionTo(mlEC);
            }
        }
    }

}
