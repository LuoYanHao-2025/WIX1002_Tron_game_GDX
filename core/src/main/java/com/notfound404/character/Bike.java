package com.notfound404.character;

import java.lang.reflect.Constructor;

import com.badlogic.gdx.Game;
import com.notfound404.arena.GameArena;
import com.notfound404.arena.GameArena.Direction;
import com.badlogic.gdx.graphics.Color;

//Superclass for all bike types
//所有战车类型的父类
public abstract class Bike extends Mobile{
    
    protected GameArena.Direction dir;

    //Accumulator to handle `float` movement versus `int` grids conflicts
    //用于处理“float”移动与“int”网格冲突的累加器
    protected float accumulator;

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
            case 5: //The disco damage -1
                lp-=1.0f;
                break;
            case 6://The disco function handle the picking
                break;
            case -1:
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

    public void pickDisco(){
        if(discoSlots<discoMAX){
            discoSlots++;
        }
    }

    @Override
    public void dispose(){
        isActive = false;
    }
    @Override
    public boolean isDisposed(){
        return !isActive;
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
    private float accumulatorX;
    private float accumulatorY;
    private Direction verDir;
    private Direction horiDir;

    disco(Bike master, int x, int y) {
        super(x, y, 5, 5);
        arena = master.arena;
        masterBike = master;
        float vx = x-this.x;
        float vy = y-this.y;
        float normV = (float) Math.sqrt(vx*vx + vy*vy);
        this.kx = vx / normV;
        this.ky = vy / normV;
        horiDir = kx >= 0 ? Direction.RIGHT : Direction.LEFT; 
        verDir = ky >= 0 ? Direction.UP : Direction.DOWN;
        this.kx = Math.abs(kx);
        this.ky = Math.abs(ky);
        this.discoRange = master.discoRange;
        isActive = true;
        accumulatorX = accumulatorY =0;
    }


    @Override

    public void update(float deltaTime) {
        super.update(deltaTime);

        //if dead disco(did not really run the the update)
        if(idNum ==6){
            if(masterBike.getX() == x && masterBike.getY() ==y){
                masterBike.pickDisco();
                dispose();
            }
        }
    }

    @Override
    protected void moveOneStep(){
        if(discoRange<=0){
            landDown();
            isActive = false;
            return;
        }
        arena.setCellValue(x, y, 0);
        accumulatorX+=kx;
        accumulatorY+=ky;
        if(accumulatorX>=1){
            accumulatorX--;
            discoRange--;
            switch (horiDir) {
                case RIGHT:
                    x++;
                    break;
                default:
                    x--;
            }
        }
        if(accumulatorY>=1){
            accumulatorY--;
            discoRange--;
            switch (verDir) {
                case UP:
                    y++;
                    break;
                default:
                    y--;
            }
        }

        crashHandle();
    }

    private void landDown(){
        idNum = 6;
        arena.setCellValue(x,y,idNum);
    }

    private void crashHandle(){
        switch (arena.getCellValue(x, y)) {
            case 4://Wall collision
                arena.addExplosion(x, y);
            case -1://Fly out of the Arena
                dispose();
            case 5:
            case 6:
                break;
            case 1://Kill the Trail
                trailCrash();
            case 2://Cover the Bike
            case 3://Kill/Cover Accelerator
            case 0://Nothing
            default:
                arena.setCellValue(x, y, idNum);
        }
    }

    private void trailCrash(){
        for(int i = -1; i<=1;i++){
            for(int j = -1; j<=1; j++){
                if(arena.getCellValue(x+i, y+j)==1)
                    arena.setCellValue(x+i, y+j, 0);
            }
        }
        arena.addExplosion(x, y);
        arena.setCellValue(x, y, idNum);        
    }

    @Override
    public void dispose(){
        //Never draw again, the position on the map becomes empty.
        isActive = false;
        idNum = 0;
        arena.setCellValue(x, y, idNum);
    }

    @Override
    public boolean isDisposed(){
        return idNum == 0;
    }
}