package OkZoomer;

import battlecode.common.*;

import java.util.ArrayList;
import java.util.Objects;

public class PoliScan {
    public RobotController rc;
    public ArrayList<RobotInfo> nearbyMucks;
    public RobotInfo closest;

    public PoliScan(RobotController r){
        rc = r;
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
        return nearbyMucks.size();
    }

    public int GetNearbyEnemies(){
        nearbyMucks = new ArrayList<>();
        RobotInfo m = null;
        for(RobotInfo r : rc.senseNearbyRobots(-1,rc.getTeam().opponent())){
            if(r.getType().equals(RobotType.MUCKRAKER) || r.getType().equals(RobotType.ENLIGHTENMENT_CENTER) || r.getType().equals(RobotType.POLITICIAN)){
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
        }
        closest = m;
        return nearbyMucks.size();
    }

    public boolean GetNearbyNeutEC(){
        int count  = 0 ;
        for(RobotInfo r : rc.senseNearbyRobots(-1,Team.NEUTRAL)){
            closest = r;
            count++;
        }
        return count > 0;
    }

}
