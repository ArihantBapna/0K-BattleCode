package OkZoomer;

import battlecode.common.*;

import java.util.ArrayList;
import java.util.Objects;

public class SlandScan {
    public RobotController rc;
    public ArrayList<RobotInfo> nearbyMucks;
    public Direction avoid;
    public RobotInfo closest;

    public SlandScan(RobotController r){
        rc = r;
        avoid = Constants.randomDirection();
        nearbyMucks = new ArrayList<>();
    }

    public int GetNearbyMucks(){
        nearbyMucks = new ArrayList<>();
        RobotInfo m = null;
        for(RobotInfo r : rc.senseNearbyRobots(-1,rc.getTeam().opponent())){
            if(r.getType().equals(RobotType.MUCKRAKER)){
                nearbyMucks.add(r);
                //Getting the closest muckracker robotinfo
                if(Objects.isNull(m)){
                    m = r;
                }else{
                    if(m.getLocation().distanceSquaredTo(rc.getLocation()) > r.getLocation().distanceSquaredTo(rc.getLocation())){
                        m = r;
                    }
                }

            }
        }
        closest = m;
        SetAvoidDirection();

        return nearbyMucks.size();
    }

    public int GetNearbyEnemy(){
        nearbyMucks = new ArrayList<>();
        RobotInfo m = null;
        for(RobotInfo r : rc.senseNearbyRobots(-1,rc.getTeam().opponent())){
                nearbyMucks.add(r);
                //Getting the closest enemy robotinfo
                if(Objects.isNull(m)){
                    m = r;
                }else{
                    if(m.getLocation().distanceSquaredTo(rc.getLocation()) > r.getLocation().distanceSquaredTo(rc.getLocation())){
                        m = r;
                    }
                }

        }
        closest = m;
        return nearbyMucks.size();
    }

    public int GetNearbySland(){
        int c = 0;
        for(RobotInfo r : rc.senseNearbyRobots(2,rc.getTeam())){
            c++;
        }
        return c;
    }

    private void SetAvoidDirection() {
        if(nearbyMucks.size()>0){
            avoid = closest.getLocation().directionTo(rc.getLocation());
        }
    }
}
