package OkZoomer;

import battlecode.common.*;

import java.util.Objects;

public class Movement {
    public RobotController rc;
    public Direction current;
    public MapLocation adjLoc;
    public int EC;
    public MapLocation mlEC;

    public Movement(RobotController r) throws GameActionException {
        rc = r;
        current = Constants.randomDirection();
        DoInitialSetup();
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

    public void FinishMove() throws GameActionException{
        rc.move(current);
        adjLoc = Constants.AdjustLocation(rc.getLocation());
    }

}
