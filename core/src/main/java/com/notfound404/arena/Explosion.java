package com.notfound404.arena;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

//This class implements explosion effects in the arena
public class Explosion {
    private static final int PARTICLE_COUNT = 20;
    private Particle[] particles;

    public Explosion(int centerX, int centerY) {
        particles = new Particle[PARTICLE_COUNT];
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            particles[i] = new Particle(centerX, centerY);
        }
    }

    public boolean update(float deltaTime) {
        boolean isActive = true;
        for (Particle p : particles) {
            if(p.update(deltaTime)) {
                isActive = false;
            }
        }
        return isActive;
    }

    class Particle{
        private int x, y;
        Color color;
        float lifeTime;
        float maxLifeTime;
        float velX, velY;
        float accumulatorX, accumulatorY;

        //Hit the Trail
        public Particle(int startX, int startY) {
            this.x = startX;
            this.y = startY;
            this.color = new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1);
            this.maxLifeTime = MathUtils.random(0.5f, 1.5f);
            this.lifeTime = maxLifeTime;
            float angle = MathUtils.random(0, 2 * MathUtils.PI);
            float speed = MathUtils.random(50, 150);
            this.velX = MathUtils.cos(angle) * speed;
            this.velY = MathUtils.sin(angle) * speed;
            this.accumulatorX = 0;
            this.accumulatorY = 0;
        }

        public boolean update(float deltaTime) {
            if (lifeTime <= 0) return false;
            lifeTime -= deltaTime;          

            accumulatorX += velX * deltaTime;
            accumulatorY += velY * deltaTime;

            x += (int)accumulatorX;
            y += (int)accumulatorY;

            accumulatorX -= (int)accumulatorX;
            accumulatorY -= (int)accumulatorY;

            return lifeTime >0;
        }

        public int getX(){return x;}
        public int getY(){return y;}
    }
}
