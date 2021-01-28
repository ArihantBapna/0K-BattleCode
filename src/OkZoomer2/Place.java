package OkZoomer2;

import battlecode.common.MapLocation;

public class Place {
    public int loc;
    public String head;

    public Place(){
        head = "";
        loc = 0;
    }

    public MapLocation GenerateLocation(){
        return new MapLocation(loc/128,loc%128);
    }

    public int GetEncodedFlag(String h){
        return Integer.parseInt(h+loc);
    }
}
