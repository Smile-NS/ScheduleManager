package schedule.manager.schedulemanager.pages.manage;

import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimerTask;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import static schedule.manager.schedulemanager.Main.DISPLAY_HEIGHT;
import static schedule.manager.schedulemanager.pages.Page.*;

public class TimeTable extends TimerTask {

    private final Map<String, Long> phaseMap = new LinkedHashMap<>();

    protected final Map<String, Integer> sizeMap;

    public TimeTable(){
        LineBorder border = new LineBorder(Color.BLACK, 3, false);
        panel.setBorder(border);

        phaseMap.put("phase1", 10000L);
        phaseMap.put("phase2", 2000L);
        phaseMap.put("phase3", 30000L);
        phaseMap.put("phase4", 25000L);
        phaseMap.put("phase5", 40000L);

        sizeMap = getSizeMap();
    }

    private int progress = 0;

    @Override
    public void run() {
        setBackground();

        Color color = new Color(156, 255, 201);
        int phase = DISPLAY_HEIGHT - sizeMap.get("phase1");
        TimeBar bar = new TimeBar(color, progress, phase);
        bar.progress();
        progress++;

        setCell();

        panel.draw();
    }

    protected Map<String, Integer> getSizeMap() {
        double min = getMinTime();
        Map<String, Double> ratioMap = new LinkedHashMap<>();
        Map<String, Integer> sizeMap = new LinkedHashMap<>();

        double sum = 0;
        for(Map.Entry<String, Long> entry : phaseMap.entrySet()){
            long value = entry.getValue();
            double ratio = value / min;
            sum += ratio;

            ratioMap.put(entry.getKey(), ratio);
        }

        double minSize = DISPLAY_HEIGHT / sum;
        for(Map.Entry<String, Double> entry : ratioMap.entrySet()){
            double ratio = entry.getValue();
            int result = (int) (minSize * ratio);
            sizeMap.put(entry.getKey(), result);
        }

        return sizeMap;
    }

    protected long getMinTime() {
        long min = 0;
        for(Map.Entry<String, Long> entry : phaseMap.entrySet()) {
            long value = entry.getValue();
            if (min == 0) {
                min = value;
                continue;
            }
            min = Math.min(value, min);
        }
        return min;
    }

    private void setCell(){
        gra.setColor(BLACK);

        setLineWidth(1);
        gra.drawLine(100, 0, 100, DISPLAY_HEIGHT);
        gra.drawLine(200, 0, 200, DISPLAY_HEIGHT);

        int sum = 0;
        for(Map.Entry<String, Integer> entry : sizeMap.entrySet()) {
            int value = entry.getValue();
            sum += value;
            int y = DISPLAY_HEIGHT - sum;

            gra.drawLine(0, y, 500, y);
        }

        gra.setColor(BLACK);
        setLineWidth(3);
        gra.drawLine(1, 0, 1, DISPLAY_HEIGHT);
    }

    private void setBackground(){
        gra.setColor(WHITE);
        gra.fillRect(0, 0, 1000, DISPLAY_HEIGHT);
    }
}
