package schedule.manager.schedulemanager.pages;

import schedule.manager.schedulemanager.DisplayType;
import schedule.manager.schedulemanager.ScheduleFrame;
import schedule.manager.schedulemanager.SchedulePanel;
import schedule.manager.schedulemanager.jackson.QuickJackson;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static java.awt.Color.WHITE;

/**
 * 各ページの親クラス
 * 使用頻度の高いメソッドなどを提供する
 */
public class Page {

    public static DisplayType disType = DisplayType.MENU;

    protected static QuickJackson json;

    public static final ScheduleFrame frame = new ScheduleFrame();
    public static final SchedulePanel panel = new SchedulePanel();
    public static final Graphics gra = panel.image.createGraphics();
    public static final Graphics2D gra2 = (Graphics2D) gra;

    public static final Color BUTTON_COLOR = new Color(240, 240, 240);
    public static final Font FONT = new Font("メイリオ", Font.PLAIN, 16);
    public static final Font SMALL_FONT = new Font("メイリオ", Font.PLAIN, 14);
    public static final LineBorder BORDER = new LineBorder(Color.BLACK, 1, false);

    public static final int START_X = 150;
    public static final int START_Y = 25;

    /**
     * テキスト入りボタンを生成する
     * @param x x座標
     * @param y y座標
     * @param width 幅
     * @param height 高さ
     * @param text テキスト
     * @return 生成されたテキスト入りJButton
     */
    public static JButton createButton(int x, int y, int width, int height, String text){
        JButton button = createButton(x, y, width, height);
        button.setText(text);

        return button;
    }

    /**
     * 画像入りボタンを生成する
     * @param x x座標
     * @param y y座標
     * @param width 幅
     * @param height 高さ
     * @param icon 画像
     * @return 生成された画像入りJButton
     */
    public static JButton createButton(int x, int y, int width, int height, ImageIcon icon){
        JButton button = createButton(x, y, width, height);
        button.setIcon(icon);

        return button;
    }

    /**
     * ボタンを生成する
     * @param x x座標
     * @param y y座標
     * @param width 幅
     * @param height 高さ
     * @return 生成されたJButton
     */
    public static JButton createButton(int x, int y, int width, int height){
        JButton button = new JButton();

        button.setBounds(x, y, width, height);
        button.setBackground(BUTTON_COLOR);
        button.setFont(FONT);
        button.setBorder(BORDER);
        button.setFocusPainted(false);

        panel.add(button);

        return button;
    }

    /**
     * 「次へ」のボタンを生成する
     * @param x x座標
     * @param y y座標
     * @return 生成させたJButton
     */
    public static JButton createNextButton(int x, int y){
        return createButton(x, y, 120, 50, "次へ");
    }

