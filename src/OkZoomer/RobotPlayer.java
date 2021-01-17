package OkZoomer;

import battlecode.common.*;

public class RobotPlayer {
    static RobotController rc;
    static int turnCount = 0;

    public static void run(RobotController rc) throws GameActionException{
        RobotPlayer.rc = rc;
        RobotType type = rc.getType();

        if (type == RobotType.ENLIGHTENMENT_CENTER) {
            ECenter e = new ECenter(rc);
            while(true){
                turnCount++;
                e.doRun();
                Clock.yield();
            }
        } else if (type == RobotType.POLITICIAN) {
            Poli e = new Poli(rc);
            while(true){
                turnCount++;
                e.doRun();
                Clock.yield();
            }
        } else if (type == RobotType.SLANDERER) {
            Sland e = new Sland(rc);
            while(true){
                turnCount++;
                e.doRun();
                Clock.yield();

            }
        } else if (type == RobotType.MUCKRAKER) {
            Muck e = new Muck(rc);
            while(true){
                turnCount++;
                e.doRun();
                Clock.yield();
            }
        }
    }

}
