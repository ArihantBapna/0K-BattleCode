package Kyle;

import battlecode.common.*;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class ECenterSpawns {

    public static final RobotType[] spawnableRobot = {
            RobotType.POLITICIAN,
            RobotType.SLANDERER,
            RobotType.MUCKRAKER,
    };

    public static final Direction[] directions = {
            Direction.NORTH,
            Direction.NORTHEAST,
            Direction.EAST,
            Direction.SOUTHEAST,
            Direction.SOUTH,
            Direction.SOUTHWEST,
            Direction.WEST,
            Direction.NORTHWEST,
    };

    public List<Double> probDist;
    public RobotController rc;

    public ECenterSpawns(RobotController r) {
        rc = r;
        probDist = Arrays.asList(0.34,0.46,0.3);
    }

    public void DoRandomSpawns(RobotType toBuild) throws GameActionException {
        int influence = rc.getInfluence();
        int inf = 0;
        switch(toBuild){
            case POLITICIAN:{
                if(influence < 100) inf = 1;
                else inf = (int) (0.25*influence);
                break;
            }
            case SLANDERER:{
                if(RobotPlayer.turnCount < 200){
                    inf = (int) (0.9*influence);
                    break;
                }else{
                    if(rc.getInfluence() < 1000){
                        toBuild = RobotType.MUCKRAKER;
                        inf = 1;
                    }
                }
            }
            case MUCKRAKER:{
                if(RobotPlayer.turnCount > 100) inf = (int) (0.3*influence);
                else inf = 1;
                break;
            }
        }
        for (Direction dir : directions) {
            if (rc.canBuildRobot(toBuild, dir, inf)) {
                rc.buildRobot(toBuild, dir, inf);
                RobotInfo r = rc.senseRobotAtLocation(rc.getLocation().add(dir));
                UpdateECList(r);
            }
        }
    }

    public void DoRandomSpawns(RobotType toBuild, int i) throws GameActionException {
        for (Direction dir : directions) {
            if (rc.canBuildRobot(toBuild, dir, i)) {
                rc.buildRobot(toBuild, dir, i);
                RobotInfo r = rc.senseRobotAtLocation(rc.getLocation().add(dir));
                UpdateECList(r);
            }
        }
    }

    public RobotType GetRandomRobot(){
        return getRobotType(Arrays.asList(spawnableRobot), probDist);
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

    public void UpdateECList(RobotInfo r) {

    }


}
