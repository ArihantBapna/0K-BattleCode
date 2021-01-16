package OkTomber;

import battlecode.common.*;

import java.util.*;

public class ECenter {

    public RobotController rc;
    public ArrayList<Integer> robotId;
    public double infPR;

    private int modeEC;
    private int probMode;
    private List<Double> probDist;
    private List<RobotType> robType;
    private ArrayList<Integer> stuckId;

    public ECenter(RobotController rc){
        this.rc = rc;
        robotId = new ArrayList<>();
        stuckId = new ArrayList<>();

        probDist = Arrays.asList(0.45,0.45,0.1);
        robType = Arrays.asList(Constants.spawnableRobot);
        infPR = 0;
        modeEC = 0;
        probMode = 0;
    }

    public void doRun() throws GameActionException {

        if(RobotPlayer.turnCount % 2 != 0){
            DoSpawns();
        }
        if(RobotPlayer.turnCount >= 300){
            TryVote();
        }
        RunChecksums();
    }

    private int ThreatAssess() throws GameActionException {
        for(RobotInfo r : rc.senseNearbyRobots(-1,rc.getTeam().opponent())){
            if(r.getType().equals(RobotType.ENLIGHTENMENT_CENTER)){
                probDist = Arrays.asList(0.3,0.1,0.6);
                probMode = 1;
                return 50;
            }
        }
        return 25;
    }

    private void TryVote() throws GameActionException {
        int inf = rc.getInfluence();
        int bid = (int) (0.3 * inf);
        if(rc.canBid(bid)){
            rc.bid(bid);
        }
    }

    private void DoSpawns() throws GameActionException {
        if(RobotPlayer.turnCount == 1 && rc.getInfluence() >= 25){
            DoRandomSpawns(RobotType.POLITICIAN,ThreatAssess());
        }else if(RobotPlayer.turnCount == 3 && rc.getInfluence() >= 107){
            DoRandomSpawns(RobotType.SLANDERER,107);
        }else if(RobotPlayer.turnCount == 5 && rc.getInfluence() >= 1){
            DoRandomSpawns(RobotType.MUCKRAKER,1);
        }else if(RobotPlayer.turnCount > 300){
            probDist = Arrays.asList(0.56,0.34,0.1);
            DoRandomSpawns(GetRandomRobot());
        }else{
            if(probMode == 1){
                int con = 0;
                for(RobotInfo r : rc.senseNearbyRobots(2,rc.getTeam().opponent())){
                    con += r.getConviction();
                }
                if(rc.getInfluence() > con){
                    DoRandomSpawns(RobotType.POLITICIAN,con);
                }
                else if(RobotPlayer.turnCount > 100){
                    probDist = Arrays.asList(0.45,0.45,0.1);
                }
                else{
                    return;
                }
            }
            DoRandomSpawns(GetRandomRobot());
        }
    }


    private void DoRandomSpawns(RobotType toBuild, int i) throws GameActionException{
        for (Direction dir : Constants.directions) {
            if (rc.canBuildRobot(toBuild, dir, i)) {
                rc.buildRobot(toBuild, dir, i);
                RobotInfo r = rc.senseRobotAtLocation(rc.getLocation().add(dir));
                int id = r.getID();
                robotId.add(id);
            }
        }
    }

    private void DoRandomSpawns(RobotType toBuild) throws GameActionException{
        int influence = rc.getInfluence();
        int inf = 0;
        switch(toBuild){
            case POLITICIAN:{
                if(influence < 100) inf = 1;
                else inf = (int) (0.25*influence);
                break;
            }
            case SLANDERER:{
                inf = (int) (0.7*influence);
                break;
            }
            case MUCKRAKER:{
                if(RobotPlayer.turnCount > 100) inf = (int) (0.3*influence);
                else inf = 1;
                break;
            }
        }
        for (Direction dir : Constants.directions) {
            if (rc.canBuildRobot(toBuild, dir, inf)) {
                rc.buildRobot(toBuild, dir, inf);
                RobotInfo r = rc.senseRobotAtLocation(rc.getLocation().add(dir));
                int id = r.getID();
                robotId.add(id);
            }
        }
    }

    private RobotType GetRandomRobot(){
        return getRobotType(robType, probDist);
    }
    private RobotType getRobotType(List<RobotType> robType, List<Double> probDist) {
        double p = Math.random();
        double total = 0.0d;
        TreeMap<Double, RobotType> map = new TreeMap<>();
        for (int i = 0; i < robType.size(); i++) {
            map.put(total += probDist.get(i), robType.get(i));
        }
        return map.ceilingEntry(p).getValue();
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

    private void DoVotes() throws GameActionException{
        if(rc.getRoundNum() > 200){
            if(rc.canBid((int) (rc.getInfluence()*0.2))){
                rc.bid((int) (rc.getInfluence()*0.2));
            }
        }else{
            if(rc.canBid(2)){
                rc.bid(2);
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
