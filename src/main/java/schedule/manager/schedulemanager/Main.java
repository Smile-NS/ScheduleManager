package schedule.manager.schedulemanager;

import schedule.manager.schedulemanager.pages.CreateProjectPage;
import schedule.manager.schedulemanager.pages.MenuPage;
import schedule.manager.schedulemanager.pages.Page;
import schedule.manager.schedulemanager.pages.PhaseOptionPage;

import java.awt.*;

public class Main {

    public static final int FRAME_HEIGHT = 800;
    public static final int DISPLAY_HEIGHT = 765;

    public static void main(String[] args){

        // デバッグ用
        switch (Page.disType) {
            case MENU:
                new MenuPage();
                break;
            case CREATE_PROJECT:
                new CreateProjectPage();
                break;
            case OPTION:
                new PhaseOptionPage();
                break;
        }

        Page.frame.draw(Page.panel, BorderLayout.CENTER);
    }
}
