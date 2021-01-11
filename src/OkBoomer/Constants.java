package OkBoomer;

import battlecode.common.*;

import java.util.Random;

public class Constants {
    public static final Direction[] directions = {
            Direction.NORTH,
            Direction.NORTHEAST,
            Direction.EAST,
            Direction.SOUTHEAST,
            Direction.SOUTH,
            Direction.SOUTHWEST,
            Direction.WEST,
            Direction.NORTHWEST,
    };
    public static final RobotType[] spawnableRobot = {
            RobotType.POLITICIAN,
            RobotType.SLANDERER,
            RobotType.MUCKRAKER,
    };

    public static Direction randomDirection(){
        return directions[(int) (Math.random() * directions.length)];
    }

    public static Direction randomRotation(Direction d){
        int[] val = new int[]{0,1,2,3,4,5,6,7};
        int sel = new Random().nextInt(val.length);
        for(int i=0;i<val[sel];i++) d = d.rotateRight();
        return d;
    }
}
