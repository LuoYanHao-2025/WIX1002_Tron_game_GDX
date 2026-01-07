package com.notfound404.arena;

import java.util.ArrayList;
import com.notfound404.character.Bike;

public class GameArena {
    
    //The cells of the arena
    //竞技场的单元格
    private final static int ARENA_WIDTH = 40;
    private final static int ARENA_HEIGHT = 40;

    //The size of each cell in pixels
    //像素尺寸
    public final static int CELL_SIZE = 8;

    /*The grid representing the arena
    * Each cell can have the following values:
    * 0 = empty cell
    * 1 = trail cell
    * 2 = bike cell
    * 3 = accelerator cell
    * 4 = wall cell
    * 5 = disco cell
    * 6 = dead disco cell
    * 
    * -1 = out of bounds/cliff
    * */
    private int[][] grid;

    //Defining directions for bike movement
    //Use W, A, S, D for UP, LEFT, DOWN, RIGHT respectively
    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    ArrayList<Bike> bikes;
    ArrayList<Explosion> explosions;


    public GameArena() {
        grid = new int[ARENA_WIDTH][ARENA_HEIGHT];
        bikes = new ArrayList<Bike>();
        explosions = new ArrayList<Explosion>();
    }


    public int getCellValue(int x, int y) {
        if (x < 0 || x >= ARENA_WIDTH || y < 0 || y >= ARENA_HEIGHT) {
            return -1; // Out of bounds
        }
        return grid[x][y];
    }

    public void setCellValue(int x, int y, int value) {
        if (x < 0 || x >= ARENA_WIDTH || y < 0 || y >= ARENA_HEIGHT) {
            return; // Out of bounds
        }
        grid[x][y] = value;
    }

    public void addBike(Bike bike) {
        bikes.add(bike);
    }

    public void addExplosion(int x, int y) {
        explosions.add(new Explosion(x, y));
    }
}