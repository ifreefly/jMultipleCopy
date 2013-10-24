/*
 * author:idevcod@gmail.com
 * date:2013/9/16
 * description:���̸߳���
 * version:0.1
 * nextVersionDescription:ʵ�ֶ��߳�����
 * */
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.ThreadGroup;
import java.net.HttpURLConnection;
import java.net.URL;
//import httpDownload;
import static java.lang.System.*;

class save_thread extends Thread {
	private String name;
	//private FileInputStream input=null;
	BufferedInputStream input=null; 
	private RandomAccessFile rfwrite=null;
	private long beginPos=0,endPos=0,currentPos=0;
	private int c;
	private byte b[]=new byte[1024];
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
		//String srcFile="/src/myproject.zip";
		String desFile="/home/dell/workspace/copy_java/src/des.mp3";
		String srcFile="http://localhost/src.mp3";
		//File in=new File(srcFile);
		//BufferedInputStream in = null ;
		URL url=new URL(srcFile);
		HttpURLConnection httpurl=(HttpURLConnection)url.openConnection();
		rfwrite=new RandomAccessFile(desFile,"rw");
		//input= new FileInputStream(in);
		input = new BufferedInputStream(httpurl.getInputStream());
		}catch(IOException e){
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		try {
			currentPos=beginPos;
			out.println("�߳�"+name+"��ʼ�ֽ���"+beginPos);
			out.println("�߳�"+name+"���۽����ֽ���"+endPos);
			long skip_bytes=input.skip(beginPos);
			rfwrite.seek(skip_bytes);
			out.println();
			while((c=input.read(b))!=-1){
				currentPos+=c;
				if(currentPos>=endPos){//�ÿ��Ѵ������
					rfwrite.write(b, 0, (c-(int)((currentPos-endPos))));
					break;
				}else{
					rfwrite.write(b);
				}
			}
			out.println("�߳�"+name+"�����ֽ���"+currentPos);
			input.close();
			rfwrite.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
public class cp {
	private long fileLength=0;
	int threads=5;
	long blocks=0;
	private long beginPos=0,endPos=0;
	private httpDownload httpdown=new httpDownload("http://localhost/src.mp3");
	protected void cp_file() throws IOException{
		/*System.out.println(System.getProperty("user.dir"));
		String srcFile="/src/myproject.zip";
		File input=new File(srcFile);
		fileLength=input.length();*/
		fileLength=new Long(httpdown.getContentLength());
		// TODO Auto-generated catch block					
		ThreadGroup tg=new ThreadGroup("download");
		out.println(fileLength);
		long starttime=System.currentTimeMillis();//�����
		blocks=fileLength/threads;
		if(fileLength<=0){//�޷��ӷ�������ȡ�ļ����ȣ����õ��߳�����
			setThreads(1);
		}
		for(int i=0;i<threads;i++){
			if(i!=threads-1)
				endPos=beginPos+blocks;
			else{
				endPos=fileLength;
				out.println(i+"��"+endPos);
			}
			save_thread download=new save_thread(tg,Integer.toString(i),beginPos,endPos);
			download.start();
			beginPos=endPos;
		}
		while (tg.activeCount() > 0) {  
			out.println("��߳���"+tg.activeCount());
            try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
        }  
		in.close();
		long endtime=System.currentTimeMillis();
		long usetime=(endtime-starttime)/1000;
		out.println("������ʱ"+usetime);
		out.println("end");
	}
	public int getThreads() {
		return threads;
	}
	public void setThreads(int threads) {
		this.threads = threads;
	}
	public static void main(String args[]) throws IOException{
		cp mycp=new cp();
		mycp.cp_file();
		out.println("ok");
	}
}
