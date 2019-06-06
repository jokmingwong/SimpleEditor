package SimpleEditor;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

/**
 * 此类用于自动补全 自动补全的范围包括： C++/Java
 * 关键字，括号，引号，以及文本之前已经出现过的自定义方法名、变量名、变量名
 *
 * @author dengkunquan
 */

public class AutoComplete implements DocumentListener {

    private Map<String, String> bracketMap;
    private SupportedKeywords sk;

    private enum Mode {
        INSERTING, COMPLETED
    }

    private boolean isKeyword;
    private Mode mode = Mode.INSERTING;
    private final UI ui;
    private final JTextArea textArea;
    private static final String COMMIT_ACTION = "commit";
    private int position;
    private String content;
    private ArrayList<String> wordsInContent;


    public AutoComplete(UI ui, ArrayList<String> words) {
        this.wordsInContent = (ArrayList<String>) words.clone();

        this.bracketMap = sk.getBracketMap();

        // access the editor
        this.ui = ui;
        this.textArea = ui.getTextArea();

        // set the handler for the enter key
        InputMap inputMap = textArea.getInputMap();
        ActionMap actionMap = textArea.getActionMap();
        inputMap.put(KeyStroke.getKeyStroke("ENTER"), COMMIT_ACTION);
        actionMap.put(COMMIT_ACTION, new CommitAction());

        Collections.sort(words);
    }


    /**
     * Whenever a character has been typed in to the editor, this method is actived to find whether it has any keyword completion.
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        this.position = e.getOffset();
        this.content = null;

        try {
            this.content = this.textArea.getText(0, position + 1);
        } catch (BadLocationException badLocationException) {
            badLocationException.printStackTrace();
        }

        if (e.getLength() != 1) {
            return;
        }

        this.isKeyword = isBracket(content.charAt(position), position);

        //Get the beginning of the word being typed
        int start = getStartOfTypingWord();

        //Auto complete will start
        //after two characters are typed
        if (start + 2 > this.position) {
            return;
        }

        //Search for a match on the word being typed
        //in the keywords ArrayList
        String prefix = content.substring(start + 1);

        // TODO 这里应该是从词库里找出拥有这个前缀的某一个word的位置，但我们要的是全部word的位置
        // TODO 所以可以改成用trie树方便检索（不然每次都遍历太慢了）
        int n = -Collections.binarySearch(wordsInContent, prefix);

        if (0 < n && n < wordsInContent.size()) {
            String match = wordsInContent.get(n - 1);

            if (match.startsWith(prefix)) {
                String completion = match.substring(position - start);
                isKeyword = true;
                SwingUtilities.invokeLater(
                        new CompletionTask(completion, position + 1));
            } else {
                mode = Mode.INSERTING;
            }
        }

    }



    private int getStartOfTypingWord(){
        for (int start = this.position; start >= 0; start--) {
            if (!Character.isLetter(content.charAt(start))) {
                return start;
            }
        }
        return 0;
    }



    private boolean isBracket(char c, int pos) {
        //String of the last typed character
        String s = String.valueOf(c);

        for (String key : bracketMap.keySet()) {
            if (key.equals(s)) {
                SwingUtilities.invokeLater(
                        new CompletionTask(bracketMap.get(key), pos + 1));
                return false;
            }
        }
        return true;
    }





    /**
     * Handles the auto complete suggestion
     * generated when the user is typing a
     * word that matches a keyword.
     */
    private class CompletionTask
            implements Runnable {

        private final String completion;
        private final int position;

        public CompletionTask(String completion, int position) {
            this.completion = completion;
            this.position = position;
        }

        @Override
        public void run() {
            textArea.insert(completion, position);

            textArea.setCaretPosition(position + completion.length());
            textArea.moveCaretPosition(position);
            mode = Mode.COMPLETED;
            if (!isKeyword) {
                textArea.addKeyListener(new HandleBracketEvent());
            }
        }
    }


    /**
     * Enter key is pressed in response to an
     * auto complete suggestion. Respond
     * appropriately.
     */
    private class CommitAction
            extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (mode == Mode.COMPLETED) {
                int pos = textArea.getSelectionEnd();

                if (isKeyword) {
                    textArea.insert(" ", pos);
                    textArea.setCaretPosition(pos + 1);
                    mode = Mode.INSERTING;
                }
            } else {
                mode = Mode.INSERTING;
                textArea.replaceSelection("\n");
            }
        }
    }

    /**
     * Additional logic for bracket auto complete
     */
    private class HandleBracketEvent
            implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            //Bracket auto complete needs special attention.
            //Multiple possible responses are needed.
            String keyEvent = String.valueOf(e.getKeyChar());
            for (String bracketCompletion : bracketMap.values()) {
                if (keyEvent.equals(bracketCompletion)) {
                    textArea.replaceRange("", position, position + 1);
                    mode = Mode.INSERTING;
                    textArea.removeKeyListener(this);
                }
            }
            int currentPosition = textArea.getCaretPosition();
            switch (e.getKeyChar()) {
                case '\n':
                    textArea.insert("\n\n", currentPosition);
                    textArea.setCaretPosition(currentPosition + 1);
                    mode = Mode.INSERTING;
                    textArea.removeKeyListener(this);
                    break;
                default:
                    textArea.setCaretPosition(position);
                    mode = Mode.INSERTING;
                    textArea.removeKeyListener(this);
                    break;
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {}
    }


    @Override
    public void removeUpdate(DocumentEvent e) {}

    @Override
    public void changedUpdate(DocumentEvent e) {}
<<<<<<< HEAD
}
=======
}
>>>>>>> f6fcf86eb8d9c694ca54c8820425cde5810ea527
