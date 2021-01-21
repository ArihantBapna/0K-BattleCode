package OkCoomers;

import battlecode.common.*;

public class SlandMoveMid extends Movement  {
    public SlandMoveMid(RobotController r) throws GameActionException {
        super(r);
    }

    public void DoMove() throws GameActionException{
        if(scan.GetNearbyMucks() > 0){
            current = scan.mucks.get(0).getLocation().directionTo(rc.getLocation());
        }else if(scan.GetNearbyAll() !=0){
            current = scan.nearby.get(0).getLocation().directionTo(rc.getLocation());
        }
        TryRandomMove();
    }
}
