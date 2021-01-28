package Final0k;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

import java.util.Arrays;

public class Spawner extends ECenterSpawns {
    public Spawner(RobotController r) {
        super(r);
    }

    public void DoSpawn() throws GameActionException {
        if(rc.getRoundNum() == 1){
            DoRandomSpawns(RobotType.SLANDERER,130);
        }
        else{
            if(rc.getRoundNum() < 300){
                probDist = Arrays.asList(0.25,0.25,0.5);
                DoRandomSpawns(GetRandomRobot());
            }
        }
    }
}
