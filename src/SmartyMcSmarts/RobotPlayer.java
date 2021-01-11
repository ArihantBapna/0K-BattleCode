package SmartyMcSmarts;

import battlecode.common.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public strictfp class RobotPlayer {
    static RobotController rc;

    static final RobotType[] spawnableRobot = {
            RobotType.POLITICIAN,
            RobotType.SLANDERER,
            RobotType.MUCKRAKER,
    };


    static Direction current;

    static MapLocation goal;

    static int mode = 0;
    /*
    * 0 = Search mode. Move around the map trying to find enemy base
    * 1 = Attack mode. Move in towards enemy base given map location
     */

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
    static final Direction[] reverse = {
            Direction.NORTHWEST,
            Direction.WEST,
            Direction.SOUTHWEST,
            Direction.SOUTH,
            Direction.SOUTHEAST,
            Direction.EAST,
            Direction.NORTHEAST,
            Direction.NORTH,
    };

    static int turnCount;

    static ArrayList<Integer> robotId = new ArrayList<>();


    static boolean mapped = false;

    static int south = 0;
    static int north = 0;
    static int west = 0;
    static int east = 0;

    static int x = 0;
    static int y = 0;

    static MapLocation realLoc;

    /**
     * run() is the method that is called when a robot is instantiated in the Battlecode world.
     * If this method returns, the robot dies!
     **/
    @SuppressWarnings("unused")
    public static void run(RobotController rc) throws GameActionException {

        // This is the RobotController object. You use it to perform actions from this robot,
        // and to get information on its current status.
        RobotPlayer.rc = rc;

        turnCount = 0;

        current = randomDirection();

        if(rc.getType().equals(RobotType.ENLIGHTENMENT_CENTER)){
            //Its an ec
        }else{
            SetMappingBot();
        }

        while (true) {
            turnCount += 1;
            // Try/catch blocks stop unhandled exceptions, which cause your robot to freeze
            try {
                // Here, we've separated the controls into a different method for each RobotType.
                // You may rewrite this into your own control structure if you wish.
                System.out.println("I'm a " + rc.getType() + "! Location " + rc.getLocation());
                switch (rc.getType()) {
                    case ENLIGHTENMENT_CENTER: runEnlightenmentCenter(); break;
                    case POLITICIAN:           runPolitician();          break;
                    case SLANDERER:            runSlanderer();           break;
                    case MUCKRAKER:            runMuckraker();           break;
                }

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println(rc.getType() + " Exception");
                e.printStackTrace();
            }
        }
    }

    static void runEnlightenmentCenter() throws GameActionException {
        RobotType toBuild = randomSpawnableRobotType();
        int influence = 50;
        for (Direction dir : directions) {
            if (rc.canBuildRobot(toBuild, dir, influence)) {
                rc.buildRobot(toBuild, dir, influence);
                RobotInfo r = rc.senseRobotAtLocation(rc.getLocation().add(dir));
                int id = r.getID();
                robotId.add(id);
            }
        }
        if(!mapped){
            if(south != 0 || north != 0){
                if(east != 0 || west != 0){
                    //You have adjusted the map coordinates
                    mapped = true;
                }else{
                    StoreMapEc();
                }
            }else StoreMapEc();
        }else{
            StoreEcFlag();
        }
    }

    static void SetMappingBot() throws GameActionException{
        for(RobotInfo r : rc.senseNearbyRobots(-1,rc.getTeam())){
            if(r.getType().equals(RobotType.ENLIGHTENMENT_CENTER)){
                Direction d = r.getLocation().directionTo(rc.getLocation());
                realLoc = new MapLocation(64,64).add(d);
                RobotPlayer.x = realLoc.x;
                RobotPlayer.y = realLoc.y;
            }
        }
        UpdateBotFlag();
    }

    static void UpdateBotFlag() throws GameActionException{
        String flag = MakeECFlag(realLoc.x,realLoc.y,"98");
        rc.setFlag(Integer.parseInt(flag));
    }

    static void StoreEcFlag() throws GameActionException{
        if(rc.getFlag(rc.getID()) == 0){
            String flag = "99";
            int x = 0;
            int y = 0;
            if(south != 0){
                if(west != 0){
                    x = rc.getLocation().x - west;
                }else{
                    x = east - rc.getLocation().x;
                }
                y = rc.getLocation().y - south;
            }else{
                if(west != 0){
                    x = rc.getLocation().x - west;
                }else{
                    x = east - rc.getLocation().x;
                }
                y = north - rc.getLocation().y;
            }

            flag = MakeECFlag(x,y,flag);

            RobotPlayer.x = x;
            RobotPlayer.y = y;
            rc.setFlag(Integer.parseInt(flag));
        }
    }

    static void StoreMapEc() throws GameActionException{
        Iterator<Integer> rit = robotId.iterator();
        while(rit.hasNext()){
            Integer i = rit.next();
            if(rc.canGetFlag(i)){
                int flag = rc.getFlag(i);
                String fl = String.valueOf(flag);

                if(fl.length() >= 2){

                    int var = Integer.parseInt(fl.substring(2));

                    switch (fl.substring(0, 2)) {
                        case "11":
                            south = var;
                            break;
                        case "21":
                            north = var;
                            break;
                        case "31":
                            east = var;
                            break;
                        case "41":
                            west = var;
                        default:
                            break;
                    }
                }
            }else{
                rit.remove();
            }
        }
    }

    static void runPolitician() throws GameActionException {
        Team enemy = rc.getTeam().opponent();
        int actionRadius = rc.getType().actionRadiusSquared;
        RobotInfo[] attackable = rc.senseNearbyRobots(actionRadius, enemy);
        if (attackable.length != 0 && rc.canEmpower(actionRadius)) {
            System.out.println("empowering...");
            rc.empower(actionRadius);
            System.out.println("empowered");
            return;
        }
        Move();
    }

    static void runSlanderer() throws GameActionException {
        Move();
    }

    static void runMuckraker() throws GameActionException {
        Team enemy = rc.getTeam().opponent();
        int actionRadius = rc.getType().actionRadiusSquared;
        for (RobotInfo robot : rc.senseNearbyRobots(actionRadius, enemy)) {
            if (robot.type.canBeExposed()) {
                // It's a slanderer... go get them!
                if (rc.canExpose(robot.location)) {
                    System.out.println("e x p o s e d");
                    rc.expose(robot.location);
                    return;
                }
            }
        }
        Move();

    }

    static void Scan() throws GameActionException{
        for(RobotInfo r : rc.senseNearbyRobots()){
            if(r.getTeam().equals(rc.getTeam().opponent()) && r.getType().equals(RobotType.ENLIGHTENMENT_CENTER)){
                goal = r.getLocation();
                mode = 1;
            }
        }
    }

    static void Move() throws GameActionException {
        if(mode == 0) SearchMove();
        else if(mode == 1) AttackMove();
    }

    static void AttackMove() throws  GameActionException{
        Direction d = rc.getLocation().directionTo(goal);
        for(int i=0;i<8;i++){
            if(!tryMove(d)) d = randomRotation(d);
            else return;
        }
    }

    static void SearchMove() throws GameActionException{
        for(int i=0;i<8;i++){
            if(!rc.canMove(current)) {
                if(!rc.onTheMap(rc.getLocation().add(current))){
                    if(rc.getFlag(rc.getID()) <= 1){
                        switch (current) {
                            case SOUTH:
                                rc.setFlag(WriteDirFlag(Direction.SOUTH));
                                break;
                            case NORTH:
                                rc.setFlag(WriteDirFlag(Direction.NORTH));
                                break;
                            case EAST:
                                rc.setFlag(WriteDirFlag(Direction.EAST));
                                break;
                            case WEST:
                                rc.setFlag(WriteDirFlag(Direction.WEST));
                                break;
                        }
                    }
                    current = current.opposite();
                    current = current.rotateRight();
                }else if(rc.isLocationOccupied(rc.getLocation().add(current))){
                    current = randomDirection();
                }else{
                    current = randomRotation(current);
                }
            }
            else{
                rc.move(current);
                realLoc = realLoc.add(current);
                UpdateBotFlag();
                return;
            }
        }
        Scan();
    }

    /**
     * Returns a random Direction.
     *
     * @return a random Direction
     */
    static Direction randomDirection() {
        return directions[(int) (Math.random() * directions.length)];
    }

    static Direction randomRotation(Direction d){
        int[] val = new int[]{0,1,2,3,4,5,6,7};
        int sel = new Random().nextInt(val.length);
        for(int i=0;i<val[sel];i++){
            d = d.rotateRight();
        }
        return d;
    }

    /**
     * Returns a random spawnable RobotType
     *
     * @return a random RobotType
     */
    static RobotType randomSpawnableRobotType() {
        return spawnableRobot[(int) (Math.random() * spawnableRobot.length)];
    }

    static int WriteFlag(){
        int x = goal.x;
        int y = goal.y;
        String flag = "10" + String.valueOf(x).length() + x + "20" + String.valueOf(y).length() + y;
        return Integer.parseInt(flag);
    }
    static int WriteDirFlag(Direction dir){
        switch (dir) {
            case SOUTH: {
                int y = rc.getLocation().y;
                String flag = "11" + String.valueOf(y);
                return Integer.parseInt(flag);
            }
            case NORTH: {
                String flag = "21" + String.valueOf(rc.getLocation().y);
                return Integer.parseInt(flag);
            }
            case EAST: {
                String flag = "31" + String.valueOf(rc.getLocation().x);
                return Integer.parseInt(flag);

            }
            case WEST: {
                String flag = "41" + String.valueOf(rc.getLocation().x);
                return Integer.parseInt(flag);
            }
        }
        return 1;
    }

    static MapLocation ReadFlag(int flag){
        String fl = String.valueOf(flag);

        String i1 = fl.substring(0,2); //Should be 01
        String i2 = fl.substring(2,3); //Should be the length of x-coordinate
        int l1 = Integer.parseInt(i2);

        String x1 = fl.substring(4,(4+l1));//Gives the x-coordinate
        int x = Integer.parseInt(x1);

        String i3 = fl.substring((4+l1),(6+l1));//Should be 02
        String i4 = fl.substring((6+l1),(7+l1));//Should be length of y-coordinate
        int l2 = Integer.parseInt(i4);

        String x2 = fl.substring((7+l1),(7+l1+l2));//Gives the y-coordinate
        int y = Integer.parseInt(x2);

        MapLocation m = new MapLocation(x,y);

        return m;

    }

    static String MakeECFlag(int x, int y, String flag){
        if(String.valueOf(x).length() <= 1){
            if(String.valueOf(y).length() <= 1){
                flag += "0" + String.valueOf(x) + "0" + String.valueOf(y);
            }else{
                flag += "0" + String.valueOf(x) + String.valueOf(y);
            }
        }else{
            if(String.valueOf(y).length() <= 1){
                flag += String.valueOf(x) + "0" + String.valueOf(y);
            }else{
                flag += String.valueOf(x) + String.valueOf(y);
            }
        }
        return flag;
    }

    /**
     * Attempts to move in a given direction.
     *
     * @param dir The intended direction of movement
     * @return true if a move was performed
     * @throws GameActionException
     */
    static boolean tryMove(Direction dir) throws GameActionException {
        System.out.println("I am trying to move " + dir + "; " + rc.isReady() + " " + rc.getCooldownTurns() + " " + rc.canMove(dir));
        if (rc.canMove(dir)) {
            rc.move(dir);
            realLoc = realLoc.add(current);
            UpdateBotFlag();
            return true;
        } else return false;
    }
}

