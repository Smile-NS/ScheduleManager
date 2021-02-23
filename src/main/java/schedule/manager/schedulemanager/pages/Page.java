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

import static java.awt.Color.WHITE;

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

    public static JButton createButton(int x, int y, int width, int height, String text){
        JButton button = createButton(x, y, width, height);
        button.setText(text);

        return button;
    }

    public static JButton createButton(int x, int y, int width, int height, ImageIcon icon){
        JButton button = createButton(x, y, width, height);
        button.setIcon(icon);

        return button;
    }

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

    public static JButton createNextButton(int x, int y){
        return createButton(x, y, 120, 50, "次へ");
    }

    public static JCheckBox createCheckBox(int x, int y, int width, int height, String text, int textPos, int textGap){
        String texturePath = replaceSep("textures\\check_box");
        ImageIcon icon = new ImageIcon(
                replaceSep(texturePath + "\\non_check.png")
        );
        ImageIcon selectedIcon = new ImageIcon(
                replaceSep(texturePath + "\\check.png")
        );
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

    public static JLabel createTextLabel(int x, int y, int width, int height, String text){
        JLabel label = new JLabel(text);
        label.setFont(FONT);
        label.setBounds(x, y, width, height);

        panel.add(label);

        return label;
    }

    public JTextField createTextField(int x, int y, int width, int height){
        JTextField field = new JTextField();
        field.setFont(SMALL_FONT);
        field.setBounds(x, y, width, height);

        panel.add(field);

        return field;
    }

    public JTextField createTextField(int x, int y, int width, int height, String text){
        JTextField field = createTextField(x, y, width, height);
        field.setText(text);

        return field;
    }

    public boolean isSaveFile(File file, String name){
        StringBuilder path = new StringBuilder(file.getPath());
        if (!String.valueOf(path).contains(File.separator)) return false;

        String fileName = path.substring(path.lastIndexOf(File.separator));
        if (!fileName.equalsIgnoreCase(File.separator + name + ".json")) return false;

        StringBuilder fileDir = new StringBuilder(path);
        fileDir.setLength(path.length() - fileName.length());
        return new File(String.valueOf(fileDir)).exists();
    }

    protected void saveProject() throws Exception{
        json.save();
        System.out.println("プロジェクトを保存しました");
    }

    protected void onClickedNext() throws Exception{
    }

    public static ImageIcon createImage(String filePath, int width, int height) throws IOException {
        BufferedImage original = ImageIO.read(new File(Page.replaceSep(filePath)));
        Image icon = original.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        return new ImageIcon(icon);
    }

    public static String replaceSep(String path){
        return path.replaceAll("\"", File.separator);
    }

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

    public static void setLineWidth(int width){
        BasicStroke bs = new BasicStroke(width);
        gra2.setStroke(bs);
    }

    public static void init(){
        panel.removeAll();
        panel.draw();
        panel.setBackground(WHITE);
    }
}
