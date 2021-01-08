package movement;
import battlecode.common.*;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class RobotPlayer {
    static RobotController rc;

    static final RobotType[] spawnableRobot = {
            RobotType.POLITICIAN,
            RobotType.SLANDERER,
            RobotType.MUCKRAKER,
    };

    static final Direction[] directions = {
            Direction.NORTH,
            Direction.NORTHEAST,
            Direction.EAST,
            Direction.SOUTHEAST,
            Direction.SOUTH,
            Direction.SOUTHWEST,
            Direction.WEST,
            Direction.NORTHWEST,
    };

    static int turnCount;

    //Main Run method
    public static void run(RobotController rc) throws GameActionException {
        RobotPlayer.rc = rc;
        turnCount = 0;

        while(true){
            try{
                switch (rc.getType()) {
                    case ENLIGHTENMENT_CENTER: runEnlightenmentCenter(); break;
                    case POLITICIAN:           runPolitician();          break;
                    case SLANDERER:            runSlanderer();           break;
                    case MUCKRAKER:            runMuckraker();           break;
                }
            }catch(Exception e){
                System.out.println(rc.getType() + " Exception");
                e.printStackTrace();
            }
            Clock.yield();
        }
    }

    private static void runMuckraker() throws GameActionException {

        Team enemy = rc.getTeam().opponent();
        int actionRadius = rc.getType().actionRadiusSquared;
        for (RobotInfo robot : rc.senseNearbyRobots(actionRadius, enemy)) {
            if (robot.type.canBeExposed()) {
                if (rc.canExpose(robot.location)) {
                    System.out.println("e x p o s e d");
                    try {
                        rc.expose(robot.location);
                    } catch (GameActionException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
        }
        Move(directions);

    }

    private static void runSlanderer() throws GameActionException {
        Move(directions);
    }

    private static void runPolitician() throws GameActionException {
        Team enemy = rc.getTeam().opponent();
        int actionRadius = rc.getType().actionRadiusSquared;
        RobotInfo[] attackable = rc.senseNearbyRobots(actionRadius, enemy);
        if (attackable.length != 0 && rc.canEmpower(actionRadius)) {
            System.out.println("empowering...");
            try {
                rc.empower(actionRadius);
            } catch (GameActionException e) {
                e.printStackTrace();
            }
            System.out.println("empowered");
            return;
        }

        Move(directions);

    }

    private static void runEnlightenmentCenter() {
        RobotType toBuild = randomSpawnableRobotType();
        int influence = 50;
        for (Direction dir : directions) {
            if (rc.canBuildRobot(toBuild, dir, influence)) {
                try {
                    rc.buildRobot(toBuild, dir, influence);
                } catch (GameActionException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
        }
    }

    //Helpful methods

    //rnd direction
    static Direction randomDirection() {
        return directions[(int) (Math.random() * directions.length)];
    }

    static Direction fromLoc(MapLocation m1, MapLocation m2){

        double angle = Math.atan2(m2.y-m1.y,m2.x-m1.x); //find the angle of the vector btn the points
        angle += Math.PI;
        angle /= Math.PI/4;
        int halfQ = (int) angle;
        halfQ %= 8;
        switch(halfQ){
            case 1: return Direction.NORTHWEST;
            case 2: return Direction.WEST;
            case 3: return Direction.SOUTHWEST;
            case 4: return Direction.SOUTH;
            case 5: return Direction.SOUTHEAST;
            case 6: return Direction.EAST;
            case 7: return Direction.NORTHEAST;
            default: return Direction.NORTH; //case 0
        }
    }

    //rnd bottype
    static RobotType randomSpawnableRobotType() {
        return spawnableRobot[(int) (Math.random() * spawnableRobot.length)];
    }

    //gets the easiest path to traverse

    static void Move(Direction[] tries) throws GameActionException {
        if(rc.getCooldownTurns() < 1 && rc.isReady()){
            int rnd = (int) (Math.random() * tries.length);
            if(!tryMove(tries[rnd])){
                tries = ArrayUtils.remove(tries, Arrays.asList(tries).indexOf(tries[rnd]));
                Move(tries);
            }
        }else return;
    }

    //attempts to move, true if success.
    static boolean tryMove(Direction dir) throws GameActionException {
        if (rc.canMove(dir)) {
            rc.move(dir);
            return true;
        } else return false;
    }
}
