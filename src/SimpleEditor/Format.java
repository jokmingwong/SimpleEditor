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
 * 格式化代码
 * 请直接调用Format.format(textArea);
 * @author LiNan
 **/
public class Format {


	public static void format(JTextArea textArea) {
		String string = textArea.getText();
    	string = Format.formatNow(string);
    	textArea.setText(string);
	}
	public static String formatNow(String string) {
		String newString = changeStrToUUid(string,"\"");
		newString = changeStrToUUid(newString,"'");
		newString = changeLineFeeds(newString,"\n","");
		newString = changeLineFeeds(newString,";",";\n");
		newString = changeLineFeeds(newString,"//","\n//");
		newString = changeLineFeeds(newString,"{","{\n");
		newString = changeLineFeeds(newString,"}","}\n");
		newString = changeLineFeeds(newString,"/*","\n/*\n");
		newString = changeLineFeeds(newString,"* @","\n* @");
		newString = changeLineFeeds(newString,"*/","\n*/\n");
		newString = changeIndent(newString,"\n");
		for(Map.Entry<String, String> r : mapZY.entrySet()){
			newString = newString.replace(r.getKey(),r.getValue());
		}
		if(newString==null)
			return string;
		return newString;
	}
	
	public static Map<String,String> mapZY = new HashMap<String,String>();
	/**
	 * 循环替换指定字符为随机uuid  并将uui存入全局map:mapZY   
	 * @param string   字符串
	 * @param type    指定字符
	 **/
	public static String changeStrToUUid(String string,String type){
	    Matcher slashMatcher = Pattern.compile(type).matcher(string);
	    boolean bool = false;
	    StringBuilder sb = new StringBuilder();
	    int indexHome = -1; //开始截取下标
	    while(slashMatcher.find()) {
	       int indexEnd = slashMatcher.start();
	       String tmp = string.substring(indexHome+1,indexEnd); //获取"号前面的数据
	       if(indexHome == -1 ||bool == false){
	    	   sb.append(tmp);
	    	   bool = true;
	    	   indexHome = indexEnd;
	       }else{
	    	   if(bool){
	    		   String tem2 = "";
	    		   for( int i=indexEnd-1 ; i>-1 ; i-- ){
		   				char c = string.charAt(i);
		   				if(c == '\\'){
		   					tem2 += c;
		   				}else{
		   					break;
		   				}
	   				}
	    		   int tem2Len = tem2.length();
		   			if(tem2Len>-1){
		   				if(tem2Len % 2==1){ 
		   					//奇数 非结束符
		   				}else{
		   					//偶数 结束符号
		   				   String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
		   				   uuid = type+uuid+type;
		   				   mapZY.put(uuid, type+tmp+type);
		   				   sb.append(uuid);
		   				   bool = false;
		   				   indexHome = indexEnd;
		   				}
		   			}
	    	   }
	       }
	    }
	    sb.append(string.substring(indexHome+1,string.length()));
	    return sb.toString();
	 }
	
	
	
	//处理换行
	public static String changeLineFeeds(String string,String a,String b){
		try{
			string = string.replace(a, "$<<linanA>>$<<linanB>>");
			String  arr[] = string.split("$<<linanA>>");
			StringBuilder result = new StringBuilder();
			if(arr != null){
				for(int i=0;i<arr.length;i++){
					String t = arr[i];
					result.append(t.trim());
					if(t.indexOf("//")!=-1 && "\n".equals(a)){
						result.append("\n");
					}
				}
			}
			String res = result.toString();
			res = res.replace("$<<linanB>>", b);
			res = res.replace("$<<linanA>>", "");
			return res;
		}catch(Exception e){
		}
		return null;
	}
	
	//处理缩进
	public static String changeIndent(String string,String a){
		try{
			String  arr[] = string.split(a);
			StringBuilder result = new StringBuilder();
			if(arr != null){
				String zbf = "    ";
				Stack<String> stack = new Stack<String>();
				for(int i=0;i<arr.length;i++){
					String tem = arr[i].trim();
					if(tem.indexOf("{")!=-1){
						String kg = getStack(stack,false);
						if(kg == null){
							result.append((tem+"\n"));
							kg = "";
						}else{
							kg = kg + zbf;
							result.append(kg+tem+"\n");	
						}
						stack.push(kg);
					}else if(tem.indexOf("}")!=-1){
						String kg = getStack(stack,true);
						if(kg == null){
							result.append(tem+"\n");
						}else{
							result.append(kg+tem+"\n");
						}
					}else{
						String kg = getStack(stack,false);
						if(kg == null){
							result.append(tem+"\n");
						}else{
							result.append(kg+zbf+tem+"\n");
						}
					}
				}
			}
			String res = result.toString();
			return res;
		}catch(Exception e){}
		return null;
	}
	
	/**
	 * @param stack 
	 * @param bool true 弹出  false 获取
	 **/
	public static String getStack(Stack<String> stack,boolean bool){
		String result = null;
		try{
			if(bool){
				return stack.pop();
			}
			return stack.peek();
		}catch(EmptyStackException  e){
		}
		return result;
	}
	
}