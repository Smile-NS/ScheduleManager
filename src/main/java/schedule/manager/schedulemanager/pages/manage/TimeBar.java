package schedule.manager.schedulemanager.pages.manage;

import java.awt.*;

import static java.awt.Color.*;
import static schedule.manager.schedulemanager.Main.DISPLAY_HEIGHT;
import static schedule.manager.schedulemanager.pages.Page.gra;
import static schedule.manager.schedulemanager.pages.Page.setLineWidth;

public class TimeBar {

    private final int HEIGHT;
    private final int REM;
    private final Color COLOR;
    private final int PHASE;

    public TimeBar(Color color, int height, int phase) {
        this.COLOR = color;
        this.HEIGHT = height;
        this.REM = DISPLAY_HEIGHT - HEIGHT;
        this.PHASE = phase;
    }

    public void progress() {
        gra.setColor(COLOR);
        gra.fillRect(200, REM, 300, HEIGHT);
        setDifference();

        setFrontLine();
    }

    private void setFrontLine() {
        gra.setColor(RED);
        setLineWidth(3);

        gra.drawLine(0, REM, 500, REM);
    }

    private void setDifference() {
        if (PHASE < REM) {
            Color color = new Color(206, 255, 241);
            gra.setColor(color);
            gra.fillRect(100, PHASE, 100, REM - PHASE);
        }

        if (PHASE > REM) {
            Color color = new Color(255, 179, 57);
            gra.setColor(color);
            gra.fillRect(100, REM, 100, PHASE - REM);
        }
    }
}
