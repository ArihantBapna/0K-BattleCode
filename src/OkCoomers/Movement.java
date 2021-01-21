package OkCoomers;

import battlecode.common.*;
import scala.util.Try;

public class Movement {

    public RobotController rc;
    public Direction current;
    public MapLocation adjLoc;
    public MapLocation mlEC;
    public int EC;

    public Scan scan;
    public Communication comm;

    public Movement(RobotController r) throws GameActionException {
        rc = r;
        scan = new Scan(rc);
        comm = new Communication(rc,this);
        current = Constants.randomDirection();
        DoInitialSetup();
    }

    /*
    * current is the direction that the bot is "facing" and it will try to move there
    * Every bot has its own movement class which extends Movement, over there we will just set current using directionTo the goal or random
    * FindOptimalMove first off tries to move directly in the current direction, if it fails, then it checks the +2 and -2 directions and picks the one with the lowest pass score
    * If FindOptimalMove succeeds it moves the bot, and updates its adjLoc (adjusted Location) which is the x/128 and y%128 coords
    * If it fails, then nothing happens.
     */

    public void FindBestMove() throws GameActionException{
        if(FindOptimalMove()) FinishMove();
        else TryRandomMove();
    }

    public boolean FindOptimalMove() throws GameActionException {
        Direction temp = current;
        double cost = rc.sensePassability(rc.getLocation().add(current));
        if(rc.canMove(temp)){
            return true;
        }else{
            //If bot has cd reset, then do the rest of it, otherwise just return false
            if(rc.isReady()){
                cost = 0;
            }else{
                return false;
            }
        }

        //Check East pass
        for(int i=0;i<2;i++){
            temp = temp.rotateRight();
            double tempCost = rc.sensePassability(rc.getLocation().add(temp));
            if(cost < tempCost && rc.canMove(temp)){
                cost = tempCost;
            }
        }

        //Check West pass
        for(int i=0;i<2;i++){
            temp = temp.rotateLeft();
            double tempCost = rc.sensePassability(rc.getLocation().add(temp));
            if(cost < tempCost && rc.canMove(temp)){
                cost = tempCost;
            }
        }

        //Ensure it found a path
        if(cost != 0 ){
            current = temp;
            return rc.canMove(current);
        }

        //If it can't move
        return false;
    }

    public void FinishMove() throws GameActionException{
        if(rc.canMove(current) && !rc.isLocationOccupied(rc.getLocation().add(current))){
            rc.move(current);
            adjLoc = adjLoc.add(current);
        }
    }

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
        }
        return rc.canMove(current);
    }


    /*
    * If the bot was spawned by an EC it gets the id and the coords of the EC
    * If it was a converted bot then it sets the EC id to 0 and coords will be null
    * Poli's are the only unit that can be "converted" so they will have a PoliConvertedMove to accompany it
     */


    public void DoInitialSetup() {
        EC = 0; //In case the bot is a converted one and not a spawned one
        int id = 0;
        for(RobotInfo r : rc.senseNearbyRobots(-1,rc.getTeam())){
            if(r.getType().equals(RobotType.ENLIGHTENMENT_CENTER)){
                id = r.getID();
                mlEC = r.getLocation();
                current = r.getLocation().directionTo(rc.getLocation());
            }
        }
        EC = id;
        adjLoc = Constants.AdjustLocation(rc.getLocation());
    }
}
