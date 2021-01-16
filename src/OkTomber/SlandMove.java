package OkTomber;

import battlecode.common.*;

public class SlandMove{

    public RobotController rc;
    public int mode;
    public boolean moved;
    public int EC;

    public Direction current;
    public MapLocation goal;
    public MapLocation adjLoc;
    public Scan sc;

    public SlandMove(RobotController r) throws GameActionException {
        rc = r;
        mode = 0;
        moved = false;
        sc = new Scan(rc);

        DoInitialSetup();
        current = Constants.randomDirection();
    }

    public void DoSlandMove() throws GameActionException {
        RunSlandScan();
    }



    public void DoInitialSetup() throws GameActionException {
        EC = sc.GetECid();
        UpdateAdjLoc();
    }

    public void UpdateAdjLoc(){
        int f = 128*(rc.getLocation().x%128) + (rc.getLocation().y%128);
        adjLoc = new MapLocation((f/128),(f%128));
    }

    public void RunSlandScan() throws GameActionException {
        for(RobotInfo r : rc.senseNearbyRobots(-1,rc.getTeam().opponent())){
            if(r.getType().equals(RobotType.MUCKRAKER)){
                current = r.getLocation().directionTo(rc.getLocation());
            }
        }
        TryRandomMove();
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
        }else{
            return true;
        }
        return false;
    }

    public void FinishMove() throws GameActionException{
        rc.move(current);
        UpdateAdjLoc();
    }

}
