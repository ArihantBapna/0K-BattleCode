package Final0k;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

import java.util.ArrayList;

public class ECenter {
    public RobotController rc;

    public static ArrayList<Integer> muck;
    public static ArrayList<Integer> sland;
    public static ArrayList<Integer> poli;

    public ECenterSpawns spawn;

    public static int bid;
    public static int votes;
    public static int votesNow;


    public ECenter(RobotController r) {
        rc = r;
        muck = new ArrayList<>();
        sland = new ArrayList<>();
        poli = new ArrayList<>();
        spawn = new ECenterSpawns(rc);
        bid = 0;
        votes = rc.getTeamVotes();
    }

    public void DoRun() throws GameActionException {
        votesNow = rc.getTeamVotes();
        spawn.DoRandomSpawns(spawn.GetRandomRobot());
        TryVote();
    }

    public void TryVote() throws GameActionException {
        int inf = rc.getInfluence();
        int bid = (int) (0.3 * inf);
        if(rc.canBid(bid)){
            if(rc.getRoundNum()<300){
                rc.bid(2);
            }else{
                rc.bid(bid);
            }
        }
    }
}
