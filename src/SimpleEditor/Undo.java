package simplejavatexteditor;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.undo.*;

public class Undo extends JFrame {

    /**
	 * �����ͳ���
	 * ��ť���ƻ�����editMenu�����
	 * ������������
	 */
	private static final long serialVersionUID = 1L;
	public static UndoManager undomg = new UndoManager();
	public static JMenuItem undo,redo;
	
	/* 
	 * �ð�ť����
	 * ��������button��textarea����
	 * Undo.UndoInit(undoButton, redoButton, textArea);
	 * ���ǳ����Ŀ�ݼ�ֻ������Ϊalt+z
	 */
    public static void UndoButtonInit(JButton unbtn,JButton rebtn,JTextArea textArea) {
    	
        textArea.getDocument().addUndoableEditListener(undomg);

        
        unbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if(undomg.canUndo()) {
                    undomg.undo();
                } else {
                    JOptionPane.showMessageDialog(null,"�޷�����","����",JOptionPane.WARNING_MESSAGE);
                }
            }
        });
      //�������ÿ�ݼ�alt+z unbtn.setMnemonic(KeyEvent.VK_Z);
        rebtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if(undomg.canRedo()) {
                    undomg.redo();
                } else {
                    JOptionPane.showMessageDialog(null,"�޷��ָ�","����",JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
    }
    /* 
     * �ڲ˵��ﳷ������ݼ�ctrl+z
     * ����û�п�ݼ�
     * Undo.undoManuInit(menuEdit, undoIcon);
     * */
    public static void undoManuInit(JMenu menuEdit,ImageIcon undoIcon,ImageIcon redoIcon) {
    	 undo = new JMenuItem("Undo",undoIcon);
    	 undo.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent evt) {
                 if(undomg.canUndo()) {
                     undomg.undo();
                 } else {
                     JOptionPane.showMessageDialog(null,"�޷�����","����",JOptionPane.WARNING_MESSAGE);
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
                     JOptionPane.showMessageDialog(null,"�޷��ָ�","����",JOptionPane.WARNING_MESSAGE);
                 }
             }
         });
         menuEdit.add(redo);
         
    }
}