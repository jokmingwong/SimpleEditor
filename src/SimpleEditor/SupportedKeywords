package SimpleEditor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
/**
 * <h1>A class to store the programming language keywords and
 * provide access to them.</h1>
 *
 * <p>Makes multiple language support possible and makes adding new language
 * support convenient. To add more keywords, add a string array and getters
 * to this class. Then, update the switch statement in UI.java.</p>
 */
public class SupportedKeywords {

    private static String[] supportedLanguages = {".cpp",".java"};

    private static String[] java = {"abstract", "assert", "boolean",
            "break", "byte", "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else", "extends", "false",
            "final", "finally", "float", "for", "goto", "if", "implements",
            "import", "instanceof", "int", "System", "out", "print()", "println()",
            "new", "null", "package", "private", "protected", "public", "interface",
            "long", "native", "return", "short", "static", "strictfp", "super", "switch",
            "synchronized", "this", "throw", "throws", "transient", "true",
            "try", "void", "volatile", "while", "String"};

    private static String[] cpp = { "auto", "const", "double", "float", "int", "short",
                "struct", "unsigned", "break", "continue", "else", "for", "long", "signed",
                "switch", "void", "case", "default", "enum", "goto", "register", "sizeof",
                "typedef", "volatile", "char", "do", "extern", "if", "return", "static",
                "union", "while", "asm", "dynamic_cast", "namespace", "reinterpret_cast", "try",
                "bool", "explicit", "new", "static_cast", "typeid", "catch", "false", "operator",
                "template", "typename", "class", "friend", "private", "this", "using", "const_cast",
                "inline", "public", "throw", "virtual", "delete", "mutable", "protected", "true", "wchar_t" };

    public String[] getSupportedLanguages() {
        return supportedLanguages;
    }

    private static String[] brackets = { "{", "[","(" };
    private static String[] bCompletions = { "}","]", ")" };
    private static HashMap<String, String> bracketMap = new HashMap<>();
    static {
        bracketMap.put("{", "{}");
        bracketMap.put("[", "[]");
        bracketMap.put("(", "()");
    }
    public String[] getJavaKeywords() {
        return java;
    }
    public String[] getCppKeywords() {
        return cpp;
    }
    public ArrayList<String> getBracketCompletions() {
        return new ArrayList<>(Arrays.asList(bCompletions));
    }
    public ArrayList<String> getBrackets() {
        return new ArrayList<>(Arrays.asList(brackets));
    }
    public ArrayList<String> setKeywords(String[] arr) {
        return new ArrayList<>(Arrays.asList(arr));
    }

    public Map<String, String> getBracketMap(){
        return bracketMap;
    }

}
