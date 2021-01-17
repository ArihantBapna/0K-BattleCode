package OkZoomer;

import battlecode.common.*;

public class Poli {

    public RobotController rc;
    public PoliMove move;

    public Poli(RobotController r) throws GameActionException {
        rc = r;
        move = new PoliMove(rc);
    }

    public void doRun() throws GameActionException {
        move.DoMove();
    }
}
