package OkCoomers;

import battlecode.common.*;

public class PoliMove extends Movement {
    public PoliMove(RobotController r) throws GameActionException {
        super(r);
    }

    public void DoMove() throws GameActionException{

        int i = scan.GetNearbyEC();
        if(i!=0){
            current = rc.getLocation().directionTo(scan.ec.get(0).getLocation());
            if(rc.getLocation().isAdjacentTo(scan.ec.get(0).getLocation())){
                current = Direction.CENTER;
            }
        }else{
            if(scan.GetNearbyNeutral() > 0){
                current = rc.getLocation().directionTo(scan.neutral.get(0).getLocation());
            }
        }
        TryToEmpower();
        TryRandomMove();
    }
    public void TryToEmpower() throws GameActionException{
        if(scan.GetNearbyEnemies() > 3){
            if(rc.getInfluence() > 1){
                int actionRadius = rc.getType().actionRadiusSquared-3;
                if(rc.getLocation().distanceSquaredTo(scan.enemies.get(0).getLocation()) <= actionRadius){
                    if(rc.canEmpower(actionRadius)){
                        rc.empower(actionRadius);
                    }
                }
            }
        }else if(scan.GetNearbyNeutral() > 0){
            if(rc.getInfluence() > 1){
                int actionRadius = rc.getType().actionRadiusSquared-3;
                if(rc.getLocation().distanceSquaredTo(scan.neutral.get(0).getLocation()) <= actionRadius){
                    if(rc.canEmpower(actionRadius)){
                        rc.empower(actionRadius);
                    }
                }
            }
        }
    }
}
