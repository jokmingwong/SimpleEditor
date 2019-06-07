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
 *
 *  创建方式：直接 AutoComplete autoComplete = new AutoComplete(ui, al);
 *  创建时自动启用 autoComplete 功能
 *
 *  autoComplete 功能的开关由 disableAutoComplete(autoComplete)
 *  和 enableAutoComplete(autoComplete) 来控制
 *
 *  核心功能——关键词补全：已经实现
 *  目前剩余需求：
 *  1、候选词按照使用频率排序（目前候选词顺序不会改变）
 *  2、尝试加上tab键，但是通过tab选取候选项的时候要吞掉tab （目前如果直接加tab的话，tab不会被吞掉）
 *  3、改进数据结构，keyword set加上以前上下文写过的词（直接上三叉树，目前只有预设的关键词能补全）
 *  4、括号自动补全 （目前括号不能自动补全）
 *  5、word长度大于等于2的时候才放出候选项 （目前word长度为1的时候也会跳出候选框，非常麻烦）
 *  6、向前查找的时候，碰到空白字符再停（目前碰到非字母就会停下）
 *
 * @author  dengkunquan
 * @date    2019-06-07
 */

public class AutoComplete {

    private SupportedKeywords sk = new SupportedKeywords();
    private Map<String, String> bracketMap = sk.getBracketMap();

    private enum Mode {
        INSERTING, COMPLETED
    }

    private Mode mode = Mode.COMPLETED;
    private final UI ui;
    private final JTextArea txtInput;
    private final DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
    private final JComboBox cbInput = new JComboBox(model) {
        public Dimension getPreferredSize() {
            return new Dimension(super.getPreferredSize().width, 0);
        }
    };
    private static final String COMMIT_ACTION = "commit";
    private String content = null;
    private ArrayList<String> wordsInContent;
    private Trie trie;
    private actionListener actListener;
    private keyAdapter keyListener;
    private documentListener docListener;

    public AutoComplete(UI ui, ArrayList<String> al) {
        this.ui = ui;
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

        setAdjusting(cbInput, false);

        /* step 2：往model里加word */
        for (String item : items) {
            model.addElement(item);
        }

        /* step 3：将候选框默认设置为不选中 */
        cbInput.setSelectedItem(null);

        /* step 4：添加行为监听器 */
        // ->：匿名函数
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
            if (!isAdjusting(cbInput)) {
                if (cbInput.getSelectedItem() != null) {
                    setText(txtInput, cbInput);
//                    txtInput.setText(txtInput.getText() + cbInput.getSelectedItem().toString()); // TODO 需要更改，应为追加而非设置文本
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
            setAdjusting(cbInput, true);
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {          // 如果在显示候选框的情况下打了空格，就关掉它
                cbInput.setPopupVisible(false);
            }

            if ( mode == Mode.INSERTING
                    && (e.getKeyCode() == KeyEvent.VK_ENTER
                    || e.getKeyCode() == KeyEvent.VK_TAB
                    || e.getKeyCode() == KeyEvent.VK_UP
                    || e.getKeyCode() == KeyEvent.VK_DOWN)) {
                e.setSource(cbInput);                           // 将按键时间重定向至cbInput
                cbInput.dispatchEvent(e);                       // 按照按键来调度选项
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    setText(txtInput, cbInput);
//                    txtInput.setText(txtInput.getText() + Objects.requireNonNull(cbInput.getSelectedItem()).toString()); // TODO 此处需要修改，应为追加文本而非设置文本
                    cbInput.setPopupVisible(false);             // 回车和tab会打印并关闭候选框
                    mode = Mode.COMPLETED;
                }
            }

            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {         // 退格会关闭候选框
                cbInput.setPopupVisible(false);
            }
            setAdjusting(cbInput, false);
        }
    }


    private class documentListener implements DocumentListener {
        final JTextArea txtInput;
        ArrayList<String> items;

        documentListener(final JTextArea txtInput,
                         ArrayList<String> items) {
            this.txtInput = txtInput;
            this.items = items;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            updateList();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateList();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateList();
        }

        private void updateList() {
            setAdjusting(cbInput, true);
            model.removeAllElements();
            String input = getPrefix(txtInput.getText());
            if (!input.isEmpty()) {
                updateItem(items);
                for (String item : items) {
                    if (item.startsWith(input)) {
                        model.addElement(item);
                    }
                }
            }
            if(model.getSize() > 0){
                cbInput.setPopupVisible(true);
                mode = Mode.INSERTING;
            } else {
                cbInput.setPopupVisible(false);
                mode = Mode.COMPLETED;
            }

            setAdjusting(cbInput, false);
        }

        // TODO 将这个方法改造成用于支持 updateList 方法的类，主要用于更新list的内容
        // 依赖于trie树的词频统计
        private void updateItem(ArrayList<String> items) {

        }
    }

    private void setText(JTextArea txtInput, JComboBox cbInput){
        int position = txtInput.getCaretPosition() +
                1 +                     // the length of the space
                Objects.requireNonNull(cbInput.getSelectedItem()).toString().length() -
                getPrefix(txtInput.getText()).length();
        txtInput.setText(insertCompletion(txtInput, cbInput));
        txtInput.setCaretPosition(position);
    }

    private String getPrefix(String content) {
        int position = txtInput.getCaretPosition();
        int start = Utility.getStartOfTypingWord(position-1, content);
        return content.substring(start + 1, position);
    }

    private String insertCompletion(JTextArea txtInput, JComboBox cbInput){
        int position = txtInput.getCaretPosition();
        String content = txtInput.getText();
        int start = Utility.getStartOfTypingWord(position-1, content);

        return content.substring(0, start+1) +
                Objects.requireNonNull(cbInput.getSelectedItem()).toString() +
                " " +
                content.substring(position); // TODO 此处需要修改，应为追加文本而非设置文本
    }

    /**
     * TODO
     * 原有的那个更新方法可以抛弃了，但是以下要求和逻辑还需要保留
     * 1、括号强制自动补全
     * 2、关键字的补全可能需要通过 position 来完善
     * （3、后续可能更改性能问题）
     */
    /*private void updateList(DocumentEvent e) {
        position = e.getOffset();

        try {
            content = txtInput.getText(0, position + 1);
        } catch (BadLocationException ble) {
            ble.printStackTrace();
        }

        if (e.getLength() != 1) {
            return;
        }

        if (Utility.isBracket(content.charAt(position), bracketMap)) {
            isKeyword = false;
            Utility.invokeBracket(content, position, bracketMap);
        }
    }*/


    private boolean isAdjusting(JComboBox cbInput) {
        if (cbInput.getClientProperty("is_adjusting") instanceof Boolean) {
            return (Boolean) cbInput.getClientProperty("is_adjusting");
        }
        return false;
    }

    private void setAdjusting(JComboBox cbInput, boolean adjusting) {
        cbInput.putClientProperty("is_adjusting", adjusting);
    }

    public static void enableAutoComplete(AutoComplete autoComplete){
        autoComplete.getTxtInput().getDocument().addDocumentListener(autoComplete.getDocListener());
    }

    public static void disableAutoComplete(AutoComplete autoComplete){
        autoComplete.getTxtInput().getDocument().removeDocumentListener(autoComplete.getDocListener());
    }


    public JTextArea getTxtInput() {
        return txtInput;
    }

    public documentListener getDocListener() {
        return docListener;
    }
}
