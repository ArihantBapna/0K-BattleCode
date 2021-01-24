package OkZoomer2;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class Sland {

    public SlandMove move;
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
