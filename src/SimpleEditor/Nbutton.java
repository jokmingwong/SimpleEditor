package SimpleEditor;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 此类用于Button的自定义美化，提供给UI调用
 * 1.无边框button
 * 2.cover显示背景色
 * 3.按下显示
 *
 *
 * @author  pj
 */

public class Nbutton extends JButton {
    private Color roverColor = new Color(191, 191, 191);
    private Border emptyBorder = BorderFactory.createEmptyBorder(0,0, 0, 0);
    Nbutton(ImageIcon icon){
        super(icon);
        this.setOpaque(false);
        this.setBorder(emptyBorder);
        this.setContentAreaFilled(false);
        this.setFocusPainted(true);
        this.setRolloverEnabled(true);

        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                if (isRolloverEnabled()) {
                    setBackground(roverColor);
                    setContentAreaFilled(true);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (isRolloverEnabled()) {
                    setContentAreaFilled(false);
                }
            }
        });
    }



}
