package com.notfound404.mapsystem;

import java.util.Random;

public class RandomMapGenerator {
    private static final int SIZE = 44;
    private Random random = new Random();

    public int[][] generateMatrix() {
        int[][] grid = new int[SIZE][SIZE];

        // 1. 初始化结构：边界 -1，围墙 4
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (i < 2 || i >= SIZE - 2 || j < 2 || j >= SIZE - 2) {
                    grid[i][j] = -1;
                } else if (i == 2 || i == SIZE - 3 || j == 2 || j == SIZE - 3) {
                    grid[i][j] = 4;
                } else {
                    grid[i][j] = 0;
                }
            }
        }

        // 2. 簇状生成山体 (4)，保证 0 > 3 > 4 的比例
        int mountainClusters = random.nextInt(5) + 3; 
        for (int i = 0; i < mountainClusters; i++) {
            int rx = random.nextInt(30) + 5;
            int ry = random.nextInt(30) + 5;
            // 生成 2x2 或 3x3 的掩体块
            for(int dx=0; dx<2; dx++) {
                for(int dy=0; dy<2; dy++) {
                    grid[rx+dx][ry+dy] = 4;
                }
            }
        }

        // 3. 随机分布一些加速带 (3)
        for (int i = 0; i < 20; i++) {
            int x = random.nextInt(38) + 3;
            int y = random.nextInt(38) + 3;
            if (grid[x][y] == 0) grid[x][y] = 3;
        }

        return grid;
    }
}