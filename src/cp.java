/*
 * author:idevcod@gmail.com
 * date:2013/9/16
 * description:���̸߳���
 * version:0.1
 * nextVersionDescription:ʵ�ֶ��߳�����
 * */
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.ThreadGroup;
import java.net.HttpURLConnection;
import java.net.URL;
import static java.lang.System.*;

class save_thread extends Thread {
	private String name;
	private String contentRange;
	InputStream input=null; 
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
		currentPos=beginPos;
	}

	@Override
	public void run() {
		try{
		String desFile="/home/dell/workspace/copy_java/src/des.mp3";
		String srcFile="http://mylzu.net/src12.mp3";
		URL url=new URL(srcFile);
		HttpURLConnection httpurl=(HttpURLConnection)url.openConnection();
		rfwrite=new RandomAccessFile(desFile,"rw");
		httpurl.setRequestProperty("User-Agent", "jmultidownload");//����user-agent
		contentRange="bytes="+beginPos+"-";//�����ļ�����ʼλ��
		httpurl.setRequestProperty("RANGE", contentRange);//�����ļ�����ʼλ��
		out.println("�߳�"+name+"ContentRange="+httpurl.getHeaderField("Content-Range"));
		input = httpurl.getInputStream();
		rfwrite.seek(beginPos);
		out.println();
		out.println("�߳�"+name+"��ʼ�ֽ���"+currentPos);
		out.println("�߳�"+name+"���۽����ֽ���"+endPos);
		out.println();
		}catch(IOException e){
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		try {
			while((c=input.read(b,0,1024))>0){
				currentPos+=c;
				if(currentPos>=endPos){//�ÿ��Ѵ������
					rfwrite.write(b, 0, (c-(int)((currentPos-endPos))));
					//currentPos+=(c-(int)((currentPos-endPos)));
					break;
				}else{
					rfwrite.write(b,0,c);
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
	private httpDownload httpdown=new httpDownload("http://mylzu.net/src12.mp3");
	protected void cp_file() throws IOException{
		fileLength=new Long(httpdown.getContentLength());
		// TODO Auto-generated catch block					
		ThreadGroup tg=new ThreadGroup("download");
		out.println("�ļ�����Ϊ"+fileLength);
		long starttime=System.currentTimeMillis();//�����
		blocks=fileLength/threads;
		if(fileLength<=0){//�޷��ӷ�������ȡ�ļ����ȣ����õ��߳�����
			out.println("���߳�����");
			setThreads(1);
		}
		//long starttime=System.currentTimeMillis();
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
			//out.println("��߳���"+tg.activeCount());
            try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
        }  
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
