package Kyle;

import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class RobotPlayer {

    public static Muck m;
    public static Sland s;
    public static Poli p;
    public static ECenter e;

    static RobotController rc;
    static int turnCount = 0;

    public static void run(RobotController r) throws GameActionException{
        rc = r;
        RobotType type = rc.getType();


        switch(type){
            case SLANDERER:{
                s = new Sland(rc);
                while (true){
                    s.DoRun();
                    turnCount++;
                    Clock.yield();
                }
            }
            case MUCKRAKER:{
                m = new Muck(rc);
                while(true){
                    m.DoRun();
                    turnCount++;
                    Clock.yield();
                }
            }
            case POLITICIAN:{
                p = new Poli(rc);
                while(true){
                    p.DoRun();
                    turnCount++;
                    Clock.yield();
                }

            }case ENLIGHTENMENT_CENTER:{
                e = new ECenter(rc);
                while(true){
                    e.doRun();
                    turnCount++;
                    Clock.yield();
                }

            }
        }
    }


}
