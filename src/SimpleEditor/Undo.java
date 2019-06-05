package simplejavatexteditor;

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
	public static UndoManager undomg = new UndoManager();
	public static JMenuItem undo,redo;
	
	/* 
	 * 用按钮控制
	 * 传入两个button和textarea即可
	 * Undo.UndoInit(undoButton, redoButton, textArea);
	 * 但是撤销的快捷键只能设置为alt+z
	 */
    public static void UndoButtonInit(JButton unbtn,JButton rebtn,JTextArea textArea) {
    	
        textArea.getDocument().addUndoableEditListener(undomg);

        
        unbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if(undomg.canUndo()) {
                    undomg.undo();
                } else {
                    JOptionPane.showMessageDialog(null,"无法撤销","警告",JOptionPane.WARNING_MESSAGE);
                }
            }
        });
      //这里设置快捷键alt+z unbtn.setMnemonic(KeyEvent.VK_Z);
        rebtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if(undomg.canRedo()) {
                    undomg.redo();
                } else {
                    JOptionPane.showMessageDialog(null,"无法恢复","警告",JOptionPane.WARNING_MESSAGE);
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
                 if(undomg.canUndo()) {
                     undomg.undo();
                 } else {
                     JOptionPane.showMessageDialog(null,"无法撤销","警告",JOptionPane.WARNING_MESSAGE);
                 }
             }
         });
         undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
         menuEdit.add(undo);
   
         redo = new JMenuItem("Redo",redoIcon);
         redo.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent evt) {
                 if(undomg.canRedo()) {
                     undomg.redo();
                 } else {
                     JOptionPane.showMessageDialog(null,"无法恢复","警告",JOptionPane.WARNING_MESSAGE);
                 }
             }
         });
         menuEdit.add(redo);
         
    }
}