package SimpleEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FindWord extends JFrame implements ActionListener {
    // Set a action listener
    // private static final long serialVersionUID=1L;
    private int startIndex = 0, select_from = 1;

    // Set swing component
    private JTextField textFind, textReplace;
    private JButton findButton, findNext, replace, replaceAll, cancel;
    private JTextArea text;

    public FindWord(JTextArea text) {
        // initialize the component
        this.text = text;
        setSize(460,230);
        setTitle("查找");
        setBackground(Color.white);
        setIconImage(new ImageIcon("icons/main.png").getImage());

        JLabel label1 = new JLabel("Find:");
        JLabel label2 = new JLabel("Replace:");
        textFind = new JTextField(50);
        textFind.setFont(new Font("Consolas", Font.BOLD, 13));

        textReplace = new JTextField(50);
        findButton = new JButton("Find:");
        findNext = new JButton("Find next:");
        replace = new JButton("Replace");
        replaceAll = new JButton("Replace all:");
        cancel = new JButton("Cancel");

        // NOT UNDERSTAND
        setLayout(null);

        // Set label size
        int labelWidth = 50;
        int labelHeight = 30;

        // Label and text bound designation
        label1.setBounds(10, 10, labelWidth, labelHeight);
        add(label1);

        textFind.setBounds(10 + labelWidth, 10, 120, 20);
        add(textFind);

        label2.setBounds(10, 10 + labelHeight + 10, labelWidth, labelHeight);
        add(label2);

        textReplace.setBounds(10 + labelWidth, 10 + labelHeight + 10, 120, 20);
        add(textReplace);

        // Button bound designation and add listener
        findButton.setBounds(250, 5, 115, 20);
        add(findButton);
        findButton.addActionListener(this);

        findNext.setBounds(250, 30, 115, 20);
        add(findNext);
        findNext.addActionListener(this);

        replace.setBounds(250, 50, 115, 20);
        add(replace);
        replace.addActionListener(this);

        replaceAll.setBounds(250, 72, 115, 20);
        add(replaceAll);
        replaceAll.addActionListener(this);

        cancel.setBounds(250, 94, 115, 20);
        add(cancel);
        cancel.addActionListener(this);

        // set visible and we can add more attributions in this area
        setLocationRelativeTo(text);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void find() {
        // Using indexOf method to search
        select_from = text.getText().indexOf(textFind.getText());

        // The result of not found
        if (select_from == -1) {
            startIndex = 0;
            JOptionPane.showMessageDialog(null, "Cannot find the content!");
            return;
        } else if (select_from == text.getText().lastIndexOf(textFind.getText())) {
            startIndex = 0;
        }
        int select_to = select_from + textFind.getText().length();
        text.select(select_from, select_to);
    }

    public void findNext() {
        String selection = text.getSelectedText();
        // verify whether it is null
        if (selection == null) {
            selection = textFind.getText();
            if (selection == null) {
                selection = JOptionPane.showInputDialog("Find:");
                textFind.setText(selection);
            }
        }
        // In case that the selectStart or selectEnd is null value
        try {
            int selectStart = text.getText().indexOf(selection, startIndex);
            int selectEnd = selectStart + selection.length();
            text.select(selectStart, selectEnd);
            startIndex = selectEnd + 1;

            if (selectStart == text.getText().lastIndexOf(selection)) {
                startIndex = 0;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void replace() {
        try {
            find();
            if (select_from != -1)
                text.replaceSelection(textReplace.getText());
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    void replaceAll() {
        text.setText(text.getText().replaceAll(textFind.getText(), textReplace.getText()));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == findButton)
            find();

        else if (actionEvent.getSource() == findNext)
            findNext();

        else if (actionEvent.getSource() == replace)
            replace();

        else if (actionEvent.getSource() == replaceAll)
            replaceAll();

        else if (actionEvent.getSource() == cancel)
            this.setVisible(false);
    }

}
