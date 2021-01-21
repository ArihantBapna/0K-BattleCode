package OkCoomers;

import battlecode.common.*;

public class Sland {

    public RobotController rc;
    public SlandMoveEarly move1;
    public SlandMoveMid move2;
    public SlandMoveLate move3;

    public Sland(RobotController r) throws GameActionException {
        rc = r;
        move1 = new SlandMoveEarly(rc);
        move2 = new SlandMoveMid(rc);
        move3 = new SlandMoveLate(rc);
    }

    public void DoRun() throws GameActionException{
        if(RobotPlayer.turnCount < 50){
            //Keep moving in circle

            move1.DoMove();

        }else if(RobotPlayer.turnCount < 300){
            //Move away from all units

            move2.DoMove();

        }else{
            //You've converted, now go do shit

            move3.DoMove();
        }
    }

}
