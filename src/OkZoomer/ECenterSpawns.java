package OkZoomer;

import battlecode.common.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

public class ECenterSpawns {

    public RobotController rc;
    private List<Double> probDist;

    public ECenterSpawns(RobotController r) {
        rc = r;
    }

    public void ReallyEarlySpawns(int turn) throws GameActionException {
        RobotType toBuild;
        int inf;
        switch(turn){
            case 1:{
                toBuild = RobotType.POLITICIAN;
                inf = 25;
                break;
            }
            case 3:{
                toBuild = RobotType.SLANDERER;
                inf = 107;
                break;
            }
            case 5:{
                toBuild = RobotType.MUCKRAKER;
                inf = 1;
                break;
            }
            default:{
                toBuild = RobotType.SLANDERER;
                inf = rc.getInfluence();
            }
        }
        DoRandomSpawns(toBuild,inf);
    }

    public void EarlySpawns() throws GameActionException{
        probDist = Arrays.asList(0.45,0.45,0.1);
        RemoveConvertedSland();
        if(ECenter.sland.size() > 20){
            probDist = Arrays.asList(0.45,0.1,0.45);
        }
        DoRandomSpawns(GetRandomRobot());
    }

    public void MidGameSpawns() throws GameActionException{
        probDist = Arrays.asList(0.56,0.2,0.24);
        RemoveConvertedSland();
        DoRandomSpawns(GetRandomRobot());
        TryVote();
    }

    public void TryVote() throws GameActionException {
        int inf = rc.getInfluence();
        int bid = (int) (0.3 * inf);
        if(rc.canBid(bid)){
            rc.bid(bid);
        }
    }

    public void RemoveConvertedSland() throws GameActionException{
        Iterator<Integer> it = ECenter.sland.iterator();
        while(it.hasNext()){
            int id = it.next();
            if(rc.canGetFlag(id)){
                if(rc.getFlag(id) != 0){
                    it.remove();
                }
            }else{
                it.remove();
            }
        }
    }

    private void DoRandomSpawns(RobotType toBuild) throws GameActionException {
        int influence = rc.getInfluence();
        int inf = 0;
        switch(toBuild){
            case POLITICIAN:{
                if(influence < 100) inf = 1;
                else inf = (int) (0.25*influence);
                break;
            }
            case SLANDERER:{
                inf = (int) (0.7*influence);
                break;
            }
            case MUCKRAKER:{
                if(RobotPlayer.turnCount > 100) inf = (int) (0.3*influence);
                else inf = 1;
                break;
            }
        }
        for (Direction dir : OkTomber.Constants.directions) {
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

    private RobotType GetRandomRobot(){
        return getRobotType(Arrays.asList(Constants.spawnableRobot), probDist);
    }
    private RobotType getRobotType(List<RobotType> robType, List<Double> probDist) {
        double p = Math.random();
        double total = 0.0d;
        TreeMap<Double, RobotType> map = new TreeMap<>();
        for (int i = 0; i < robType.size(); i++) {
            map.put(total += probDist.get(i), robType.get(i));
        }
        return map.ceilingEntry(p).getValue();
    }

    private static RobotType randomSpawnableRobotType() {
        return OkTomber.Constants.spawnableRobot[(int) (Math.random() *  OkTomber.Constants.spawnableRobot.length)];
    }


}
