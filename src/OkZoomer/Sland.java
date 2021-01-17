package OkZoomer;

import battlecode.common.*;

public class Sland {

    public SlandMove move;
    public PoliMove moveNew;
    public RobotController rc;

    public int currentType;

    public Sland(RobotController r) throws GameActionException {
        rc = r;
        move = new SlandMove(rc);
        currentType = 0;
    }

    public void doRun() throws GameActionException {
        if(rc.getType().equals(RobotType.SLANDERER) && currentType == 0){
            move.CheckSurroundingsAndMove();
        }else{
            move.DoConvertedMove();
            EnsureFlagUpdate();
        }
    }

    public void EnsureFlagUpdate() throws GameActionException{
        if(rc.getFlag(rc.getID()) == 0){
            rc.setFlag(1);
        }
    }
}
