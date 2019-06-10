package SimpleEditor;

import javax.swing.*;
import java.util.*;

/**
 * 一个工具类，用于支持 AutoComplete 类的实现
 *
 * @author dengkunquan
 * @date 2019-06-09
 */
class Utility {
    private static Set<String> getSpaceSet() {
        return spaceSet;
    }

    private static Set<String> spaceSet = SupportedKeywords.getSpaceSet();

    private static Map<String, String> brackmap = SupportedKeywords.getBracketMap();

    /**
     * 用于寻找结尾在position位置的单词的开头的前一位的位置
     *
     * @param position 单词末尾位置
     * @param content  总文本
     * @return 该单词的第一个元素（包括字母、数字、-）的前一位
     */
    private static int getStartOfTypingWord(int position, String content) {
        int len = content.length();
        int start = position >= len ? len - 1 : position;
        for (; start >= 0; start--) {
            if (!Character.isLetter(content.charAt(start)) &&
                    !Character.isDigit(content.charAt(start)) &&
                    content.charAt(start) != '_' &&
                    content.charAt(start) != '-') {
                break;
            }
        }
        return start;
    }

    private static int getEndOfTypingWord(int position, String content) {
        int len = content.length();
        int end = position >= len ? len - 1 : position;
        for (; end <= len - 1; end++) {
            if (!Character.isLetter(content.charAt(end)) &&
                    !Character.isDigit(content.charAt(end)) &&
                    content.charAt(end) != '_' &&
                    content.charAt(end) != '-') {
                break;
            }
        }
        return end;
    }


    /**
     * 用于支持括号补全
     * 功能是用于判断光标前面是不是括号，若是左括号且没有组成括号对则补全它
     *
     * @param txtInput   JTextArea
     * @param bracketMap 括号映射，"(":"()"，分别有大中小括号
     * @return 是否是单个左括号，若是则补全并返回true，否则什么都不干并且返回false
     */
    static boolean checkForBracket(JTextArea txtInput, Map<String, String> bracketMap) {
        // 这段代码是用于括号补全的
        int position = txtInput.getCaretPosition() - 1;
        String content = txtInput.getText();
        if (position >= 0) {
            char c = content.charAt(position);
            String s = String.valueOf(c);

            if (!isBracketPair(content, position)
                    && Utility.isBracket(s, bracketMap)) {
                int start = Utility.getStartOfTypingWord(position, content);
                txtInput.setText(
                        Utility.generateNewContext(content,
                                bracketMap.get(s),
                                start,
                                position));
                return true;
            }
        }

        return false;
    }

    /**
     * 用于支持checkForBracket方法，判断某个光标左右是否能组成一对括号对
     *
     * @param content  全文文本
     * @param position 光标位置
     * @return 光标左右是否能组成一对括号对
     */
    private static boolean isBracketPair(String content, int position) {
        int len = content.length();
        if (position + 1 >= len || position < 0) {
            return false;
        }

        char c = content.charAt(position);
        String s = String.valueOf(c);
        char next_c = content.charAt(position + 1);
        String next_s = String.valueOf(next_c);
        return brackmap.values().contains(s + next_s);
    }

    /**
     * 判断某个字符串是否左括号
     *
     * @param s          需要判断是否左大/中/小括号的字符串
     * @param bracketMap 括号映射，key为左大/中/小括号，value分别对应为大/中/小括号对
     * @return 字符串s是否左大/中/小括号
     */
    private static boolean isBracket(String s, Map<String, String> bracketMap) {
        return bracketMap.keySet().contains(s);
    }


    /**
     * 分离文本为一个个单词
     *
     * @param content 需要分离单词的文本
     * @return String数组，包含文本中的所有的单词（单词的界定由空白字符决定）
     */
    private static String[] getAllWords(String content) {
        String[] words = content.split("[\\s+`1234567890-=\\[\\];',./~!@#$%^&*(){}|:\"<>?]");
        ArrayList<String> newWords = new ArrayList<>();
        addAllWordWithoutEmpty(newWords, words);
        return newWords.toArray(new String[0]);
    }

    static String[] getAllWords(String content, int position) {
        String[] words = content.split("[\\s+`1234567890-=\\[\\];',./~!@#$%^&*(){}|:\"<>?]");
        int len = content.length();
        position = position >= len ? len : position;
        ArrayList<String> newWords = new ArrayList<>();
        addAllWordWithoutEmpty(newWords, words);
        return newWords.toArray(new String[0]);
    }

    private static void addAllWordWithoutEmpty(ArrayList<String> newWords,
                                               String[] words) {
        for (String word : words) {
            if (!word.isEmpty()) {
                newWords.add(word);
            }
        }
    }

    /**
     * 用于将选中的单词插入到JTextArea中
     *
     * @param txtInput JTextArea
     * @param cbInput  JComBox，候选框
     */
    static void setText(JTextArea txtInput, JComboBox cbInput) {
        int position = txtInput.getCaretPosition();
        txtInput.setText(insertCompletion(txtInput, cbInput));

        position = getEndOfTypingWord(position, txtInput.getText()) + 1;
        txtInput.setCaretPosition(position);
    }


    /**
     * 用于获得光标所指 word 的前缀
     * 这里的前缀是指word开头到光标的子串
     *
     * @param content  总文本，避免重复读取文本造成效率降低
     * @param position 一个位置，指向某一个可见字母
     * @return 光标所指 word 的前缀
     */
    static String getPrefixForInsert(String content, int position) {
        int start = Utility.getStartOfTypingWord(position, content);
        String prefix = content.substring(start + 1, position + 1 >= start + 1 ? position + 1 : start + 1);       // 分离出来便于调试
        return prefix;
    }

