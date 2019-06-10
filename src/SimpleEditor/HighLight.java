package SimpleEditor;


import java.awt.*;
import java.util.TreeSet;
import javax.swing.text.*;


/**
 * Finish the highlight function and recall function
 * using class Highlight and the class JTextComponent
 *
 * @author adam
 */
class HighLight extends DefaultHighlighter.DefaultHighlightPainter {
    /**
     * Construct HighLight using RGB value directly
     *
     * @param RGBValue RGB value is an integer to define a color
     */
    HighLight(int RGBValue) {
        super(new Color(RGBValue));
    }

    /**
     * Construct function will be built by the color,
     * use it by new HighLight(new Color(RGB VALUE))
     *
     * @param color this is a element using new Color(int RGBValue) to create
     */
    HighLight(Color color) {
        super(color);
    }


    /**
     * @param textComponent a java text component in java swing
     * @param pattern String list which are supported key words
     */
    void paint(JTextComponent textComponent, String[] pattern) {
        try {
            cancelPaint(textComponent);
            Highlighter highlighter = textComponent.getHighlighter();
            Document document = textComponent.getDocument();
            String text = document.getText(0, document.getLength());
            // Find the word directly by using the method indexOf()

            // fix the bug of index error @author Adam
            for (String p : pattern) {
                int pos = 0;
                for (;(pos = text.indexOf(p, pos)) >= 0;pos+=p.length()) {
                    //if(pos==0||(pos+p.length()==text.length())||(text.charAt(pos-1)==' '&&text.charAt(pos+p.length())==' ')) {

                    if(pos>0&&(Character.isLetter(text.charAt(pos-1))||Character.isDigit(text.charAt(pos-1))))continue;
                    if(pos+p.length()<text.length()&&(Character.isLetter(pos+p.length())||Character.isDigit(pos+p.length())))continue;

                    highlighter.addHighlight(pos, pos + p.length(), this);
                   // }
                }
            }

         /*   TreeSet<String>patternTreeSet=new TreeSet<>();
            for(String pp:pattern){
                patternTreeSet.add(pp);
            }
            StringBuilder word;
            int len=text.length();
            for(int pos=0;pos<len;pos++){
                if(text.charAt(pos)==' ')continue;
                word=new StringBuilder("");
                while (text.charAt(pos)!=' '&& pos<len){
                    word.append(text.charAt(pos));
                    pos++;
                }
                if(patternTreeSet.contains(word.toString())){
                    highlighter.addHighlight(pos-word.length(), pos, this);
                }
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param textComponent The text we want to cancel the highlight
     * Cancel the highlight paint on the text component
     */
    private void cancelPaint(JTextComponent textComponent) {
        Highlighter ht = textComponent.getHighlighter();
        Highlighter.Highlight[] highlights = ht.getHighlights();
        for (Highlighter.Highlight h : highlights) {
            if (h.getPainter() instanceof HighLight)
                ht.removeHighlight(h);
        }
    }

}