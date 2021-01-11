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

    public Movement(RobotController r, int idEC){
        rc = r;
        EC = idEC;
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
        if(c==2){
            if(TrySetGoal()){
                FindPathToGoal();
            }else{
                TryRandomMove();
            }
        }else{
            if(!Constants.AdjustLocation(rc.getLocation()).isAdjacentTo(sc.goal)){
                goal = sc.goal;
                FindPathToGoal();
                AlertOthers();
            }
        }
    }

    private void DoInitialSetup(){
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
        int flag = (128*(rc.getLocation().x % 128)) + (rc.getLocation().y % 128);
        rc.setFlag(FlagHandle.SetLocFlagInt(flag,"20"));
    }

    private void FindPathToGoal() throws GameActionException{
        current = adjLoc.directionTo(goal);
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
            if(String.valueOf(flag).substring(0,1).equals("3")){
                String f = String.valueOf(flag);
                String map = f.substring(1);
                int m = Integer.parseInt(map);

                int x = m/128;
                int y = m%128;

                goal = new MapLocation(x,y);
                mode = 1;

                int n = 128*(rc.getLocation().x % 128) + (rc.getLocation().y % 128);
                rc.setFlag(FlagHandle.SetLocFlagInt(n,"2"));

                return true;
            }
        }
        return false;
    }

}
