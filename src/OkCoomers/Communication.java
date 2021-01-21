package OkCoomers;

import battlecode.common.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class Communication {

    public RobotController rc;
    public Movement move;
    public Scan scan;

    public Communication(RobotController r,Movement m){
        rc = r;
        move = m;
        scan = m.scan;
    }
    public Communication(RobotController r, Scan sc){
        rc = r;
        scan = sc;
    }

    public boolean TryMuckFlag(ArrayList<Integer> muck) throws GameActionException{
        int val = 0;
        Iterator<Integer> i = muck.iterator();
        while(i.hasNext()){
            int id = i.next();
            if(rc.canGetFlag(id)){
                if(rc.getFlag(id) != 0){
                    val = rc.getFlag(id);
                    break;
                }
            }else{
                i.remove();
            }
        }
        if(val != 0 ){
            rc.setFlag(val);
            return true;
        }else{
            return false;
        }
    }

    public boolean TrySetFlag() throws GameActionException{
        if(scan.GetNearbyEC() > 0){
            rc.setFlag(Constants.EncodeLocation(scan.ec.get(0).getLocation()));
            return true;
        }
        return false;
    }

    public boolean TryReadFlag() throws GameActionException{
        int flag = 0;
        if(!(move.EC == 0)){
            if(rc.canGetFlag(move.EC)){
                flag = rc.getFlag(move.EC);
            }
        }
        if(flag != 0 ){
            move.current = Constants.AdjustLocation(rc.getLocation()).directionTo(Constants.DecodeLocation(flag));
            return true;
        }
        return false;
    }



}
