/**
 * @name        Simple Java NotePad
 * @package     ph.notepad
 * @file        UI.java
 * @author      SORIA Pierre-Henry
 * @email       pierrehs@hotmail.com
 * @link        http://github.com/pH-7
 * @copyright   Copyright Pierre-Henry SORIA, All Rights Reserved.
 * @license     Apache (http://www.apache.org/licenses/LICENSE-2.0)
 * @create      2012-05-04
 * @update      2015-09-4
 *
 *
 * @modifiedby  Achintha Gunasekara
 * @modweb      http://www.achinthagunasekara.com
 * @modemail    contact@achinthagunasekara.com
 *
 * @Modifiedby SidaDan
 * @modemail Fschultz@sinf.de
 * Bug fixed. If JTextArea txt not empty and the user will
 * shutdown the Simple Java NotePad, then the Simple Java NotePad
 * is only hidden (still running). So I added a WindowListener
 * an call method dispose() for this frame.
 * Tested with java 8.
 */

package PandaEditor;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
/**
 * 此类用于显示各种关于信息
 * 1.关于我们
 * 2.关于软件
 *
 *
 * @author  pj
 */

public class About {

    private final JFrame frame;
    private final JPanel panel;
    private String contentText;
    private final JLabel text;

    public About(UI ui) {
        panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        frame = new JFrame();

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });


        frame.setVisible(true);
        frame.setSize(500,300);
        frame.setLocationRelativeTo(ui);
        text = new JLabel();
    }

    public void me() {
        frame.setTitle("About Me - 开发团队");

        contentText =
        "<html><body><p>" +
        "Author: <br />" + "16141031 彭婧<br />"+
        "16151091 李楠<br />"+
        "16031141 黄钰铭<br />"+
        "16041228 邓坤权<br />"+
        "Contact me at: " +
        "<a href='mailto:16141031@buaa.edu.cn?subject=about us'>" + "16141031@buaa.edu.cn" + "</a>" +
                "<br /><br />" + "Contact me at:<br /> WeChat:inskil<br />" +
        "</p></body></html>";

        text.setText(contentText);
        panel.add(text);
        frame.add(panel);
    }

    public void software() {
        frame.setTitle("About Me - 软件说明" );

        contentText =
        "<html><body><p>" +
        "Name: 简单代码编辑器<br />" +
        "Version: 1.0" +
        "</p></body></html>";

        text.setText(contentText);
        panel.add(text);
        frame.add(panel);
    }

}