    /**
     * 利用关键词生成新的文本框文本，用于支持关键词的补全操作
     *
     * @param txtInput JTextArea，文本输入区域
     * @param cbInput  JComboBox，候选框
     * @return 新的文本
     */
    private static String insertCompletion(JTextArea txtInput, JComboBox cbInput) {
        int position = txtInput.getCaretPosition();
        String content = txtInput.getText();
        int start = Utility.getStartOfTypingWord(position - 1, content);

        return generateNewContext(content,
                Objects.requireNonNull(cbInput.getSelectedItem()).toString() + " ",
                start,
                position);
    }

    /**
     * 用于生成新的文本区域文本，由于补全关键词和括号时候，实现逻辑不同，因此将此功能提取成一个独立的method
     *
     * @param content       原始文本
     * @param insertString  要补全的文本
     * @param startOfWord   要补全的文本在原始文本中已有的前缀的起始位置
     * @param caretPosition 光标位置
     * @return 补全文本后的新的总文本
     */
    private static String generateNewContext(String content, String insertString, int startOfWord, int caretPosition) {
        return content.substring(0, startOfWord + 1) +
                insertString +
                content.substring(caretPosition);
    }

    /**
     * 用于判断光标左右是否都是空白字符
     *
     * @param content  总文本
     * @param position 光标位置
     * @return 光标左右是否都是空白字符，若是则返回true，否则返回false
     */
    static boolean isEmpty(String content, int position) {
        int len = content.length();
        if (position < 0 || position == len) {
            return true;
        } else {
            return Utility.getSpaceSet().contains(content.substring(position, position + 1));
        }
    }

    /**
     * 用于更新自动补全的候选框中的内容。
     * 本方法会将前缀为prefix的关键词以及文本编辑区域中已经出现过的变量名
     * 按照出现频率添加到候选框中，保证使用频率高的词语处于候选区域的上部，
     * 方便用户使用。
     * 机制：自带关键词默认为1，再加上它在文本编辑区域中的出现频率
     *
     * @param content 全文文本
     * @param items   java/C++关键词
     * @param prefix  用户输入的前缀
     * @param model   补全候选框
     */
    static void updateModel(String content,
                            ArrayList<String> items,
                            String prefix,
                            DefaultComboBoxModel<String> model) {

        String[] words = Utility.getAllWords(content);
        TernarySearchTrie<Integer> trie = new TernarySearchTrie<Integer>();
        putAllWordsIntoTrie(trie, words);

        Map<String, Integer> newItems = new HashMap<>();
        putKeywordIntoMap(newItems, items, prefix);

        Iterable<String> keys = trie.keysWithPrefix(prefix);
        putAllWordsIntoMap(newItems, trie, keys);

        ArrayList<String> Items = updateItem(newItems);
        addAllElementIntoModel(prefix, model, Items);
    }

    private static void putAllWordsIntoTrie(TernarySearchTrie<Integer> trie,
                                            String[] words) {
        for (String word : words) {
            Integer val = trie.get(word);
            val = val == null ? 0 : val;
            trie.put(
                    word,
                    val + 1
            );
        }
    }

    private static void putKeywordIntoMap(Map<String, Integer> newItems,
                                          ArrayList<String> items,
                                          String prefix) {
        for (String item : items) {
            if (item.startsWith(prefix)) {
                newItems.put(item,
                        (newItems.get(item) == null ? 0 : newItems.get(item)) + 1);
            }
        }
    }

    private static void putAllWordsIntoMap(Map<String, Integer> newItems,
                                           TernarySearchTrie<Integer> trie,
                                           Iterable<String> keys) {
        for (String key : keys) {
            newItems.put(key,
                    (newItems.get(key) == null ? 0 : newItems.get(key)) + trie.get(key));
        }
    }

    private static void addAllElementIntoModel(String prefix,
                                               DefaultComboBoxModel<String> model,
                                               ArrayList<String> Items) {
        for (String item : Items) {
            if (item.startsWith(prefix)) {
                model.addElement(item);
            }
        }
    }

    // 依赖于三叉树的词频统计
    // 统计所有词，然后排序
    private static ArrayList<String> updateItem(Map<String, Integer> items) {
        ArrayList<String> Items = new ArrayList<>(items.keySet());
        Collections.sort(Items, new Comparator<String>() {
            @Override
            public int compare(String key1, String key2) {

                return items.get(key2) - items.get(key1);
            }
        });
        return Items;
    }

    /**
     * 判断程序是否正在调整候选框内容
     *
     * @param cbInput JComboBox 候选框
     * @return 候选框内容是否正在调整
     */
    static boolean isAdjusting(JComboBox cbInput) {
        if (cbInput.getClientProperty("is_adjusting") instanceof Boolean) {
            return (Boolean) cbInput.getClientProperty("is_adjusting");
        }
        return false;
    }

    /**
     * 设置候选框的是否正在调整
     *
     * @param cbInput   JComboBox 候选框
     * @param adjusting 是否正在调整候选框内容
     */
    static void setAdjusting(JComboBox cbInput, boolean adjusting) {
        cbInput.putClientProperty("is_adjusting", adjusting);
    }
}
