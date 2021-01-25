package Kyle;

import battlecode.common.*;

import java.util.*;

public class Movement {
    public RobotController rc;
    public Direction current;
    public Scanner scan;

    public MapLocation adjLoc;
    public MapLocation base;

    public ArrayList<MapLocation> travel;

    public int depth;

    public static final Direction[] directions = {
            Direction.NORTH,
            Direction.NORTHEAST,
            Direction.EAST,
            Direction.SOUTHEAST,
            Direction.SOUTH,
            Direction.SOUTHWEST,
            Direction.WEST,
            Direction.NORTHWEST,
    };

    public Movement(RobotController r) throws GameActionException {
        rc = r;
        adjLoc = AdjustLocation(rc.getLocation());
        current = randomDirection();
        base = rc.getLocation();
        travel = new ArrayList<>();
        scan = new Scanner(rc);

        depth = 3;
        if (rc.getType() == RobotType.SLANDERER) {
            depth = 1;
        }
    }

    public void TryRandomMove() throws GameActionException{
        //TryFindBetterMove();
        if(FindRandomMove()){
            FinishMove();
        }
    }

    public void OptimizeDirection() throws GameActionException {
        double[] cost = new double[3];

        SortedMap<Direction,Double> path = new TreeMap<Direction,Double>();
        path.put(current,0.0);
        path.put(current.rotateLeft(),0.0);
        path.put(current.rotateRight(),0.0);

        for(Map.Entry<Direction,Double> e : path.entrySet()){
            Direction dir = e.getKey();
            MapLocation m = rc.getLocation().add(dir);
            for(int j=0;j<2;j++){
                if(rc.onTheMap(m)){
                    e.setValue(e.getValue() + rc.sensePassability(m));
                }else{
                    e.setValue(e.getValue() - 10);
                }
            }
        }
        LinkedHashMap<Direction, Double> reverseSortedMap = new LinkedHashMap<>();
        path.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(),x.getValue()));

        for (Map.Entry<Direction, Double> e : reverseSortedMap.entrySet()) {
            if (rc.canMove(e.getKey())) {
                current = e.getKey();
                break;
            }
        }
        TryRandomMove();
    }

    public void JustMove() throws GameActionException{
        if(FindRandomMove()){
            FinishMove();
        }
    }

    public boolean FindRandomMove() throws GameActionException {
        if(!rc.canMove(current)){
            if(!rc.onTheMap(rc.getLocation().add(current))){
                current = current.opposite();
                for(int i=0;i<8;i++){
                    current = current.rotateRight();
                    if(rc.canMove(current)) return true;
                }
            }else{
                for(int i=0;i<8;i++){
                    current = current.rotateRight();
                    if(rc.canMove(current)) return true;
                }
            }
        }
        return rc.canMove(current);
    }

    public void CheckAllPaths(MapLocation r) throws GameActionException{
        double[] cost = new double[8];

        SortedMap<Direction,Double> path = new TreeMap<Direction,Double>();
        for(Direction d : directions){
            path.put(d,0.0);
        }

        for(Map.Entry<Direction,Double> e : path.entrySet()){
            Direction dir = e.getKey();
            MapLocation m = rc.getLocation();
            for(int j=0;j<depth;j++){
                if(rc.onTheMap(m.add(dir))){
                    if(rc.isLocationOccupied(m.add(dir))){
                        int dist = rc.getLocation().add(dir).distanceSquaredTo(base) - rc.getLocation().distanceSquaredTo(base);
                        e.setValue(e.getValue() - 100);
                    }else{
                        int dist = rc.getLocation().add(dir).distanceSquaredTo(base) - rc.getLocation().distanceSquaredTo(base);
                        e.setValue(e.getValue() + (dist * (rc.sensePassability(m.add(dir))) ));
                    }
                }else{
                    e.setValue(e.getValue()-(200));
                }
                m = m.add(dir);
            }
        }

        LinkedHashMap<Direction, Double> reverseSortedMap = new LinkedHashMap<>();
        path.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(),x.getValue()));

        for (Map.Entry<Direction, Double> e : reverseSortedMap.entrySet()) {
            if (rc.canMove(e.getKey())) {
                current = e.getKey();
                break;
            }
        }

    }

    public void FindPathToGoal(MapLocation goal) throws GameActionException {
        double[] cost = new double[8];

        SortedMap<Direction,Double> path = new TreeMap<Direction,Double>();
        for(Direction d : directions){
            path.put(d,0.0);
        }

        for(Map.Entry<Direction,Double> e : path.entrySet()){
            Direction dir = e.getKey();
            MapLocation m = rc.getLocation();
            for(int j=0;j<depth;j++){
                if(rc.onTheMap(m.add(dir))){
                    double dist = rc.getLocation().distanceSquaredTo(goal) - rc.getLocation().add(dir).distanceSquaredTo(base);
                    e.setValue(e.getValue() + ((double) (1/dist) * (rc.sensePassability(m.add(dir))) ));
                }
            }
        }

        LinkedHashMap<Direction, Double> reverseSortedMap = new LinkedHashMap<>();
        path.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(),x.getValue()));

        for (Map.Entry<Direction, Double> e : reverseSortedMap.entrySet()) {
            if (rc.canMove(e.getKey())) {
                current = e.getKey();
                break;
            }
        }

    }

    public void TryFindBetterMove() throws GameActionException{

        Direction temp = current;
        Direction stored = current;
        double cost = 0;
        int dist = rc.getLocation().distanceSquaredTo(base);

        /*
        * for(int i=0;i<2;i++){
            temp = temp.rotateLeft();
            if(rc.onTheMap(rc.getLocation().add(temp))){
                if(cost < rc.sensePassability(rc.getLocation().add(temp))){
                    if(base.distanceSquaredTo(rc.getLocation().add(temp)) > dist){
                        stored = temp;
                        cost = rc.sensePassability(rc.getLocation().add(temp));
                    }
                }
            }
        }
        for(int i=0;i<2;i++){
            temp = temp.rotateRight();
            if(rc.onTheMap(rc.getLocation().add(temp))){
                if(cost < rc.sensePassability(rc.getLocation().add(temp))){
                    if(base.distanceSquaredTo(rc.getLocation().add(temp)) > dist){
                        stored = temp;
                        cost = rc.sensePassability(rc.getLocation().add(temp));
                    }
                }
            }
         */

        for(int i=0;i<8;i++){
            temp = temp.rotateRight();
            if(rc.onTheMap(rc.getLocation().add(temp))){
                if(cost < rc.sensePassability(rc.getLocation().add(temp))){
                    if(base.distanceSquaredTo(rc.getLocation().add(temp)) > dist){
                        stored = temp;
                        cost = rc.sensePassability(rc.getLocation().add(temp));
                    }
                }else if(!travel.contains(rc.getLocation().add(temp))){
                    stored = temp;
                    cost = rc.sensePassability(rc.getLocation().add(temp));
                }
            }
        }

        System.out.println(stored.toString());
        current = stored;

    }

    public void FinishMove() throws GameActionException {
        if(rc.canMove(current) && !rc.isLocationOccupied(rc.getLocation().add(current))){
            travel.add(rc.getLocation());
            try{
                rc.move(current);
                travel.add(rc.getLocation().subtract(current));
            }catch(GameActionException e){
                System.out.println("I found a bad move");
            }
            adjLoc = adjLoc.add(current);
        }
    }

    public static MapLocation AdjustLocation(MapLocation uAdj){
        int val = 128*(uAdj.x%128) + (uAdj.y%128);
        return new MapLocation((val/128),(val%128));
    }

    public static Direction randomDirection(){
        return directions[(int) (Math.random() * directions.length)];
    }

    public static Direction randomRotation(Direction d){
        int[] val = new int[]{0,1,2,3,4,5,6,7};
        int sel = new Random().nextInt(val.length);
        for(int i=0;i<val[sel];i++) d = d.rotateRight();
        return d;
    }
}
