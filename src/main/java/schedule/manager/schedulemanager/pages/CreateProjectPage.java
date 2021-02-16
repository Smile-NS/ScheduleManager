package schedule.manager.schedulemanager.pages;

import schedule.manager.schedulemanager.DisplayType;
import schedule.manager.schedulemanager.exceptions.IllegalSettingException;
import schedule.manager.schedulemanager.jackson.QuickJackson;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.awt.Color.WHITE;

public class CreateProjectPage extends Page {

    private final JTextField F_fileDir = createTextField(150, 50, 660, 25,
            replaceSep(System.getProperty("user.dir") + "\\untitled.json"));
    private final JTextField F_projName = createTextField(150, 150, 700, 25, "untitled");
    private final JFileChooser saveFileDia = new JFileChooser();
    private final JCheckBox C_projTime;
    private final JCheckBox C_phaseTime;

    public CreateProjectPage(){
        init();

        this.C_projTime = createCheckBox(
                200, 260, 200, 25, "プロジェクト時間重視", JCheckBox.LEFT, 20);
        this.C_phaseTime = createCheckBox(
                200, 285, 200, 25, "フェーズ時間重視", JCheckBox.LEFT, 48);

        setDirSelectField();
        setProjNameField();
        setSelectTimeField();
        setNextButton();
        setCancelButton();

        panel.add(F_fileDir);
        panel.add(saveFileDia);
        panel.add(F_projName);
    }
    
    private void setDirSelectField(){

        createTextLabel(START_X, START_Y, 200, 25, "プロジェクトの保存先");

        saveFileDia.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        JButton B_dirSelect = createButton(810, 50, 40, 24, "...");
        B_dirSelect.addActionListener(e -> {
            int selected = saveFileDia.showSaveDialog(frame);
            if (selected == JFileChooser.APPROVE_OPTION) {
                File file = saveFileDia.getSelectedFile();
                F_fileDir.setText(replaceSep(file.getPath() + "\\" + F_projName.getText() + ".json"));
            }
        });
    }

    private void setProjNameField(){
        createTextLabel(150, 125, 200, 25, "プロジェクトの名前");

        F_projName.addActionListener(e -> syncFilePath());

        F_projName.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                syncFilePath();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                syncFilePath();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }

    private void syncFilePath(){
        StringBuilder str = new StringBuilder(F_fileDir.getText());
        if (!String.valueOf(str).contains(File.separator)) return;

        String fileName = str.substring(str.lastIndexOf(File.separator));
        str.setLength(str.length() - fileName.length());

        String title = F_projName.getText();
        str.append(File.separator).append(title).append(".json");

        F_fileDir.setText(String.valueOf(str));
    }

    private void setNextButton() {
        JButton next = createNextButton(600, 600);
        next.addActionListener(e -> {
            try {
                onClickedNext();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    @Override
    protected void onClickedNext() throws Exception{
        String title = F_projName.getText();
        String path = F_fileDir.getText();

        if (title.length() != 0 && new File(path).isDirectory()){
            if (path.substring(path.length() - 1).equalsIgnoreCase(File.separator))
                F_fileDir.setText(replaceSep(path + title + ".json"));
            else
                F_fileDir.setText(replaceSep(path + "\\" + title + ".json"));
        }

        saveProject();
        new PhaseOptionPage();
    }

    private void setCancelButton(){
        JButton cancelButton = createButton(730, 600, 120, 50, "キャンセル");
        cancelButton.addActionListener(e -> {
            new MenuPage();
            disType = DisplayType.MENU;
        });
    }

    private void setSelectTimeField(){
        C_projTime.setSelected(true);
        C_projTime.addActionListener(e -> C_phaseTime.setSelected(false));
        C_phaseTime.addActionListener(e -> C_projTime.setSelected(false));

        createTextLabel(150, 225, 200, 25, "時間進行タイプの設定");
    }

    @Override
    protected void saveProject() throws Exception{
        Map<String, Object> config = new HashMap<>();
        File file = new File(F_fileDir.getText());

        if (!isSaveFile(file, F_projName.getText()))
            throw new IllegalSettingException("そのパスは利用できません");

        if (C_projTime.isSelected()) config.put("type", "project_time");
        else if (C_phaseTime.isSelected()) config.put("type", "phase_time");
        else throw new IllegalSettingException("時間進行タイプが設定されていません");

        config.put("title", F_projName.getText());

        json = new QuickJackson(file);
        json.setJsonWithMap(config);
        super.saveProject();
    }
}
