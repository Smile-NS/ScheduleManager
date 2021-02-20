package schedule.manager.schedulemanager.pages.manage;

import schedule.manager.schedulemanager.pages.Page;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static schedule.manager.schedulemanager.Main.FRAME_HEIGHT;

public class ProjectManagePage extends Page {

    private File PROJECT;
    private final JPanel sidebar = new JPanel();

    public ProjectManagePage(File file){
        this.PROJECT = file;
        sidebar.setBackground(Color.WHITE);
        sidebar.setPreferredSize(new Dimension(400, FRAME_HEIGHT));
        frame.draw(sidebar, BorderLayout.WEST);

        gra2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Font font = new Font("SansSerif", Font.PLAIN, 20);
        gra.setFont(font);

        new TimeTable();
    }
}
