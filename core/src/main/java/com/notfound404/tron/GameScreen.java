package com.notfound404.tron;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.notfound404.arena.GameArena;


/** Game screen where the main gameplay occurs. */
public class GameScreen implements Screen {

    public final Main game;

    //Here need to declare objects will be used in the Game.
    //这里需要声明一些游戏内要用到的对象，例如车，地图，背景音乐等等
    private GameArena arena;

    Vector2 touchPos;//Mouse position

    GameScreen(Main game) {
        this.game = game;
        //Initialization Objects declared above
        //接下来初始化上面声明的对象，加载文件就在这里，可以写一些method或者class来增强可读性

        //Input to Game configuration
        //设置游戏：可能有读取存档和新游戏两种方案。现在只做新游戏
        arena = new GameArena();
        touchPos = new Vector2();
    }

    @Override
    public void show() {
        // Prepare your screen here.
    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
        input();
        logic();
        draw();
    }

    private void input(){
    }

    private void logic(){
    }

    private void draw(){
        //Clean the screen
        ScreenUtils.clear(Color.BLACK);
        game.shapeRenderer.setProjectionMatrix(game.viewport.getCamera().combined);
        game.shapeRenderer.begin(ShapeType.Filled);

        //Draw the Arena
        arena.draw(game.shapeRenderer);

        game.shapeRenderer.end();

        //Maybe something else will be drawn here
        //Like the text messages
        //Life point display...
    }



    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        // Resize your screen here. The parameters represent the new window size.
        game.viewport.update(width, height,true);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
        
    }
}