package SimpleEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * setting 目前包含字体设置
 *
 * @author pj
 */


public class Setting extends JFrame {
    // Set swing component
    private JComboBox fontType,fontSize;
    private JTextArea textArea;

    public Setting(JTextArea text) {
        // initialize the component
        this.textArea = text;
        setSize(420,220);
        setTitle("设置");
        setBackground(Color.white);
        setIconImage(new ImageIcon("icons/main.png").getImage());
        setLayout(null);
        //FONT FAMILY SETTINGS SECTION START
        fontType = new JComboBox();

        //GETTING ALL AVAILABLE FONT FOMILY NAMES
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        for (int i = 0; i < fonts.length; i++) {
            //Adding font family names to font[] array
            fontType.addItem(fonts[i]);
        }
        //Setting maximize size of the fontType ComboBox
        fontType.setMaximumSize(new Dimension(170, 30));
        fontType.setToolTipText("Font Type");

        JLabel typeTips = new JLabel("字体");
        typeTips.setBounds(20,20,40,30);
        fontType.setBounds(66,20,180,30);
        add(typeTips);
        add(fontType);


        //Adding Action Listener on fontType JComboBox
        fontType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                //Getting the selected fontType value from ComboBox
                String p = fontType.getSelectedItem().toString();
                //Getting size of the current font or text
                int s = textArea.getFont().getSize();
                textArea.setFont(new Font(p, Font.PLAIN, s));
            }
        });

        //FONT FAMILY SETTINGS SECTION END
        //FONT SIZE SETTINGS START
        fontSize = new JComboBox();

        for (int i = 5; i <= 100; i++) {
            fontSize.addItem(i);
        }
        fontSize.setMaximumSize(new Dimension(70, 30));
        fontSize.setToolTipText("Font Size");


        JLabel sizeTips = new JLabel("字号");
        sizeTips.setBounds(20,66,40,30);
        fontSize.setBounds(66,66,180,30);
        add(sizeTips);
        add(fontSize);

        fontSize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                String sizeValue = fontSize.getSelectedItem().toString();
                int sizeOfFont = Integer.parseInt(sizeValue);
                String fontFamily = textArea.getFont().getFamily();

                Font font1 = new Font(fontFamily, Font.PLAIN, sizeOfFont);
                textArea.setFont(font1);
            }
        });


        // set visible and we can add more attributions in this area
        setLocationRelativeTo(text);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    }


}

