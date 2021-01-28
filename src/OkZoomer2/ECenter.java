package OkZoomer2;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

import java.util.ArrayList;
import java.util.Objects;

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

        Place p = comm.ReadMuckFlags();

        if(!Objects.isNull(p)){
            if(String.valueOf(rc.getFlag(rc.getID())).substring(0,1).equals("3")){
                if(p.head.equals("4")){
                    int self = Integer.parseInt(String.valueOf(rc.getFlag(rc.getID())).substring(1));
                    if(p.loc == self){
                        rc.setFlag(p.GetEncodedFlag("4"));
                    }
                }
            }else{
                if(p.head.equals("2")){
                    rc.setFlag(p.GetEncodedFlag("3"));
                }
            }
        }

        if(rc.getRoundNum() < 6){
            spawn.ReallyEarlySpawns(rc.getRoundNum());
        }else if(rc.getRoundNum() < 500){
            spawn.EarlySpawns();
        }else{
            spawn.MidGameSpawns();
        }
    }


}
