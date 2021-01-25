package Kyle;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class Sland extends Bot {

    public SlandMove move2;

    public Sland(RobotController r) throws GameActionException {
        super(r);
        move2 = new SlandMove(rc);
        move2.current = Movement.randomDirection();
    }

    public void DoRun() throws GameActionException{
        if (move2.base != rc.getLocation()) {
            move2.CheckSurroundings();
        }
        move2.OptimizeDirection();
    }

    public void TryAndConvert() throws GameActionException{
        if(move2.scan.enemies.get(0).getLocation().isWithinDistanceSquared(rc.getLocation(),rc.getType().actionRadiusSquared-4)){
            if(rc.canEmpower(rc.getType().actionRadiusSquared-4)){
                if(rc.getInfluence() != 1){
                    rc.empower(rc.getType().actionRadiusSquared-4);
                }
            }
        }
    }
}
