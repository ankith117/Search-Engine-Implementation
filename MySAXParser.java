
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;


public class MySAXParser implements ContentHandler{	

	private int iter;
	private String currentElement;
    String indexpath;
	public int pageCount = 0;
	private boolean gotid=true;
	StringBuffer title,data,hpdata,info,ctg,redirect;
	String originaltitle;
	int id;
	long time;
	Index i;
	Set<String> stopwordsset;
	Set<Integer> dirty;
	Stemmer stm;
//	Set<String> repeats;
	RegexMatcher l;
	public MySAXParser(String x){		indexpath=x;	}

	public static void main(String args[]) {
		/*	args[]:
		 * 	0: filename
		 * 	1: input xml path
		 * 	2: output index path 
		 */
		
      try {
      	  XMLReader parser=XMLReaderFactory.createXMLReader();
      	  //ContentHandler handler=new MySAXParser(args[3]);
      	  Scanner sc=new Scanner(System.in);
      	  System.out.println("\nEnter the directory address to store the index files:(e.g. 'J:/IREpath/' ***** END WITH '/' *****)");
      	  String indexpath=sc.nextLine();
      	  //indexpath="J:/IRE/new index on 8gb/";
      	  ContentHandler handler=new MySAXParser(indexpath);
      	  parser.setContentHandler(handler);
      	  //parser.parse(args[2]);
      	  System.out.println("\nEnter the input xml path+filename");
      	  String path=sc.nextLine();
      	  //path="J:/IRE/splitfiles100mb/xaaak1.txt";
      	  parser.parse(path);
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

      public void startDocument()    throws SAXException{
    	  	time=System.currentTimeMillis();
    	    iter=100;
    	  	i=new Index(iter,indexpath);
    	  	stm=new Stemmer();
    	  	l=new RegexMatcher();
    	  	stopwordsset=new HashSet<String>();
    	  	dirty=new HashSet<Integer>();
    		stopwordsset.add("a");stopwordsset.add("able");stopwordsset.add("about");stopwordsset.add("above");stopwordsset.add("according");stopwordsset.add("accordingly");stopwordsset.add("across");stopwordsset.add("actually");stopwordsset.add("after");stopwordsset.add("afterwards");stopwordsset.add("again");stopwordsset.add("against");stopwordsset.add("all");stopwordsset.add("allow");stopwordsset.add("allows");stopwordsset.add("almost");stopwordsset.add("alone");stopwordsset.add("along");stopwordsset.add("already");stopwordsset.add("also");stopwordsset.add("although");stopwordsset.add("always");stopwordsset.add("am");stopwordsset.add("among");stopwordsset.add("amongst");stopwordsset.add("an");stopwordsset.add("and");stopwordsset.add("another");stopwordsset.add("any");stopwordsset.add("anybody");stopwordsset.add("anyhow");stopwordsset.add("anyone");stopwordsset.add("anything");stopwordsset.add("anyway");stopwordsset.add("anyways");stopwordsset.add("anywhere");stopwordsset.add("apart");stopwordsset.add("appear");stopwordsset.add("appreciate");stopwordsset.add("appropriate");stopwordsset.add("are");stopwordsset.add("around");stopwordsset.add("as");stopwordsset.add("aside");stopwordsset.add("ask");stopwordsset.add("asking");stopwordsset.add("associated");stopwordsset.add("at");stopwordsset.add("available");stopwordsset.add("away");stopwordsset.add("awfully");stopwordsset.add("b");stopwordsset.add("be");stopwordsset.add("became");stopwordsset.add("because");stopwordsset.add("become");stopwordsset.add("becomes");stopwordsset.add("becoming");stopwordsset.add("been");stopwordsset.add("before");stopwordsset.add("beforehand");stopwordsset.add("behind");stopwordsset.add("being");stopwordsset.add("believe");stopwordsset.add("below");stopwordsset.add("beside");stopwordsset.add("besides");stopwordsset.add("best");stopwordsset.add("better");stopwordsset.add("between");stopwordsset.add("beyond");stopwordsset.add("both");stopwordsset.add("brief");stopwordsset.add("but");stopwordsset.add("by");dirty.add(15367482);dirty.add(15368293);dirty.add(15390760);dirty.add(15558194);dirty.add(15657948);dirty.add(15662941);dirty.add(15754443);dirty.add(34851);dirty.add(52946);dirty.add(64184);dirty.add(116239);dirty.add(148566);dirty.add(150466);dirty.add(164652);    		dirty.add(227001);dirty.add(228972);dirty.add(297300);dirty.add(373594);dirty.add(419677);dirty.add(531588);dirty.add(532248);dirty.add(605020);dirty.add(668818);dirty.add(690543);dirty.add(698635);dirty.add(698655);dirty.add(698730);dirty.add(698752);    		dirty.add(979547);dirty.add(1341423);dirty.add(1363705);dirty.add(1373826);dirty.add(1377126);dirty.add(1405757);dirty.add(1419154);dirty.add(1421760);    		   		dirty.add(14181770);dirty.add(14322944);dirty.add(14729391);dirty.add(15062099);dirty.add(15070813);dirty.add(15107354);stopwordsset.add("were");stopwordsset.add("what");stopwordsset.add("whatever");stopwordsset.add("when");stopwordsset.add("whence");stopwordsset.add("whenever");stopwordsset.add("where");stopwordsset.add("whereafter");stopwordsset.add("whereas");stopwordsset.add("whereby");stopwordsset.add("wherein");stopwordsset.add("whereupon");stopwordsset.add("wherever");stopwordsset.add("whether");stopwordsset.add("which");stopwordsset.add("gotten");stopwordsset.add("greetings");dirty.add(15114761);dirty.add(15151958);dirty.add(15228886);dirty.add(15249085);
    		stopwordsset.add("c");stopwordsset.add("came");stopwordsset.add("can");stopwordsset.add("cannot");stopwordsset.add("cant");stopwordsset.add("cause");stopwordsset.add("causes");stopwordsset.add("certain");stopwordsset.add("certainly");stopwordsset.add("changes");stopwordsset.add("clearly");stopwordsset.add("co");stopwordsset.add("com");stopwordsset.add("come");stopwordsset.add("comes");stopwordsset.add("concerning");stopwordsset.add("consequently");stopwordsset.add("consider");stopwordsset.add("considering");stopwordsset.add("contain");stopwordsset.add("containing");stopwordsset.add("contains");stopwordsset.add("corresponding");stopwordsset.add("could");dirty.add(16134846);dirty.add(16654033);dirty.add(17139505);dirty.add(17482809);dirty.add(17660625);dirty.add(17746479);dirty.add(17891744);dirty.add(17894901);dirty.add(18020823);dirty.add(18063299);    		dirty.add(18152906);dirty.add(18356374);dirty.add(18646246);dirty.add(19007303);dirty.add(19443633);dirty.add(19445675);dirty.add(19494011);dirty.add(19554243);dirty.add(19660478);dirty.add(19802152);dirty.add(19907129);stopwordsset.add("course");stopwordsset.add("currently");stopwordsset.add("d");stopwordsset.add("definitely");stopwordsset.add("described");stopwordsset.add("despite");stopwordsset.add("did");stopwordsset.add("different");stopwordsset.add("do");stopwordsset.add("does");stopwordsset.add("doing");stopwordsset.add("done");stopwordsset.add("down");stopwordsset.add("downwards");stopwordsset.add("during");stopwordsset.add("e");stopwordsset.add("each");stopwordsset.add("edu");stopwordsset.add("eg");dirty.add(2306610);dirty.add(2446700);dirty.add(2948091);dirty.add(3196567);dirty.add(3424099);dirty.add(3445901);    		dirty.add(3977551);dirty.add(4080311);dirty.add(4194649);dirty.add(4279584);dirty.add(4320488);dirty.add(4391669);dirty.add(4484707);dirty.add(4527230);dirty.add(4593621);dirty.add(4593803);    		dirty.add(4688775);dirty.add(4691968);dirty.add(5832692);dirty.add(5996807);dirty.add(6354352);dirty.add(7021307);dirty.add(7184342);dirty.add(7482164);dirty.add(7482207);dirty.add(7482208);dirty.add(7482210);    		dirty.add(7917708);dirty.add(7950964);dirty.add(7972072);dirty.add(8152335);dirty.add(8547904);dirty.add(8548032);dirty.add(8548068);dirty.add(8548081);dirty.add(9077580);dirty.add(9214296);    		dirty.add(9415312);dirty.add(9421734);dirty.add(9654649);dirty.add(10057844);dirty.add(10126718);dirty.add(10403041);dirty.add(10408186);dirty.add(10559582);dirty.add(10666589);dirty.add(10818984);dirty.add(10820959);stopwordsset.add("truly");stopwordsset.add("try");stopwordsset.add("trying");stopwordsset.add("twice");stopwordsset.add("two");    		stopwordsset.add("u");stopwordsset.add("un");stopwordsset.add("under");stopwordsset.add("unfortunately");stopwordsset.add("unless");stopwordsset.add("unlikely");stopwordsset.add("while");stopwordsset.add("whither");stopwordsset.add("who");stopwordsset.add("whoever");stopwordsset.add("whole");stopwordsset.add("whom");stopwordsset.add("would");stopwordsset.add("would");stopwordsset.add("x");stopwordsset.add("y");stopwordsset.add("yes");stopwordsset.add("yet");stopwordsset.add("you");stopwordsset.add("eight");stopwordsset.add("either");stopwordsset.add("else");stopwordsset.add("elsewhere");stopwordsset.add("welcome");stopwordsset.add("well");stopwordsset.add("went");
    		stopwordsset.add("h");stopwordsset.add("had");stopwordsset.add("happens");stopwordsset.add("hardly");stopwordsset.add("has");stopwordsset.add("have");stopwordsset.add("having");stopwordsset.add("he");stopwordsset.add("hello");stopwordsset.add("help");stopwordsset.add("hence");stopwordsset.add("her");stopwordsset.add("here");stopwordsset.add("hereafter");stopwordsset.add("hereby");stopwordsset.add("herein");stopwordsset.add("hereupon");stopwordsset.add("hers");stopwordsset.add("herself");stopwordsset.add("hi");stopwordsset.add("him");stopwordsset.add("himself");stopwordsset.add("his");stopwordsset.add("hither");stopwordsset.add("hopefully");stopwordsset.add("how");stopwordsset.add("howbeit");stopwordsset.add("however");stopwordsset.add("i");stopwordsset.add("ie");stopwordsset.add("if");stopwordsset.add("ignored");stopwordsset.add("immediate");stopwordsset.add("in");stopwordsset.add("inasmuch");stopwordsset.add("inc");stopwordsset.add("indeed");stopwordsset.add("indicate");stopwordsset.add("indicated");stopwordsset.add("indicates");stopwordsset.add("inner");stopwordsset.add("insofar");stopwordsset.add("instead");stopwordsset.add("into");stopwordsset.add("inward");stopwordsset.add("is");stopwordsset.add("it");stopwordsset.add("its");stopwordsset.add("itself");stopwordsset.add("j");stopwordsset.add("just");stopwordsset.add("k");stopwordsset.add("keep");stopwordsset.add("keeps");stopwordsset.add("kept");stopwordsset.add("know");stopwordsset.add("knows");stopwordsset.add("known");stopwordsset.add("l");stopwordsset.add("last");stopwordsset.add("lately");stopwordsset.add("later");stopwordsset.add("latter");stopwordsset.add("latterly");stopwordsset.add("least");stopwordsset.add("less");stopwordsset.add("lest");stopwordsset.add("let");stopwordsset.add("like");stopwordsset.add("liked");stopwordsset.add("likely");stopwordsset.add("little");stopwordsset.add("ll");stopwordsset.add("looking");stopwordsset.add("looks");stopwordsset.add("ltd");stopwordsset.add("m");stopwordsset.add("mainly");stopwordsset.add("many");stopwordsset.add("may");stopwordsset.add("maybe");stopwordsset.add("me");stopwordsset.add("mean");stopwordsset.add("meanwhile");stopwordsset.add("merely");stopwordsset.add("might");stopwordsset.add("more");stopwordsset.add("moreover");stopwordsset.add("most");stopwordsset.add("mostly");stopwordsset.add("much");stopwordsset.add("must");stopwordsset.add("my");stopwordsset.add("myself");dirty.add(34606);dirty.add(34612);dirty.add(34630);dirty.add(34690);stopwordsset.add("whose");stopwordsset.add("why");stopwordsset.add("will");stopwordsset.add("willing");stopwordsset.add("wish");stopwordsset.add("with");stopwordsset.add("within");stopwordsset.add("without");stopwordsset.add("wonder");stopwordsset.add("until");stopwordsset.add("unto");stopwordsset.add("up");stopwordsset.add("upon");stopwordsset.add("us");stopwordsset.add("use");stopwordsset.add("used");stopwordsset.add("useful");stopwordsset.add("uses");stopwordsset.add("using");stopwordsset.add("usually");stopwordsset.add("uucp");stopwordsset.add("v");stopwordsset.add("value");stopwordsset.add("various");stopwordsset.add("ve");stopwordsset.add("very");stopwordsset.add("via");stopwordsset.add("viz");stopwordsset.add("vs");stopwordsset.add("w");stopwordsset.add("want");stopwordsset.add("wants");stopwordsset.add("was");stopwordsset.add("way");stopwordsset.add("we");
    		stopwordsset.add("n");stopwordsset.add("name");stopwordsset.add("namely");stopwordsset.add("nd");stopwordsset.add("near");stopwordsset.add("nearly");stopwordsset.add("necessary");stopwordsset.add("need");stopwordsset.add("needs");stopwordsset.add("neither");stopwordsset.add("never");stopwordsset.add("nevertheless");stopwordsset.add("new");stopwordsset.add("next");stopwordsset.add("nine");stopwordsset.add("no");stopwordsset.add("nobody");stopwordsset.add("non");stopwordsset.add("none");stopwordsset.add("noone");stopwordsset.add("nor");stopwordsset.add("normally");stopwordsset.add("not");stopwordsset.add("nothing");stopwordsset.add("novel");stopwordsset.add("now");stopwordsset.add("nowhere");stopwordsset.add("o");stopwordsset.add("obviously");stopwordsset.add("of");stopwordsset.add("off");    		dirty.add(15367482);dirty.add(15368293);stopwordsset.add("your");stopwordsset.add("yours");stopwordsset.add("yourself");stopwordsset.add("yourselves");stopwordsset.add("z");stopwordsset.add("zero");stopwordsset.add("often");stopwordsset.add("oh");stopwordsset.add("ok");stopwordsset.add("okay");stopwordsset.add("old");stopwordsset.add("on");stopwordsset.add("once");stopwordsset.add("one");stopwordsset.add("ones");stopwordsset.add("only");stopwordsset.add("onto");stopwordsset.add("or");stopwordsset.add("other");stopwordsset.add("others");stopwordsset.add("otherwise");stopwordsset.add("ought");stopwordsset.add("our");stopwordsset.add("ours");stopwordsset.add("ourselves");stopwordsset.add("out");stopwordsset.add("outside");stopwordsset.add("over");stopwordsset.add("overall");stopwordsset.add("own");stopwordsset.add("p");stopwordsset.add("particular");stopwordsset.add("particularly");stopwordsset.add("per");stopwordsset.add("perhaps");stopwordsset.add("placed");stopwordsset.add("please");stopwordsset.add("plus");stopwordsset.add("possible");stopwordsset.add("presumably");stopwordsset.add("probably");stopwordsset.add("provides");stopwordsset.add("q");stopwordsset.add("que");stopwordsset.add("quite");stopwordsset.add("qv");stopwordsset.add("r");stopwordsset.add("rather");stopwordsset.add("rd");stopwordsset.add("re");stopwordsset.add("really");stopwordsset.add("reasonably");stopwordsset.add("regarding");stopwordsset.add("regardless");stopwordsset.add("regards");stopwordsset.add("relatively");stopwordsset.add("respectively");stopwordsset.add("right");dirty.add(11372086);dirty.add(11617931);dirty.add(11845710);dirty.add(11846930);dirty.add(12014170);dirty.add(12103250);dirty.add(12106732);dirty.add(12107555);dirty.add(12108322);dirty.add(12163175);    		dirty.add(12276510);dirty.add(12316542);dirty.add(12777091);dirty.add(12888177);dirty.add(12890434);dirty.add(12997120);dirty.add(13243113);dirty.add(13492991);dirty.add(13846967);dirty.add(13859431); stopwordsset.add("enough");stopwordsset.add("entirely");stopwordsset.add("especially");stopwordsset.add("et");stopwordsset.add("etc");stopwordsset.add("even");stopwordsset.add("ever");stopwordsset.add("every");stopwordsset.add("everybody");stopwordsset.add("everyone");stopwordsset.add("everything");stopwordsset.add("everywhere");stopwordsset.add("ex");stopwordsset.add("exactly");stopwordsset.add("example");stopwordsset.add("except");stopwordsset.add("f");stopwordsset.add("far");stopwordsset.add("few");stopwordsset.add("fifth");
    		stopwordsset.add("s");stopwordsset.add("said");stopwordsset.add("same");stopwordsset.add("saw");stopwordsset.add("say");stopwordsset.add("saying");stopwordsset.add("says");stopwordsset.add("second");stopwordsset.add("secondly");stopwordsset.add("see");stopwordsset.add("seeing");stopwordsset.add("seem");stopwordsset.add("seemed");stopwordsset.add("seeming");stopwordsset.add("seems");stopwordsset.add("seen");stopwordsset.add("self");stopwordsset.add("selves");stopwordsset.add("sensible");stopwordsset.add("sent");stopwordsset.add("serious");stopwordsset.add("seriously");stopwordsset.add("seven");stopwordsset.add("several");stopwordsset.add("shall");stopwordsset.add("she");stopwordsset.add("should");stopwordsset.add("since");stopwordsset.add("six");stopwordsset.add("so");stopwordsset.add("some");stopwordsset.add("somebody");stopwordsset.add("somehow");stopwordsset.add("someone");stopwordsset.add("something");stopwordsset.add("sometime");stopwordsset.add("sometimes");stopwordsset.add("somewhat");stopwordsset.add("somewhere");stopwordsset.add("soon");stopwordsset.add("sorry");stopwordsset.add("specified");stopwordsset.add("specify");stopwordsset.add("specifying");stopwordsset.add("still");stopwordsset.add("sub");stopwordsset.add("such");stopwordsset.add("sup");stopwordsset.add("sure");stopwordsset.add("t");stopwordsset.add("take");stopwordsset.add("taken");stopwordsset.add("tell");stopwordsset.add("tends");stopwordsset.add("th");stopwordsset.add("than");stopwordsset.add("thank");stopwordsset.add("thanks");stopwordsset.add("thanx");stopwordsset.add("that");stopwordsset.add("thats");stopwordsset.add("the");stopwordsset.add("their");stopwordsset.add("theirs");stopwordsset.add("them");stopwordsset.add("themselves");stopwordsset.add("then");stopwordsset.add("thence");stopwordsset.add("there");stopwordsset.add("thereafter");stopwordsset.add("thereby");stopwordsset.add("therefore");stopwordsset.add("therein");stopwordsset.add("theres");stopwordsset.add("thereupon");stopwordsset.add("these");stopwordsset.add("they");stopwordsset.add("think");stopwordsset.add("third");stopwordsset.add("this");stopwordsset.add("thorough");stopwordsset.add("thoroughly");stopwordsset.add("those");stopwordsset.add("though");stopwordsset.add("three");stopwordsset.add("through");stopwordsset.add("throughout");stopwordsset.add("thru");stopwordsset.add("thus");stopwordsset.add("to");stopwordsset.add("together");stopwordsset.add("too");stopwordsset.add("took");stopwordsset.add("toward");stopwordsset.add("towards");stopwordsset.add("tried");stopwordsset.add("tries");dirty.add(1423322);dirty.add(1424666);dirty.add(1424208);    		dirty.add(1447085);dirty.add(1589246);dirty.add(1828342);dirty.add(1976857);stopwordsset.add("first");stopwordsset.add("five");stopwordsset.add("followed");stopwordsset.add("following");stopwordsset.add("follows");stopwordsset.add("for");stopwordsset.add("former");stopwordsset.add("formerly");stopwordsset.add("forth");stopwordsset.add("four");stopwordsset.add("from");stopwordsset.add("further");stopwordsset.add("furthermore");stopwordsset.add("g");stopwordsset.add("get");stopwordsset.add("gets");stopwordsset.add("getting");stopwordsset.add("given");stopwordsset.add("gives");stopwordsset.add("go");stopwordsset.add("goes");stopwordsset.add("going");stopwordsset.add("gone");stopwordsset.add("got");
    		
    		System.out.println("Parsing started");   
	  }      
      
      public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    	
    	 currentElement = qName;
         if (currentElement.equals("page")) {
            pageCount++;
            gotid=false;
		}
         else if(currentElement.equals("text")){   
        	 //System.out.println("pageid:"+pageCount+" id:"+id);
        	 data=new StringBuffer("");
        	 hpdata=new StringBuffer("");
      		 info=new StringBuffer("");
      		 ctg=new StringBuffer("");
         }
         else if(currentElement.equals("title"))       	 title=new StringBuffer();
    	  
      }
      
	  public void characters(char[] chars, int start, int length) throws SAXException {
			  if (currentElement.equals("id")&&gotid==false) {
	        	 	id=Integer.valueOf(new String(chars,start,length));
	            	gotid=true;
	         }
	         else if (currentElement.equals("text"))          data.append(new String(chars, start, length));
	         else if(currentElement.equals("title"))       	 title.append(new String(chars, start, length));
		  
	  }
	  
      public StringBuffer	getStoppedText(StringBuffer text){			
    		String words[]=text.toString().split(" ");
    		StringBuffer output=new StringBuffer();
    		for(int i=0;i<words.length;i++){
    			if(!stopwordsset.contains(words[i])){
    				output.append(words[i]+" ");
    			}
    		}
    		return output;
      }
     
      public void endElement(String uri, String localName, String qName)            throws SAXException {
        
    	 if(qName.equals("page")){
        	 gotid=true;
         }
         else if(qName.equals("text")){
        	 data=l.matchAscii(l.matchNewLine(data));
        	 redirect=l.matchRedirect(data);
        	 if(redirect.length()!=0||dirty.contains(id)){	/*redirect=getTitle(redirect);*/data=new StringBuffer("");}
        	 else{
        		 info=getInfo(data);
        	 	 data=getStoppedText(data);
//        		 repeats=new HashSet<String>();
        		 hpdata=getHPData(data);
        		 ctg=getCtg(data);  
        		 data=getData(data);
        	 }
        	 if(redirect.length()!=0)	originaltitle=redirect.toString();
        	 else 						originaltitle=title.toString();
        	 title=getTitle(title);
        	 i.add(title,id,info,hpdata,ctg,data,redirect,originaltitle);
        	 title=null;         info=null;        	 	 data=null;
        	 ctg=null;        	 hpdata=null;        	
//        	 repeats=null;
        	 if(pageCount%iter==0)	i.print(pageCount);
         }
         currentElement = "";         
      }      
     
      public StringBuffer getTitle(StringBuffer text)	{	
  		 return l.matchSpaces(getStoppedText(stm.getStemmedresult(l.removeCamel(l.matchAscii(text)))));
  	  }
      
  	  public StringBuffer getInfo(StringBuffer data)	{		
  		  return l.matchSpaces(getStoppedText(stm.getStemmedresult(l.getInfo(data))));							
  	  }
  	  
  	  public StringBuffer getHPData(StringBuffer data)	{
  		StringBuffer temp=l.matchSpaces(getStoppedText(stm.getStemmedresult(l.matchHPData(data))));
  		String[] array=temp.toString().split(" ");
//  		for(int i=0;i<array.length;i++)		if(!repeats.contains(array[i]))	repeats.add(array[i]);	
  		return temp;
  	  }
  	  
  	  public StringBuffer getCtg(StringBuffer data)	{		return l.matchSpaces(getStoppedText(stm.getStemmedresult(l.getCtg(data))));	}
  	  
  	  public StringBuffer getData(StringBuffer data)	{	
  		StringBuffer temp=l.matchSpaces(getStoppedText(stm.getStemmedresult(l.match10(data))));
  		String[] array=temp.toString().split(" ");
  		StringBuffer text=new StringBuffer();
  		for(int i=0;i<array.length;i++)		
//  			if(!repeats.contains(array[i]))	
  				text.append(array[i]+" ");		
  		return text;	
  	  }
  	  
  	  public void endDocument()    throws SAXException{	
		  	i.print(pageCount);
		  	System.out.println("Merging started");
		  	i.merge(pageCount);
		  	i=null;
		  	stm=null;
		  	l=null;
		  	stopwordsset=null;
		  	time=System.currentTimeMillis()-time;
			System.out.println("\npagecount"+pageCount+"\nEnded after(seconds): "+time/1000);
			//Scanner sc=new Scanner(System.in);
			//new Searcher(sc.nextLine());
	  }
	public void ignorableWhitespace(char[] arg0, int arg1, int arg2)throws SAXException {}
	public void processingInstruction(String arg0, String arg1)throws SAXException {}
	public void setDocumentLocator(Locator arg0) {}
	public void skippedEntity(String arg0) throws SAXException {}
	public void startPrefixMapping(String prefix,String url){}
	public void endPrefixMapping(String prefix){}
   }


