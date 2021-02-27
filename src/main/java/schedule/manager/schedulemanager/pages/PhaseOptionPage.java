package schedule.manager.schedulemanager.pages;

import schedule.manager.schedulemanager.DisplayType;
import schedule.manager.schedulemanager.exceptions.IllegalOperationException;
import schedule.manager.schedulemanager.exceptions.IllegalSettingException;
import schedule.manager.schedulemanager.pages.manage.ProjectManagePage;

import javax.swing.*;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PhaseOptionPage extends Page{

    private static int pageAmount = 0;

    public PhaseOptionPage(){
        init();

        try {
            setCreatePhaseField();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JButton next = createNextButton(730, 650);
        next.addActionListener(e -> {
            try {
                if (Phase.getCount() == 0)
                    throw new IllegalSettingException("フェーズを追加してください");
                saveProject();
                onClickedNext();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    private void setCreatePhaseField() throws IOException {
        if (pageAmount == 0) createTextLabel(START_X, START_Y, 200, 25, "フェーズの設定");
        createTextLabel(200, 60, 100, 25, "追加");

        JButton button = createButton(250, 60, 25, 25,
                createImage("textures/button/add.png", 25, 25));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

        button.addActionListener(e -> {
            try {
                onAdd();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    private void onAdd() throws Exception {
        List<Phase> phaseList = Phase.getPhases();
        int count = phaseList.size() - pageAmount * 4;

        try {
            if (count >= 4 && count % 4 == 0)
                throw new IllegalOperationException("さらにフェーズを追加したい場合は\n「さらにフェーズを追加する」を押してください");

            new Phase(225, 100 + count * 145);

            if ((count + 1) % 4 == 0){
                JButton button = createButton( 600, 650, 120, 50,
                        setTextAsHTML("さらにフェーズを追加する"));
                button.addActionListener(e -> {
                    pageAmount++;
                    new PhaseOptionPage();
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onClickedNext() throws IOException{
        new ProjectManagePage(json);
        disType = DisplayType.MANAGE;
    }

    @Override
    protected void saveProject() throws Exception{
        List<Phase> list = Phase.getPhases();
        Map<String, Long> phaseMap = new LinkedHashMap<>();

        for (Phase phase : list)
            phaseMap.put(phase.getTitle(), phase.getTime());

        json.put("phases", phaseMap);
        super.saveProject();
    }
}
