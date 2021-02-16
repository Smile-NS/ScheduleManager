package schedule.manager.schedulemanager.exceptions;

import javax.swing.*;

import static schedule.manager.schedulemanager.pages.Page.frame;

public class IllegalOperationException extends Exception{

    private static final long serialVersionUID = 1L;

    public IllegalOperationException(String message){
        super(message);
        JOptionPane.showMessageDialog(frame, message, "不正な操作", JOptionPane.ERROR_MESSAGE);
    }
}
