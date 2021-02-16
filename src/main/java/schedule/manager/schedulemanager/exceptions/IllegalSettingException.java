package schedule.manager.schedulemanager.exceptions;

import javax.swing.*;

import static schedule.manager.schedulemanager.pages.Page.frame;

public class IllegalSettingException extends Exception{

    private static final long serialVersionUID = 1L;

    public IllegalSettingException(String message){
        super(message);
        JOptionPane.showMessageDialog(frame, message, "不正な設定", JOptionPane.WARNING_MESSAGE);
    }
}
