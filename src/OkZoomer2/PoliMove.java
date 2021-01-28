package OkZoomer2;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.Team;

import java.util.Objects;

public class PoliMove extends Movement {

    static int dis;
    public PoliScan scan;

    public PoliMove(RobotController r) throws GameActionException {
        super(r);
        dis = 20;
        scan = new PoliScan(rc);
    }

    public void DoMove() throws GameActionException{
        if(rc.getInfluence() <= 20){
            if(scan.GetNearbyMucks() > 0){
                TryAndSubdue();
                if(!rc.getLocation().isAdjacentTo(scan.closest.getLocation())){
                    current = rc.getLocation().directionTo(scan.closest.getLocation());
                    TryRandomMove();
                }
            }else{
                if(!Objects.isNull(mlEC)){
                    DefensiveRotation();
                }else{
                    OptimizeDirection(current);
                }
            }
        }else{
            TryGivingSpeech();
            if(scan.GetNearbyEnemies() > 0){
                TryAndSubdue();
                current = rc.getLocation().directionTo(scan.closest.getLocation());
                TryRandomMove();
            }else if(scan.GetNearbyNeutEC()){
                TryGivingSpeech();
                current = rc.getLocation().directionTo(scan.closest.getLocation());
                TryRandomMove();
                System.out.println("Current: " +current);
            }else{
                CheckAllPaths(rc.getLocation());
                OptimizeDirection(current);
            }

        }
    }

    public void TryGivingSpeech() throws GameActionException{
        Team enemy = rc.getTeam().opponent();
        int actionRadius = rc.getType().actionRadiusSquared;
        RobotInfo[] attack = rc.senseNearbyRobots(actionRadius, enemy);
        RobotInfo[] neut = rc.senseNearbyRobots(actionRadius, Team.NEUTRAL);
        if (attack.length != 0 && rc.canEmpower(actionRadius)) {
            rc.empower(actionRadius);
        }else if(neut.length != 0 && rc.canEmpower(actionRadius)){
            rc.empower(actionRadius);
        }
    }

    public void TryAndConvert() throws GameActionException{
        if(scan.closest.getLocation().isWithinDistanceSquared(rc.getLocation(),rc.getType().actionRadiusSquared)){
            if(rc.canEmpower(rc.getType().actionRadiusSquared)){
                if(rc.getInfluence() != 1){
                    rc.empower(rc.getType().actionRadiusSquared);
                }
            }
        }
    }

    public void TryAndSubdue() throws GameActionException{
        if(scan.closest.getLocation().isWithinDistanceSquared(rc.getLocation(),rc.getType().actionRadiusSquared)){
            if(rc.canEmpower(rc.getType().actionRadiusSquared)){
                if(rc.getInfluence() != 1){
                    rc.empower(rc.getType().actionRadiusSquared);
                }
            }
        }
    }

    public void DefensiveRotation() throws GameActionException {
        if(rc.getLocation().isWithinDistanceSquared(mlEC,dis)){
            if(rc.getLocation().isWithinDistanceSquared(mlEC,9)){
                current = rc.getLocation().directionTo(mlEC).opposite();
            }else{
                current = current.rotateRight();
            }
        }else{
            current = rc.getLocation().directionTo(mlEC);
        }
        TryRandomMove();
    }

}
