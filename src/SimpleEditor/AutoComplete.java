package simplejavatexteditor;

import java.util.ArrayList;
import java.util.Map;
import java.util.Collections;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import SimpleEditor.SupportedKeywords;

/**
 * 此类用于自动补全 自动补全的范围包括： C++/Java
 * 关键字，括号，引号，以及文本之前已经出现过的自定义方法名、变量名、变量名
 * 
 * @author dengkunquan
 * 
 */

public class AutoComplete implements DocumentListener {

    private Map<String, String> bracketMap;
    private SupportedKeywords sk;

    private final UI ui;
    private final JTextArea textArea;
    private static final String COMMIT_ACTION = "commit";
    private int position;
    private String content;

    public AutoComplete(UI ui, ArrayList<String> words) {
        this.wordsInContent = words.clone();

        this.brackets = sk.getBrackets();
        bracketMap = sk.getBracketMap();

        // access the editor
        this.ui = ui;
        this.textArea = ui.getEditor();

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
    public void insertUpdate(DocumentEvent e){
        this.position = e.getOffset();
        this.content = null;

        try{
            this.content = this.textArea.getText(0, position + 1);
        } catch (BadLocationException badLocationException){
            badLocationException.printStackTrace();
        }

        if(e.getLength() != 1){
            return;
        }

    }

}
