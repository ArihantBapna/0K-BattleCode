package movement;

import battlecode.common.*;

import java.util.*;

public class RobotPlayer {
    static RobotController rc;
    
    static double tan_Pi_div_8= Math.sqrt(2.0) - 1.0;

    static final RobotType[] spawnableRobot = {
            RobotType.POLITICIAN,
            RobotType.SLANDERER,
            RobotType.MUCKRAKER,
    };

    static int age = 0;

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
            age++;
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
        Move();

    }

    private static void runSlanderer() throws GameActionException {
        Move();
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

        Move();

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

    //rnd bottype
    static RobotType randomSpawnableRobotType() {
        return spawnableRobot[(int) (Math.random() * spawnableRobot.length)];
    }

    //gets the easiest path to traverse

    static void Move() throws GameActionException {
        if(age > 50){
            RobotInfo[] nearby = rc.senseNearbyRobots();
            int[] dirWeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0}; //Order: N , E , S , W , NW , NE , SE , SW


            for(RobotInfo r : nearby){
                if(r.getTeam().equals(rc.getTeam())){
                    Direction d = GetDirection(rc.getLocation(),r.getLocation());
                    System.out.println("Bot found at " +d.toString());
                    switch(d){
                        case NORTH:{
                            dirWeights[2]++;
                            dirWeights[6]++;
                            dirWeights[7]++;
                        }
                        case EAST:{
                            dirWeights[3]++;
                            dirWeights[4]++;
                            dirWeights[7]++;
                        }
                        case SOUTH:{
                            dirWeights[0]++;
                            dirWeights[4]++;
                            dirWeights[5]++;
                        }
                        case WEST:{
                            dirWeights[1]++;
                            dirWeights[5]++;
                            dirWeights[6]++;
                        }
                        case NORTHWEST:{
                            dirWeights[1]++;
                            dirWeights[2]++;
                            dirWeights[6]++;
                        }
                        case NORTHEAST:{
                            dirWeights[2]++;
                            dirWeights[3]++;
                            dirWeights[7]++;
                        }
                        case SOUTHEAST:{
                            dirWeights[0]++;
                            dirWeights[3]++;
                            dirWeights[4]++;
                        }
                        case SOUTHWEST:{
                            dirWeights[0]++;
                            dirWeights[1]++;
                            dirWeights[5]++;
                        }
                        default: dirWeights[0]++;
                    }
                }
            }
            Map<Direction,Integer> aDir = new TreeMap<Direction,Integer>();
/*
            aDir.put(Direction.NORTH,dirWeights[0]);
            aDir.put(Direction.EAST,dirWeights[1]);
            aDir.put(Direction.SOUTH,dirWeights[2]);
            aDir.put(Direction.WEST,dirWeights[3]);
            aDir.put(Direction.NORTHWEST,dirWeights[4]);
            aDir.put(Direction.NORTHEAST,dirWeights[5]);
            aDir.put(Direction.SOUTHEAST,dirWeights[6]);
            aDir.put(Direction.SOUTHWEST,dirWeights[7]);
 */

            aDir.put(Direction.SOUTH,dirWeights[0]);
            aDir.put(Direction.WEST,dirWeights[1]);
            aDir.put(Direction.NORTH,dirWeights[2]);
            aDir.put(Direction.EAST,dirWeights[3]);
            aDir.put(Direction.SOUTHEAST,dirWeights[4]);
            aDir.put(Direction.SOUTHWEST,dirWeights[5]);
            aDir.put(Direction.NORTHWEST,dirWeights[6]);
            aDir.put(Direction.NORTHEAST,dirWeights[7]);

            SortedSet<Map.Entry<Direction, Integer>> uDir = entriesSortedByValues(aDir);
            for(Map.Entry<Direction,Integer> e : uDir){
                if(tryMove(e.getKey())){
                    System.out.println(rc.getType() +" moved " +e.getKey() +" because it had a value of " +e.getValue());
                }
            }
            return;
        }else{
            while(true){
                if(tryMove(randomDirection())) return;
            }
        }
    }
    static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
                new Comparator<Map.Entry<K,V>>() {
                    @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        int res = e1.getValue().compareTo(e2.getValue());
                        return res != 0 ? res : 1;
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

    static Direction GetDirection(MapLocation start, MapLocation end)
    {

        double dx = end.x - start.x;
        double dy = end.y - start.y;


        if (Math.abs(dx) > Math.abs(dy))
        {
            if (Math.abs(dy / dx) <= tan_Pi_div_8)
            {
                return dx > 0 ? Direction.EAST : Direction.WEST;
            }

            else if (dx > 0)
            {
                return dy > 0 ? Direction.NORTHEAST : Direction.SOUTHEAST;
            }
            else
            {
                return dy > 0 ? Direction.NORTHWEST : Direction.SOUTHWEST;
            }
        }

        else if (Math.abs(dy) > 0)
        {
            if (Math.abs(dx / dy) <= tan_Pi_div_8)
            {
                return dy > 0 ? Direction.NORTH : Direction.SOUTH;
            }
            else if (dy > 0)
            {
                return dx > 0 ? Direction.NORTHEAST : Direction.NORTHWEST;
            }
            else
            {
                return dx > 0 ? Direction.SOUTHEAST : Direction.SOUTHWEST;
            }
        }
        else
        {
            System.out.println("No direction found");
            return Direction.NORTH;
        }


    }

    
    //attempts to move, true if success.
    static boolean tryMove(Direction dir) throws GameActionException {
        if (rc.canMove(dir)) {
            rc.move(dir);
            return true;
        } else return false;
    }
}
