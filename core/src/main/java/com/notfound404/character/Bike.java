package com.notfound404.character;

import java.lang.reflect.Constructor;

import com.badlogic.gdx.Game;
import com.notfound404.arena.GameArena;
import com.badlogic.gdx.graphics.Color;

//Superclass for all bike types
//所有战车类型的父类
public abstract class Bike {
    
    //Position and speed of the bike
    //Unit: Game Arena cells
    //位置和速度
    //单位：竞技场单元格

    protected int x;
    protected int y;
    protected int speed;

    protected GameArena.Direction dir;

    //Accumulator to handle `float` movement versus `int` grids conflicts
    //用于处理“float”移动与“int”网格冲突的累加器
    protected float accumulator;

    //The arena where the bike is located
    //战车所在的竞技场
    protected GameArena arena;

    //Properties of the bike
    //战车的属性

    protected Color color;
    protected float lp;
    protected float maxLP;
    protected int exp;//For enemy bikes, exp awarded when destroyed; for player bike, current exp
    protected int level;
    protected boolean isAlive;
    protected boolean hasAccelerator;
    protected boolean isIneffective;
    protected int discoSlots;
    protected int discoMAX;
    

    //Constants for bike behavior
    //战车行为常量
    protected final static int ACCELERATOR_DURATION = 3; //Duration of accelerator effect in seconds
    protected final static int INEFFECTIVE_DURATION = 3; //Duration of disco/trail collision effects in distance units

    //Attack properties
    //攻击属性
    protected int discoRange;
    protected int trailLength;
    protected Trail bikeTrail;

    //Constructor
    public Bike(GameArena arena, int x, int y, Color color) {
        this.arena = arena;
        this.x = x;
        this.y = y;
        this.color = color;
        this.dir = GameArena.Direction.UP;
        this.accumulator = 0f;
        this.isAlive = true;
        this.hasAccelerator = false;
        this.isIneffective = false;
        this.discoSlots = 3;
        this.discoMAX = 3;
        this.discoRange = 3;
    }


    public int getX() {return x;}
    public int getY() {return y;}

    
    
    public void setDirection(GameArena.Direction dir) {
        this.dir = dir;
    }

    abstract void update(float deltaTime);

    protected void moveOneStep() {
        switch (dir) {
            case UP:
                y += 1;
                break;
            case DOWN:
                y -= 1;
                break;
            case LEFT:
                x -= 1;
                break;
            case RIGHT:
                x += 1;
                break;
        }
        switch(arena.getCellValue(x, y)){
            case 0: //Empty cell
                break;
            case 1: //Trail cell
                if (!isIneffective) {
                    lp -= 0.5f;
                    isIneffective = true;
                }
                break;
            case 2: //Bike cell
                lp -= 1.0f;
                break;
            case 3: //Accelerator cell
                hasAccelerator = true;
                break;
            case 4: //Wall cell
                lp =0;
                isAlive = false;
                break;
            case 5: //Disco cell: pick up disco
                //Only pick up if it's your own disco
                //Remain to be implemented
                break;
            default: //Out of bounds/cliff
                lp = 0;
                isAlive = false;
                break;
        }
    }

    public void shootDisco(int targetX, int targetY) {
        if (discoSlots > 0) {
            disco newDisco = new disco(this, targetX, targetY);
            discoSlots -= 1;
            //Add disco to arena's disco list
            //Remain to be implemented
        }
    }

}

/** Disco
 * 飞碟类
 * It breaks through trails and damages bikes on contact.
 * It stops after a certain duration.
 */
class disco {
    private int x;
    private int y;
    private final float vx;
    private final float vy;
    private int discoRange;
    private float accumulatorX;
    private float accumulatorY;
    private boolean isActive;

    public disco(Bike master, int x, int y) {
        this.x = master.getX();
        this.y = master.getY();
        float vx = x-this.x;
        float vy = y-this.y;
        float normV = (float) Math.sqrt(vx*vx + vy*vy);
        this.vx = vx / normV;
        this.vy = vy / normV;
        this.discoRange = master.discoRange;
        accumulatorX = accumulatorY = 0;
        isActive = true;
    }

    public void update(float deltaTime) {
        if (!isActive) return;
        accumulatorX += deltaTime * vx * 10; //Speed factor
        accumulatorY += deltaTime * vy * 10;
        //Loop to move in integer steps
    }
    public int getX() { return x; }
    public int getY() { return y; }
}