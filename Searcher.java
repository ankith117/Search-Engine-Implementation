import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Searcher{
	String titlelist,infolist,ctglist,outlist,datalist;
	String[] titlewords,infowords,ctgwords,outwords,datawords;
	String Indexpath;
	BufferedReader br;
	Set<String> stopwordsset;
	RegexMatcher l;
	Stemmer stm;
	Map<String,Long>sectitle,secinfo,secout,seccat,secdata;	Map<Integer,Long>secrdtitle,secid;
	Map<String,StringBuffer>titleposting,infoposting,outposting,catposting,dataposting;	
	Map<Integer,Double>tlinks,ilinks,olinks,clinks,dlinks;	
	Map<Double,StringBuffer>intersectionmap,unionmap;
	Set<Integer> tiset,iiset,oiset,ciset,diset,intersectionset,unionset;
	Set<Integer> st[],si[],sc[],so[],sd[],sdout[];
	int N;
	public void loadSecFiles(){
		try {
		sectitle=new TreeMap<String,Long>();
		br = new BufferedReader(new FileReader(Indexpath+"SecTitleIndex.txt"));
		String line = br.readLine();
		while(line!=null){				
			sectitle.put(line.split(":")[0],Long.valueOf(line.split(":")[1]));
			line = br.readLine();
		}
		br.close();
		
		secrdtitle=new TreeMap<Integer,Long>();
		br = new BufferedReader(new FileReader(Indexpath+"SecRedirectIndex.txt"));
		line = br.readLine();
		while(line!=null){				
			secrdtitle.put(Integer.valueOf(line.split(":")[0]),Long.valueOf(line.split(":")[1]));
			line = br.readLine();
		}
		br.close();
		
		secinfo=new TreeMap<String,Long>();
		br = new BufferedReader(new FileReader(Indexpath+"SecInfoIndex.txt"));
		line = br.readLine();
		while(line!=null){				
			secinfo.put(line.split(":")[0],Long.valueOf(line.split(":")[1]));
			line = br.readLine();
		}
		br.close();
		
		secout=new TreeMap<String,Long>();
		br = new BufferedReader(new FileReader(Indexpath+"SecOutlinksIndex.txt"));
		line = br.readLine();
		while(line!=null){				
			secout.put(line.split(":")[0],Long.valueOf(line.split(":")[1]));
			line = br.readLine();
		}
		br.close();
		
		seccat=new TreeMap<String,Long>();
		br = new BufferedReader(new FileReader(Indexpath+"SecCategoryIndex.txt"));
		line = br.readLine();
		while(line!=null){				
			seccat.put(line.split(":")[0],Long.valueOf(line.split(":")[1]));
			line = br.readLine();
		}
		br.close();
		
		secdata=new TreeMap<String,Long>();
		br = new BufferedReader(new FileReader(Indexpath+"SecTextIndex.txt"));
		line = br.readLine();
		while(line!=null){				
			secdata.put(line.split(":")[0],Long.valueOf(line.split(":")[1]));
			line = br.readLine();
		}
		br.close();
		
		secid=new TreeMap<Integer,Long>();
		br = new BufferedReader(new FileReader(Indexpath+"SecIdIndex.txt"));
		line = br.readLine();
		while(line!=null){				
			secid.put(Integer.valueOf(line.split(":")[0]),Long.valueOf(line.split(":")[1]));
			line = br.readLine();
		}
		br.close();
		
		} catch (IOException e) {e.printStackTrace();		}
	}

	public Searcher(String path,int pageCount){		
		N=pageCount;
		Indexpath=path;
		loadSecFiles();
		l=new RegexMatcher();
		stm=new Stemmer();
		titlelist="";ctglist="";infolist="";outlist="";datalist="";
		
		titleposting=new HashMap<String,StringBuffer>();
		//tset=new HashSet<Integer>();
		tiset=new HashSet<Integer>();
		tlinks=new HashMap<Integer,Double>();
		
		infoposting=new HashMap<String,StringBuffer>();
		//iset=new HashSet<Integer>();
		ilinks=new HashMap<Integer,Double>();
		
		outposting=new HashMap<String,StringBuffer>();
		//oset=new HashSet<Integer>();
		olinks=new HashMap<Integer,Double>();
		
		//cset=new HashSet<Integer>();
		clinks=new HashMap<Integer,Double>();		
		catposting=new HashMap<String,StringBuffer>();
		
		dlinks=new HashMap<Integer,Double>();		
		dataposting=new HashMap<String,StringBuffer>();	
		//doutiset=new HashSet<Integer>();
		//doutlinks=new HashMap<Integer,Double>();		
		//dataoutposting=new HashMap<String,StringBuffer>();	
		
		intersectionset=new HashSet<Integer>();
		unionset=new HashSet<Integer>();
		intersectionmap=new TreeMap<Double,StringBuffer>(Collections.reverseOrder());
		unionmap=new TreeMap<Double,StringBuffer>(Collections.reverseOrder());
	}

	public void getResults(String query){
		long time=System.currentTimeMillis();
		
		getParsed(query+" ");		
		titlewords=titlelist.split(" ");		infowords=infolist.split(" ");			ctgwords=ctglist.split(" ");	
		outwords=outlist.split(" ");			datawords=datalist.split(" ");
		if(titlelist.length()>0){
		st=new HashSet[titlewords.length];
		for(int i=0;i<titlewords.length;i++)	st[i]=new HashSet<Integer>();
		for(int i=0;i<titlewords.length;i++)	getTID(titlewords[i],i);
		//for(int i=0;i<st.length;i++)				tset.addAll(st[i]);
		tiset=st[0];
		for(int i=1;i<st.length;i++)			if(st[i].size()>0)	tiset.retainAll(st[i]);
		//getRedirects();
		titleRanking();
		//System.out.println(tset);
		}
		if(infolist.length()>0){
		si=new HashSet[infowords.length];
		for(int i=0;i<infowords.length;i++)		si[i]=new HashSet<Integer>();
		for(int i=0;i<infowords.length;i++)		getIID(infowords[i],i);
		//for(int i=0;i<si.length;i++)				iset.addAll(si[i]);
		iiset=si[0];
		for(int i=1;i<si.length;i++)			if(si[i].size()>0)	iiset.retainAll(si[i]);
		infoRanking();
		}
		if(outlist.length()>0){
		so=new HashSet[outwords.length];
		for(int i=0;i<outwords.length;i++)		so[i]=new HashSet<Integer>();
		for(int i=0;i<outwords.length;i++)		getOID(outwords[i],i);
		//for(int i=0;i<so.length;i++)				oset.addAll(so[i]);
		oiset=so[0];
		for(int i=1;i<so.length;i++)			if(so[i].size()>0)	oiset.retainAll(so[i]);
		outRanking();
		//createOutTree();
		//System.out.println(oset);
		}
		if(ctglist.length()>0){
		sc=new HashSet[ctgwords.length];
		for(int i=0;i<ctgwords.length;i++)		sc[i]=new HashSet<Integer>();
		for(int i=0;i<ctgwords.length;i++)		getCID(ctgwords[i],i);
		//for(int i=0;i<sc.length;i++)				cset.addAll(sc[i]);
		ciset=sc[0];
		for(int i=1;i<sc.length;i++)			if(sc[i].size()>0)	ciset.retainAll(sc[i]);
		catRanking();	
		}
		if(datalist.length()>0){
		sd=new HashSet[datawords.length];
		//sdout=new HashSet[datawords.length];
		for(int i=0;i<datawords.length;i++)		sd[i]=new HashSet<Integer>();
		//for(int i=0;i<datawords.length;i++)		sdout[i]=new HashSet<Integer>();
		for(int i=0;i<datawords.length;i++)		getDID(datawords[i],i);
		diset=sd[0];
		for(int i=1;i<sd.length;i++)			if(sd[i].size()>0)	diset.retainAll(sd[i]);
		//doutiset=sdout[0];
		//for(int i=1;i<sdout.length;i++)			if(sdout[i].size()>0)	doutiset.retainAll(sdout[i]);
		dataRanking();		
		/*Iterator<Integer> i=doutiset.iterator();
		while(i.hasNext()){
			int pid=i.next();
			if(diset.contains(pid))			dlinks.put(pid,dlinks.get(pid)+doutlinks.get(pid));
			else{							diset.add(pid);				dlinks.put(pid,doutlinks.get(pid));			}
		}
		diset.addAll(doutiset);*/
		}
		if(titlelist.length()>0)					unionset.addAll(tiset);
		if(infolist.length()>0)						unionset.addAll(iiset);
		if(datalist.length()>0)						unionset.addAll(diset);
		if(outlist.length()>0)						unionset.addAll(oiset);
		if(ctglist.length()>0)						unionset.addAll(ciset);
		
		Iterator<Integer> iter=unionset.iterator();
		while(iter.hasNext())	intersectionset.add(iter.next());
		
		if(titlelist.length()>0&&tiset.size()>0)	intersectionset.retainAll(tiset);
		if(outlist.length()>0&&oiset.size()>0)		intersectionset.retainAll(oiset);
		if(datalist.length()>0&&diset.size()>0)		intersectionset.retainAll(diset);
		if(ctglist.length()>0&&ciset.size()>0)		intersectionset.retainAll(ciset);
		if(infolist.length()>0&&iiset.size()>0)		intersectionset.retainAll(iiset);
		unionset.removeAll(intersectionset);
		int count=0;
		if(intersectionset.size()>0){
			Iterator<Integer> iterator1=intersectionset.iterator();
			while(iterator1.hasNext()){
				int pid=iterator1.next();
				double rank=0;
				if(dlinks.containsKey(pid))	rank+=dlinks.get(pid);
				if(ilinks.containsKey(pid))	rank+=ilinks.get(pid);
				if(olinks.containsKey(pid))	rank+=olinks.get(pid);
				if(clinks.containsKey(pid))	rank+=clinks.get(pid);
				if(tlinks.containsKey(pid))	rank+=tlinks.get(pid);
				//System.out.println(rank);
				if(intersectionmap.containsKey(rank))	
					intersectionmap.put(rank,new StringBuffer(intersectionmap.get(rank).toString()+pid+"#"));
				else									
					intersectionmap.put(rank,new StringBuffer(pid+"#"));
			}
		}
		long time1=(System.currentTimeMillis()-time);
		if(intersectionmap.size()>0){
			System.out.println("\n--------------BEST RESULTS-------------- \t TIME = "+time1+" mS\n\nRank.\tPageID\t\tTitle\n");
			//System.out.println(intersectionmap);
			Set<Map.Entry<Double,StringBuffer>> set = intersectionmap.entrySet();
			Iterator<Map.Entry<Double,StringBuffer>> iterator = set.iterator();
			while(iterator.hasNext()&&count<10) {	
				Map.Entry<Double,StringBuffer> entry = (Map.Entry<Double,StringBuffer>)iterator.next();	
				double key=entry.getKey();
				String[] values=entry.getValue().toString().split("#");
				for(int i=0;i<values.length&&count<10;i++,count++){
					Set<Map.Entry<Integer,Long>> set1 = secid.entrySet();
					Iterator<Map.Entry<Integer,Long>> iterator1 = set1.iterator();
					long bytes=0,oldvalue=0,limit=0;
					int pid=Integer.valueOf(values[i]);
					while(iterator1.hasNext()) {	
						Map.Entry<Integer,Long> entry1 = (Map.Entry<Integer,Long>)iterator1.next();	
						int key1=entry1.getKey();
						long value=entry1.getValue();
						if(key1>pid)					{bytes=oldvalue;limit=key1;break;}
						else if(key1==pid)				{bytes=value;break;}
						else								oldvalue=value;
					}
					RandomAccessFile raf;
					try {
						raf = new RandomAccessFile(Indexpath+"IdIndex.txt","r");
						raf.seek(bytes);
						String line=raf.readLine();
						while(line!=null&&Integer.valueOf(line.split(":",2)[0])<pid&&Integer.valueOf(line.split(":",2)[0])!=limit)		line=raf.readLine();
						raf.close();
						if(line!=null&&Integer.valueOf(line.split(":",2)[0])==pid){
							System.out.println((count+1)+"\t"+values[i]+"\t\t"+line.split(":",2)[1].trim());
						}
					}
					catch (FileNotFoundException e) {		e.printStackTrace();	}
					catch (IOException e)			{		e.printStackTrace();	}
				}
			}
		}
		if(count<10){
			Iterator<Integer> iterator1=unionset.iterator();
			while(iterator1.hasNext()){
				int pid=iterator1.next();
				double rank=0;
				if(dlinks.containsKey(pid))	rank+=dlinks.get(pid);
				if(ilinks.containsKey(pid))	rank+=ilinks.get(pid);
				if(olinks.containsKey(pid))	rank+=olinks.get(pid);
				if(clinks.containsKey(pid))	rank+=clinks.get(pid);
				if(tlinks.containsKey(pid))	rank+=tlinks.get(pid);
				//System.out.println(rank);
				if(unionmap.containsKey(rank))	
					unionmap.put(rank,new StringBuffer(unionmap.get(rank).toString()+pid+"#"));
				else									
					unionmap.put(rank,new StringBuffer(pid+"#"));
			}
			long time2=(System.currentTimeMillis()-time);
			System.out.println("\n--------------PARTIAL RESULTS-------------- \t TIME = "+time2+" mS\n\nRank.\tPageID\t\tTitle\n");
				Set<Map.Entry<Double,StringBuffer>> set = unionmap.entrySet();
				Iterator<Map.Entry<Double,StringBuffer>> iterator = set.iterator();
				while(iterator.hasNext()&&count<10) {	
					Map.Entry<Double,StringBuffer> entry = (Map.Entry<Double,StringBuffer>)iterator.next();	
					double key=entry.getKey();
					String[] values=entry.getValue().toString().split("#");
					for(int i=0;i<values.length&&count<10;i++,count++){
						Set<Map.Entry<Integer,Long>> set1 = secid.entrySet();
						Iterator<Map.Entry<Integer,Long>> iterator2 = set1.iterator();
						long bytes=0,oldvalue=0,limit=0;
						int pid=Integer.valueOf(values[i]);
						while(iterator2.hasNext()) {	
							Map.Entry<Integer,Long> entry1 = (Map.Entry<Integer,Long>)iterator2.next();	
							int key1=entry1.getKey();
							long value=entry1.getValue();
							if(key1>pid)					{bytes=oldvalue;limit=key1;break;}
							else if(key1==pid)				{bytes=value;break;}
							else								oldvalue=value;
						}
						RandomAccessFile raf;
						try {
							raf = new RandomAccessFile(Indexpath+"IdIndex.txt","r");
							raf.seek(bytes);
							String line=raf.readLine();
							while(line!=null&&Integer.valueOf(line.split(":",2)[0])<pid&&Integer.valueOf(line.split(":",2)[0])!=limit)		line=raf.readLine();
							raf.close();
							if(line!=null&&Integer.valueOf(line.split(":",2)[0])==pid){
								System.out.println((count+1)+"\t"+values[i]+"\t\t"+line.split(":",2)[1].trim());
							}
						}
						catch (FileNotFoundException e) {		e.printStackTrace();	}
						catch (IOException e)			{		e.printStackTrace();	}
					}
				}
		}
		if(count==0)	System.out.print("Sorry!! No Results Found!!!");
	}
	
	void getTID(String title,int k){
		title=title.trim();
		Set<Map.Entry<String,Long>> set = sectitle.entrySet();
		Iterator<Map.Entry<String,Long>> iterator = set.iterator();
		long bytes=0,oldvalue=0;
		String limit="";
		while(iterator.hasNext()) {			
			Map.Entry<String,Long> entry = (Map.Entry<String,Long>)iterator.next();	
			String key=entry.getKey();
			long value=entry.getValue();
			if(key.compareTo(title)>0)			{bytes=oldvalue;limit=key;break;}
			else if(key.compareTo(title)==0)	{bytes=value;break;}
			else								oldvalue=value;
		}
		RandomAccessFile raf;
		try {
		raf = new RandomAccessFile(Indexpath+"TitleIndex.txt","r");
		raf.seek(bytes);
		String line=raf.readLine();
		while(line!=null&&line.split(":")[0].compareTo(title)<0&&line.split(":")[0].compareTo(limit)!=0)		line=raf.readLine();
		raf.close();
		if(line!=null&&line.split(":")[0].compareTo(title)==0){
			titleposting.put(line.split(":")[0],new StringBuffer(line.split(":")[1]));
			String[] pages=line.split(":")[1].split("#");
			for(int i=0;i<pages.length;i++)					st[k].add(Integer.valueOf(pages[i]));
		}
		}
		catch (FileNotFoundException e) {		e.printStackTrace();	}
		catch (IOException e)			{		e.printStackTrace();	}
	}

	public void getRedirects(){
		String remove=" ";
		Iterator<Integer> ti = tiset.iterator();
		try {
		Set<Integer> temp1=new HashSet<Integer>();
		while(ti.hasNext()){
			String rdtitles=" ";
				int value=ti.next();
				
				Set<Map.Entry<Integer,Long>> set = secrdtitle.entrySet();
				Iterator<Map.Entry<Integer,Long>> iterator = set.iterator();
				long bytes=0,oldvalue=0;
				long limit=0;
				while(iterator.hasNext()) {
					Map.Entry<Integer,Long> entry = (Map.Entry<Integer,Long>)iterator.next();	
					int key=entry.getKey();
					long bytevalue=entry.getValue();
					if(key>value)			{bytes=oldvalue;limit=key;break;}
					else if(key==value)		{bytes=bytevalue;break;}
					else					oldvalue=bytevalue;
				}
				RandomAccessFile raf;
				raf = new RandomAccessFile(Indexpath+"RedirectIndex.txt","r");
				raf.seek(bytes);
				String line=raf.readLine();
				while(line!=null&&Integer.valueOf(line.split(":")[0])<value&&Integer.valueOf(line.split(":")[0])!=limit)		line=raf.readLine();
				raf.close();
				if(line!=null&&Integer.valueOf(line.split(":")[0])==value){
					remove+=value+" ";
					rdtitles+=line.split(":")[1]+" ";
				}
				//System.out.println(rdtitles);
				String[] rdt=rdtitles.trim().split(" ");
				st=new HashSet[rdt.length];
				for(int i=0;i<rdt.length;i++)	st[i]=new HashSet<Integer>();
				for(int i=0;i<rdt.length;i++)	getTID(l.matchSpaces(new StringBuffer(getStoppedText(stm.getStemmedresult(l.removeCamel(l.matchAscii(new StringBuffer(rdt[i])))).toString()))).toString(),i);
				
				Set<Integer> temp=new HashSet<Integer>(st[0]);
				for(int i=1;i<st.length;i++)		temp.retainAll(st[i]);
				temp1.addAll(temp);
		}
		tiset.addAll(temp1);
		String[] rmv=remove.trim().split(" ");
		if(rmv.length>0)	for(int i=0;i<rmv.length;i++)	if(rmv[i].length()>0)		tiset.remove(Integer.valueOf(rmv[i]));
		} 
		
		catch (FileNotFoundException e) {e.printStackTrace();}
		catch (IOException e) {e.printStackTrace();	}	
		//System.out.println("Final titles: "+tset);
	}	

	public void titleRanking(){
		Map<Integer,Double> pscore=new HashMap<Integer,Double>();
		Map<Integer,Integer> pcount=new HashMap<Integer,Integer>();
		Set<Map.Entry<String,StringBuffer>> set = titleposting.entrySet();
		Iterator<Map.Entry<String,StringBuffer>> iterator = set.iterator();
		while(iterator.hasNext()){
			Map.Entry<String,StringBuffer> entry = (Map.Entry<String,StringBuffer>)iterator.next();
			//System.out.println(entry.getValue());
			String pages[]=entry.getValue().toString().split("#");
			double idf=Math.log10(N/pages.length);
			for(int i=0;i<pages.length;i++){
				if(pcount.containsKey(Integer.valueOf(pages[i])))	pcount.put(Integer.valueOf(pages[i]),pcount.get(Integer.valueOf(pages[i]))+1);
				else												pcount.put(Integer.valueOf(pages[i]),1);
				if(pscore.containsKey(Integer.valueOf(pages[i])))	pscore.put(Integer.valueOf(pages[i]),pcount.get(Integer.valueOf(pages[i]))+idf);
				else												pscore.put(Integer.valueOf(pages[i]),idf);				
			}
		}
		//System.out.println(pcount);
		//System.out.println(pscore);
		Iterator<Integer> iterator1 = tiset.iterator();
		while(iterator1.hasNext()){
			int pid=iterator1.next();
			double prank=((pcount.get(pid)*10000+pscore.get(pid))/titlewords.length)*100;
			tlinks.put(pid,prank);
		}
		//System.out.println(tlinks);
	}

	public String getStoppedText(String text){			
		String words[]=text.toString().split(" ");
		String output="";
		for(int i=0;i<words.length;i++)		if(!stopwordsset.contains(words[i]))			output+=words[i]+" ";
		return output;
	}

	public void getParsed(String query){
		stopwordsset=new HashSet<String>();
		stopwordsset.add("a");stopwordsset.add("able");stopwordsset.add("about");stopwordsset.add("above");stopwordsset.add("according");stopwordsset.add("accordingly");stopwordsset.add("across");stopwordsset.add("actually");stopwordsset.add("after");stopwordsset.add("afterwards");stopwordsset.add("again");stopwordsset.add("against");stopwordsset.add("all");stopwordsset.add("allow");stopwordsset.add("allows");stopwordsset.add("almost");stopwordsset.add("alone");stopwordsset.add("along");stopwordsset.add("already");stopwordsset.add("also");stopwordsset.add("although");stopwordsset.add("always");stopwordsset.add("am");stopwordsset.add("among");stopwordsset.add("amongst");stopwordsset.add("an");stopwordsset.add("and");stopwordsset.add("another");stopwordsset.add("any");stopwordsset.add("anybody");stopwordsset.add("anyhow");stopwordsset.add("anyone");stopwordsset.add("anything");stopwordsset.add("anyway");stopwordsset.add("anyways");stopwordsset.add("anywhere");stopwordsset.add("apart");stopwordsset.add("appear");stopwordsset.add("appreciate");stopwordsset.add("appropriate");stopwordsset.add("are");stopwordsset.add("around");stopwordsset.add("as");stopwordsset.add("aside");stopwordsset.add("ask");stopwordsset.add("asking");stopwordsset.add("associated");stopwordsset.add("at");stopwordsset.add("available");stopwordsset.add("away");stopwordsset.add("awfully");stopwordsset.add("b");stopwordsset.add("be");stopwordsset.add("became");stopwordsset.add("because");stopwordsset.add("become");stopwordsset.add("becomes");stopwordsset.add("becoming");stopwordsset.add("been");stopwordsset.add("before");stopwordsset.add("beforehand");stopwordsset.add("behind");stopwordsset.add("being");stopwordsset.add("believe");stopwordsset.add("below");stopwordsset.add("beside");stopwordsset.add("besides");stopwordsset.add("best");stopwordsset.add("better");stopwordsset.add("between");stopwordsset.add("beyond");stopwordsset.add("both");stopwordsset.add("brief");stopwordsset.add("but");stopwordsset.add("by");
		stopwordsset.add("c");stopwordsset.add("came");stopwordsset.add("can");stopwordsset.add("cannot");stopwordsset.add("cant");stopwordsset.add("cause");stopwordsset.add("causes");stopwordsset.add("certain");stopwordsset.add("certainly");stopwordsset.add("changes");stopwordsset.add("clearly");stopwordsset.add("co");stopwordsset.add("com");stopwordsset.add("come");stopwordsset.add("comes");stopwordsset.add("concerning");stopwordsset.add("consequently");stopwordsset.add("consider");stopwordsset.add("considering");stopwordsset.add("contain");stopwordsset.add("containing");stopwordsset.add("contains");stopwordsset.add("corresponding");stopwordsset.add("could");stopwordsset.add("course");stopwordsset.add("currently");stopwordsset.add("d");stopwordsset.add("definitely");stopwordsset.add("described");stopwordsset.add("despite");stopwordsset.add("did");stopwordsset.add("different");stopwordsset.add("do");stopwordsset.add("does");stopwordsset.add("doing");stopwordsset.add("done");stopwordsset.add("down");stopwordsset.add("downwards");stopwordsset.add("during");stopwordsset.add("e");stopwordsset.add("each");stopwordsset.add("edu");stopwordsset.add("eg");stopwordsset.add("eight");stopwordsset.add("either");stopwordsset.add("else");stopwordsset.add("elsewhere");stopwordsset.add("enough");stopwordsset.add("entirely");stopwordsset.add("especially");stopwordsset.add("et");stopwordsset.add("etc");stopwordsset.add("even");stopwordsset.add("ever");stopwordsset.add("every");stopwordsset.add("everybody");stopwordsset.add("everyone");stopwordsset.add("everything");stopwordsset.add("everywhere");stopwordsset.add("ex");stopwordsset.add("exactly");stopwordsset.add("example");stopwordsset.add("except");stopwordsset.add("f");stopwordsset.add("far");stopwordsset.add("few");stopwordsset.add("fifth");stopwordsset.add("first");stopwordsset.add("five");stopwordsset.add("followed");stopwordsset.add("following");stopwordsset.add("follows");stopwordsset.add("for");stopwordsset.add("former");stopwordsset.add("formerly");stopwordsset.add("forth");stopwordsset.add("four");stopwordsset.add("from");stopwordsset.add("further");stopwordsset.add("furthermore");stopwordsset.add("g");stopwordsset.add("get");stopwordsset.add("gets");stopwordsset.add("getting");stopwordsset.add("given");stopwordsset.add("gives");stopwordsset.add("go");stopwordsset.add("goes");stopwordsset.add("going");stopwordsset.add("gone");stopwordsset.add("got");stopwordsset.add("gotten");stopwordsset.add("greetings");
		stopwordsset.add("h");stopwordsset.add("had");stopwordsset.add("happens");stopwordsset.add("hardly");stopwordsset.add("has");stopwordsset.add("have");stopwordsset.add("having");stopwordsset.add("he");stopwordsset.add("hello");stopwordsset.add("help");stopwordsset.add("hence");stopwordsset.add("her");stopwordsset.add("here");stopwordsset.add("hereafter");stopwordsset.add("hereby");stopwordsset.add("herein");stopwordsset.add("hereupon");stopwordsset.add("hers");stopwordsset.add("herself");stopwordsset.add("hi");stopwordsset.add("him");stopwordsset.add("himself");stopwordsset.add("his");stopwordsset.add("hither");stopwordsset.add("hopefully");stopwordsset.add("how");stopwordsset.add("howbeit");stopwordsset.add("however");stopwordsset.add("i");stopwordsset.add("ie");stopwordsset.add("if");stopwordsset.add("ignored");stopwordsset.add("immediate");stopwordsset.add("in");stopwordsset.add("inasmuch");stopwordsset.add("inc");stopwordsset.add("indeed");stopwordsset.add("indicate");stopwordsset.add("indicated");stopwordsset.add("indicates");stopwordsset.add("inner");stopwordsset.add("insofar");stopwordsset.add("instead");stopwordsset.add("into");stopwordsset.add("inward");stopwordsset.add("is");stopwordsset.add("it");stopwordsset.add("its");stopwordsset.add("itself");stopwordsset.add("j");stopwordsset.add("just");stopwordsset.add("k");stopwordsset.add("keep");stopwordsset.add("keeps");stopwordsset.add("kept");stopwordsset.add("know");stopwordsset.add("knows");stopwordsset.add("known");stopwordsset.add("l");stopwordsset.add("last");stopwordsset.add("lately");stopwordsset.add("later");stopwordsset.add("latter");stopwordsset.add("latterly");stopwordsset.add("least");stopwordsset.add("less");stopwordsset.add("lest");stopwordsset.add("let");stopwordsset.add("like");stopwordsset.add("liked");stopwordsset.add("likely");stopwordsset.add("little");stopwordsset.add("ll");stopwordsset.add("looking");stopwordsset.add("looks");stopwordsset.add("ltd");stopwordsset.add("m");stopwordsset.add("mainly");stopwordsset.add("many");stopwordsset.add("may");stopwordsset.add("maybe");stopwordsset.add("me");stopwordsset.add("mean");stopwordsset.add("meanwhile");stopwordsset.add("merely");stopwordsset.add("might");stopwordsset.add("more");stopwordsset.add("moreover");stopwordsset.add("most");stopwordsset.add("mostly");stopwordsset.add("much");stopwordsset.add("must");stopwordsset.add("my");stopwordsset.add("myself");
		stopwordsset.add("n");stopwordsset.add("name");stopwordsset.add("namely");stopwordsset.add("nd");stopwordsset.add("near");stopwordsset.add("nearly");stopwordsset.add("necessary");stopwordsset.add("need");stopwordsset.add("needs");stopwordsset.add("neither");stopwordsset.add("never");stopwordsset.add("nevertheless");stopwordsset.add("new");stopwordsset.add("next");stopwordsset.add("nine");stopwordsset.add("no");stopwordsset.add("nobody");stopwordsset.add("non");stopwordsset.add("none");stopwordsset.add("noone");stopwordsset.add("nor");stopwordsset.add("normally");stopwordsset.add("not");stopwordsset.add("nothing");stopwordsset.add("novel");stopwordsset.add("now");stopwordsset.add("nowhere");stopwordsset.add("o");stopwordsset.add("obviously");stopwordsset.add("of");stopwordsset.add("off");stopwordsset.add("often");stopwordsset.add("oh");stopwordsset.add("ok");stopwordsset.add("okay");stopwordsset.add("old");stopwordsset.add("on");stopwordsset.add("once");stopwordsset.add("one");stopwordsset.add("ones");stopwordsset.add("only");stopwordsset.add("onto");stopwordsset.add("or");stopwordsset.add("other");stopwordsset.add("others");stopwordsset.add("otherwise");stopwordsset.add("ought");stopwordsset.add("our");stopwordsset.add("ours");stopwordsset.add("ourselves");stopwordsset.add("out");stopwordsset.add("outside");stopwordsset.add("over");stopwordsset.add("overall");stopwordsset.add("own");stopwordsset.add("p");stopwordsset.add("particular");stopwordsset.add("particularly");stopwordsset.add("per");stopwordsset.add("perhaps");stopwordsset.add("placed");stopwordsset.add("please");stopwordsset.add("plus");stopwordsset.add("possible");stopwordsset.add("presumably");stopwordsset.add("probably");stopwordsset.add("provides");stopwordsset.add("q");stopwordsset.add("que");stopwordsset.add("quite");stopwordsset.add("qv");stopwordsset.add("r");stopwordsset.add("rather");stopwordsset.add("rd");stopwordsset.add("re");stopwordsset.add("really");stopwordsset.add("reasonably");stopwordsset.add("regarding");stopwordsset.add("regardless");stopwordsset.add("regards");stopwordsset.add("relatively");stopwordsset.add("respectively");stopwordsset.add("right");
		stopwordsset.add("s");stopwordsset.add("said");stopwordsset.add("same");stopwordsset.add("saw");stopwordsset.add("say");stopwordsset.add("saying");stopwordsset.add("says");stopwordsset.add("second");stopwordsset.add("secondly");stopwordsset.add("see");stopwordsset.add("seeing");stopwordsset.add("seem");stopwordsset.add("seemed");stopwordsset.add("seeming");stopwordsset.add("seems");stopwordsset.add("seen");stopwordsset.add("self");stopwordsset.add("selves");stopwordsset.add("sensible");stopwordsset.add("sent");stopwordsset.add("serious");stopwordsset.add("seriously");stopwordsset.add("seven");stopwordsset.add("several");stopwordsset.add("shall");stopwordsset.add("she");stopwordsset.add("should");stopwordsset.add("since");stopwordsset.add("six");stopwordsset.add("so");stopwordsset.add("some");stopwordsset.add("somebody");stopwordsset.add("somehow");stopwordsset.add("someone");stopwordsset.add("something");stopwordsset.add("sometime");stopwordsset.add("sometimes");stopwordsset.add("somewhat");stopwordsset.add("somewhere");stopwordsset.add("soon");stopwordsset.add("sorry");stopwordsset.add("specified");stopwordsset.add("specify");stopwordsset.add("specifying");stopwordsset.add("still");stopwordsset.add("sub");stopwordsset.add("such");stopwordsset.add("sup");stopwordsset.add("sure");stopwordsset.add("t");stopwordsset.add("take");stopwordsset.add("taken");stopwordsset.add("tell");stopwordsset.add("tends");stopwordsset.add("th");stopwordsset.add("than");stopwordsset.add("thank");stopwordsset.add("thanks");stopwordsset.add("thanx");stopwordsset.add("that");stopwordsset.add("thats");stopwordsset.add("the");stopwordsset.add("their");stopwordsset.add("theirs");stopwordsset.add("them");stopwordsset.add("themselves");stopwordsset.add("then");stopwordsset.add("thence");stopwordsset.add("there");stopwordsset.add("thereafter");stopwordsset.add("thereby");stopwordsset.add("therefore");stopwordsset.add("therein");stopwordsset.add("theres");stopwordsset.add("thereupon");stopwordsset.add("these");stopwordsset.add("they");stopwordsset.add("think");stopwordsset.add("third");stopwordsset.add("this");stopwordsset.add("thorough");stopwordsset.add("thoroughly");stopwordsset.add("those");stopwordsset.add("though");stopwordsset.add("three");stopwordsset.add("through");stopwordsset.add("throughout");stopwordsset.add("thru");stopwordsset.add("thus");stopwordsset.add("to");stopwordsset.add("together");stopwordsset.add("too");stopwordsset.add("took");stopwordsset.add("toward");stopwordsset.add("towards");stopwordsset.add("tried");stopwordsset.add("tries");stopwordsset.add("truly");stopwordsset.add("try");stopwordsset.add("trying");stopwordsset.add("twice");stopwordsset.add("two");
		stopwordsset.add("u");stopwordsset.add("un");stopwordsset.add("under");stopwordsset.add("unfortunately");stopwordsset.add("unless");stopwordsset.add("unlikely");stopwordsset.add("until");stopwordsset.add("unto");stopwordsset.add("up");stopwordsset.add("upon");stopwordsset.add("us");stopwordsset.add("use");stopwordsset.add("used");stopwordsset.add("useful");stopwordsset.add("uses");stopwordsset.add("using");stopwordsset.add("usually");stopwordsset.add("uucp");stopwordsset.add("v");stopwordsset.add("value");stopwordsset.add("various");stopwordsset.add("ve");stopwordsset.add("very");stopwordsset.add("via");stopwordsset.add("viz");stopwordsset.add("vs");stopwordsset.add("w");stopwordsset.add("want");stopwordsset.add("wants");stopwordsset.add("was");stopwordsset.add("way");stopwordsset.add("we");stopwordsset.add("welcome");stopwordsset.add("well");stopwordsset.add("went");stopwordsset.add("were");stopwordsset.add("what");stopwordsset.add("whatever");stopwordsset.add("when");stopwordsset.add("whence");stopwordsset.add("whenever");stopwordsset.add("where");stopwordsset.add("whereafter");stopwordsset.add("whereas");stopwordsset.add("whereby");stopwordsset.add("wherein");stopwordsset.add("whereupon");stopwordsset.add("wherever");stopwordsset.add("whether");stopwordsset.add("which");stopwordsset.add("while");stopwordsset.add("whither");stopwordsset.add("who");stopwordsset.add("whoever");stopwordsset.add("whole");stopwordsset.add("whom");stopwordsset.add("whose");stopwordsset.add("why");stopwordsset.add("will");stopwordsset.add("willing");stopwordsset.add("wish");stopwordsset.add("with");stopwordsset.add("within");stopwordsset.add("without");stopwordsset.add("wonder");stopwordsset.add("would");stopwordsset.add("would");stopwordsset.add("x");stopwordsset.add("y");stopwordsset.add("yes");stopwordsset.add("yet");stopwordsset.add("you");stopwordsset.add("your");stopwordsset.add("yours");stopwordsset.add("yourself");stopwordsset.add("yourselves");stopwordsset.add("z");stopwordsset.add("zero");   	  	
	
		Pattern p;
		Matcher m;
		boolean result;
		StringBuffer parsedresult=new StringBuffer("");
		p = Pattern.compile("(title:\\s*)(\\w*?)(\\s)");
		m = p.matcher(query);
		result = m.find();
		while(result) {
			titlelist+=m.group(2)+" ";
			m.appendReplacement(parsedresult, " ");
			result = m.find();
		}
		titlelist=l.matchSpaces(stm.getStemmedresult(new StringBuffer(getStoppedText(titlelist)))).toString().trim();
		
		parsedresult=new StringBuffer("");
		p = Pattern.compile("(infobox:\\s*)(\\w*?)(\\s)");
		m = p.matcher(query);
		result = m.find();
		while(result) {
			infolist+=m.group(2)+" ";
			m.appendReplacement(parsedresult, " ");
			result = m.find();
		}
		infolist=l.matchSpaces(new StringBuffer(getStoppedText(stm.getStemmedresult(new StringBuffer(infolist)).toString()))).toString().trim();
		
        parsedresult=new StringBuffer("");
        p = Pattern.compile("(category:\\s*)(\\w*?)(\\s)");
        m = p.matcher(query);
        result = m.find();
        while(result) {
        	ctglist+=m.group(2)+" ";
            m.appendReplacement(parsedresult, " ");
            result = m.find();
        }
        ctglist=l.matchSpaces(stm.getStemmedresult(new StringBuffer(getStoppedText(ctglist)))).toString().trim();
                 
        parsedresult=new StringBuffer("");
        p = Pattern.compile("(outlink:\\s*)(\\w*?)(\\s)");
        m = p.matcher(query);
        result = m.find();
        while(result) {
        	outlist+=m.group(2)+" ";
            m.appendReplacement(parsedresult, " ");
            result = m.find();
        }
        outlist=l.matchSpaces(new StringBuffer(getStoppedText(stm.getStemmedresult(new StringBuffer(getStoppedText(outlist))).toString()))).toString().trim();
        
        parsedresult=new StringBuffer("");
        p = Pattern.compile("(content:\\s*)(\\w*?)(\\s)");
        m = p.matcher(query);
        result = m.find();
        while(result) {
        	datalist+=m.group(2)+" ";
            m.appendReplacement(parsedresult, " ");
            result = m.find();
        }
        datalist=l.matchSpaces(stm.getStemmedresult(new StringBuffer(getStoppedText(datalist)))).toString().trim();	 
    }

	void getIID(String info,int k){
		info=info.trim();
		Set<Map.Entry<String,Long>> set = secinfo.entrySet();
		Iterator<Map.Entry<String,Long>> iterator = set.iterator();
		long bytes=0,oldvalue=0;
		String limit="";
		while(iterator.hasNext()) {			
			Map.Entry<String,Long> entry = (Map.Entry<String,Long>)iterator.next();	
			String key=entry.getKey();
			long value=entry.getValue();
			if(key.compareTo(info)>0)			{bytes=oldvalue;limit=key;break;}
			else if(key.compareTo(info)==0)	{bytes=value;break;}
			else								oldvalue=value;
		}
		//System.out.println(bytes);
		RandomAccessFile raf;
		try {
		raf = new RandomAccessFile(Indexpath+"InfoIndex.txt","r");
		raf.seek(bytes);
		String line=raf.readLine();
		while(line!=null&&line.split(":")[0].compareTo(info)<0&&line.split(":")[0].compareTo(limit)!=0)		line=raf.readLine();
		raf.close();
		if(line!=null&&line.split(":")[0].compareTo(info)==0){
			infoposting.put(line.split(":")[0],new StringBuffer(line.split(":")[1]));
			String[] pages=line.split(":")[1].split("#");
			for(int i=0;i<pages.length;i++)					si[k].add(Integer.valueOf(pages[i]));
		}
		}
		catch (FileNotFoundException e) {		e.printStackTrace();	}
		catch (IOException e)			{		e.printStackTrace();	}
		//System.out.println(infoposting);
	}

	public void infoRanking(){
		Map<Integer,Double> pscore=new HashMap<Integer,Double>();
		Map<Integer,Integer> pcount=new HashMap<Integer,Integer>();
		Set<Map.Entry<String,StringBuffer>> set = infoposting.entrySet();
		Iterator<Map.Entry<String,StringBuffer>> iterator = set.iterator();
		while(iterator.hasNext()){
			Map.Entry<String,StringBuffer> entry = (Map.Entry<String,StringBuffer>)iterator.next();
			//System.out.println(entry.getValue());
			String pages[]=entry.getValue().toString().split("#");
			double idf=Math.log10(N/pages.length);
			for(int i=0;i<pages.length;i++){
				if(pcount.containsKey(Integer.valueOf(pages[i])))	pcount.put(Integer.valueOf(pages[i]),pcount.get(Integer.valueOf(pages[i]))+1);
				else												pcount.put(Integer.valueOf(pages[i]),1);
				if(pscore.containsKey(Integer.valueOf(pages[i])))	pscore.put(Integer.valueOf(pages[i]),pcount.get(Integer.valueOf(pages[i]))+idf);
				else												pscore.put(Integer.valueOf(pages[i]),idf);				
			}
		}
		//System.out.println(pcount);
		//System.out.println(pscore);
		Iterator<Integer> iterator1 = iiset.iterator();
		while(iterator1.hasNext()){
			int pid=iterator1.next();
			double prank=((pcount.get(pid)*10000+pscore.get(pid))/infowords.length);
			ilinks.put(pid,prank);
		}
		//System.out.println(ilinks);
	}
	
	public void getOID(String out,int k){
			try{
			out=out.trim();
			Set<Map.Entry<String,Long>> set = secout.entrySet();
			Iterator<Map.Entry<String,Long>> iterator = set.iterator();
			long bytes=0,oldvalue=0;
			String limit="";
			while(iterator.hasNext()) {			
				Map.Entry<String,Long> entry = (Map.Entry<String,Long>)iterator.next();	
				String key=entry.getKey();
				long value=entry.getValue();
				if(key.compareTo(out)>0)			{bytes=oldvalue;limit=key;break;}
				else if(key.compareTo(out)==0)		{bytes=value;break;}
				else								oldvalue=value;
			}
			//System.out.println(bytes);
			RandomAccessFile raf;
			
			raf = new RandomAccessFile(Indexpath+"OutlinksIndex.txt","r");
			raf.seek(bytes);
			String line=raf.readLine();
			while(line!=null&&line.split(":")[0].compareTo(out)<0&&line.split(":")[0].compareTo(limit)!=0)		line=raf.readLine();
			raf.close();
			if(line!=null&&line.split(":")[0].compareTo(out)==0){
				outposting.put(line.split(":")[0],new StringBuffer(line.split(":")[1]));
				String[] pages=line.split(":")[1].split("#");
				for(int i=0;i<pages.length;i++)					so[k].add(Integer.valueOf(pages[i].split("-")[0]));
			}
			}
			catch (FileNotFoundException e) {		e.printStackTrace();	}
			catch (IOException e)			{		e.printStackTrace();	}
			//System.out.println(infoposting);
	}	
			
	public void outRanking(){
		Map<Integer,Double> pscore=new HashMap<Integer,Double>();
		Map<Integer,Integer> pcount=new HashMap<Integer,Integer>();
		Set<Map.Entry<String,StringBuffer>> set = outposting.entrySet();
		Iterator<Map.Entry<String,StringBuffer>> iterator = set.iterator();
		while(iterator.hasNext()){
			Map.Entry<String,StringBuffer> entry = (Map.Entry<String,StringBuffer>)iterator.next();
			//System.out.println(entry.getValue());
			String pages[]=entry.getValue().toString().split("#");
			double idf=Math.log10(N/pages.length);
			for(int i=0;i<pages.length;i++){
				int tf=Integer.valueOf(pages[i].split("-")[1]);
				int pid=Integer.valueOf(pages[i].split("-")[0]);
				if(pcount.containsKey(pid))			pcount.put(pid,pcount.get(pid)+1);
				else								pcount.put(pid,1);
				if(pscore.containsKey(pid))			pscore.put(pid,pcount.get(pid)+tf*idf);
				else								pscore.put(pid,tf*idf);				
			}
		}
		//System.out.println(pcount);
		//System.out.println(pscore);
		Iterator<Integer> iterator1 = oiset.iterator();
		while(iterator1.hasNext()){
			int pid=iterator1.next();
			double prank=((pcount.get(pid)*10000+pscore.get(pid))/outwords.length)*10;
			olinks.put(pid,prank);
		}
		//System.out.println(olinks);
	}	
	
	public void getCID(String cat,int k){
		try{
		cat=cat.trim();
		Set<Map.Entry<String,Long>> set = seccat.entrySet();
		Iterator<Map.Entry<String,Long>> iterator = set.iterator();
		long bytes=0,oldvalue=0;
		String limit="";
		while(iterator.hasNext()) {			
			Map.Entry<String,Long> entry = (Map.Entry<String,Long>)iterator.next();	
			String key=entry.getKey();
			long value=entry.getValue();
			if(key.compareTo(cat)>0)			{bytes=oldvalue;limit=key;break;}
			else if(key.compareTo(cat)==0)		{bytes=value;break;}
			else								oldvalue=value;
		}
		RandomAccessFile raf;		
		raf = new RandomAccessFile(Indexpath+"CategoryIndex.txt","r");
		raf.seek(bytes);
		String line=raf.readLine();
		while(line!=null&&line.split(":")[0].compareTo(cat)<0&&line.split(":")[0].compareTo(limit)!=0)		line=raf.readLine();
		raf.close();
		if(line!=null&&line.split(":")[0].compareTo(cat)==0){
			catposting.put(line.split(":")[0],new StringBuffer(line.split(":")[1]));
			String[] pages=line.split(":")[1].split("#");
			if(line.length()<50000)	for(int i=0;i<pages.length;i++)					sc[k].add(Integer.valueOf(pages[i].split("-")[0]));
			else					for(int i=0;i<pages.length;i++)	if(Integer.valueOf(pages[i].split("-")[1])>2)	sc[k].add(Integer.valueOf(pages[i].split("-")[0]));
		}
		}
		catch (FileNotFoundException e) {		e.printStackTrace();	}
		catch (IOException e)			{		e.printStackTrace();	}
		//System.out.println(infoposting);
}	
		
	public void catRanking(){
		Map<Integer,Double> pscore=new HashMap<Integer,Double>();
		Map<Integer,Integer> pcount=new HashMap<Integer,Integer>();
		Set<Map.Entry<String,StringBuffer>> set = catposting.entrySet();
		Iterator<Map.Entry<String,StringBuffer>> iterator = set.iterator();
		while(iterator.hasNext()){
			Map.Entry<String,StringBuffer> entry = (Map.Entry<String,StringBuffer>)iterator.next();
			String pages[]=entry.getValue().toString().split("#");
			double idf=Math.log10(N/pages.length);
			for(int i=0;i<pages.length;i++){
				int tf=Integer.valueOf(pages[i].split("-")[1]);
				int pid=Integer.valueOf(pages[i].split("-")[0]);
				if(pcount.containsKey(pid))			pcount.put(pid,pcount.get(pid)+1);
				else								pcount.put(pid,1);
				if(pscore.containsKey(pid))			pscore.put(pid,pcount.get(pid)+tf*idf);
				else								pscore.put(pid,tf*idf);				
			}
		}
		Iterator<Integer> iterator1 = ciset.iterator();
		while(iterator1.hasNext()){
			int pid=iterator1.next();
			double prank=((pcount.get(pid)*10000+pscore.get(pid))/ctgwords.length);
			clinks.put(pid,prank);
		}
	//System.out.println(clinks);
	}	

	public void getDID(String data,int k){
		try{		
			data=data.trim();
			/*
			Set<Map.Entry<String,Long>> set = secout.entrySet();
			Iterator<Map.Entry<String,Long>> iterator = set.iterator();
			long bytes=0,oldvalue=0;
			String limit="";
			while(iterator.hasNext()) {			
				Map.Entry<String,Long> entry = (Map.Entry<String,Long>)iterator.next();	
				String key=entry.getKey();
				long value=entry.getValue();
				if(key.compareTo(data)>0)			{bytes=oldvalue;limit=key;break;}
				else if(key.compareTo(data)==0)		{bytes=value;break;}
				else								oldvalue=value;
			}
			RandomAccessFile raf;		
			raf = new RandomAccessFile(Indexpath+"OutlinksIndex.txt","r");
			raf.seek(bytes);
			String line=raf.readLine();
			while(line!=null&&line.split(":")[0].compareTo(data)<0&&line.split(":")[0].compareTo(limit)!=0)		line=raf.readLine();
			raf.close();
			if(line!=null&&line.split(":")[0].compareTo(data)==0){
				dataoutposting.put(line.split(":")[0],new StringBuffer(line.split(":")[1]));
				String[] pages=line.split(":")[1].split("#");
				for(int i=0;i<pages.length;i++)					sdout[k].add(Integer.valueOf(pages[i].split("-")[0]));
			}
			//System.out.println(dataoutposting);	
			*/
			Set<Map.Entry<String,Long>> set = secdata.entrySet();
			Iterator<Map.Entry<String,Long>> iterator = set.iterator();
			long bytes=0,oldvalue=0;
			String limit="";
			while(iterator.hasNext()) {			
				Map.Entry<String,Long> entry = (Map.Entry<String,Long>)iterator.next();	
				String key=entry.getKey();
				long value=entry.getValue();
				if(key.compareTo(data)>0)			{bytes=oldvalue;limit=key;break;}
				else if(key.compareTo(data)==0)		{bytes=value;break;}
				else								oldvalue=value;
			}		
			RandomAccessFile raf = new RandomAccessFile(Indexpath+"TextIndex.txt","r");
			raf.seek(bytes);
			String line=raf.readLine();
			while(line!=null&&line.split(":")[0].compareTo(data)<0&&line.split(":")[0].compareTo(limit)!=0)		line=raf.readLine();
			raf.close();
			if(line!=null&&line.split(":")[0].compareTo(data)==0){
				dataposting.put(line.split(":")[0],new StringBuffer(line.split(":")[1]));
				String[] pages=line.split(":")[1].split("#");
				if(line.length()<50000)	for(int i=0;i<pages.length;i++)					sd[k].add(Integer.valueOf(pages[i].split("-")[0]));
				else					for(int i=0;i<pages.length;i++)	if(Integer.valueOf(pages[i].split("-")[1])>2)	sd[k].add(Integer.valueOf(pages[i].split("-")[0]));

				//for(int i=0;i<pages.length;i++)					sd[k].add(Integer.valueOf(pages[i].split("-")[0]));
			}
		}
		catch (FileNotFoundException e) {		e.printStackTrace();	}
		catch (IOException e)			{		e.printStackTrace();	}
		//System.out.println(dataposting);
	}	
		
	public void dataRanking(){
		/*
		Map<Integer,Double> poutscore=new HashMap<Integer,Double>();
		Map<Integer,Integer> poutcount=new HashMap<Integer,Integer>();
		Set<Map.Entry<String,StringBuffer>> set = dataoutposting.entrySet();
		Iterator<Map.Entry<String,StringBuffer>> iterator = set.iterator();
		while(iterator.hasNext()){
			Map.Entry<String,StringBuffer> entry = (Map.Entry<String,StringBuffer>)iterator.next();
			String pages[]=entry.getValue().toString().split("#");
			double idf=Math.log10(N/pages.length);
			for(int i=0;i<pages.length;i++){
				int tf=Integer.valueOf(pages[i].split("-")[1]);
				int pid=Integer.valueOf(pages[i].split("-")[0]);
				if(poutcount.containsKey(pid))			poutcount.put(pid,poutcount.get(pid)+1);
				else									poutcount.put(pid,1);
				if(poutscore.containsKey(pid))			poutscore.put(pid,poutcount.get(pid)+tf*idf);
				else									poutscore.put(pid,tf*idf);				
			}
		}
		Iterator<Integer> iterator1 = doutiset.iterator();
		while(iterator1.hasNext()){
			int pid=iterator1.next();
			double prank=((poutcount.get(pid)*10000+poutscore.get(pid))/datawords.length);
			doutlinks.put(pid,prank);
		}
		*/
		Map<Integer,Double> pscore=new HashMap<Integer,Double>();
		Map<Integer,Integer> pcount=new HashMap<Integer,Integer>();
		Set<Map.Entry<String,StringBuffer>> set = dataposting.entrySet();
		Iterator<Map.Entry<String,StringBuffer>> iterator = set.iterator();
		while(iterator.hasNext()){
			Map.Entry<String,StringBuffer> entry = (Map.Entry<String,StringBuffer>)iterator.next();
			String pages[]=entry.getValue().toString().split("#");
			double idf=Math.log10(N/pages.length);
			for(int i=0;i<pages.length;i++){
				int tf=Integer.valueOf(pages[i].split("-")[1]);
				int pid=Integer.valueOf(pages[i].split("-")[0]);
				if(pcount.containsKey(pid))			pcount.put(pid,pcount.get(pid)+1);
				else								pcount.put(pid,1);
				if(pscore.containsKey(pid))			pscore.put(pid,pcount.get(pid)+tf*idf);
				else								pscore.put(pid,tf*idf);				
			}
		}
		
		Iterator<Integer> iterator1 = diset.iterator();
		while(iterator1.hasNext()){
			int pid=iterator1.next();
			double prank=((pcount.get(pid)*10000+pscore.get(pid))/datawords.length);
			dlinks.put(pid,prank);
		}
		
	}	
	
	public static void main(String x[]){
		System.out.println("Enter index path(J:/IRE/new index on 8gb/): ");
		Scanner sc=new Scanner(System.in);
		String path=sc.nextLine();
		Searcher s=new Searcher(path,2930478);
		System.out.println("Searching started::Enter query:\n");
		sc=new Scanner(System.in);
		String query=sc.nextLine();
		s.getResults(query);
//		Searcher s=new Searcher("J:/IRE/new index on 8gb/",2930478);
//		s.getResults(" content: sachin title: fdsafvdv");
	}
}