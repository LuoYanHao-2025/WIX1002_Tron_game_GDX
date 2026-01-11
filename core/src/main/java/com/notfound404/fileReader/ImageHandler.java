package com.notfound404.fileReader;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.notfound404.arena.GameArena;
import java.io.*;
import java.util.Scanner;

public class ImageHandler {

    private final static int CELLSIZE = 8;//Pixels width/height for a grid
    
    //Patter for bike and disco
    //0 for background, 1 for white pixels, 2 for self color 
    private int[][] bikeShape;
    private int[][] discoShape;

    /** Can store wall in a Sprite file
     *  But not in a matrix, which consume to much time for each time we draw
     * 
     * The same for trailUnit
     * 
     * 墙体较多，不存在矩阵中
     * 目前仅以纯白色格点表示
     * 如果需要一定的图形，要存在Sprite图中（要么一开始就有png，要么用 shaperenderer 根据矩阵画一张静态图储存成sprite）
     * 轨道单元同理
     */

    private GameArena arena;
    private ShapeRenderer shaperRdr;

    public ImageHandler(GameArena arena, ShapeRenderer shapeRdr){
        this.arena = arena;
        this.shaperRdr = shapeRdr;
        //正式版这里读文件，现在用手动的矩阵取代
        bikeShape = bikeShapeReader("bike.txt");
        discoShape = discoShapeReader("disco.txt");
    }

    private static int[][] bikeShapeReader(String fin){
        int[][] bikeShape = new int[8][8];
        String path = "/image/" + fin;

        try(Scanner scanner = new Scanner(new FileInputStream(path))){
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (scanner.hasNextInt()) {
                        bikeShape[row][col] = scanner.nextInt();
                    }
            }
        }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return bikeShape;
    }

    private static int[][] discoShapeReader(String fin){
        int[][] discoShape = new int[8][8];
        String path = "/image/" + fin;

        try(Scanner scanner = new Scanner(new FileInputStream(path))){
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (scanner.hasNextInt()) {
                        discoShape[row][col] = scanner.nextInt();
                    }
            }
        }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return discoShape;
    }

    public void drawBike(){}

    public void drawDisco(){}

    public void drawTrail(){}

    public void drawWall(){}

    public void drawExplosion(){}
}