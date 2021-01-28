package OkZoomer2;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

import java.util.ArrayList;

public class ECenter {

    public RobotController rc;

    public ECenterComm comm;
    public ECenterSpawns spawn;

    public static ArrayList<Integer> muck;
    public static ArrayList<Integer> sland;
    public static ArrayList<Integer> poli;

    public ECenter(RobotController r) {

        rc = r;
        comm = new ECenterComm(rc);
        spawn = new ECenterSpawns(rc);

        muck = new ArrayList<>();
        sland = new ArrayList<>();
        poli = new ArrayList<>();
    }



    public void doRun() throws GameActionException {
        if(rc.getRoundNum() < 6){
            spawn.ReallyEarlySpawns(rc.getRoundNum());
        }else if(rc.getRoundNum() < 500){
            spawn.EarlySpawns();
        }else{
            spawn.MidGameSpawns();
        }
    }


}
