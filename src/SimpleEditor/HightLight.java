package SimpleEditor;

import java.awt.*;
import javax.swing.text.*;


public class HightLight extends DefaultHighlighter.DefaultHighlightPainter {

    public HightLight(Color color) {
        super(color);
    }

    public void paint(JTextComponent textComponent, String[] pattern) {

    }

    public void cancelPaint(JTextComponent textComponent) {

    }

}
