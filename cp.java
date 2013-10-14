/*
 * author:idevcod@gmail.com
 * date:2013/9/16
 * description:多线程复制
 * version:0.1
 * nextVersionDescription:实现多线程下载
 * */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.ThreadGroup;
import static java.lang.System.*;

class save_thread extends Thread {
	private String name;
	private FileInputStream input=null;
	private RandomAccessFile rfwrite=null;
	private long beginPos=0,endPos=0,currentPos=0;
	private int c;
	private byte b[]=new byte[128];
	public save_thread(String name){
		this.name=name;
	}
	public save_thread(ThreadGroup tg,String name,long beginPos,long endPos){
		super(tg,name);
		this.name=name;
		this.beginPos=beginPos;
		this.endPos=endPos;
	}

	@Override
	public void run() {
		try{
		String srcFile="/home/dell/workspace/copy_java/src/Brand New Start.mp3";
		String desFile="/home/dell/workspace/copy_java/src/des.mp3";
		File in=new File(srcFile);
		rfwrite=new RandomAccessFile(desFile,"rw");
		input= new FileInputStream(in);
		}catch(IOException e){
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		try {
			currentPos=beginPos;
			out.println("线程"+name+"开始字节是"+beginPos);
			out.println("线程"+name+"理论结束字节是"+endPos);
			long skip_bytes=input.skip(beginPos);
			rfwrite.seek(skip_bytes);
			while((c=input.read(b))!=-1){
				//out.println("线程"+name+"在活动");
				currentPos+=c;
				if(currentPos>endPos){//该块已传输结束
					//out.println(">endPos读取的文件有"+(c-(currentPos-endPos)));//currentPos-endPos为超出自己本块的数据量
					rfwrite.write(b, 0, (c-(int)((currentPos-endPos))));
					//currentPos=endPos+1;
					break;
				}else{
					//out.println("读取的文件有"+c);
					rfwrite.write(b);
				}
			}
			out.println("线程"+name+"结束字节是"+currentPos);
			input.close();
			rfwrite.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
public class cp {
	//private int fileLength=0;
	private long fileLength=0;
	int threads=5;
	long blocks=0;
	private long beginPos=0,endPos=0;
	//byte b[]=new byte[4*1024];
	protected void cp_file() throws IOException{
		System.out.println(System.getProperty("user.dir"));
		String srcFile="/home/dell/workspace/copy_java/src/Brand New Start.mp3";
		File input=new File(srcFile);
		fileLength=input.length();
		// TODO Auto-generated catch block					
		ThreadGroup tg=new ThreadGroup("download");
		out.println(fileLength);
		long starttime=System.currentTimeMillis();//毫秒记
		blocks=fileLength/threads;
		for(int i=0;i<threads;i++){
			if(i!=threads-1)
				endPos=beginPos+blocks;
			else{
				endPos=fileLength;
				out.println(i+"个"+endPos);
			}
			save_thread download=new save_thread(tg,Integer.toString(i),beginPos,endPos);
			download.start();
			beginPos=endPos;
		}
		//fileLength=(int)fileLength;
		/*save_thread download=new save_thread(tg,"download",beginPos,endPos,in,rfwrite);
		download.start();*/
		while (tg.activeCount() > 0) {  
			out.println("活动线程有"+tg.activeCount());
            try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
        }  
		in.close();
		//rfwrite.close();
		long endtime=System.currentTimeMillis();
		long usetime=(endtime-starttime)/1000;
		out.println("复制用时"+usetime);
		out.println("end");
	}
	public static void main(String args[]) throws IOException{
		cp mycp=new cp();
		mycp.cp_file();
		out.println("ok");
	}
}
