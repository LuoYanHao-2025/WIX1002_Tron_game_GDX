package com.notfound404.filereader;

import com.notfound404.arena.GameArena;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.notfound404.mapsystem.MapSelector;
import com.notfound404.mapsystem.RandomMapGenerator;

/**
 * 地图加载器 - 负责执行最终的地图写入动作
 */
public class MapLoader {

    /**
     * 核心加载方法
     * @param arena 竞技场对象
     * @param selector 传入选择器实例，自动判断加载方式
     */
    public void loadMap(GameArena arena, MapSelector selector) {
        if (selector.isRandomMode()) {
            // 模式 A: 现场随机生成
            loadRandomGeneratedMap(arena);
        } else {
            // 模式 B: 读取指定的 txt 文件
            loadMapFromFile(arena, selector.getSelectedMapName());
        }
    }

    /**
     * 逻辑 A: 直接调用生成器获取矩阵并写入 Arena
     */
    private void loadRandomGeneratedMap(GameArena arena) {
        RandomMapGenerator generator = new RandomMapGenerator();
        int[][] matrix = generator.generateMatrix(); // 获取随机生成的 44x44 矩阵
        
        for (int r = 0; r < 44; r++) {
            for (int c = 0; c < 44; c++) {
                arena.setCellValue(c, r, matrix[r][c]);
            }
        }
        //System.out.println("MapLoader: 随机地图已即时生成并加载完成。");
    }

    /**
     * 逻辑 B: 原有的文件读取逻辑
     */
    private void loadMapFromFile(GameArena arena, String mapName) {
        String path = "/com/notfound404/map/" + mapName;
        
        try (InputStream is = getClass().getResourceAsStream(path);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            if (is == null) {
                System.err.println("错误: 找不到地图文件 " + path);
                return;
            }

            String line;
            int row = 0;
            while ((line = reader.readLine()) != null && row < 44) {
                // 兼容空格或逗号分隔符
                String[] values = line.trim().split("[,\\s]+");
                
                for (int col = 0; col < values.length && col < 44; col++) {
                    try {
                        int cellValue = Integer.parseInt(values[col]);
                        arena.setCellValue(col, row, cellValue);
                    } catch (NumberFormatException e) {
                        arena.setCellValue(col, row, 0); // 异常数据默认为空地
                    }
                }
                row++;
            }
            //System.out.println("MapLoader: 成功从文件加载地图: " + mapName);

        } catch (Exception e) {
            System.err.println("MapLoader: 读取文件异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
}