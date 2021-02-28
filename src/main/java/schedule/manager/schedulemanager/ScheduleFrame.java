package schedule.manager.schedulemanager;

import schedule.manager.schedulemanager.pages.Page;

import javax.swing.*;
import java.io.IOException;

import static java.awt.Color.BLACK;
import static schedule.manager.schedulemanager.Main.FRAME_HEIGHT;

/**
 * メインウィンドウの生成用クラス
 */
public class ScheduleFrame extends JFrame {

    /**
     * コンストラクタ
     */
    public ScheduleFrame(){
        try {
            ImageIcon icon = Page.createImage("textures/icon.png");
            setIconImage(icon.getImage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Schedule Manager");
        setSize(1000 ,FRAME_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        setBackground(BLACK);
        setVisible(true);
    }

    /**
     * ウィンドウの変更内容を描画する
     * @param panel パネル
     * @param layout レイアウト
     */
    public void draw(JPanel panel, String layout){
        getContentPane().add(panel, layout);
        repaint();
        revalidate();
    }
}
