package OkTomber;

import battlecode.common.*;

public class Scan {
    public RobotController rc;
    public int cd;
    public MapLocation goal;

    public Scan(RobotController r){
        rc = r;
        cd = 0;
        goal = new MapLocation(129,129);
    }

    public int SearchForEC() throws GameActionException{
        int c = 2;
        for(RobotInfo r : rc.senseNearbyRobots(-1,rc.getTeam().opponent())){
            if(r.getType().equals(RobotType.ENLIGHTENMENT_CENTER)){
                MapLocation rLoc = r.getLocation();
                if(rc.getLocation().isAdjacentTo(rLoc)){
                    return 1;
                }
                c = ScannedEC(rLoc);
                if(c < 2){
                    goal = rLoc;
                }
            }
        }

        return c;
    }

    public int GetECid() throws GameActionException{
        for(RobotInfo r : rc.senseNearbyRobots(2,rc.getTeam())){
            if(r.getType().equals(RobotType.ENLIGHTENMENT_CENTER)){
                return r.getID();
            }
        }
        return 0;
    }

    public int ScannedEC(MapLocation rLoc) throws GameActionException{

        //2 is its not free
        //1 is it needs to be closer to investigate
        //0 is free

        for(Direction d : Constants.directions){
            if(rc.canSenseLocation(rLoc.add(d))){
                if(rc.isLocationOccupied(rLoc.add(d))){
                    RobotInfo ap = rc.senseRobotAtLocation(rLoc.add(d));
                    if(!ap.getTeam().equals(rc.getTeam())){
                        return 0;
                    }
                }else{
                    return 0;
                }
            }
        }
        return 3;
    }

}
