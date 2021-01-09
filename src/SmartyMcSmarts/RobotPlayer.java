package SmartyMcSmarts;

import battlecode.common.*;

import java.util.ArrayList;
import java.util.Arrays;
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

        System.out.println("I'm a " + rc.getType() + " and I just got created!");
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
        for(int i : robotId){
            try{
                int flag = rc.getFlag(i);
                String fl = String.valueOf(flag);
                if(fl.substring(0,2).equals("11")){
                    System.out.println("YES! SOMEONE FOUND THE SOUTH POLE " +fl.substring(2,fl.length()));
                }
            }catch(GameActionException e){
                System.out.println("Robot " +i +" has died");
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
                    if(current.equals(Direction.SOUTH)){
                        rc.setFlag(WriteDirFlag(Direction.SOUTH));
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
        if(dir.equals(Direction.SOUTH)){
            int y = rc.getLocation().y;
            String flag = "11" + String.valueOf(y);
            return Integer.parseInt(flag);
        }else{
            return 1;
        }
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
            return true;
        } else return false;
    }
}
