package OkZoomer2;

import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

import java.util.Iterator;

public class ECenterComm {

    public RobotController rc;

    public ECenterComm(RobotController r) {
        rc = r;
    }

    public void GetECFlag() throws GameActionException{
        Iterator<Integer> it = ECenter.muck.iterator();
        int f = 0;
        while(it.hasNext()){
            if(Clock.getBytecodesLeft() < 1){
                return;
            }
            int flag = it.next();
            if(!rc.canGetFlag(flag)){
                it.remove();
            }else{
                if(flag != 0){
                    f = flag;
                }
            }
        }
        rc.setFlag(f);
    }

}
