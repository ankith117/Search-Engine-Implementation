import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Index{
	TreeMap<String,StringBuffer> tm,im,cm,hm,dm;
	TreeMap<Integer,String> ot,rm;
	String[] titlewords,infowords,hpdatawords,ctgwords,datawords;//,redirectwords;
	String redirectwords,originaltitle;
	Integer pid;
	BufferedWriter bw;
	int iter;
	String path;
	public Index(int x,String y){
		iter=x;	
		path=y;
		rm=new TreeMap<Integer,String>();
		ot=new TreeMap<Integer,String>();
		tm=new TreeMap<String,StringBuffer>();
		im=new TreeMap<String,StringBuffer>();		
	}
	public void add(StringBuffer title,int id,StringBuffer info,StringBuffer hpdata,StringBuffer ctg,StringBuffer data,StringBuffer redirect,String otstring){
		if(cm==null)	cm=new TreeMap<String,StringBuffer>();
		if(hm==null)	hm=new TreeMap<String,StringBuffer>();
		if(dm==null)	dm=new TreeMap<String,StringBuffer>();
	
		if(title.length()>0) titlewords=title.toString().split(" ");
		else			   	 titlewords=null;
		if(info.length()>0)	 infowords=info.toString().split(" ");
		else			   	 infowords=null;
		pid=id;
		if(hpdata.length()>0)hpdatawords=hpdata.toString().split(" ");
		else 				 hpdatawords=null;
		if(ctg.length()>0)	 ctgwords=ctg.toString().split(" ");
		else 				 ctgwords=null;		
		if(data.length()>0)	 datawords=data.toString().split(" ");
		else				 datawords=null;	
		
		redirectwords=redirect.toString().trim();
		originaltitle=otstring;
		
		createIndex(pid);
	}
	
	public void createIndex(Integer pid){	
		StringBuffer p=new StringBuffer(pid+"#");
		
		if(redirectwords!=null&&redirectwords.length()>0)	rm.put(pid,redirectwords);
		ot.put(pid,originaltitle);
		
		
		if(titlewords!=null)
			for(int i=0;i<titlewords.length;i++)
				if(titlewords[i].length()>0){
					if(!tm.containsKey(titlewords[i]))			tm.put(titlewords[i],new StringBuffer(pid+"#"));	
					else										tm.put(titlewords[i],tm.get(titlewords[i]).append(p));
				}
		
		if(infowords!=null)		
		for(int i=0;i<infowords.length;i++)
			if(infowords[i].length()>0){			
				if(!im.containsKey(infowords[i]))			im.put(infowords[i],new StringBuffer(pid+"#"));	
				else										im.put(infowords[i],im.get(infowords[i]).append(p));
			}
		
		if(ctgwords!=null){	
			Map<String,Integer> cmap=new HashMap<String,Integer>();
			for(int i=0;i<ctgwords.length;i++)
			if(ctgwords[i].length()>0){			
				if(!cmap.containsKey(ctgwords[i]))			cmap.put(ctgwords[i],new Integer(1));	
				else{	Integer j=cmap.get(ctgwords[i]);	j++;		cmap.put(ctgwords[i], j);		}
			}
			Set<Map.Entry<String,Integer>> set = cmap.entrySet();
			Iterator<Map.Entry<String,Integer>> iterator = set.iterator();
			while(iterator.hasNext()) {
				Map.Entry<String,Integer> entry = (Map.Entry<String,Integer>)iterator.next();	
				String key=entry.getKey();
				int value=entry.getValue();
				if(!cm.containsKey(key))			cm.put(key,new StringBuffer(pid+"-"+value+"#"));	
				else								cm.put(key,cm.get(key).append(pid+"-"+value+"#"));
			}
			cmap=null;set=null;iterator=null;
		}
		if(hpdatawords!=null){
			Map<String,Integer> hpmap=new HashMap<String,Integer>();
			for(int i=0;i<hpdatawords.length;i++)
				if(hpdatawords[i].length()>0){			
					if(!hpmap.containsKey(hpdatawords[i]))			hpmap.put(hpdatawords[i],new Integer(1));	
					else{	Integer j=hpmap.get(hpdatawords[i]);	j++;		hpmap.put(hpdatawords[i], j);		}
				}
			Set<Map.Entry<String,Integer>> set = hpmap.entrySet();
			Iterator<Map.Entry<String,Integer>> iterator = set.iterator();
			while(iterator.hasNext()) {
				Map.Entry<String,Integer> entry = (Map.Entry<String,Integer>)iterator.next();	
				String key=entry.getKey();
				int value=entry.getValue();
				if(!hm.containsKey(key))			hm.put(key,new StringBuffer(pid+"-"+value+"#"));	
				else								hm.put(key,hm.get(key).append(pid+"-"+value+"#"));
			}
			hpmap=null;set=null;iterator=null;
		}
		if(datawords!=null){
			Map<String,Integer> dmap=new HashMap<String,Integer>();
			for(int i=0;i<datawords.length;i++)
				if(datawords[i].length()>0){			
					if(!dmap.containsKey(datawords[i]))			dmap.put(datawords[i],new Integer(1));	
					else{	Integer j=dmap.get(datawords[i]);	j++;		dmap.put(datawords[i], j);		}
				}
			Set<Map.Entry<String,Integer>> set = dmap.entrySet();
			Iterator<Map.Entry<String,Integer>> iterator = set.iterator();
			while(iterator.hasNext()) {
				Map.Entry<String,Integer> entry = (Map.Entry<String,Integer>)iterator.next();	
				String key=entry.getKey();
				int value=entry.getValue();
				if(!dm.containsKey(key))			dm.put(key,new StringBuffer(pid+"-"+value+"#"));	
				else								dm.put(key,dm.get(key).append(pid+"-"+value+"#"));
			}
			dmap=null;set=null;iterator=null;
		}
	}

	public void print(int n){
		double d=n;
		String s=new Integer(new Double(Math.ceil(d/iter)).intValue()).toString();
		try {
		bw = new BufferedWriter(new FileWriter(path+"c"+s+".txt",false));
		Set<Map.Entry<String,StringBuffer>> set = cm.entrySet();
		Iterator<Map.Entry<String,StringBuffer>> iterator = set.iterator();

		while(iterator.hasNext()) {
			Map.Entry<String,StringBuffer> entry = (Map.Entry<String,StringBuffer>)iterator.next();	
			StringBuffer indxwrite=new StringBuffer(entry.getKey() +  ":" +        entry.getValue() + "\n");
			bw.write(indxwrite.toString());        
		}
		bw.flush();
		bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		cm=null;
		ctgwords=null;
		try {
		bw = new BufferedWriter(new FileWriter(path+"h"+s+".txt",false));
		Set<Map.Entry<String,StringBuffer>> set = hm.entrySet();
		Iterator<Map.Entry<String,StringBuffer>> iterator = set.iterator();
		while(iterator.hasNext()) {
			Map.Entry<String,StringBuffer> entry = (Map.Entry<String,StringBuffer>)iterator.next();			
            StringBuffer indxwrite=new StringBuffer(entry.getKey() +  ":" +        entry.getValue() + "\n");
            bw.write(indxwrite.toString());
		}
		bw.flush();
		bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		hm=null;
		hpdatawords=null;
		try {
		bw = new BufferedWriter(new FileWriter(path+"d"+s+".txt",false));
		Set<Map.Entry<String,StringBuffer>> set = dm.entrySet();
		Iterator<Map.Entry<String,StringBuffer>> iterator = set.iterator();
		while(iterator.hasNext()) {
			Map.Entry<String,StringBuffer> entry = (Map.Entry<String,StringBuffer>)iterator.next();
            StringBuffer indxwrite=new StringBuffer(entry.getKey()+":" +entry.getValue()+"\n");
            bw.write(indxwrite.toString());
		}
		bw.flush();
		bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		dm=null;
		datawords=null;
		
	}
	public void merge(int n){
		double d=n;
		File f=null,temp=null;
		
		try {bw = new BufferedWriter(new FileWriter(path+"TitleIndex.txt",false));	
		Set<Map.Entry<String,StringBuffer>> set = tm.entrySet();
		Iterator<Map.Entry<String,StringBuffer>> iterator = set.iterator();
		while(iterator.hasNext()) {
			Map.Entry<String,StringBuffer> entry = (Map.Entry<String,StringBuffer>)iterator.next();
            StringBuffer indxwrite=new StringBuffer(entry.getKey()+":" +entry.getValue()+"\n");
            bw.write(indxwrite.toString());
		}
		bw.flush();
		bw.close();			 
		}catch (IOException e) {			e.printStackTrace();			}
		tm=null;
		titlewords=null;

		try {bw = new BufferedWriter(new FileWriter(path+"RedirectIndex.txt",false));	
		Set<Map.Entry<Integer,String>> set = rm.entrySet();
		Iterator<Map.Entry<Integer,String>> iterator = set.iterator();
		while(iterator.hasNext()) {
			Map.Entry<Integer,String> entry = (Map.Entry<Integer,String>)iterator.next();
            StringBuffer indxwrite=new StringBuffer(entry.getKey()+":" +entry.getValue()+"\n");
            bw.write(indxwrite.toString());
		}
		bw.flush();
		bw.close();			 
		}catch (IOException e) {			e.printStackTrace();			}
		rm=null;
		redirectwords=null;
		
		try {bw = new BufferedWriter(new FileWriter(path+"IdIndex.txt",false));	
		Set<Map.Entry<Integer,String>> set = ot.entrySet();
		Iterator<Map.Entry<Integer,String>> iterator = set.iterator();
		while(iterator.hasNext()) {
			Map.Entry<Integer,String> entry = (Map.Entry<Integer,String>)iterator.next();
            StringBuffer indxwrite=new StringBuffer(entry.getKey()+":" +entry.getValue()+"\n");
            bw.write(indxwrite.toString());
		}
		bw.flush();
		bw.close();			 
		}catch (IOException e) {			e.printStackTrace();			}
		ot=null;
		originaltitle=null;
		
		try {bw = new BufferedWriter(new FileWriter(path+"InfoIndex.txt",false));
		//for(Map.Entry<String,StringBuffer> entry: im.entrySet())		bw.write(entry.getKey()+":"+entry.getValue()+"\n");			
		Set<Map.Entry<String,StringBuffer>> set = im.entrySet();
		Iterator<Map.Entry<String,StringBuffer>> iterator = set.iterator();
		while(iterator.hasNext()) {
			Map.Entry<String,StringBuffer> entry = (Map.Entry<String,StringBuffer>)iterator.next();
            StringBuffer indxwrite=new StringBuffer(entry.getKey()+":" +entry.getValue()+"\n");
            bw.write(indxwrite.toString());
		}
		bw.flush();
		bw.close();
		}catch (IOException e) {			e.printStackTrace();			}
		im=null;
		infowords=null;
		
		Integer N=new Integer(new Double(Math.ceil(d/iter)).intValue());
		try {
			int no=N.intValue();
			BufferedReader fr1,fr2;
			BufferedWriter fr3;
			String line1,line2;
			String word1,word2;
			no=N.intValue();
			for(int i=1;i<no;i+=2){
				File f1=new File(path+"c"+Integer.toString(i)+".txt");
				File f2=new File(path+"c"+Integer.toString(i+1)+".txt");
				
				fr1 = new BufferedReader(new FileReader(f1));
				fr2=  new BufferedReader(new FileReader(f2));
				no++;
				f=new File(path+"c"+no+".txt");
				fr3=new BufferedWriter(new FileWriter(f));
				line1=fr1.readLine();
				if(line1!=null)word1=line1.split(":")[0];				else			word1="";
				line2=fr2.readLine();
				if(line2!=null)word2=line2.split(":")[0];				else			word2="";
				while(line1!=null&&line2!=null){
					if(word1.compareTo(word2)>0){
						fr3.write(word2+":"+line2.split(":")[1]+"\n");
						line2=fr2.readLine();
						if(line2!=null)word2=line2.split(":")[0];
					}
					else if(word1.compareTo(word2)<0){
						fr3.write(word1+":"+line1.split(":")[1]+"\n");
						line1=fr1.readLine();
						if(line1!=null)word1=line1.split(":")[0];
					}
					else{
						fr3.write(word2+":"+line1.split(":")[1]+line2.split(":")[1]+"\n");
						line1=fr1.readLine();
						if(line1!=null)word1=line1.split(":")[0];
						line2=fr2.readLine();
						if(line2!=null)word2=line2.split(":")[0];
					}
				}
				while(line1!=null){
					fr3.write(line1+"\n");
					line1=fr1.readLine();					
				}
				while(line2!=null){
					fr3.write(line2+"\n");
					line2=fr2.readLine();
				}
				fr1.close();
				fr2.close();
				fr3.flush();
				fr3.close();
				f1.delete();
				f2.delete();
			}
			temp=new File(path+"CategoryIndex.txt");
			temp.delete();
			f.renameTo(new File(path+"CategoryIndex.txt"));
			
			
			
			no=N.intValue();
			for(int i=1;i<no;i+=2){
				File f1=new File(path+"h"+Integer.toString(i)+".txt");
				File f2=new File(path+"h"+Integer.toString(i+1)+".txt");
				fr1 = new BufferedReader(new FileReader(f1));
				fr2=  new BufferedReader(new FileReader(f2));
				
				no++;
				f=new File(path+"h"+no+".txt");
				fr3=new BufferedWriter(new FileWriter(f));
				line1=fr1.readLine();
				if(line1!=null)word1=line1.split(":")[0];				else			word1="";
				line2=fr2.readLine();
				if(line2!=null)word2=line2.split(":")[0];				else			word2="";
				while(line1!=null&&line2!=null){
					if(word1.compareTo(word2)>0){
						fr3.write(word2+":"+line2.split(":")[1]+"\n");						fr3.flush();
						line2=fr2.readLine();
						if(line2!=null)word2=line2.split(":")[0];
					}
					else if(word1.compareTo(word2)<0){
						fr3.write(word1+":"+line1.split(":")[1]+"\n");		fr3.flush();
						line1=fr1.readLine();
						if(line1!=null)word1=line1.split(":")[0];
					}
					else{
						fr3.write(word2+":"+line1.split(":")[1]+line2.split(":")[1]+"\n");	fr3.flush();
						line1=fr1.readLine();
						if(line1!=null)word1=line1.split(":")[0];
						line2=fr2.readLine();
						if(line2!=null)word2=line2.split(":")[0];
					}
				}
				while(line1!=null){
					fr3.write(line1+"\n");	fr3.flush();
					line1=fr1.readLine();					
				}
				while(line2!=null){
					fr3.write(line2+"\n");	fr3.flush();
					line2=fr2.readLine();
				}
				
				fr1.close();
				fr2.close();
				fr3.flush();
				fr3.close();
				f1.delete();
				f2.delete();
			}
			temp=new File(path+"OutlinksIndex.txt");
			temp.delete();
			f.renameTo(new File(path+"OutlinksIndex.txt"));
		
			no=N.intValue();
			for(int i=1;i<no;i+=2){
				File f1=new File(path+"d"+Integer.toString(i)+".txt");
				File f2=new File(path+"d"+Integer.toString(i+1)+".txt");
				fr1 = new BufferedReader(new FileReader(f1));
				fr2=  new BufferedReader(new FileReader(f2));
				
				no++;
				f=new File(path+"d"+no+".txt");
				fr3=new BufferedWriter(new FileWriter(f));
				line1=fr1.readLine();
				if(line1!=null)word1=line1.split(":")[0];				else			word1="";
				line2=fr2.readLine();
				if(line2!=null)word2=line2.split(":")[0];				else			word2="";
				while(line1!=null&&line2!=null){
					if(word1.compareTo(word2)>0){
						fr3.write(word2+":"+line2.split(":")[1]+"\n");fr3.flush();
						line2=fr2.readLine();
						if(line2!=null)word2=line2.split(":")[0];
					}
					else if(word1.compareTo(word2)<0){
						fr3.write(word1+":"+line1.split(":")[1]+"\n");fr3.flush();
						line1=fr1.readLine();
						if(line1!=null)word1=line1.split(":")[0];
					}
					else{
						fr3.write(word2+":"+line1.split(":")[1]+line2.split(":")[1]+"\n");fr3.flush();
						line1=fr1.readLine();
						if(line1!=null)word1=line1.split(":")[0];
						line2=fr2.readLine();
						if(line2!=null)word2=line2.split(":")[0];
					}
				}
				while(line1!=null){
					fr3.write(line1+"\n");fr3.flush();
					line1=fr1.readLine();					
				}
				while(line2!=null){
					fr3.write(line2+"\n");fr3.flush();
					line2=fr2.readLine();
				}
				fr1.close();
				fr2.close();
				fr3.flush();
				fr3.close();
				f1.delete();
				f2.delete();
			}
			temp=new File(path+"TextIndex.txt");
			temp.delete();
			f.renameTo(new File(path+"TextIndex.txt"));
			
			new SecIndexer(path);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	public void print1(){
		System.out.println("title map:");
		for(Map.Entry<String,StringBuffer> entry: tm.entrySet()){
			System.out.println("key:\t"+entry.getKey()+"\tvalue:\t"+entry.getValue());
		}
		System.out.println("info map:");
		for(Map.Entry<String,StringBuffer> entry: im.entrySet()){
			System.out.println("key:\t"+entry.getKey()+"\tvalue:\t"+entry.getValue());
		}
		System.out.println("ctg map:");
		for(Map.Entry<String,StringBuffer> entry: cm.entrySet()){
			System.out.println("key:\t"+entry.getKey()+"\tvalue:\t"+entry.getValue());
		}
		System.out.println("hp map:");
		for(Map.Entry<String,StringBuffer> entry: hm.entrySet()){
			System.out.println("key:\t"+entry.getKey()+"\tvalue:\t"+entry.getValue());
		}
		System.out.println("d map:");
		for(Map.Entry<String,StringBuffer> entry: dm.entrySet()){
			System.out.println("key:\t"+entry.getKey()+"\tvalue:\t"+entry.getValue());
		}
	}
	public static void main(String x[]){
		Index i=new Index(5,"kjkj");		
		i.add(new StringBuffer("hithere"),1,new StringBuffer("hi"),	new StringBuffer("  bi bi "),	new StringBuffer("hi ci"),	new StringBuffer("asdf fgdf asdf"),	 new StringBuffer("xxx "),"");
		i.add(new StringBuffer("hi ci"),2,	 new StringBuffer(""),		new StringBuffer("hello hi"),	new StringBuffer("hi"),		new StringBuffer(" fgdf "),	 new StringBuffer("xxx "),"");
		i.add(new StringBuffer("hi hi"),3,	 new StringBuffer("there"),	new StringBuffer(""),			new StringBuffer("ci "),	new StringBuffer(" "),	 new StringBuffer(""),"");
		i.print1();
		i=null;
	}
}