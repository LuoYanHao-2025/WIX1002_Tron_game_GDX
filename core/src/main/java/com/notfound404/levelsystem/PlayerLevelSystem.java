package com.notfound404.levelsystem;

import com.notfound404.character.Enemy;
import com.notfound404.character.Player;

/**
 * 玩家专用升级系统 - 扩展了技能选择功能（每2级一次）
 */
public class PlayerLevelSystem extends BaseLevelSystem {
    
    // 玩家特有的技能统计
    private Player player; // 持有玩家引用，用于修改属性

    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * 玩家升级逻辑
     */
    @Override
    protected void levelUp() {
        currentLevel++;
        currentXP = currentXP - currentXPCap; // 扣除消耗掉的经验，保留溢出的部分
        currentXPCap *= XP_CAP_MULTIPLIER;   // 提高下一级的难度
        
        // 1. 调用通用的基础属性提升
        applyBaseStatUpgrade();
        
        // 2. 玩家特有逻辑：每2级获得一次技能强化机会
        if (currentLevel >= 2 && currentLevel % 2 == 0) {
            if (player != null) {
            player.setDiscoMAX(player.getDiscoMAX() + 1);
            player.setDiscoSlots(player.getDiscoSlots() + 1); // 升级奖励补弹
            player.setDiscoRange(player.getDiscoRange() + 1);
        }
    }
    
    /**
     * 直接修改Player实体类中的属性字段
     */
    @Override
    protected void applyBaseStatUpgrade() {
        if (player != null) {
            // 使用 Getter 和 Setter
            player.setMaxLP((float)(player.getMaxLP() * STAT_MULTIPLIER));
            player.setLP(player.getMaxLP()); // 升级补满血
            player.setSpeed((float)(player.getSpeed() * STAT_MULTIPLIER));
        
            // 同步等级和经验
            player.setLevel(currentLevel);
            player.setExp((int)currentXP);
        }
    }

    /**
     * 根据敌人难度和等级获取经验值
     */
    public void addXPFromEnemy(Enemy enemy) {
        addXP(enemy.getXPForDefeating());
    }
    
    // --- 飞盘射击逻辑判断接口 ---
    public boolean canShootDisc(int currentDiscs) { return currentDiscs >= discsPerShot; }
    public int consumeDiscsForShot() { return discsPerShot; }

    // --- Getter 供 Player 实体调用 ---
    public int getBounceCount() { return bounceCount; }
    public int getMaxDiscs() { return maxDiscs; }
    public int getProjectileCount() { return projectileCount; }
    public int getDiscsPerShot() { return discsPerShot; }
}