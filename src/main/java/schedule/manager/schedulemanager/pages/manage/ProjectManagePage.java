package schedule.manager.schedulemanager.pages.manage;

import schedule.manager.schedulemanager.jackson.QuickJackson;
import schedule.manager.schedulemanager.pages.Page;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static schedule.manager.schedulemanager.Main.DISPLAY_HEIGHT;
import static schedule.manager.schedulemanager.Main.FRAME_HEIGHT;

public class ProjectManagePage extends Page {

    private final JPanel sidebar = new JPanel();
    private final TimeTable table;

    public ProjectManagePage(QuickJackson json) throws IOException {
        init();
        sidebar.setLayout(null);
        sidebar.setBackground(Color.WHITE);
        sidebar.setPreferredSize(new Dimension(400, FRAME_HEIGHT));
        frame.draw(sidebar, BorderLayout.WEST);
        panel.setBounds(0, 0, 600, DISPLAY_HEIGHT);

        gra2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Font font = new Font("SansSerif", Font.PLAIN, 20);
        gra2.setFont(font);

        setNextButton();
        table = new TimeTable(getTimeMap(json));
    }

    private void setNextButton() {
        JButton button = new JButton("次のフェーズへ");
        button.setBounds(100, 400, 200, 50);
        button.setBackground(BUTTON_COLOR);
        button.setFont(FONT);
        button.setBorder(BORDER);
        button.setFocusPainted(false);
        sidebar.add(button);
        button.addActionListener(e -> table.next());
    }

    private Map<String, Long> getTimeMap(QuickJackson json) {
        Map<String, Long> map = new LinkedHashMap<>();

        for (Map.Entry<String, Object> entry : json.getMap("phases").entrySet()) {
            String key = entry.getKey();
            long value = Long.parseLong(entry.getValue().toString()) * 1000;

            map.put(key, value);
        }
        return map;
    }
}
