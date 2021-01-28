package OkZoomer2;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

import java.util.Iterator;

public class ECenterComm {

    public RobotController rc;

    public ECenterComm(RobotController r) {
        rc = r;
    }

    public Place ReadMuckFlags() throws GameActionException{
        Place p = new Place();
        int selfFlag = rc.getFlag(rc.getID());
        String h = String.valueOf(selfFlag).substring(0,1);
        Iterator<Integer> it = ECenter.muck.iterator();
        while(it.hasNext()){
            int id = it.next();
            if(rc.canGetFlag(id)){
                int flag = rc.getFlag(id);
                if(h.equals("0")){
                    if(String.valueOf(flag).substring(0,1).equals("2")){
                        p.head = "2";
                        p.loc = Integer.parseInt(String.valueOf(flag).substring(1));
                    }
                }else if(h.equals("3")){
                    if(String.valueOf(flag).substring(0,1).equals("4")){
                        p.head = "4";
                        p.loc = Integer.parseInt(String.valueOf(flag).substring(1));
                    }
                }
            }else{
                it.remove();
            }
        }
        return p;
    }


}
