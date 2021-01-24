package OkBloomer;

import battlecode.common.*;

public class Sland extends Bot {

    public SlandMove move2;

    public Sland(RobotController r) throws GameActionException {
        super(r);
        move2 = new SlandMove(rc);

    }

    public void DoRun() throws GameActionException{
        move2.CheckSurroundings();
        if(rc.getType().equals(RobotType.POLITICIAN)){
            if(move2.DoConvertedMove()) TryAndConvert();
        }
        move2.JustMove();
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
