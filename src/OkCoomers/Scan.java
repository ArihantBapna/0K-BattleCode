package OkCoomers;

import battlecode.common.*;

import java.lang.reflect.Array;
import java.util.*;

public class Scan {
    public RobotController rc;

    public ArrayList<RobotInfo> nearby;
    public ArrayList<RobotInfo> enemies;
    public ArrayList<RobotInfo> neutral;
    public ArrayList<RobotInfo> friendly;

    public ArrayList<RobotInfo> mucks;
    public ArrayList<RobotInfo> sland;
    public ArrayList<RobotInfo> poli;
    public ArrayList<RobotInfo> ec;

    public Scan(RobotController r){
        rc = r;
    }


    /*
     * All possible scanners we may need for the different bots
     * They fill the lists with all nearby bots of the requested type, then sort it by distance to rc
     * We can then pick the closest ones or further shortlist based on a different radius param
     */

    public int GetNearbyFriendly() throws GameActionException{
        friendly = new ArrayList<>();
        Collections.addAll(friendly,rc.senseNearbyRobots(-1,rc.getTeam()));
        friendly.sort(new CompareDistance(rc));
        return friendly.size();
    }

    public int GetNearbyEnemies() throws GameActionException{
        enemies = new ArrayList<>();
        Collections.addAll(enemies, rc.senseNearbyRobots(-1, rc.getTeam().opponent()));
        enemies.sort(new CompareDistance(rc));
        return enemies.size();
    }

    public int GetNearbyAll() throws GameActionException{
        nearby = new ArrayList<>();
        Collections.addAll(nearby, rc.senseNearbyRobots(-1));
        nearby.sort(new CompareDistance(rc));
        return nearby.size();
    }

    public int GetNearbyNeutral() throws GameActionException{
        neutral = new ArrayList<>();
        Collections.addAll(neutral, rc.senseNearbyRobots(-1,Team.NEUTRAL));
        neutral.sort(new CompareDistance(rc));
        return neutral.size();
    }

    public int GetNearbyMucks() throws GameActionException{
        mucks = new ArrayList<>();
        for(RobotInfo r : rc.senseNearbyRobots(-1,rc.getTeam().opponent())){
            if(r.getType().equals(RobotType.MUCKRAKER)){
                mucks.add(r);
            }
        }
        mucks.sort(new CompareDistance(rc));
        return mucks.size();
    }

    public int GetNearbyPoli() throws GameActionException{
        poli = new ArrayList<>();
        for(RobotInfo r : rc.senseNearbyRobots(-1,rc.getTeam().opponent())){
            if(r.getType().equals(RobotType.POLITICIAN)){
                poli.add(r);
            }
        }
        poli.sort(new CompareDistance(rc));
        return poli.size();
    }

    public int GetNearbySland() throws GameActionException{
        sland = new ArrayList<>();
        for(RobotInfo r : rc.senseNearbyRobots(-1,rc.getTeam().opponent())){
            if(r.getType().equals(RobotType.SLANDERER)){
                sland.add(r);
            }
        }
        sland.sort(new CompareDistance(rc));
        return sland.size();
    }

    public int GetNearbyEC() throws GameActionException{
        ec = new ArrayList<>();
        for(RobotInfo r : rc.senseNearbyRobots(-1,rc.getTeam().opponent())){
            if(r.getType().equals(RobotType.ENLIGHTENMENT_CENTER)){
                ec.add(r);
            }
        }
        ec.sort(new CompareDistance(rc));
        return ec.size();
    }



}
