package com.notfound404.character;
import java.util.LinkedList;

public class Trail {
    /*The Trail is a queue of TrailUnit objects, when it is created, it is added to the arena's trail list.
     * When the trail reaches its maximum length, the first element is removed from the arena's trail list.
     * When the trail is crashed by a bike or a disco, the ID in arena will be set to 0.
     * By rendering the trail, the trail is drawn on the screen.
     * Discovery a unit is of ID 0, it is considered a crash point. Remove it from the trail list.
     * */

    private LinkedList<TrailUnit> trailUnits;
    private int maxTrailLength;

    private Bike ownerBike;

    /* Trail unit class */
    class TrailUnit{
        
        //Position of the trail unit
        private int x;
        private int y;

        public TrailUnit(int x, int y) {
            this.x = x;
            this.y = y;;
        }

        public int getX() { return x; }
        public int getY() { return y; }

    }
}
