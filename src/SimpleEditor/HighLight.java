package SimpleEditor;


import java.awt.*;
import javax.swing.text.*;


/**
 * Finish the highlight function and recall function
 * using class Highlight and the class JTextComponent
 *
 * @author adam
 */
public class HighLight extends DefaultHighlighter.DefaultHighlightPainter {
    /**
     * Construct HighLight using RGB value directly
     *
     * @param RGBValue RGB value is an integer to define a color
     */
    public HighLight(int RGBValue) {
        super(new Color(RGBValue));
    }

    /**
     * Construct function will be built by the color,
     * use it by new HighLight(new Color(RGB VALUE))
     *
     * @param color this is a element using new Color(int RGBValue) to create
     */
    public HighLight(Color color) {
        super(color);
    }


    /**
     * @param textComponent a java text component in java swing
     * @param pattern String list which are supported key words
     */
    public void paint(JTextComponent textComponent, String[] pattern) {
        try {
            cancelPaint(textComponent);
            Highlighter highlighter = textComponent.getHighlighter();
            Document document = textComponent.getDocument();
            String text = document.getText(0, document.getLength());
            // Find the word directly by using the method indexOf()
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