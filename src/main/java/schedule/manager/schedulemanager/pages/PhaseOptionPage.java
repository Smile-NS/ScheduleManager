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

/**
 * フェーズの設定をするクラス
 */
public class PhaseOptionPage extends Page{

    private static int pageAmount = 0;

    /**
     * コンストラクタ
     */
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

    /**
     * 設定欄を追加する領域
     * @throws IOException 画像生成時に投げられる例外
     */
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

    /**
     * +ボタンを押したときの処理
     * @throws IllegalOperationException 一つのページに5つ以上設定欄を追加しようとしたとき
     */
    private void onAdd() throws IllegalOperationException {
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

    /**
     * 「次へ」ボタンを押した時の処理
     */
    @Override
    protected void onClickedNext() {
        new ProjectManagePage(json);
        disType = DisplayType.MANAGE;
    }

    /**
     * プロジェクトを保存する
     * @throws Exception セーブ時に投げられる例外
     */
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
