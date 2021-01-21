package OkCoomers;

import battlecode.common.*;

import java.util.Objects;

public class SlandMoveLate extends Movement {
    public SlandMoveLate(RobotController r) throws GameActionException {
        super(r);
    }

    public void DoMove() throws GameActionException{
        int i = scan.GetNearbyEC();
        boolean toEmpower = false;
        RobotInfo target = null;

        if(i!=0){
            current = rc.getLocation().directionTo(scan.ec.get(0).getLocation());
            toEmpower = true;
            target = scan.ec.get(0);
        }else{
            if(scan.GetNearbyNeutral() != 0){
                current = rc.getLocation().directionTo(scan.neutral.get(0).getLocation());
                toEmpower = true;
                target = scan.neutral.get(0);
            }else{
                if(scan.GetNearbyFriendly() != 0){
                    current = scan.friendly.get(0).getLocation().directionTo(rc.getLocation());
                }else{
                    if(scan.GetNearbyMucks() != 0){
                        current = rc.getLocation().directionTo(scan.mucks.get(0).getLocation());
                        toEmpower = true;
                        target = scan.mucks.get(0);
                    }
                }
            }
        }
        if(toEmpower && !Objects.isNull(target)){
            TryAndEmpower(target);
        }
        TryRandomMove();
    }

    public void TryAndEmpower(RobotInfo target) throws GameActionException {
        if(rc.canEmpower(rc.getType().actionRadiusSquared)){
            if(target.getLocation().distanceSquaredTo(rc.getLocation())<= rc.getType().actionRadiusSquared){
                if(rc.getInfluence() > 1){
                    rc.empower(rc.getType().actionRadiusSquared);
                }
            }
        }
    }
}
