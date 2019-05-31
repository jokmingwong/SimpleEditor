package SimpleEditor;

import java.awt.*;
import javax.swing.text.*;


public class HighLight extends DefaultHighlighter.DefaultHighlightPainter {

    public HighLight(Color color) {
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
        Highlighter ht = textComponent.getHighlighter();
        Highlighter.Highlight[] highlights = ht.getHighlights();
        for (Highlighter.Highlight h : highlights) {
            if (h.getPainter() instanceof HighLight)
                ht.removeHighlight(h);
        }
    }

}
