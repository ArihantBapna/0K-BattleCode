package Final0k;

import battlecode.common.*;
import java.util.*;

public class Movement {

    public RobotController rc;
    public Direction current;
    public Scan scan;

    public int EC;
    public MapLocation adjLoc;
    public MapLocation base;

    public Movement(RobotController r) throws GameActionException {
        rc = r;
        scan = new Scan(rc);
        current = Constants.randomDirection();
        base = rc.getLocation();
    }

    public void TryRandomMove() throws GameActionException{
        if(FindRandomMove()) rc.move(current);
    }

    public boolean FindRandomMove() throws GameActionException {
        Direction d;
        if(!rc.canMove(current)){
            if(!rc.onTheMap(rc.getLocation().add(current))){
                current = current.opposite();

                for(int i=0;i<8;i++){
                    d = GetDirectionFromI(i, current);
                    if(rc.canMove(d)) {
                        current = d;
                        return true;
                    }
                }
            }else{
                for(int i=0;i<8;i++){
                    d = GetDirectionFromI(i, current);
                    if(rc.canMove(d)){
                        current = d;
                        return true;
                    }
                }
            }
        }
        return rc.canMove(current);
    }

    public Direction GetDirectionFromI(int i, Direction d) throws GameActionException{
        switch (i){
            case 0:{
                return d.rotateLeft();
            }
            case 1:{
                return d.rotateRight();
            }
            case 2:{
                d = d.rotateRight();
                return d.rotateRight();
            }
            case 3:{
                d = d.rotateLeft();
                return d.rotateLeft();
            }
            case 4:{
                d = d.rotateRight();
                d = d.rotateRight();
                return d.rotateRight();
            }
            case 5:{
                d = d.rotateLeft();
                d = d.rotateLeft();
                return d.rotateLeft();
            }
            case 6:{
                d = d.rotateRight();
                d = d.rotateRight();
                d = d.rotateRight();
                return d.rotateRight();
            }
            case 7:{
                d = d.rotateLeft();
                d = d.rotateLeft();
                d = d.rotateLeft();
                return d.rotateLeft();
            }
            default:{
                return Constants.randomRotation(d);
            }
        }
    }

    public void CheckAllPaths(MapLocation r) throws GameActionException{
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
        TryRandomMove();
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

}
