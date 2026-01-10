package com.notfound404.AI_system;

import com.notfound404.arena.GameArena;
import com.notfound404.arena.GameArena.Direction;
import com.notfound404.character.Enemy;
import com.notfound404.character.Player;
import java.util.*;

public class EnemyAI {
    private Enemy enemy;
    private GameArena arena;
    private Player player;
    private Random random = new Random();

    // 计时器与参数
    private float timer = 0, shootTimer = 0;
    private float moveInterval, shootCooldown;

    public EnemyAI(Enemy enemy, GameArena arena, Player player) {
        this.enemy = enemy;
        this.arena = arena;
        this.player = player;

        // 简化：用公式代替 switch 配置难度参数
        int diff = enemy.getDifficulty();
        this.moveInterval = Math.max(0, 0.6f - (diff * 0.15f)); // 难度越高，反应越快(0.45s -> 0s)
        this.shootCooldown = Math.max(0.2f, 3.5f - (diff * 0.8f)); // 难度越高，射速越快
    }

    public void update(float dt) {
        if (enemy.lp <= 0) return;

        // 1. 移动逻辑 (带反应延迟)
        timer += dt;
        if (timer >= moveInterval) {
            makeMoveDecision();
            timer = 0;
        }

        // 2. 射击逻辑
        shootTimer += dt;
        if (shootTimer >= shootCooldown) {
            tryShoot();
        }
    }

    private void makeMoveDecision() {
        // 预判前方坐标
        int nx = enemy.x + getDX(enemy.dir);
        int ny = enemy.y + getDY(enemy.dir);

        // 决策：如果前方危险，或者 (随机概率满足难度要求 且 有更好路径) -> 改变方向
        boolean isFrontBlocked = !isSafe(nx, ny);
        boolean wantsToHunt = random.nextDouble() < (enemy.getDifficulty() * 0.25); // 25%~100% 进攻欲

        if (isFrontBlocked || wantsToHunt) {
            Direction bestDir = findBestDirection();
            if (bestDir != null) enemy.dir = bestDir;
        }
    }

    // 寻找最佳方向（优先安全，其次离玩家近）
    private Direction findBestDirection() {
        Direction best = null;
        double minDst = Double.MAX_VALUE;

        for (Direction d : Direction.values()) {
            // 排除反方向（不能直接掉头）
            if (isOpposite(d, enemy.dir)) continue;

            int nx = enemy.x + getDX(d);
            int ny = enemy.y + getDY(d);

            if (isSafe(nx, ny)) {
                // 计算距离玩家的距离 (加入一点随机干扰防止走位太死板)
                double dst = Math.pow(nx - player.x, 2) + Math.pow(ny - player.y, 2);
                if (enemy.getDifficulty() < 3) dst += random.nextInt(50);

                if (dst < minDst) {
                    minDst = dst;
                    best = d;
                }
            }
        }
        return best; // 如果全是死路，返回 null (听天由命)
    }

    private void tryShoot() {
        if (player == null) return;

        // 判断是否同行或同列
        int dx = player.x - enemy.x;
        int dy = player.y - enemy.y;
        boolean alignX = (dx == 0), alignY = (dy == 0);

        if (!alignX && !alignY) return; // 不在直线上

        // 判断距离限制 (难度4无限距离，其他难度递增)
        int dist = Math.abs(dx + dy);
        if (enemy.getDifficulty() < 4 && dist > enemy.getDifficulty() * 15) return;

        // 判断朝向是否正确
        boolean facingPlayer = (alignX && ((dy > 0 && enemy.dir == Direction.UP) || (dy < 0 && enemy.dir == Direction.DOWN))) ||
            (alignY && ((dx > 0 && enemy.dir == Direction.RIGHT) || (dx < 0 && enemy.dir == Direction.LEFT)));

        if (facingPlayer) {
            System.out.println("Enemy Fire!"); // 替换为 enemy.fireDisco()
            shootTimer = 0;
        }
    }

    // === 辅助工具方法 ===

    private boolean isSafe(int x, int y) {
        int v = arena.getCellValue(x, y);
        // 0=空, 3=加速, 5=飞盘 是安全的
        return v == 0 || v == 3 || v == 5;
    }

    private boolean isOpposite(Direction d1, Direction d2) {
        return (getDX(d1) + getDX(d2) == 0) && (getDY(d1) + getDY(d2) == 0);
    }

    // 将方向转换为坐标增量的简易写法
    private int getDX(Direction d) { return d == Direction.RIGHT ? 1 : (d == Direction.LEFT ? -1 : 0); }
    private int getDY(Direction d) { return d == Direction.UP ? 1 : (d == Direction.DOWN ? -1 : 0); }
}
