import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMatcher {
	Pattern p1,p2,p3,p4,p5,p6,p7,p8,p9;
	public RegexMatcher(){
		p1=Pattern.compile("[\n\"]");
		p2 = Pattern.compile("\\s+");
		p3 = Pattern.compile("[^\\p{ASCII}]");
		p4 = Pattern.compile("(\\{\\{Infobox\\s+)([\\w\\.\\s]*)(|)");
		p5 = Pattern.compile("(\\[\\[Category:)([\\w\\s]*)");
		p6 = Pattern.compile("(\\[\\[)([a-zA-Z\\d\\s\\(\\)|_,\\-']+)(\\]\\])");
		p7 = Pattern.compile("(\\{\\{)(main)([a-zA-Z\\s\\(\\)\\-'_\\d,|]+)(\\}\\})");
		p8 = Pattern.compile("(#REDIRECT\\s*?)(\\[\\[([a-zA-Z\\d\\s_\\-]+)\\]\\])");
		p9 = Pattern.compile("(<.*?>.*?<.*?/.*?>)|(&lt;.*?/.*&gt;)|(\\[\\[.*?\\]\\])|(<!--.*?-->)|(\\s+)|(\\[http.*?[:?//*\\./&]+.*?\\])");
	}

	public StringBuffer matchNewLine(StringBuffer s){
		Matcher m;
		boolean result;
		StringBuffer parsedresult;
        m = p1.matcher(s);
        parsedresult=new StringBuffer();
        result = m.find();
        while(result) {
            m.appendReplacement(parsedresult, " ");
            result = m.find();
        }
        m.appendTail(parsedresult);
        return parsedresult;
	}
	public StringBuffer matchSpaces(StringBuffer s){
		Matcher m;
		boolean result;
		StringBuffer parsedresult;
        m = p2.matcher(s);
        parsedresult=new StringBuffer();
        result = m.find();
        while(result) {
            m.appendReplacement(parsedresult, " ");
            result = m.find();
        }
        m.appendTail(parsedresult);
        return parsedresult;
	}
	public StringBuffer matchAscii(StringBuffer s){
		Matcher m;
		boolean result;
		StringBuffer parsedresult;
        m = p3.matcher(s);
        parsedresult=new StringBuffer();
        result = m.find();
        while(result) {
            m.appendReplacement(parsedresult, " ");
            result = m.find();
        }
        m.appendTail(parsedresult);
        return parsedresult;
	}
	public StringBuffer getInfo(StringBuffer s){
		Matcher m;
		boolean result;
		StringBuffer parsedresult;
		StringBuffer output=new StringBuffer();
		m = p4.matcher(s);
		parsedresult=new StringBuffer();
		result = m.find();
        while(result) {
        	output.append(m.group(2)+" ");
            m.appendReplacement(parsedresult, " ");
            result = m.find();
        }
        m.appendTail(parsedresult);
        return output;
	}
	public StringBuffer removeCamel(StringBuffer s){
		StringBuffer output=new StringBuffer();
		for(int i=0;i<s.length();i++){
			if(Character.isLowerCase(s.charAt(i)))			output.append(s.charAt(i));
			else output.append(" "+Character.toLowerCase(s.charAt(i)));
		}
		return output;
	}
	public StringBuffer getCtg(StringBuffer s){
		Matcher m;
		boolean result;
		StringBuffer parsedresult;
		StringBuffer output=new StringBuffer();
		m = p5.matcher(s);
		parsedresult=new StringBuffer();
		result = m.find();
        while(result) {
        	output.append(m.group(2)+" ");
            m.appendReplacement(parsedresult, " ");
            result = m.find();
        }
        m.appendTail(parsedresult);
        return output;
	}
	
	public StringBuffer matchHPData(StringBuffer s){
		Matcher m;
		boolean result;
		StringBuffer parsedresult;
		StringBuffer output=new StringBuffer();
        m = p6.matcher(s);
        parsedresult = new StringBuffer();
        result = m.find();        
        while(result) {
        	output.append(m.group(2)+" ");
            m.appendReplacement(parsedresult, " ");
            result = m.find();
        }
        StringBuffer temp=new StringBuffer(s);
        m = p7.matcher(temp);
        parsedresult = new StringBuffer();
        result = m.find();        
        while(result) {
        	output.append(m.group(3)+" ");
            m.appendReplacement(parsedresult, " ");
            result = m.find();
        }
        return output;
	}
	public StringBuffer matchRedirect(StringBuffer s){
		Matcher m;
		StringBuffer output=new StringBuffer("");
		if(!s.toString().startsWith("#REDIRECT"))	return output;
        m = p8.matcher(s);
        if(m.find())	output=new StringBuffer(m.group(3)+" ");
        return output;
   }
		
	public StringBuffer match10(StringBuffer s){
		int count=0;
        boolean flag=false;
        StringBuffer output=new StringBuffer();
        for(int i=0;i<s.length()-1;i++){
        	if(s.charAt(i)=='{'){
        		if(!flag)	output.append(" ");
        		flag=true;
        		count++;
        	}
        	else if(s.charAt(i)=='}')   		count--;	
        	if(!flag)output.append(s.charAt(i));
        	if(count==0)	flag=false;	
        }
		Matcher m;
		boolean result;
		StringBuffer parsedresult;
        m = p9.matcher(output);
        parsedresult = new StringBuffer();
        result = m.find();
        while(result) {
            m.appendReplacement(parsedresult, " ");
            result = m.find();
        }
        m.appendTail(parsedresult);
        return parsedresult;
	}
    public static void main(String[] args) throws Exception {
        RegexMatcher r=new RegexMatcher();
        //StringBuffer s=new StringBuffer(("afhsdkjf{{infobox hi this is suseel|kuma|r dfa}} as{{infobox hi again |dfa"));
        StringBuffer s1=r.matchAscii(new StringBuffer("AccessibleComputing"));
        System.out.println(r.removeCamel(s1));
        //System.out.println((s));
    }
}