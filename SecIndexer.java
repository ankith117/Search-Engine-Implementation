import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SecIndexer{
	String path;
	public SecIndexer(String y){
		try{
			path=y;
			
			BufferedReader br=new BufferedReader(new FileReader(new File(path+"CategoryIndex.txt")));
			BufferedWriter bw=new BufferedWriter(new FileWriter(new File(path+"SecCategoryIndex.txt")));
			String line=br.readLine();
			long noofbytes=0;

			for(int count=0;line!=null;count++){
				if(count!=0&&count%100==0){
					String i=line.split(":")[0];
					bw.write(i+":"+noofbytes+"\n");
				}				
				noofbytes+=line.length()+1;
				line=br.readLine();
			}
			
			bw.flush();
			bw.close();
			
			
			br=new BufferedReader(new FileReader(new File(path+"OutlinksIndex.txt")));
			bw=new BufferedWriter(new FileWriter(new File(path+"SecOutlinksIndex.txt")));
			line=br.readLine();
			noofbytes=0;
			for(int count=0;line!=null;count++){
				if(count!=0&&count%100==0){
					String i=line.split(":")[0];
					bw.write(i+":"+noofbytes+"\n");
				}				
				noofbytes+=line.length()+1;
				line=br.readLine();
			}
			bw.flush();
			bw.close();
	
			br=new BufferedReader(new FileReader(new File(path+"TextIndex.txt")));
			bw=new BufferedWriter(new FileWriter(new File(path+"SecTextIndex.txt")));
			line=br.readLine();
			noofbytes=0;
			for(int count=0;line!=null;count++){
				if(count!=0&&count%100==0){
					String i=line.split(":")[0];
					bw.write(i+":"+noofbytes+"\n");
				}				
				noofbytes+=line.length()+1;
				line=br.readLine();
			}
			bw.flush();
			bw.close();	
			
			br=new BufferedReader(new FileReader(new File(path+"IdIndex.txt")));
			bw=new BufferedWriter(new FileWriter(new File(path+"SecIdIndex.txt")));
			line=br.readLine();
			noofbytes=0;
			for(int count=0;line!=null;count++){
				if(count!=0&&count%100==0){
					String i=line.split(":")[0];
					bw.write(i+":"+noofbytes+"\n");
				}				
				noofbytes+=line.length()+1;
				line=br.readLine();
			}
			bw.flush();
			bw.close();
			
			br=new BufferedReader(new FileReader(new File(path+"RedirectIndex.txt")));
			bw=new BufferedWriter(new FileWriter(new File(path+"SecRedirectIndex.txt")));
			line=br.readLine();
			noofbytes=0;
			for(int count=0;line!=null;count++){
				if(count!=0&&count%100==0){
					String i=line.split(":")[0];
					bw.write(i+":"+noofbytes+"\n");
				}				
				noofbytes+=line.length()+1;
				line=br.readLine();
			}
			bw.flush();
			bw.close();
			
			br=new BufferedReader(new FileReader(new File(path+"TitleIndex.txt")));
			bw=new BufferedWriter(new FileWriter(new File(path+"SecTitleIndex.txt")));
			line=br.readLine();
			noofbytes=0;
			for(int count=0;line!=null;count++){
				if(count!=0&&count%100==0){
					String i=line.split(":")[0];
					bw.write(i+":"+noofbytes+"\n");
				}				
				noofbytes+=line.length()+1;
				line=br.readLine();
			}
			bw.flush();
			bw.close();
			
			br=new BufferedReader(new FileReader(new File(path+"InfoIndex.txt")));
			bw=new BufferedWriter(new FileWriter(new File(path+"SecInfoIndex.txt")));
			line=br.readLine();
			noofbytes=0;
			for(int count=0;line!=null;count++){
				if(count!=0&&count%100==0){
					String i=line.split(":")[0];
					bw.write(i+":"+noofbytes+"\n");
				}				
				noofbytes+=line.length()+1;
				line=br.readLine();
			}
			bw.flush();
			bw.close();
			
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public static void main(String x[]){
		long time=System.currentTimeMillis();
		new SecIndexer("J:/IRE/new index on 8gb/");
		System.out.println(time=(System.currentTimeMillis()-time)/1000);
	}
}
