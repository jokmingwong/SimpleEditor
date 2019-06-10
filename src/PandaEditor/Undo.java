
package PandaEditor;
/*
 * Undo.java
 * 撤销和重做
 * @author LiNan
 * */

import java.awt.event.*;
import javax.swing.*;
import javax.swing.undo.*;

public class Undo extends JFrame {


    /**
     * 重做和撤销
     * 按钮控制或者在editMenu里面点
     * 建议两个都用
     */

    private static final long serialVersionUID = 1L;
    public static UndoManager manager = new UndoManager();
    public static JMenuItem undo,redo;

    /*
     * 用按钮控制
     * 传入两个button和textarea即可
     * Undo.UndoInit(undoButton, redoButton, textArea);
     * 但是撤销的快捷键只能设置为alt+z
     */

    public static void UndoButtonInit(JButton undoButton,JButton redoButton,JTextArea textArea) {

        textArea.getDocument().addUndoableEditListener(manager);


        undoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if(manager.canUndo()) {
                    manager.undo();
                } else {
                    JOptionPane.showMessageDialog(null,"cannot undo","warning",JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        //这里设置快捷键alt+z undoButton.setMnemonic(KeyEvent.VK_Z);
        redoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if(manager.canRedo()) {
                    manager.redo();
                } else {
                    JOptionPane.showMessageDialog(null,"cannot redo","warning",JOptionPane.WARNING_MESSAGE);
                }
            }
        });

    }

    /*
     * 在菜单里撤销，快捷键ctrl+z
     * 重做没有快捷键
     * Undo.undoManuInit(menuEdit, undoIcon);
     * */


    public static void undoManuInit(JMenu menuEdit,ImageIcon undoIcon,ImageIcon redoIcon) {
        undo = new JMenuItem("Undo",undoIcon);
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if(manager.canUndo()) {
                    manager.undo();
                } else {
                    JOptionPane.showMessageDialog(null,"cannot undo","warning",JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
        menuEdit.add(undo);

        redo = new JMenuItem("Redo",redoIcon);
        redo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if(manager.canRedo()) {
                    manager.redo();
                } else {
                    JOptionPane.showMessageDialog(null,"cannot redo","warning",JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        menuEdit.add(redo);

    }
}
