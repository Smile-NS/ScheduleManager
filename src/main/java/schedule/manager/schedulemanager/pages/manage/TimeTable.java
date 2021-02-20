package schedule.manager.schedulemanager.pages.manage;

import schedule.manager.schedulemanager.pages.Page;

import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

import static java.awt.Color.*;
import static schedule.manager.schedulemanager.Main.DISPLAY_HEIGHT;

public class TimeTable extends Page implements Runnable {

    private Map<String, Long> timeMap = new LinkedHashMap<>();
    private Map<String, Integer> heightMap;

    public static int TABLE_X = 100;
    public static int TABLE_Y = 0;

    public TimeTable(){
        timeMap.put("phase1", 12000L);
        timeMap.put("phase2", 10000L);
        timeMap.put("phase3", 10000L);
        timeMap.put("phase4", 10000L);
        timeMap.put("phase5", 10000L);
        timeMap.put("phase6", 10000L);

        Thread thread = new Thread(this);
        thread.start();

        LineBorder border = new LineBorder(Color.BLACK, 3, false);
        panel.setBorder(border);
    }

    private Iterator<String> sizeIt;
    private int progress = 0;
    private String key;
    private int value;

    @Override
    public void run() {
        long projectTime = getProjectTime();
        long waitTime = projectTime / DISPLAY_HEIGHT;
        long excess = (projectTime % DISPLAY_HEIGHT) / waitTime;
        waitTime += excess;
        timeMap = correctTime(timeMap, (projectTime % DISPLAY_HEIGHT) / waitTime);
        heightMap = getHeightMap();
        sizeIt = heightMap.keySet().iterator();
        key = sizeIt.next();
        value = heightMap.get(key);

        long startTime;
        long fpsTime = 0;
        int fps = 30;
        int disFPS = 0;
        int fpsCount = 0;

        long runTime = System.currentTimeMillis();
        long lateTime = 0;

        while (true) {
            if ((System.currentTimeMillis() - fpsTime) >= 1000) {
                fpsTime = System.currentTimeMillis();
                disFPS = fpsCount;
                fpsCount = 0;

                projectTime -= 1000;
                System.out.println(getTimeNotation(projectTime));
            }
            fpsCount++;
            startTime = System.currentTimeMillis();

            if ((System.currentTimeMillis() - runTime) >= waitTime) {

                lateTime += (System.currentTimeMillis() - runTime) - waitTime;
                runTime = System.currentTimeMillis();

                drawBackground();
                drawTimeBar();
                drawCell();

                gra2.drawString("FPS: " + disFPS, 0, DISPLAY_HEIGHT - 5);

                panel.draw();
                if (lateTime >= waitTime) {
                    long n = lateTime / waitTime;
                    lateTime -= waitTime * n;
                    progress += n;
                }
            }

            try {
                Thread.sleep(1000 / fps - (System.currentTimeMillis() - startTime));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void drawText() {
        gra2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        gra2.drawString("あああ", 100, 100);
    }

    private void drawTimeBar() {
        Color color = new Color(156, 255, 201);
        int phase = DISPLAY_HEIGHT - value;
        TimeBar bar = new TimeBar(color, progress, phase);

        bar.progress();
        bar.drawLogs();
        bar.drawDifference();
        bar.drawFrontLine();

        progress++;

        if (!sizeIt.hasNext()) return;
        if (bar.isTouch()) {
            key = sizeIt.next();
            value += heightMap.get(key);
            bar.next();
        }
    }

    private void drawCell(){
        gra.setColor(BLACK);

        setLineWidth(1);
        gra.drawLine(TABLE_X + 100, TABLE_Y, TABLE_X + 100, DISPLAY_HEIGHT);
        gra.drawLine(TABLE_X + 200, TABLE_Y, TABLE_X + 200, DISPLAY_HEIGHT);

        int sum = 0;
        for(Map.Entry<String, Integer> entry : heightMap.entrySet()) {
            int value = entry.getValue();
            sum += value;
            int y = DISPLAY_HEIGHT - sum;

            gra.drawLine(TABLE_X, TABLE_Y + y, TABLE_X + 500, TABLE_Y + y);
        }

        gra.setColor(BLACK);
        setLineWidth(3);
        gra.drawLine(TABLE_X, TABLE_Y, TABLE_X, DISPLAY_HEIGHT);
    }

    private void drawBackground(){
        gra.setColor(WHITE);
        gra.fillRect(0, 0, 1000, DISPLAY_HEIGHT);
    }

    private LinkedHashMap<String, Long> correctTime(Map<String, Long> map, long excess) {
        if (excess == 0) return new LinkedHashMap<>(map);

        List<String> keyList = new ArrayList<>(map.keySet());
        int random = (int) (Math.random() * keyList.size());
        int size = keyList.size();
        long average = excess < size ? excess : excess / size;

        if (excess < size) {
            String key = keyList.get(random);
            map.put(key, map.get(key) - average);
            return new LinkedHashMap<>(map);
        }

        for(Map.Entry<String, Long> entry : timeMap.entrySet()){
            String key = entry.getKey();
            map.put(key, map.get(key) - average);
        }

        return new LinkedHashMap<>(map);
    }

    private String getTimeNotation(long time) {
        time /= 1000;
        long hour = time / 3600;
        long minute = (time % 3600) / 60;
        long second = (time % 3600) % 60;

        String result = "";
        result += hour < 10 ? "0" + hour : hour;
        result += ":";
        result += minute < 10 ? "0" + minute : minute;
        result += ":";
        result += second < 10 ? "0" + second : second;

        return result;
    }

    private long getProjectTime() {
        long result = 0;
        for(long value : timeMap.values()) result += value;

        return result;
    }

    private Map<String, Integer> getHeightMap() {
        double min = getMinTime();
        Map<String, Double> ratioMap = new LinkedHashMap<>();
        Map<String, Integer> heightMap = new LinkedHashMap<>();

        double sum = 0;
        for(Map.Entry<String, Long> entry : timeMap.entrySet()){
            long value = entry.getValue();
            double ratio = value / min;
            sum += ratio;

            ratioMap.put(entry.getKey(), ratio);
        }

        double minSize = DISPLAY_HEIGHT / sum;
        for(Map.Entry<String, Double> entry : ratioMap.entrySet()){
            double ratio = entry.getValue();
            int result = (int) (minSize * ratio);
            heightMap.put(entry.getKey(), result);
        }

        return heightMap;
    }

    private long getMinTime() {
        long min = 0;
        for(Map.Entry<String, Long> entry : timeMap.entrySet()) {
            long value = entry.getValue();
            if (min == 0) {
                min = value;
                continue;
            }
            min = Math.min(value, min);
        }
        return min;
    }
}
