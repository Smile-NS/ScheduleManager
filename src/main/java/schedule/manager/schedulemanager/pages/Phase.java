package schedule.manager.schedulemanager.pages;

import org.apache.commons.lang.StringUtils;
import schedule.manager.schedulemanager.exceptions.IllegalSettingException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * フェーズの設定欄を司るクラス
 */
public class Phase extends Page{

    private final JTextField title;
    private final JTextField hours;
    private final JTextField minutes;
    private final JTextField seconds;

    private static int count = 0;

    private final List<Component> compList = new ArrayList<>();
    private static final List<Phase> phaseList = new ArrayList<>();

    /**
     * コンストラクタ
     * @param x 設定欄のx座標
     * @param y 設定欄のy座標
     * @throws IOException 画像生成で投げられる例外
     */
    public Phase(int x, int y) throws IOException {
        count++;

        JLabel L_phase = createTextLabel(x, y, 200, 25, "フェーズ" + count);

        JLabel L_title = createTextLabel(x + 25, y += 25, 200, 25, "名前");
        title = createTextField(x + 25, y += 25, 500, 25, "phase" + count);

        JLabel L_time = createTextLabel(x + 25, y += 25, 200, 25, "時間");

        hours = createTextField(x + 25, y += 25, 30, 25, "0");
        hours.setHorizontalAlignment(JTextField.RIGHT);
        JLabel L_hours = createTextLabel(x + 55, y, 20, 25, "h");

        minutes = createTextField(x + 70, y, 30, 25, "0");
        minutes.setHorizontalAlignment(JTextField.RIGHT);
        JLabel L_minutes = createTextLabel(x + 100, y, 20, 25, "m");

        seconds = createTextField(x + 120, y, 30, 25, "0");
        seconds.setHorizontalAlignment(JTextField.RIGHT);
        JLabel L_seconds = createTextLabel(x + 150, y, 20, 25, "s");

        ImageIcon icon = createImage("textures/button/remove.png", 25,25);
        JButton button = createButton(x + 170, y, 25, 25, icon);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.addActionListener(e -> delete());

        compList.add(title);
        compList.add(hours);
        compList.add(minutes);
        compList.add(seconds);
        compList.add(button);

        compList.add(L_phase);
        compList.add(L_title);
        compList.add(L_time);
        compList.add(L_hours);
        compList.add(L_minutes);
        compList.add(L_seconds);

        phaseList.add(this);

        panel.draw();
    }

    /**
     * 設定欄を削除する
     */
    public void delete(){
        boolean found = false;
        for (Phase phase : phaseList){

            if (phase == this) {
                found = true;
                continue;
            }

            if (found) phase.advance();
        }

        for (Component comp : compList) panel.remove(comp);
        phaseList.remove(this);

        panel.draw();
    }

    /**
     * 設定欄の位置を繰り上げる
     */
    private void advance(){
        for (Component comp : compList) {
            Point loc = comp.getLocation();
            comp.setLocation((int) loc.getX(), (int) loc.getY() - 145);
        }
    }

    /**
     * 全ての設定欄を取得する
     * @return 全ての設定欄のリスト
     */
    public static List<Phase> getPhases(){
        return phaseList;
    }

    /**
     * フェーズの累積追加回数を取得する
     * @return フェーズの累積追加回数
     */
    public static int getCount(){
        return count;
    }

    /**
     * フェーズのタイトルを取得する
     * @return フェーズのタイトル
     */
    public String getTitle(){
        return title.getText();
    }

    /**
     * フェーズの時間を取得する(秒)
     * @return フェーズの時間
     * @throws IllegalSettingException 入力されていた値が数値でない場合
     */
    public long getTime() throws IllegalSettingException {
        String sh = hours.getText();
        String sm = minutes.getText();
        String ss = seconds.getText();

        if (!StringUtils.isNumeric(sh) || !StringUtils.isNumeric(sm) || !StringUtils.isNumeric(ss))
            throw new IllegalSettingException("時間は数値を入力してください");

        long h = Long.parseLong(sh) * 3600;
        long m = Long.parseLong(sm) * 60;
        long s = Long.parseLong(ss);

        return s + m + h;
    }
}
