package SimpleEditor;

import java.awt.*;
import javax.swing.text.*;


public class HightLight extends DefaultHighlighter.DefaultHighlightPainter {

    public HightLight(Color color) {
        super(color);
    }

    public void paint(JTextComponent textComponent, String[] pattern) {
        try {
            cancelPaint(textComponent);
            Highlighter highlighter = textComponent.getHighlighter();
            Document document = textComponent.getDocument();
            String text = document.getText(0, document.getLength());
            for (String p : pattern) {
                int pos = 0;
                while ((pos = text.indexOf(p, pos)) >= 0) {
                    highlighter.addHighlight(pos, pos + p.length(), this);
                    pos += p.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void cancelPaint(JTextComponent textComponent) {

    }

}
