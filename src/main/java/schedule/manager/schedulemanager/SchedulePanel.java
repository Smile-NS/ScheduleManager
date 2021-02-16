package schedule.manager.schedulemanager;

import schedule.manager.schedulemanager.pages.Page;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static schedule.manager.schedulemanager.Main.FRAME_HEIGHT;

public class SchedulePanel extends JPanel {
    public BufferedImage image;

    public SchedulePanel(){
        super();
        this.image = new BufferedImage(1000, FRAME_HEIGHT, BufferedImage.TYPE_INT_RGB);
        this.setLayout(null);
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        if (Page.disType == DisplayType.MANAGE)
            g.drawImage(image, 0, 0, this);
    }

    public void draw(){
        repaint();
        revalidate();
    }
}
