package OkZoomer;

import battlecode.common.*;

import java.util.ArrayList;
import java.util.Iterator;

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
        }else{
            spawn.EarlySpawns();
        }
    }


}
