package OkZoomer2;

import battlecode.common.*;

import java.util.*;

public class Movement {
    public RobotController rc;
    public Direction current;
    public MapLocation adjLoc;
    public int EC;
    public MapLocation mlEC;
    public MapLocation base;

    public Movement(RobotController r) throws GameActionException {
        rc = r;
        current = Constants.randomDirection();
        mlEC = rc.getLocation();
        DoInitialSetup();
        base = rc.getLocation();
    }

    public void DoInitialSetup() {
        int id = 0;
        for(RobotInfo r : rc.senseNearbyRobots(2,rc.getTeam())){
            if(r.getType().equals(RobotType.ENLIGHTENMENT_CENTER)){
                id = r.getID();
                mlEC = r.getLocation();
                current = r.getLocation().directionTo(rc.getLocation());
            }
        }
        EC = id;
        adjLoc = Constants.AdjustLocation(rc.getLocation());
    }


    //Sequence to move randomly
    public void TryRandomMove() throws GameActionException{
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
        }else{
            return true;
        }
        return false;
    }

    public void CheckAllPaths(MapLocation r) throws GameActionException{
        if(rc.getRoundNum() % 67 == 0) base = rc.getLocation();
        double[] cost = new double[8];

        SortedMap<Direction,Double> path = new TreeMap<Direction,Double>();
        for(Direction d : Constants.directions){
            path.put(d,0.0);
        }

        for(Map.Entry<Direction,Double> e : path.entrySet()){
            Direction dir = e.getKey();
            MapLocation m = rc.getLocation();
            for(int j=0;j<3;j++){
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

    public void OptimizeDirection(Direction d) throws GameActionException {
        double[] cost = new double[3];

        SortedMap<Direction,Double> path = new TreeMap<Direction,Double>();
        path.put(d,0.0);
        path.put(d.rotateLeft(),0.0);
        path.put(d.rotateRight(),0.0);

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

    public void FinishMove() throws GameActionException{
        rc.move(current);
        adjLoc = Constants.AdjustLocation(rc.getLocation());
    }

}
