package schedule.manager.schedulemanager;

import schedule.manager.schedulemanager.pages.Page;

import javax.swing.*;
import java.io.File;

import static java.awt.Color.BLACK;
import static schedule.manager.schedulemanager.Main.FRAME_HEIGHT;

public class ScheduleFrame extends JFrame {

    public ScheduleFrame(){
        ImageIcon icon = new ImageIcon(Page.replaceSep("textures\\icon.png"));
        setIconImage(icon.getImage());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Schedule Manager");
        setSize(1000 ,FRAME_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        setBackground(BLACK);
        setVisible(true);
    }

    public void draw(JPanel panel, String layout){
        getContentPane().add(panel, layout);
        repaint();
        revalidate();
    }
}
