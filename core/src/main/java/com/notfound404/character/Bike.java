package com.notfound404.character;

import java.lang.reflect.Constructor;

import com.badlogic.gdx.Game;
import com.notfound404.arena.GameArena;
import com.badlogic.gdx.graphics.Color;

//Superclass for all bike types
//所有战车类型的父类
public abstract class Bike extends Mobile{
    
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
    public Bike(GameArena arena, int x, int y, int speed, int idNum, Color color) {
        super(x, y, speed, idNum);
        this.arena = arena;
        this.color = color;
        this.dir = GameArena.Direction.UP;
        this.hasAccelerator = false;
        this.isIneffective = false;
        this.discoSlots = 3;
        this.discoMAX = 3;
        this.discoRange = 3;
    }
  
    
    public void setDirection(GameArena.Direction dir) {
        this.dir = dir;
    }

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
                isActive = false;
                break;
            case 5: //Disco cell: pick up disco
                //Only pick up if it's your own disco
                //Remain to be implemented
                //The collision damage will be implement in Class disco
                break;
            default: //Out of bounds/cliff
                lp = 0;
                isActive = false;
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
class disco extends Mobile {

    private int discoRange;
    private Bike masterBike;
    private float kx;
    private float ky;

    public disco(Bike master, int x, int y) {
        super(x, y, 5, 5);
        masterBike = master;
        float vx = x-this.x;
        float vy = y-this.y;
        float normV = (float) Math.sqrt(vx*vx + vy*vy);
        this.kx = vx / normV;
        this.ky = vy / normV;
        this.discoRange = master.discoRange;
        isActive = true;
    }

    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    protected void moveOneStep(){}
}