    /**
     * チェックボックスを生成する
     * @param x x座標
     * @param y y座標
     * @param width 幅
     * @param height 高さ
     * @param text テキスト
     * @param textPos テキストの位置
     * @param textGap テキストとチェックボックスまでの距離
     * @return 生成されたJCheckBox
     */
    public static JCheckBox createCheckBox(int x, int y, int width, int height, String text, int textPos, int textGap){
        String texturePath = "textures/check_box";
        ImageIcon icon = null;
        ImageIcon selectedIcon = null;
        try {
            icon = createImage(texturePath + "/non_check.png");
            selectedIcon = createImage(texturePath + "/check.png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        JCheckBox checkBox = new JCheckBox(text);

        checkBox.setBounds(x, y, width, height);
        checkBox.setOpaque(false);
        checkBox.setFocusPainted(false);
        checkBox.setFont(SMALL_FONT);
        checkBox.setHorizontalTextPosition(textPos);
        checkBox.setIconTextGap(textGap);
        checkBox.setIcon(icon);
        checkBox.setSelectedIcon(selectedIcon);

        panel.add(checkBox);

        return checkBox;
    }

    /**
     * テキストラベルを生成する
     * @param x x座標
     * @param y y座標
     * @param width 幅
     * @param height 高さ
     * @param text テキスト
     * @return 生成されたJLabel
     */
    public static JLabel createTextLabel(int x, int y, int width, int height, String text){
        JLabel label = new JLabel(text);
        label.setFont(FONT);
        label.setBounds(x, y, width, height);

        panel.add(label);

        return label;
    }

    /**
     * テキストフィールドを生成する
     * @param x x座標
     * @param y y座標
     * @param width 幅
     * @param height 高さ
     * @return 生成されたJTextField
     */
    public JTextField createTextField(int x, int y, int width, int height){
        JTextField field = new JTextField();
        field.setFont(SMALL_FONT);
        field.setBounds(x, y, width, height);

        panel.add(field);

        return field;
    }

    /**
     * 既に入力されているテキストフィールドを生成する
     * @param x x座標
     * @param y y座標
     * @param width 幅
     * @param height 高さ
     * @param text テキスト
     * @return 既に入力されている生成されたJTextField
     */
    public JTextField createTextField(int x, int y, int width, int height, String text){
        JTextField field = createTextField(x, y, width, height);
        field.setText(text);

        return field;
    }

    /**
     * 利用できるセーブファイルであるか
     * @param file 判定するファイル
     * @param name ファイル名
     * @return 利用できるセーブファイルであればtrue、そうでなければfalse
     */
    public boolean isSaveFile(File file, String name){
        StringBuilder path = new StringBuilder(file.getPath());
        if (!String.valueOf(path).contains(File.separator)) return false;

        String fileName = path.substring(path.lastIndexOf(File.separator));
        if (!fileName.equalsIgnoreCase(File.separator + name + ".json")) return false;

        StringBuilder fileDir = new StringBuilder(path);
        fileDir.setLength(path.length() - fileName.length());
        return new File(String.valueOf(fileDir)).exists();
    }

    /**
     * プロジェクトを保存する
     * @throws Exception セーブ時に投げられる例外
     */
    protected void saveProject() throws Exception{
        json.save();
        System.out.println("プロジェクトを保存しました");
    }

    /**
     * 「次へ」のボタンを押したときの処理
     * @throws Exception Exception
     */
    protected void onClickedNext() throws Exception{
    }

    /**
     * 縦横の比を維持した画像を生成する
     * @param path 画像のパス
     * @param width 幅
     * @param height 高さ
     * @return 縦横の比を維持したImageIcon
     * @throws IOException IOException
     */
    public static ImageIcon createImage(String path, int width, int height) throws IOException {
        InputStream stream = Page.class.getClassLoader().getResourceAsStream(path);
        assert stream != null;
        BufferedImage original = ImageIO.read(stream);
        Image icon = original.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        return new ImageIcon(icon);
    }

    /**
     * 画像を生成する
     * @param path 画像のパス
     * @return 生成されたImageIcon
     * @throws IOException IOException
     */
    public static ImageIcon createImage(String path) throws IOException {
        InputStream stream = Page.class.getClassLoader().getResourceAsStream(path);
        assert stream != null;
        BufferedImage original = ImageIO.read(stream);
        Image icon = original.getScaledInstance(original.getWidth(), original.getHeight(), Image.SCALE_DEFAULT);
        return new ImageIcon(icon);
    }

    /**
     * ファイルパスをOSに依存しないものに置換する
     * @param path ファイルパス
     * @return OSに依存しないファイルパス
     */
    public static String replaceSep(String path){
        return path.replaceAll("\"", File.separator);
    }

    /**
     * swingのコンポーネント用にテキストをHTMLとして改行する
     * @param text 元のテキスト
     * @return 改行されたテキスト
     */
    public static String setTextAsHTML(String text){
        String[] array = text.split("\n");
        StringBuilder result = new StringBuilder("<html><body>");

        for (int i = 0;i < array.length;i++){
            if (i == 0) {
                result.append(array[0]);
                continue;
            }

            result.append("<br/>").append(array[i]);
        }
        result.append("</body></html>");

        return String.valueOf(result);
    }

    /**
     * グラフィックスの線の幅を変更する
     * @param width 線の幅
     */
    public static void setLineWidth(int width){
        BasicStroke bs = new BasicStroke(width);
        gra2.setStroke(bs);
    }

    /**
     * パネルの初期化
     */
    public static void init(){
        panel.removeAll();
        panel.draw();
        panel.setBackground(WHITE);
    }
}
