package com.notfound404.tron;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.notfound404.fileReader.ArchiveManager;
import com.notfound404.fileReader.ArchiveManager.ArchiveEntry;

public class MenuScreen implements Screen, InputProcessor {
    
    private final Main game;
    private final String[] menuItems = {
        "START GAME",
        "LOAD SYSTEM", 
        "OPTIONS",
        "RANKS",
        "EXIT"
    };
    
    private int selectedIndex = 0;
    private float glowTimer = 0f;
    private float scanLineY = 0f;
    private String playerIDInput= "";
    private boolean isLoading = false;
    private final int NOT_FOUND_SHOW = 2;
    private float notFoundTimer = 0f;
    
    // TRON Colors
    private final Color CYAN_GLOW = new Color(0f, 0.85f, 1f, 1f);
    private final Color ORANGE_GLOW = new Color(1f, 0.42f, 0f, 1f);
    private final Color GRID_COLOR = new Color(0f, 0.85f, 1f, 0.15f);
    
    public MenuScreen(Main game) {
        this.game = game;
    }
    
    @Override
    public void show() {
        // Initialize
    }
    
    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Viewport Setting
        game.viewport.apply();

        //Fix our painters to the coordinates of camera
        //绑定画图工具的坐标到相机坐标系
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.shapeRenderer.setProjectionMatrix(game.viewport.getCamera().combined);
        
        // Update timers
        glowTimer += delta;
        scanLineY += delta * 50f;
        if (scanLineY > game.viewport.getWorldHeight()) {
            scanLineY = -10f;
        }
        
        // Handle input
        if(notFoundTimer<=0 && !isLoading)
            handleInput();
        
