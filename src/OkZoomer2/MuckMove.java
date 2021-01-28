package OkZoomer2;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.Team;

import java.util.Objects;

public class MuckMove extends Movement {

    public MuckScan scan;

    public MuckMove(RobotController r) throws GameActionException {
        super(r);
        scan = new MuckScan(rc);
    }

    public void DoMove() throws GameActionException{
        TryAndExpose();
        if(scan.NearbyEC()){
            current = scan.closest;
            rc.setFlag(Constants.EncodeLocation(scan.TargetEC.getLocation(),"2"));
            if(!rc.getLocation().isAdjacentTo(scan.TargetEC.getLocation())){
                if (scan.isECSurrounded(scan.TargetEC.getLocation())) {
                    current = Constants.randomDirection();
                }
                TryRandomMove();
            }else{
                if(rc.canSenseLocation(scan.TargetEC.getLocation())){
                    RobotInfo r = rc.senseRobotAtLocation(scan.TargetEC.getLocation());
                    if(r.getTeam().equals(rc.getTeam())){
                        rc.setFlag(0);
                        current = Constants.randomDirection();
                    }
                }
            }
        }else{
            if(!Objects.isNull(scan.TargetEC)){
                if(!scan.TargetEC.getLocation().isAdjacentTo(rc.getLocation())){
                    if(scan.isECSurrounded(scan.TargetEC.getLocation())){
                        current = Constants.randomDirection();
                    }
                    TryRandomMove();
                }else{
                    if(scan.isECSurrounded(scan.TargetEC.getLocation())){
                        rc.setFlag(Constants.EncodeLocation(scan.TargetEC.getLocation(),"4"));
                    }
                }
            }else{
                int flag = 0;
                if(rc.canGetFlag(EC)){
                    flag = rc.getFlag(EC);
                }

                if(String.valueOf(rc.getFlag(rc.getID())).substring(0,1).equals("2")){
                    if(String.valueOf(flag).substring(0,1).equals("4")){
                        if(String.valueOf(flag).substring(1).equals(String.valueOf(rc.getFlag(rc.getID())).substring(1))){
                            rc.setFlag(0);
                        }
                    }
                }else if(String.valueOf(rc.getFlag(rc.getID())).substring(0,1).equals("0")){
                    if(String.valueOf(flag).substring(0,1).equals("3")){
                        current = Constants.AdjustLocation(rc.getLocation()).directionTo(Constants.DecodeLocation(flag));
                        rc.setFlag(Integer.parseInt("2" + String.valueOf(flag).substring(1)));
                        System.out.println("Current: " +current.toString());
                    }
                }
                TryRandomMove();
            }
        }
    }

    public void TryAndExpose() throws GameActionException{
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
