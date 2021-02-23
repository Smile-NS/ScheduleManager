package schedule.manager.schedulemanager.pages;

import schedule.manager.schedulemanager.DisplayType;
import schedule.manager.schedulemanager.jackson.QuickJackson;
import schedule.manager.schedulemanager.pages.manage.ProjectManagePage;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static java.awt.Color.WHITE;

public class MenuPage extends Page {

    private final JLabel titleIcon = new JLabel();
    private final JLabel titleStr = new JLabel("Schedule Manager");

    public MenuPage(){
        try {
            init();

            setButtonMeta();
            setTitleMeta();

            panel.add(titleIcon);
            panel.add(titleStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setButtonMeta(){
        int width = 250;
        int height = 50;

        JFileChooser saveFileDia = new JFileChooser();
        saveFileDia.setFileSelectionMode(JFileChooser.FILES_ONLY);
        JButton openButton = createButton(375, 490, width, height, "プロジェクトを開く");
        openButton.addActionListener(e -> {
            int selected = saveFileDia.showSaveDialog(frame);
            if (selected == JFileChooser.OPEN_DIALOG) {
                File file = saveFileDia.getSelectedFile();
                try {
                    new ProjectManagePage(new QuickJackson(file));
                    disType = DisplayType.MANAGE;
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        JButton createButton = createButton(375, 550, width, height, "新規プロジェクトを作成する");
        createButton.addActionListener(e -> {
            new CreateProjectPage();
            disType = DisplayType.CREATE_PROJECT;
        });
    }

    private void setTitleMeta() throws IOException {
        ImageIcon icon = createImage("textures\\icon.png", 100, 100);

        titleIcon.setIcon(icon);
        titleIcon.setBounds(450, 325, 100, 100);

        titleStr.setBounds(100, 200, 800, 100);
        titleStr.setHorizontalAlignment(JLabel.CENTER);
        titleStr.setFont(new Font("メイリオ", Font.BOLD, 80));
    }
}