        //draw
        draw(delta);
        
    }

    private void draw(float delta){
        // Draw
        drawBackground();
        drawTitle();
        drawMenu();
        drawScanLine();
        drawCornerDecorations();
        if(isLoading)
            drawPrompt();
        if(notFoundTimer>0){
            notFoundTimer -= delta;
            displayErrorMsg();
        }
    }
    
    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex = (selectedIndex - 1 + menuItems.length) % menuItems.length;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex = (selectedIndex + 1) % menuItems.length;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            selectMenuItem();
        }
    }
    
    private void selectMenuItem() {
        switch (selectedIndex) {
            case 0: // START GAME
                game.setScreen(new PlayerSelectionScreen(game));
                break;
            case 1: // LOAD SYSTEM
                isLoading = true;
                Gdx.input.setInputProcessor(this);
                break;
            case 2: // OPTIONS
                // TODO
                break;
            case 3: // RANKS
                game.setScreen(new LeaderBoard(game));
                break;
            case 4: // EXIT
                Gdx.app.exit();
                break;
        }
    }
    
    private void drawBackground() {
        game.shapeRenderer.begin(ShapeType.Line);
        game.shapeRenderer.setColor(GRID_COLOR);
        
        // Draw grid
        int gridSize = 50;
        for (int x = 0; x < game.viewport.getWorldWidth(); x += gridSize) {
            game.shapeRenderer.line(x, 0, x, game.viewport.getWorldHeight());
        }
        for (int y = 0; y < game.viewport.getWorldHeight(); y += gridSize) {
            game.shapeRenderer.line(0, y, game.viewport.getWorldWidth(), y);
        }
        
        game.shapeRenderer.end();
    }
    
    private void drawTitle() {
        float viewportW = game.viewport.getWorldWidth();
        float viewportH = game.viewport.getWorldHeight();

        float centerX = viewportW / 2f;
        float titleY = viewportH - 55f;
        
        game.batch.begin();
        
        // Title "Tron"
        BitmapFont font = game.font; // Assume you have a large font
        GlyphLayout layout = new GlyphLayout(font, "TRON");
        
        // Draw glow effect
        float glowAlpha = 0.5f + 0.3f * MathUtils.sin(glowTimer * 2f);
        font.setColor(CYAN_GLOW.r, CYAN_GLOW.g, CYAN_GLOW.b, glowAlpha);
        font.getData().setScale(3f);
        font.draw(game.batch, "TRON", centerX - layout.width * 3 / 2, titleY);
        font.getData().setScale(1f);
        
        // Subtitle
        font.setColor(CYAN_GLOW.r, CYAN_GLOW.g, CYAN_GLOW.b, 0.7f);
        GlyphLayout subtitleLayout = new GlyphLayout(font, "Legacy");
        font.getData().setScale(0.5f);
        font.draw(game.batch, "Legacy", centerX - subtitleLayout.width / 4 + 12, titleY - 35);
        font.getData().setScale(1f);
        
        game.batch.end();
        
        // Draw decorative lines
        game.shapeRenderer.begin(ShapeType.Filled);
        game.shapeRenderer.setColor(CYAN_GLOW);
        game.shapeRenderer.rectLine(centerX - 200, titleY - 60, centerX + 200, titleY - 60, 1);
        game.shapeRenderer.end();
    }
    
    private void drawMenu() {
        float centerX = game.viewport.getWorldWidth() / 2f;
        float itemHeight = 50f;
        float totalMenuHeight = menuItems.length * itemHeight;
        float startY = game.viewport.getWorldHeight() / 2f + (totalMenuHeight / 2f) - 65f;
        
        game.batch.begin();
        
        for (int i = 0; i < menuItems.length; i++) {
            float y = startY - i * itemHeight;
            boolean isSelected = (i == selectedIndex);
            
            BitmapFont font = game.font;
            GlyphLayout layout = new GlyphLayout(font, menuItems[i]);
            
            if (isSelected) {
                // Selected item - orange glow
                float glowAlpha = 0.8f + 0.2f * MathUtils.sin(glowTimer * 5f);
                font.setColor(ORANGE_GLOW.r, ORANGE_GLOW.g, ORANGE_GLOW.b, glowAlpha);
                
                // Draw chevron
                font.draw(game.batch, ">", centerX - layout.width / 2 - 30, y);
            } else {
                // Unselected item - cyan
                font.setColor(CYAN_GLOW.r, CYAN_GLOW.g, CYAN_GLOW.b, 0.6f);
            }
            
            font.draw(game.batch, menuItems[i], centerX - layout.width / 2, y);
        }
        
        game.batch.end();
        
        // Draw selection border
        float selectedY = startY - selectedIndex * itemHeight;
        game.shapeRenderer.begin(ShapeType.Line);
        game.shapeRenderer.setColor(ORANGE_GLOW);
        game.shapeRenderer.rect(centerX - 200, selectedY - 40, 400, 50);
        game.shapeRenderer.end();
    }
    
    private void drawScanLine() {
        game.shapeRenderer.begin(ShapeType.Filled);
        game.shapeRenderer.setColor(CYAN_GLOW.r, CYAN_GLOW.g, CYAN_GLOW.b, 0.1f);
        game.shapeRenderer.rect(0, scanLineY, game.viewport.getWorldWidth(), 10);
        game.shapeRenderer.end();
    }
    
    private void drawCornerDecorations() {
        int cornerSize = 100;
        float w = game.viewport.getWorldWidth();
        float h = game.viewport.getWorldHeight();
        
        game.shapeRenderer.begin(ShapeType.Line);
        game.shapeRenderer.setColor(CYAN_GLOW);
        
        // Top-left
        game.shapeRenderer.line(20, h - 20, 20 + cornerSize, h - 20);
        game.shapeRenderer.line(20, w - 20, 20, h - 20 - cornerSize);
        
        // Top-right
        game.shapeRenderer.line(w - 20, h - 20, 
                               w - 20 - cornerSize, h - 20);
        game.shapeRenderer.line(w - 20, h - 20,
                               w - 20, h - 20 - cornerSize);
        
        // Bottom-left
        game.shapeRenderer.line(20, 20, 20 + cornerSize, 20);
        game.shapeRenderer.line(20, 20, 20, 20 + cornerSize);
        
        // Bottom-right
        game.shapeRenderer.line(w - 20, 20,
                               w - 20 - cornerSize, 20);
        game.shapeRenderer.line(w - 20, 20,
                               w - 20, 20 + cornerSize);
        
        game.shapeRenderer.end();
        
        // Corner dots
        game.shapeRenderer.begin(ShapeType.Filled);
        game.shapeRenderer.setColor(CYAN_GLOW);
        game.shapeRenderer.circle(20, h - 20, 3);
        game.shapeRenderer.circle(w - 20, h - 20, 3);
        game.shapeRenderer.circle(20, 20, 3);
        game.shapeRenderer.circle(w - 20, 20, 3);
        game.shapeRenderer.end();
    }

    private void drawPrompt(){
        //Veil to dim the menu
        Gdx.gl.glEnable(GL20.GL_BLEND);
        game.shapeRenderer.begin(ShapeType.Filled);
        game.shapeRenderer.setColor(0, 0, 0, 0.7f);
        game.shapeRenderer.rect(0, 0, game.viewport.getWorldWidth(), game.viewport.getWorldHeight());
        game.shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        //prompt
        float promptX = game.viewport.getWorldWidth()/2f;
        float promptY = game.viewport.getWorldHeight()/2f + 45f;
        game.batch.begin();
        game.font.setColor(Color.YELLOW);
        GlyphLayout layout = new GlyphLayout(game.font, "Archive Info (Case Sensitive)");
        game.font.draw(game.batch, "Archive Info (Case Sensitive)", promptX - layout.width/2, promptY);
        promptY -= 50;
        game.font.setColor(Color.WHITE);
        layout = new GlyphLayout(game.font, "Enter Name: " + playerIDInput + "_");
        game.font.draw(game.batch, "Enter Name: " + playerIDInput + "_", promptX - layout.width/2, promptY);
        promptY -= 50;
        game.font.setColor(Color.GRAY);
        layout = new GlyphLayout(game.font, "[Press ENTER]");
        game.font.draw(game.batch, "[Press ENTER]", promptX - layout.width/2, promptY);
        game.batch.end();
    }

    private void displayErrorMsg(){
        game.batch.begin();
        game.font.setColor(Color.PINK);
        game.font.getData().setScale(4.5f);
        GlyphLayout layout = new GlyphLayout(game.font, "ID NOT FOUND!");
        game.font.draw(game.batch, "ID NOT FOUND!", game.viewport.getWorldWidth()/2f - layout.width/2, game.viewport.getWorldHeight()/2f+10f);
        game.font.getData().setScale(1f);
        game.batch.end();
    }
    
    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }
    
    @Override
    public void pause() {}
    
    @Override
    public void resume() {}
    
    @Override
    public void hide() {}
    
    @Override
    public void dispose() {}


    //Implement input-processor
    @Override
    public boolean keyTyped(char character){
        //Here we simulating a real-time keyboard input process
        if(isLoading){
            // Enter means confirming (End input)
            if (character == '\r' || character == '\n') {
                confirmName();
            }
            // BackSpace
            else if (character == 8) {
                if (playerIDInput.length() > 0) {
                    // Delete the last char
                    playerIDInput = playerIDInput.substring(0, playerIDInput.length() - 1);
                }
            }
            // concatenate
            else if (playerIDInput.length() < 12) {
                // visible/normal char(letters and numbers etc.) only
                if (character >= 32 && character <= 126) {
                    playerIDInput += character;
                }
            }
            return true;
        }
        return false;
    }

    private void confirmName() {
        //Release the input control (or we can't use it on the main menu)
        Gdx.input.setInputProcessor(null);

        ArchiveEntry record = ArchiveManager.getArchive(playerIDInput);
        if(record == null){
            playerIDInput = "";
            isLoading = false;
            notFoundTimer = NOT_FOUND_SHOW;
            return;
        }

        //Load to the recorded game
        game.setScreen(new GameScreen(game, record.map, record.heroType, record.level, record.score));
        
    }
    
     //No use
    @Override public boolean keyDown(int keycode) { return false; }
    @Override public boolean keyUp(int keycode) { return false; }
    @Override public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
    @Override public boolean mouseMoved(int screenX, int screenY) { return false; }
    @Override public boolean scrolled(float amountX, float amountY) { return false; }
    @Override public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {return false;}

}