package OkCoomers;

import OkZoomer.PoliCircleMove;
import battlecode.common.*;

import java.util.Objects;

public class Poli {
    public RobotController rc;
    public PoliMove move;
    public PoliCircleMove move2;

    public Poli(RobotController r) throws GameActionException {
        rc = r;
        move = new PoliMove(rc);
        move2 = new PoliCircleMove(rc);
    }

    public void DoRun() throws GameActionException{

        move.DoMove();

    }

}
