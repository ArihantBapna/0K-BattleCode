package OkZoomer;

import battlecode.common.*;

import java.util.ArrayList;

public class MuckScan {
    public RobotController rc;
    public ArrayList<RobotInfo> nearby;
    public Direction closest;
    public RobotInfo TargetEC;

    public MuckScan(RobotController r) throws GameActionException{
        rc = r;
        nearby = new ArrayList<>();
        TargetEC = null;
    }

    public boolean NearbyEC() throws GameActionException{
        int count = 0;
        for(RobotInfo r : rc.senseNearbyRobots(-1, rc.getTeam().opponent())){
            if(r.getType().equals(RobotType.ENLIGHTENMENT_CENTER)){
                closest = rc.getLocation().directionTo(r.getLocation());
                if(!isECSurrounded(r.getLocation())){
                    count++;
                    TargetEC = r;
                    break;
                }
            }
        }
        return count > 0;
    }

    public boolean isECSurrounded(MapLocation rLoc) throws GameActionException{
        for(Direction d : Constants.directions){
            if(rc.canSenseLocation(rLoc.add(d))){
                if(rc.isLocationOccupied(rLoc.add(d))){
                    RobotInfo ap = rc.senseRobotAtLocation(rLoc.add(d));
                    if(!ap.getTeam().equals(rc.getTeam())){
                        return false;
                    }
                }else{
                    return false;
                }
            }
        }
        return true;
    }

}
