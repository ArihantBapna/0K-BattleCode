package OkBoomer;

import battlecode.common.*;

import java.util.ArrayList;

public class Movement {
    private RobotController rc;
    private int mode;
    private boolean moved = false;
    private int EC;

    public Direction current;
    public Scanner s;
    public MapLocation goal;
    public MapLocation adjLoc;
    public RobotInfo rEC;
    public ArrayList<MapLocation> blacklist;

    public Movement(RobotController r, MapLocation adj, int idEC, RobotInfo r1){
        rc = r;
        mode = 0;
        goal = new MapLocation(0,0);
        adjLoc = adj;
        EC = idEC;
        rEC = r1;
        blacklist = new ArrayList<>();
        s = new Scanner(rc, adj);
        current = Constants.randomDirection();
    }

    public void DoMove() throws GameActionException {
        moved = false;

        if(mode == 0){
            CheckForEC();
            if(mode == 0){
                TrySetGoal();
            }
        }else if(mode == 1){
            if(String.valueOf(rc.getFlag(rc.getID())).substring(0,1).equals("2")){
                TrySetReset();
                TryFindBlackList();
            }
        }

        if(rc.getCooldownTurns() < 1){
            switch(mode){
                case 0: SearchMove(); break;
                case 1: AttackMove(); break;
                case 2: WaitConvert(); break;
            }
        }
    }

    private void WaitConvert() throws GameActionException {
        RobotInfo[] allR = rc.senseNearbyRobots(1,rc.getTeam());
        for(RobotInfo r : allR){
            if(r.getType().equals(RobotType.ENLIGHTENMENT_CENTER)){
                mode = 0;
                rc.setFlag(FlagHandle.adjLocWrite(goal,"4"));
            }
        }
    }

    private void CheckForEC() throws GameActionException {
        if(s.SearchEnemyEC(adjLoc, blacklist)){
            mode = 1;
            goal = s.goal;
        }
    }


    private void UpdateAdjLoc() throws GameActionException{
        adjLoc = adjLoc.add(current);
        String f = String.valueOf(rc.getFlag(rc.getID()));
        if(f.substring(0,1).equals("1")){
            rc.setFlag(FlagHandle.adjLocWrite(adjLoc));
        }
    }


    private void AttackMove() throws GameActionException {
        if(!ecSurrounded()){
            if(isNotNextTo()){
                current = adjLoc.directionTo(goal);
                if(!rc.canMove(current)){
                    for(int i=0;i<8;i++){
                        current = current.rotateRight();
                        if(rc.canMove(current)){
                            moved = true;
                            break;
                        }
                    }
                }else{
                    moved = true;
                }
                if(moved){
                    rc.move(current);
                    UpdateAdjLoc();
                }
            }else{
                mode = 2;
            }

        }else{
            if(isNotNextTo()){
                mode = 0;
            }else{
                mode = 2;
                rc.setFlag(FlagHandle.adjLocWrite(goal,"5"));
            }
        }
    }

    private boolean ecSurrounded() throws GameActionException {
            int x = rc.getLocation().x + (goal.x - adjLoc.x);
            int y = rc.getLocation().y + (goal.y - adjLoc.y);
            MapLocation m = new MapLocation(x,y);

            boolean surround = true;
            for(Direction d : Constants.directions){
                if(rc.canDetectLocation(m.add(d))){
                    if(!rc.isLocationOccupied(m.add(d))){
                        surround = false;
                        break;
                    }
                }else{
                    surround = false;
                    break;
                }
            }
            return surround;
    }

    private boolean isNotNextTo() throws GameActionException{
        return !adjLoc.isAdjacentTo(goal);
    }

    private void SearchMove() throws GameActionException {
        if(!rc.canMove(current)){
            if(!rc.onTheMap(rc.getLocation().add(current))){
                for(int i=0;i<16;i++){
                    current = current.opposite();
                    current = Constants.randomRotation(current);
                    if(rc.canMove(current)) {
                        moved = true;
                        break;
                    }
                }
            }else{
                for(int i=0;i<8;i++){
                    current = current.rotateRight();
                    if(rc.canMove(current)) {
                        moved = true;
                        break;
                    }
                }
            }
        }else{
            moved = true;
        }
        if(moved){
            rc.move(current);
            UpdateAdjLoc();
        }
    }

    private void TryFindBlackList() throws GameActionException{
        if(rc.canGetFlag(EC)){
            int flag = rc.getFlag(EC);
            if(String.valueOf(flag).substring(0,1).equals("5")){
                String f = String.valueOf(flag);
                String map = f.substring(1);

                int x = Integer.parseInt(map.substring(0,3));
                int y = Integer.parseInt(map.substring(3));

                blacklist.add(new MapLocation(x,y));
            }
        }
    }

    private void TrySetGoal() throws GameActionException{
        if(rc.canGetFlag(EC)){
            int flag = rc.getFlag(EC);
            if(String.valueOf(flag).substring(0,1).equals("3")){
                String f = String.valueOf(flag);
                String map = f.substring(1);

                int x = Integer.parseInt(map.substring(0,3));
                int y = Integer.parseInt(map.substring(3));

                goal = new MapLocation(x,y);
                mode = 1;
                rc.setFlag(FlagHandle.adjLocWrite(goal,"2"));
            }
        }
    }
    private void TrySetReset() throws GameActionException{
        if(rc.canGetFlag(EC)){
            int flag = rc.getFlag(EC);
            if(String.valueOf(flag).substring(0,1).equals("4")){
                goal = new MapLocation(0,0);
                mode = 0;
                rc.setFlag(FlagHandle.adjLocWrite(adjLoc));
            }
        }
    }
}
