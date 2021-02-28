package schedule.manager.schedulemanager.pages.manage;

import schedule.manager.schedulemanager.pages.Page;

import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

import static java.awt.Color.*;
import static schedule.manager.schedulemanager.Main.DISPLAY_HEIGHT;

/**
 * タイムバーを操作するクラス
 */
public class TimeTable extends Page implements Runnable {

    private final Map<String, Long> timeMap;
    private final Map<String, Long> acuTimeMap;
    private final Map<String, Integer> heightMap;
    private final Map<String, Integer> acuHeightMap;
    private final Map<String, String> logMap = new LinkedHashMap<>();

    private TimeBar bar = new TimeBar(new Color(156, 255, 201), 0, 0);

    public static int TABLE_X = 100;
    public static int TABLE_Y = 0;

    /**
     * コンストラクタ
     * @param map フェーズのMap
     */
    public TimeTable(Map<String, Long> map) {
        timeMap = map;

        Thread thread = new Thread(this);
        thread.start();

        LineBorder border = new LineBorder(Color.BLACK, 3, false);
        panel.setBorder(border);

        acuTimeMap = getAcuTimeMap();

        heightMap = getHeightMap();
        acuHeightMap = getAcuHeightMap();

        keyIt = timeMap.keySet().iterator();
        keyList = new ArrayList<>(timeMap.keySet());
        key = keyIt.next();
        frontKey = key;
        value = heightMap.get(key);
    }

    private final Iterator<String> keyIt;
    private final List<String> keyList;
    private String key;
    private String frontKey;

    private int progress = 0;
    private int point = 0;
    private int value;
    private long phaseTime = 0;
    private long elapsedTime = 0;
    private long totalDiff = 0;

