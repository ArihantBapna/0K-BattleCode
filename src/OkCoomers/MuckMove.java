package OkCoomers;

import battlecode.common.*;

import java.util.Objects;

public class MuckMove extends Movement {

    public RobotInfo target;

    public MuckMove(RobotController r) throws GameActionException {
        super(r);
    }

    public void DoMove() throws GameActionException{

        if(rc.getFlag(rc.getID()) == 0){
            comm.TryReadFlag();
        }else{
            current = Constants.AdjustLocation(rc.getLocation()).directionTo(Constants.DecodeLocation(rc.getFlag(rc.getID())));
        }

        if(scan.GetNearbyEC() > 0){
            target = scan.ec.get(0);
            current = rc.getLocation().directionTo(target.getLocation());
        }else if(!Objects.isNull(target) && !rc.getLocation().isAdjacentTo(target.getLocation())){
            if(scan.GetNearbySland() > 0){
                target = scan.sland.get(0);
                current = rc.getLocation().directionTo(target.getLocation());
            }
        }else if(scan.GetNearbySland() > 0){
            target = scan.sland.get(0);
            current = rc.getLocation().directionTo(target.getLocation());
        }

        TryToExpose();
        TryRandomMove();
    }

    public void TryToExpose() throws GameActionException{
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
