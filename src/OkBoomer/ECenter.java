package OkBoomer;

import battlecode.common.*;

import java.util.*;

public class ECenter {

    public RobotController rc;
    public int idEC = 0;
    public MapLocation adjLoc;
    public ArrayList<Integer> robotId;
    public double infPR;

    private int modeEC;
    private List<Double> probDist;
    private List<RobotType> robType;
    private ArrayList<Integer> stuckId;

    public ECenter(RobotController rc){
        this.rc = rc;
        robotId = new ArrayList<>();
        stuckId = new ArrayList<>();

        probDist = Arrays.asList(0.33,0.33,0.33);
        robType = Arrays.asList(Constants.spawnableRobot);
        infPR = 0;
        modeEC = 0;
    }

    public void doRun() throws GameActionException {
        boolean built = false;

        infPR = Math.sqrt(RobotPlayer.turnCount) * 0.2;

        String flag = String.valueOf(rc.getFlag(rc.getID())).substring(0,1);
        if(flag.equals("3")){
            probDist = Arrays.asList(0.6,0.2,0.2);
        }else{
            probDist = Arrays.asList(0.5,0.2,0.3);
        }
        RobotType toBuild = GetRandomRobot();
        int influence = (int) Math.floor(0.8*infPR);
        switch(toBuild){
            case POLITICIAN:{
                if(influence < 50) influence = 50;
                break;
            }
            case SLANDERER:{
                if(influence < 41) influence = 41;
                break;
            }
            case MUCKRAKER:{
                if(influence < 10) influence = 10;
                break;
            }
        }

        for (Direction dir : Constants.directions) {
            if (rc.canBuildRobot(toBuild, dir, influence)) {
                rc.buildRobot(toBuild, dir, influence);
                RobotInfo r = rc.senseRobotAtLocation(rc.getLocation().add(dir));
                int id = r.getID();
                robotId.add(id);
                built = true;
            }
        }

        if(rc.canBid((int) Math.floor(infPR*0.2)) && infPR >= 10){
            rc.bid((int) Math.floor(infPR*0.2));
        }


        RunChecksums();
    }

    private RobotType GetRandomRobot(){
        TreeMap<Double,RobotType> map = new TreeMap<>();
        double total = 0.0d;
        for (int i = 0; i < robType.size(); i++) {
            map.put(total += probDist.get(i), robType.get(i));
        }
        double value = Math.random();
        return map.ceilingEntry(value).getValue();
    }

    private static RobotType randomSpawnableRobotType() {
        return Constants.spawnableRobot[(int) (Math.random() *  Constants.spawnableRobot.length)];
    }

    private void RunChecksums() throws GameActionException{
        switch(modeEC){
            case 0:{
                if(CheckSelfFlag()){
                    CheckBotFlags();
                }
                break;
            }
            case 1:{
                if(stuckId.size() < 1){
                    CheckResetFlags();
                }else{
                    CheckStuck();
                }
            }
        }
    }

    private void CheckStuck() throws GameActionException{
        Iterator<Integer> i = stuckId.iterator();
        while(i.hasNext()){
            int id = i.next();
            if(rc.canGetFlag(id)){
                String header = "";
                try{
                    header = String.valueOf(rc.getFlag(id)).substring(0,1);
                }catch(GameActionException e){
                    e.printStackTrace();
                }
                if(header.equals("4")){
                    rc.setFlag(Integer.parseInt("4" + String.valueOf(rc.getFlag(id)).substring(1)));
                    modeEC = 0;
                    i.remove();
                    return;
                }
            }
        }
    }

    private void CheckResetFlags() throws GameActionException{
        Iterator<Integer> i = robotId.iterator();
        while(i.hasNext()){
            int id = i.next();
            if(rc.canGetFlag(id)){
                String flag = "";
                String header = "";
                try{
                    flag = String.valueOf(rc.getFlag(id)).substring(1);
                    header = String.valueOf(rc.getFlag(id)).substring(0,1);
                }catch (GameActionException e){
                    e.printStackTrace();
                }
                if(header.equals("4")){
                    rc.setFlag(Integer.parseInt("4" + flag));
                    modeEC = 0;
                    return;
                }else if(header.equals("5")){
                    stuckId.add(id);
                    rc.setFlag(Integer.parseInt("5" + flag));
                }
            }else i.remove();
        }
    }

    private void CheckBotFlags() throws GameActionException{
        Iterator<Integer> i = robotId.iterator();
        int count = 0;
        while(i.hasNext() && (count <= 100)){
            int id = i.next();
            if(rc.canGetFlag(id)){
                String flag = String.valueOf(rc.getFlag(id)).substring(1);
                String header = String.valueOf(rc.getFlag(id)).substring(0,1);
                if(header.equals("2")){
                    rc.setFlag(Integer.parseInt("3" + flag));
                    modeEC = 1;
                    return;
                }
            }else i.remove();
            count++;
        }
    }

    private boolean CheckSelfFlag() throws GameActionException{
        String flag = String.valueOf(rc.getFlag(rc.getID())).substring(0,1);
        return flag.equals("0") || flag.equals("4");
    }

}