    /**
     * 主要処理部分
     */
    @Override
    public void run() {
        long projectTime = getProjectTime();
        long waitTime = projectTime / DISPLAY_HEIGHT;
        long excess = (projectTime % DISPLAY_HEIGHT) / waitTime;
        totalDiff += excess;
        waitTime += excess;

        int fps = 30;
        int disFPS = 0;
        int fpsCount = 0;

        String disPhaseTime = "00:00:00";

        long time;
        long lateTime = 0;

        long fpsTime = System.currentTimeMillis();
        long runTime = System.currentTimeMillis();

        process(disPhaseTime, disFPS);
        while (true) {

            if (elapsedTime == acuTimeMap.get(frontKey)) {
                progress = acuHeightMap.get(frontKey);
                lateTime = 0;
                frontKey = point < keyList.size() ? keyList.get(point++) : frontKey;
            }

            time = System.currentTimeMillis() - fpsTime;
            if (time >= 1000) {
                fpsTime = System.currentTimeMillis();
                disFPS = fpsCount;
                fpsCount = 0;

                elapsedTime += 1000;
                phaseTime += 1000;

                long diff = time - 1000;
                totalDiff += diff;

                disPhaseTime = getTimeNotation(phaseTime);
                process(disPhaseTime, disFPS);
            }
            fpsCount++;

            if ((System.currentTimeMillis() - runTime) >= waitTime) {

                process(disPhaseTime, disFPS);
                progress++;

                long diff = (System.currentTimeMillis() - runTime) - waitTime;
                lateTime += diff;

                runTime = System.currentTimeMillis();
            }

            if (lateTime >= waitTime) {
                long n = lateTime / waitTime;
                lateTime -= waitTime * n;
                progress += n;
            }

            try {
                Thread.sleep(1000 / fps);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 描画処理
     * @param disPhaseTime 表示する経過時間
     * @param disFPS FPS
     */
    private void process(String disPhaseTime, int disFPS) {
        drawBackground();
        drawTimeBar();
        drawCell();
        drawTime(disPhaseTime);
        drawLogTime();
        drawFPS(disFPS);

        panel.draw();
    }

    /**
     * FPSを描画する
     * @param disFPS FPS
     */
    private void drawFPS(int disFPS) {
        gra2.setColor(BLACK);
        gra2.drawString("FPS: " + disFPS, TABLE_X + 400, DISPLAY_HEIGHT - 5);
    }

    /**
     * 時間を描画する
     * @param disPhaseTime 表示する経過時間
     */
    private void drawTime(String disPhaseTime) {
        for(Map.Entry<String, Integer> entry : acuHeightMap.entrySet()) {
            String key = entry.getKey();
            int y = entry.getValue();

            gra2.setColor(BLACK);
            gra2.drawString(getTimeNotation(acuTimeMap.get(key)), 0, DISPLAY_HEIGHT - y);
            gra2.setColor(GRAY);
            gra2.drawString(getTimeNotation(timeMap.get(key)), 0, DISPLAY_HEIGHT - y + 20);
        }

        int phase = DISPLAY_HEIGHT - value;
        int y = phase + (heightMap.get(key) / 2);
        gra2.setColor(BLACK);
        gra2.drawString(disPhaseTime, TABLE_X + 300, y);

        long diffPhaseTime = acuTimeMap.get(key) - elapsedTime;
        boolean over = diffPhaseTime < 0;
        diffPhaseTime *= diffPhaseTime < 0 ? -1 : 1;
        gra2.drawString(
                over ? "+" + getTimeNotation(diffPhaseTime) : "-" + getTimeNotation(diffPhaseTime),
                TABLE_X + 105, y);
    }

    /**
     * タイムバーを描画する
     */
    private void drawTimeBar() {
        Color color = new Color(156, 255, 201);
        int phase = DISPLAY_HEIGHT - value;
        bar = new TimeBar(color, progress, phase);

        bar.progress();
        bar.drawDifference();
        bar.drawLogs();
        bar.drawLogLine();
        bar.drawFrontLine();

        gra2.drawString(getTimeNotation(elapsedTime), 0, bar.getFrontLine() + 10);
    }

    /**
     * 記録用のバーを表示する
     */
    private void drawLogTime() {
        gra2.setColor(BLACK);
        for(Map.Entry<String, String> entry : logMap.entrySet()){
            String key = entry.getKey();
            int y = DISPLAY_HEIGHT - (acuHeightMap.get(key)) + (heightMap.get(key) / 2);

            gra2.drawString(entry.getValue(), TABLE_X + 5, y);
        }
    }

    /**
     * セルを描画する
     */
    private void drawCell(){
        gra.setColor(BLACK);

        setLineWidth(1);
        gra.drawLine(TABLE_X + 100, TABLE_Y, TABLE_X + 100, DISPLAY_HEIGHT);
        gra.drawLine(TABLE_X + 200, TABLE_Y, TABLE_X + 200, DISPLAY_HEIGHT);

        for(int value : acuHeightMap.values()) {
            int y = DISPLAY_HEIGHT - value;
            gra.drawLine(TABLE_X, TABLE_Y + y, TABLE_X + 500, TABLE_Y + y);
        }

        gra.setColor(BLACK);
        setLineWidth(3);
        gra.drawLine(TABLE_X, TABLE_Y, TABLE_X, DISPLAY_HEIGHT);
    }

    /**
     * 背景を描画する
     */
    private void drawBackground(){
        gra.setColor(WHITE);
        gra.fillRect(0, 0, 1000, DISPLAY_HEIGHT);
    }

    /**
     * 次のフェーズへ
     */
    public void next() {
        phaseTime = 0;
        bar.next();

        long diffPhaseTime = acuTimeMap.get(key) - elapsedTime;
        boolean over = diffPhaseTime < 0;
        diffPhaseTime *= diffPhaseTime < 0 ? -1 : 1;
        String disDiffTime = over ? "+" + getTimeNotation(diffPhaseTime) : "-" + getTimeNotation(diffPhaseTime);
        logMap.put(key, disDiffTime);

        if (!keyIt.hasNext()) return;
        key = keyIt.next();
        value += heightMap.get(key);
    }

    /**
     * 時間(ms)をh:m:sの形式で表した時間に変換する
     * @param time 時間(ms)
     * @return h:m:sの形式で表した時間
     */
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

    /**
     * 累積時間のMapを取得する
     * @return 累積時間のMap
     */
    private Map<String, Long> getAcuTimeMap() {
        Map<String, Long> map = new LinkedHashMap<>();
        long sum = 0;
        for(Map.Entry<String, Long> entry : timeMap.entrySet()){
            sum += entry.getValue();
            map.put(entry.getKey(), sum);
        }

        return map;
    }

    /**
     * 累積の高さのMapを取得する
     * @return 累積の高さのMap
     */
    private Map<String, Integer> getAcuHeightMap() {
        Map<String, Integer> map = new LinkedHashMap<>();
        int sum = 0;
        for(Map.Entry<String, Integer> entry : heightMap.entrySet()){
            sum += entry.getValue();
            map.put(entry.getKey(), sum);
        }

        return map;
    }

    /**
     * プロジェクト全体の時間を取得する
     * @return プロジェクト全体の時間
     */
    private long getProjectTime() {
        long result = 0;
        for(long value : timeMap.values()) result += value;

        return result;
    }

    /**
     * 高さのMapを取得する
     * @return 高さのMap
     */
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

    /**
     * 最短のフェーズの時間を取得する
     * @return 最短のフェーズの時間
     */
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
