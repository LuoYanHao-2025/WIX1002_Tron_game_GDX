package com.notfound404.character;
import com.notfound404.arena.GameArena;
import com.notfound404.character.Bike;
import com.badlogic.gdx.graphics.Color;

public class Player extends Bike {
    public final String playerType;//"Tron" or "Kelvin"
    protected final String playerID;//Enter by player, used for RANKING system

    public Player(String playerType, String playerID, int startX, int startY, Color color, GameArena arena) {
        super(arena, startX, startY, color);
        this.playerType = playerType;
        this.playerID = playerID;
        this.x = startX;
        this.y = startY;
        this.arena = arena;
        this.exp = 0;
        this.isAlive = true;
        this.hasAccelerator = false;
        this.isIneffective = false;
        this.discoSlots = this.discoMAX =  3;

        this.dir = GameArena.Direction.UP;
        this.accumulator = 0;

    }

}