package schedule.manager.schedulemanager.pages.manage;

import com.sun.org.apache.bcel.internal.generic.BALOAD;
import schedule.manager.schedulemanager.SchedulePanel;
import schedule.manager.schedulemanager.pages.Page;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Timer;

import static schedule.manager.schedulemanager.Main.FRAME_HEIGHT;

public class ProjectManagePage extends Page {

    private File PROJECT;
    private final JPanel sidebar = new JPanel();

    public ProjectManagePage(File file){
        this.PROJECT = file;

        sidebar.setBackground(Color.WHITE);
        sidebar.setPreferredSize(new Dimension(500, FRAME_HEIGHT));
        frame.draw(sidebar, BorderLayout.WEST);

        TimeTable task = new TimeTable();
        Timer timer = new Timer();
        timer.schedule(task, 0, 33);
    }

}
