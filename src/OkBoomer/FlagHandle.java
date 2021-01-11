package OkBoomer;

import battlecode.common.*;

public class FlagHandle {
    public static int adjLocWrite(MapLocation m){
        int x = m.x;
        int y = m.y;
        String xc = String.format("%03d",x);
        String yc = String.format("%03d",y);
        String preFlag = "1" + xc + yc;
        return Integer.parseInt(preFlag);
    }
    public static int adjLocWrite(MapLocation m, String pre){
        int x = m.x;
        int y = m.y;
        String xc = String.format("%03d",x);
        String yc = String.format("%03d",y);
        String preFlag = pre + xc + yc;
        return Integer.parseInt(preFlag);
    }

    public static int[] DecodeCordFlag(int flag){
        String f = String.valueOf(flag);
        int h = Integer.parseInt(f.substring(0,1));
        int x = Integer.parseInt(f.substring(1,4));
        int y = Integer.parseInt(f.substring(4));
        return new int[]{h,x,y};
    }

    public static MapLocation ReadCordFlag(int[] a){
        return new MapLocation(a[1],a[2]);
    }
}
