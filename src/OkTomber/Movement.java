package OkTomber;

import battlecode.common.*;

public class Movement {
    private RobotController rc;
    private int mode;
    private boolean moved;
    private int EC;

    public Direction current;
    public MapLocation goal;
    public MapLocation adjLoc;
    public Scan sc;

    public Movement(RobotController r) throws GameActionException {
        rc = r;
        mode = 0;
        moved = false;
        sc = new Scan(rc);

        DoInitialSetup();
        current = Constants.randomDirection();
    }

    public void DoMove() throws GameActionException {
        moved = false;

        RunScan();
    }

    private void RunScan() throws GameActionException {
        int c = sc.SearchForEC();
        int cd = sc.cd;
        if(c>=2){
            if(TrySetGoal()){
                if(c == 3){
                    FindAdjustedPathToGoal();
                }else{
                    TryRandomMove();
                }
            }else{
                TryRandomMove();
            }
        }else{
            if(c!=1){
                if(!Constants.AdjustLocation(rc.getLocation()).isAdjacentTo(sc.goal)){
                    goal = sc.goal;
                    FindPathToGoal();
                    AlertOthers();
                }
            }
        }
    }


    private void DoInitialSetup() throws GameActionException {
        EC = sc.GetECid();
        UpdateAdjLoc();
    }

    private void FinishMove() throws GameActionException{
        rc.move(current);
        UpdateAdjLoc();
    }

    private void UpdateAdjLoc(){
        int f = 128*(rc.getLocation().x%128) + (rc.getLocation().y%128);
        adjLoc = new MapLocation((f/128),(f%128));
    }

    private void AlertOthers() throws GameActionException{
        int flag = (128*(goal.x % 128)) + (goal.y % 128);
        rc.setFlag(FlagHandle.SetLocFlagInt(flag,"20"));
    }

    private void FindAdjustedPathToGoal() throws  GameActionException{
        current = Constants.AdjustLocation(rc.getLocation()).directionTo(goal);
        TryRandomMove();
    }

    private void FindPathToGoal() throws GameActionException{
        current = rc.getLocation().directionTo(goal);
        TryRandomMove();
    }


    private void TryRandomMove() throws GameActionException{
        if(FindRandomMove()){
            FinishMove();
        }
    }

    private boolean FindRandomMove() throws GameActionException {
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

    private boolean TrySetGoal() throws GameActionException{
        if(rc.canGetFlag(EC)){
            int flag = rc.getFlag(EC);
            if(String.valueOf(flag).length() > 1 && String.valueOf(flag).substring(0,2).equals("30")){
                String f = String.valueOf(flag);
                String map = f.substring(2);
                int m = Integer.parseInt(map);

                int x = m/128;
                int y = m%128;

                goal = new MapLocation(x,y);
                mode = 1;

                rc.setFlag(FlagHandle.SetLocFlagInt(m,"20"));

                return true;
            }
        }
        return false;
    }

}
