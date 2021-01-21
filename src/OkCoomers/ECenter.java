package OkCoomers;

import battlecode.common.*;

import java.util.ArrayList;

public class ECenter {

    public RobotController rc;

    public static ArrayList<Integer> muck;
    public static ArrayList<Integer> sland;
    public static ArrayList<Integer> poli;

    public ECenterCommunication comm;

    public ECenter(RobotController r) {
        rc = r;

        muck = new ArrayList<>();
        sland = new ArrayList<>();
        poli = new ArrayList<>();

        comm = new ECenterCommunication(rc,new Scan(rc));
    }

    public void doRun() throws GameActionException{
        if(RobotPlayer.turnCount < 100){
            ECenterEarly spawn = new ECenterEarly(rc);
            spawn.RunSpawns();
        }else if(RobotPlayer.turnCount < 300){
            ECenterMid spawn = new ECenterMid(rc);
            spawn.RunSpawns();
        }else{
            ECenterLate spawn = new ECenterLate(rc);
            spawn.RunSpawns();
        }

        comm.TryMuckFlag(muck);
    }

}
