package com.notfound404.tron;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.notfound404.fileReader.ArchiveManager;
import com.notfound404.fileReader.ArchiveManager.ArchiveEntry;

public class LeaderBoard implements Screen{
    final Main game;
    ArrayList<ArchiveManager.ArchiveEntry> topPlayers;

    private static String formatHeader = "%-6s %-16s %-8s %-8s %-10s %-12s";
    private static String formatLine = "%-8d %-12s %-8d %-16d %-10s %-12s";
    private String titleLine;
    private static final int LEFT_ALIGN_X = 80;

    public LeaderBoard(Main game){
        this.game = game;
        topPlayers = ArchiveManager.getTopScores(10);
        titleLine = String.format(formatHeader, "No.", "ID", "LV", "SCORE", "HERO", "DATE");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();
        game.font.setColor(Color.YELLOW);
        game.font.draw(game.batch, "TOP AGENTS", game.viewport.getWorldWidth()/2-50f, 370);
        
        game.font.setColor(Color.WHITE);
        float y = 345;
        int rank = 1;

        game.font.draw(game.batch, titleLine, LEFT_ALIGN_X, y);
        y-=35;
        
        for (ArchiveEntry entry : topPlayers) {
            String text = String.format(
                formatLine,
                rank, entry.playerID, entry.level, entry.score,
                entry.heroType, entry.date
            );
            game.font.draw(game.batch, text, LEFT_ALIGN_X, y);
            y -= 30;
            rank++;
        }
        
        game.font.setColor(Color.GRAY);
        game.font.draw(game.batch, "Press ESC to Return", LEFT_ALIGN_X, 50);
        game.batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game));
            this.dispose();
        }
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) { game.viewport.update(width, height, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
