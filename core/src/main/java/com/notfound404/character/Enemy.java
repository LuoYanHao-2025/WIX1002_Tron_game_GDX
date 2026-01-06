package com.notfound404.character;

import com.notfound404.arena.GameArena;
import com.badlogic.gdx.graphics.Color;

public class Enemy extends Bike {
    public static int enemyCount = 0;

    public Enemy(GameArena arena, int x, int y, Color color) {
        super(arena, x, y, color);
        enemyCount++;
    }

}
