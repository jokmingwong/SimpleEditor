package SimpleEditor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * 此类用于自动补全 自动补全的范围包括： C++/Java
 * 关键字，括号，引号，以及文本之前已经出现过的自定义方法名、变量名、变量名
 * <p>
 * 创建方式：直接 AutoComplete autoComplete = new AutoComplete(ui, al);
 * 创建时自动启用 autoComplete 功能
 * <p>
 * autoComplete 功能的开关由 disableAutoComplete(autoComplete)
 * 和 enableAutoComplete(autoComplete) 来控制
 * <p>
 * 核心功能——关键词补全：已经实现
 * 目前剩余需求：
 * 优先级  序号、内容
 * (√)    0、修复已有BUG
 * (√)    1、候选词按照使用频率排序（目前候选词顺序不会改变）
 * (2)    2、尝试加上tab键，但是通过tab选取候选项的时候要吞掉tab （目前如果直接加tab的话，tab不会被吞掉）
 * (√)    3、改进数据结构，keyword set加上以前上下文写过的词（直接上三叉树，目前只有预设的关键词能补全）
 * (1)    4、括号自动补全 （目前括号不能自动补全）
 * (√)    5、word长度大于等于2的时候才放出候选项 （目前word长度为1的时候也会跳出候选框，非常麻烦）
 * (√)    6、向前查找的时候，碰到空白字符再停（目前碰到非字母就会停下）
 *
 * @author dengkunquan
 * @date 2019-06-07
 */

public class AutoComplete {
    private enum Mode {
        INSERTING, COMPLETED
    }

    private Mode mode = Mode.COMPLETED;
    private final JTextArea txtInput;
    private final DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
    private final JComboBox cbInput = new JComboBox(model) {
        public Dimension getPreferredSize() {
            return new Dimension(super.getPreferredSize().width, 0);
        }
    };
    private actionListener actListener;
    private keyAdapter keyListener;
    private documentListener docListener;

    public AutoComplete(UI ui, ArrayList<String> al) {
        this.txtInput = ui.getEditor();
        actListener = new actionListener(txtInput, al);
        keyListener = new keyAdapter(txtInput);
        docListener = new documentListener(txtInput, al);


        setupComboBox(this.txtInput, al);
    }

    /**
     * TODO 需要注意的是，括号的补全逻辑和关键字完全不一样
     * 括号是强制补全的，而关键字是可选补全的。
     */

    private void setupComboBox(final JTextArea txtInput,
                               final ArrayList<String> items) {

        Utility.setAdjusting(cbInput, false);

        /* step 2：往model里加word */
        for (String item : items) {
            model.addElement(item);
        }

        /* step 3：将候选框默认设置为不选中 */
        cbInput.setSelectedItem(null);

        /* step 4：添加行为监听器 */
        cbInput.addActionListener(actListener);

        /* step 5：添加关键按键监听器 */
        txtInput.addKeyListener(keyListener);

        /* step 6：添加文件监听器 */
        txtInput.getDocument().addDocumentListener(docListener);

        /* step 7：设置弹窗位置 */
        txtInput.setLayout(new BorderLayout());
        txtInput.add(cbInput, BorderLayout.SOUTH);
    }

    private class actionListener implements ActionListener {
        final JTextArea txtInput;
        ArrayList<String> items;

        actionListener(final JTextArea txtInput,
                       ArrayList<String> items) {
            this.txtInput = txtInput;
            this.items = items;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!Utility.isAdjusting(cbInput)) {
                if (cbInput.getSelectedItem() != null) {
                    Utility.setText(txtInput, cbInput);
                }
            }
        }
    }


    private class keyAdapter extends KeyAdapter {
        JTextArea txtInput;

        keyAdapter(JTextArea txtInput) {
            this.txtInput = txtInput;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            Utility.setAdjusting(cbInput, true);

            if (e.getKeyCode() == KeyEvent.VK_SPACE) {          // 如果在显示候选框的情况下打了空格，就关掉它
                cbInput.setPopupVisible(false);
            }

            if (mode == Mode.INSERTING
                    // && txtInput.getCaretPosition() == insertPos
                    && (e.getKeyCode() == KeyEvent.VK_ENTER
                    || e.getKeyCode() == KeyEvent.VK_TAB
                    || e.getKeyCode() == KeyEvent.VK_UP
                    || e.getKeyCode() == KeyEvent.VK_DOWN)) {
                e.setSource(cbInput);                           // 将按键时间重定向至cbInput
                cbInput.dispatchEvent(e);                       // 按照按键来调度选项
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Utility.setText(txtInput, cbInput);
                    cbInput.setPopupVisible(false);             // 回车和tab会打印并关闭候选框
                    mode = Mode.COMPLETED;
                }
            }

            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {         // 退格会关闭候选框并且清空候选框
                model.removeAllElements();
                mode = Mode.COMPLETED;
                cbInput.setPopupVisible(false);
            }
            Utility.setAdjusting(cbInput, false);
        }
    }


    private class documentListener implements DocumentListener {
        final JTextArea txtInput;
        ArrayList<String> items;

        documentListener(final JTextArea txtInput,
                         ArrayList<String> item) {
            this.txtInput = txtInput;
            this.items = item;
        }


        // TRIGGER(s)
        // 光标的移动机制：
        // 插进去的时候，光标还没有移动
        // 先插字符，然后计算，最后移动光标
        // 每次 update 光标都是最后移动
        @Override
        public void insertUpdate(DocumentEvent e) {

            /*//此段代码用于括号补全
            if(
                    Utility.checkForBracket(txtInput, bracketMap)){
                Utility.setAdjusting(cbInput, false);
                return;
            }
            */
            updateListForInsert();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateListForInsert();
        }

        private void updateListForInsert() {
            String content = txtInput.getText();
            int position = txtInput.getCaretPosition();

            Utility.setAdjusting(cbInput, true);
            model.removeAllElements();
            String prefix = Utility.getPrefixForInsert(content, position);

            if (prefix.length() >= 2) {
                Utility.updateModel(content, items, prefix, model);
            }

            // 输入大于等于两个字符才会出现候选框
            if (model.getSize() <= 0 || prefix.length() < 2) {
                cbInput.setPopupVisible(false);
                mode = Mode.COMPLETED;
            } else {
                cbInput.setPopupVisible(true);
                mode = Mode.INSERTING;
            }

            Utility.setAdjusting(cbInput, false);

        }

    }


    static void enableAutoComplete(AutoComplete autoComplete) {
        autoComplete.getTxtInput().getDocument().addDocumentListener(autoComplete.getDocListener());
    }

    public static void disableAutoComplete(AutoComplete autoComplete) {
        autoComplete.getTxtInput().getDocument().removeDocumentListener(autoComplete.getDocListener());
    }


    public JTextArea getTxtInput() {
        return txtInput;
    }

    public documentListener getDocListener() {
        return docListener;
    }
}
