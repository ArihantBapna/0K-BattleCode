package OkBloomer;

import battlecode.common.*;

public class Muck extends Bot {
    public Muck(RobotController r) throws GameActionException {
        super(r);
    }

    public void DoRun() throws GameActionException {
        TryExpose();
        if(move.scan.GetNearbyEC() > 0){
            move.FindPathToGoal(move.scan.ec.get(0).getLocation());
            move.JustMove();
        }else{
            move.TryRandomMove();
        }
    }
    public void TryExpose() throws GameActionException{
        Team enemy = rc.getTeam().opponent();
        int actionRadius = rc.getType().actionRadiusSquared;
        for (RobotInfo robot : rc.senseNearbyRobots(actionRadius, enemy)) {
            if (robot.type.canBeExposed()) {
                if (rc.canExpose(robot.location)) {
                    rc.expose(robot.location);
                    return;
                }
            }
        }
    }
}
