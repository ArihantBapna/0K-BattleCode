package Final0k;

import battlecode.common.*;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class ECenterSpawns {
    public RobotController rc;
    public List<Double> probDist;
    public List<RobotType> spawns;

    public ECenterSpawns(RobotController r) {
        rc = r;
        spawns = Arrays.asList(RobotType.POLITICIAN, RobotType.SLANDERER, RobotType.MUCKRAKER);
        probDist = Arrays.asList(0.25,0.25,0.5);
    }

    public void DoRandomSpawns(RobotType toBuild) throws GameActionException {
        int influence = rc.getInfluence();
        int inf = 0;
        switch(toBuild){
            case POLITICIAN:{
                inf = (int) (rc.getInfluence() * 0.2);
                break;
            }
            case SLANDERER:{
                inf = (int) (0.88*influence);
                if(inf > 949) inf = 949;
                break;
            }
            case MUCKRAKER:{
                if(RobotPlayer.turnCount > 300) inf = (int) (0.3*influence);
                else inf = 1;
                break;
            }
        }
        for (Direction dir : Constants.directions) {
            if (rc.canBuildRobot(toBuild, dir, inf)) {
                rc.buildRobot(toBuild, dir, inf);
                RobotInfo r = rc.senseRobotAtLocation(rc.getLocation().add(dir));
                UpdateECList(r);
            }
        }
    }

    public void DoRandomSpawns(RobotType toBuild, int i) throws GameActionException {
        for (Direction dir : Constants.directions) {
            if (rc.canBuildRobot(toBuild, dir, i)) {
                rc.buildRobot(toBuild, dir, i);
                RobotInfo r = rc.senseRobotAtLocation(rc.getLocation().add(dir));
                UpdateECList(r);
            }
        }
    }

    public void UpdateECList(RobotInfo r) {
        switch(r.getType()){
            case POLITICIAN:{
                ECenter.poli.add(r.getID());
                break;
            }
            case MUCKRAKER:{
                ECenter.muck.add(r.getID());
                break;
            }
            case SLANDERER:{
                ECenter.sland.add(r.getID());
                break;
            }
        }
    }

    public RobotType GetRandomRobot(){
        return getRobotType(spawns, probDist);
    }
    public RobotType getRobotType(List<RobotType> robType, List<Double> probDist) {
        double p = Math.random();
        double total = 0.0d;
        TreeMap<Double, RobotType> map = new TreeMap<>();
        for (int i = 0; i < robType.size(); i++) {
            map.put(total += probDist.get(i), robType.get(i));
        }
        return map.ceilingEntry(p).getValue();
    }

    public RobotType randomSpawnableRobotType() {
        return spawns.get((int) (Math.random() * spawns.size()));
    }
}
