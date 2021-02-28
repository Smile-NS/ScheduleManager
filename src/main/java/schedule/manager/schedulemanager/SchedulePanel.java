package schedule.manager.schedulemanager;

import schedule.manager.schedulemanager.pages.Page;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static schedule.manager.schedulemanager.Main.FRAME_HEIGHT;

/**
 * パネル生成用クラス
 */
public class SchedulePanel extends JPanel {
    public BufferedImage image;

    /**
     * コンストラクタ
     */
    public SchedulePanel(){
        super();
        this.image = new BufferedImage(1000, FRAME_HEIGHT, BufferedImage.TYPE_INT_RGB);
        this.setLayout(null);
    }

    /**
     * グラフィックで描画する
     * @param g グラフィックス
     */
    @Override
    public void paint(Graphics g){
        super.paint(g);
        if (Page.disType == DisplayType.MANAGE)
            g.drawImage(image, 0, 0, this);
    }

    /**
     * パネルの変更内容を描画する
     */
    public void draw(){
        repaint();
        revalidate();
    }
}
