package SimpleEditor;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextArea;

/**
 * Format.java 格式化代码 请直接调用Format.format(textArea);
 * 
 * @author LiNan
 **/

public class Format {

	private static Map<String, String> mapZY = new HashMap<String, String>();
	
	public static void format(JTextArea textArea) {
		String string = textArea.getText();
		string = Format.formatNow(string);
		textArea.setText(string);
	}

	private static String changeIndent(String string, String a) {
		try {
			String arr[] = string.split(a);
			StringBuilder result = new StringBuilder();
			if (arr != null) {
				String _tab = "    ";
				Stack<String> stack = new Stack<String>();
				for (int i = 0; i < arr.length; i++) {
					String tmp = arr[i].trim();
					if (tmp.indexOf("{") != -1) {
						String str = getStack(stack, false);
						if (str == null) {
							result.append((tmp + "\n"));
							str = "";
						} else {
							str = str + _tab;
							result.append(str + tmp + "\n");
						}
						stack.push(str);
					} else if (tmp.indexOf("}") != -1) {
						String str = getStack(stack, true);
						if (str == null) {
							result.append(tmp + "\n");
						} else {
							result.append(str + tmp + "\n");
						}
					} else {
						String str = getStack(stack, false);
						if (str == null) {
							result.append(tmp + "\n");
						} else {
							result.append(str + _tab + tmp + "\n");
						}
					}
				}
			}
			String res = result.toString();
			return res;
		} catch (Exception e) {
		}
		return null;
	}

	private static String changeLineFeeds(String string, String a, String b) {
		try {
			string = string.replace(a, "$<<linanA>>$<<linanB>>");
			String arr[] = string.split("$<<linanA>>");
			StringBuilder result = new StringBuilder();
			if (arr != null) {
				for (int i = 0; i < arr.length; i++) {
					String t = arr[i];
					result.append(t.trim());
					if (t.indexOf("//") != -1 && "\n".equals(a)) {
						result.append("\n");
					}
				}
			}
			String res = result.toString();
			res = res.replace("$<<linanB>>", b);
			res = res.replace("$<<linanA>>", "");
			return res;
		} catch (Exception e) {
		}
		return null;
	}

	private static String changeStrToUUid(String string, String type) {
		Matcher matcher = Pattern.compile(type).matcher(string);
		boolean bool = false;
		StringBuilder sb = new StringBuilder();
		int indexFront = -1;
		while (matcher.find()) {
			int indexEnd = matcher.start();
			String tmp = string.substring(indexFront + 1, indexEnd);
			if (indexFront == -1 || bool == false) {
				sb.append(tmp);
				bool = true;
				indexFront = indexEnd;
			} else {
				if (bool) {
					String tmp2 = "";
					for (int i = indexEnd - 1; i > -1; i--) {
						char c = string.charAt(i);
						if (c == '\\') {
							tmp2 += c;
						} else {
							break;
						}
					}
					int tmp2Len = tmp2.length();
					if (tmp2Len > -1) {
						if (tmp2Len % 2 == 1) {

						} else {

							String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
							uuid = type + uuid + type;
							mapZY.put(uuid, type + tmp + type);
							sb.append(uuid);
							bool = false;
							indexFront = indexEnd;
						}
					}
				}
			}
		}
		sb.append(string.substring(indexFront + 1, string.length()));
		return sb.toString();
	}


	private static String formatNow(String string) {
		String newString = changeStrToUUid(string, "\"");
		newString = changeStrToUUid(newString, "'");
		newString = changeLineFeeds(newString, "\n", "");
		newString = changeLineFeeds(newString, ";", ";\n");
		newString = changeLineFeeds(newString, "//", "\n//");
		newString = changeLineFeeds(newString, "{", "{\n");
		newString = changeLineFeeds(newString, "}", "}\n");
		newString = changeLineFeeds(newString, "/*", "\n/*\n");
		newString = changeLineFeeds(newString, "* @", "\n* @");
		newString = changeLineFeeds(newString, "*/", "\n*/\n");
		newString = changeIndent(newString, "\n");
		for (Map.Entry<String, String> r : mapZY.entrySet()) {
			newString = newString.replace(r.getKey(), r.getValue());
		}
		if (newString == null)
			return string;
		return newString;
	}

	private static String getStack(Stack<String> stack, boolean bool) {
		String result = null;
		try {
			if (bool) {
				return stack.pop();
			}
			return stack.peek();
		} catch (EmptyStackException e) {
		}
		return result;
	}

}
