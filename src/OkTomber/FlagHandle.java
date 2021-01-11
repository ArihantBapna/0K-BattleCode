package OkTomber;

import battlecode.common.MapLocation;
import javafx.util.Pair;

public class FlagHandle {

    public static int SetLocFlagInt(int f, String pre){
        String flag = pre + String.format("%05d",f);
        int a;
        try{
            a = Integer.parseInt(flag);
        }catch(NumberFormatException e){
            return 0;
        }
        return a;
    }

    public static Pole ReadLocFlag(int flag){
        String f = String.valueOf(flag);
        String h = f.substring(0,2);
        int mapL = Integer.parseInt(f.substring(2));

        MapLocation map = new MapLocation((mapL/128),(mapL%128));
        Pole p = new Pole(map,h);
        return p;
    }

}

