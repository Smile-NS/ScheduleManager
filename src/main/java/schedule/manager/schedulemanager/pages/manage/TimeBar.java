package schedule.manager.schedulemanager.pages.manage;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.awt.Color.RED;
import static java.awt.Color.WHITE;
import static schedule.manager.schedulemanager.Main.DISPLAY_HEIGHT;
import static schedule.manager.schedulemanager.pages.Page.*;
import static schedule.manager.schedulemanager.pages.manage.TimeTable.TABLE_X;
import static schedule.manager.schedulemanager.pages.manage.TimeTable.TABLE_Y;

/**
 * タイムバーの描画用クラス
 */
public class TimeBar {

    private final int HEIGHT;
    private final int REM;
    private final Color COLOR;
    private final int PHASE;

    private static final List<int[]> logList = new ArrayList<>();
    private static final Color OVER_COLOR = new Color(255, 179, 57);
    private static final Color LESS_COLOR = new Color(206, 255, 241);
    private static final int SAME = 0;
    private static final int LESS = 1;
    private static final int OVER = 2;

    /**
     * コンストラクタ
     * @param color メインタイムバーの色
     * @param height 高さ
     * @param phase フェーズの高さ
     */
    public TimeBar(Color color, int height, int phase) {
        this.COLOR = color;
        this.HEIGHT = height;
        this.REM = DISPLAY_HEIGHT - HEIGHT;
        this.PHASE = phase;
    }

    /**
     * 進行
     */
    public void progress() {
        gra.setColor(COLOR);
        gra.fillRect(TABLE_X + 200, TABLE_Y + REM, 300, HEIGHT);
    }

    /**
     * 前線の描画
     */
    public void drawFrontLine() {
        gra.setColor(RED);
        setLineWidth(3);

        gra.drawLine(TABLE_X, TABLE_Y + REM, TABLE_X + 500, TABLE_Y + REM);
    }

    /**
     * フェーズの最大値との差分の描画
     */
    public void drawDifference() {
        if (PHASE < REM) {
            gra.setColor(LESS_COLOR);
            gra.fillRect(TABLE_X + 100, TABLE_Y + PHASE, 100, REM - PHASE);
        }

        if (PHASE > REM) {
            gra.setColor(OVER_COLOR);
            gra.fillRect(TABLE_X + 100, TABLE_Y + REM, 100, PHASE - REM);
        }
    }

    /**
     * 前線のy座標を取得する
     * @return 前線のy座標
     */
    public int getFrontLine() {
        return TABLE_Y + REM;
    }

    public void next() {
        int[] log = new int[3];

        if (PHASE < REM) {
            log[0] = PHASE;
            log[1] = REM - PHASE;
            log[2] = LESS;
        } else if (PHASE > REM) {
            log[0] = REM;
            log[1] = PHASE - REM;
            log[2] = OVER;
        } else log[0] = PHASE;

        logList.add(log);
    }

    /**
     * 記録用のバーを描画する
     */
    public void drawLogs() {
        for(int[] value : logList){
            int start = value[0];
            int height = value[1];
            int type = value[2];

            Color color = type == LESS ? LESS_COLOR :
                          type == OVER ? OVER_COLOR : WHITE;

            gra.setColor(color);
            gra.fillRect(TABLE_X, start, 100, height);

            setLineWidth(3);
            gra.setColor(RED);

        }
    }

    /**
     * 記録用の線を描画する
     */
    public void drawLogLine() {
        for(int[] value : logList){
            int start = value[0];
            int height = value[1];
            int type = value[2];

            int y = type == LESS ? start + height :
                    type == OVER ? start : start;

            BasicStroke bs2 = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f,
                    new float[] {7}, 0);
            gra2.setStroke(bs2);

            gra.drawLine(TABLE_X, TABLE_Y + y, TABLE_X + 500, TABLE_Y + y);
        }
    }
}
