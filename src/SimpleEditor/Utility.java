package SimpleEditor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;
import java.util.Set;

public class Utility {
    public static Set<String> getSpaceSet() {
        return spaceSet;
    }

    private static Set<String> spaceSet = SupportedKeywords.spaceSet;

    static int getStartOfTypingWord(int position, String content){
        int start = position;
        for (; start >= 0; start--) {
            if (!Character.isLetter(content.charAt(start))) {
                break;
            }
        }
        return start;
    }

    static boolean isBracket(char c, Map<String, String> bracketMap) {
        //String of the last typed character
        String s = String.valueOf(c);

        return bracketMap.keySet().contains(s);
    }

    static void invokeBracket(String content, int position, Map<String, String> bracketMap){
        SwingUtilities.invokeLater(
                new CompletionTask(
                        bracketMap.get(content.substring(position, position+1)),
                        position + 1));
    }


    /**
     * Handles the auto complete suggestion
     * generated when the user is typing a
     * word that matches a keyword.
     */
    static class CompletionTask
            implements Runnable {

        private final String completion;
        private final int position;

        public CompletionTask(String completion, int position) {
            this.completion = completion;
            this.position = position;
        }

        @Override
        public void run() {

        }
    }


    /**
     * Enter key is pressed in response to an
     * auto complete suggestion. Respond
     * appropriately.
     */
    static class CommitAction
            extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    /**
     * Additional logic for bracket auto complete
     */
    static class HandleBracketEvent
            implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {}
    }
}
