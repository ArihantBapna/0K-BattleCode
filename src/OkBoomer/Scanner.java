package OkBoomer;

import battlecode.common.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Scanner {

    public RobotController rc;
    public MapLocation goal;

    private MapLocation adjLoc;

    public Scanner(RobotController r, MapLocation adj){
        rc = r;
        adjLoc = adj;
    }

    public boolean SearchEnemyEC(MapLocation adj, ArrayList<MapLocation> m) throws GameActionException {
        adjLoc = adj;
        goal = new MapLocation(129,129);
        for(RobotInfo r : rc.senseNearbyRobots(-1,rc.getTeam().opponent())){
            if(r.getType().equals(RobotType.ENLIGHTENMENT_CENTER)){
                int x = adjLoc.x + (r.getLocation().x - rc.getLocation().x);
                System.out.println("X: " +r.getLocation().x +", " +rc.getLocation().x);
                int y = adjLoc.y + (r.getLocation().y - rc.getLocation().y);
                System.out.println("I found enemy at  " +x +" , " +y);
                goal = new MapLocation(x,y);
            }
        }
        if(goal.x != 129){
            if(!m.contains(goal)){
                rc.setFlag(FlagHandle.adjLocWrite(goal,"2"));
                return true;
            }
        }
        return false;
    }

}
