/*
 * author:idevcod@gmail.com
 * date:2013/10/25
 * description:���̸߳���
 * version:0.2
 * nextVersionDescription:ʵ�ֶ��߳�����
 * */
package downloadcore;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.ThreadGroup;
import java.net.HttpURLConnection;
import java.net.URL;
import static java.lang.System.*;


public class cp {
	private String srcFile,desPath,desFile;
	private long fileLength=0;
	int threads=5;
	long blocks=0;
	private long beginPos=0,endPos=0;
	private httpDownload httpdown;
	private File spaceDetect,progressReport;
	public cp(String srcFile,String desPath) throws IOException{
		this.srcFile=srcFile;
		this.desPath=desPath;
		cp_init();
	}
	
	public cp(String srcFile) throws IOException{
		this.srcFile=srcFile;
		desPath="/home/dell/workspace/copy_java/src/";
		cp_init();
	}
	
	protected void cp_init() throws IOException{
		//System.out.println(desPath);
		//System.out.println(httpdown.getFileName());
		httpdown=new httpDownload(srcFile);
		desFile=desPath+"/"+httpdown.getFileName();
		httpdown=new httpDownload(srcFile);
		spaceDetect=new File(desPath);
		progressReport=new File(desFile);
		cp_file();
	}
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
		if(fileLength>0&&fileLength>spaceDetect.getFreeSpace()){
			out.println("���̿ռ䲻�㣡");
		}
		for(int i=0;i<threads;i++){
			if(i!=threads-1)
				endPos=beginPos+blocks;
			else{
				endPos=fileLength;
				out.println(i+"��"+endPos);
			}
			save_thread download=new save_thread(tg,Integer.toString(i),srcFile,desFile,beginPos,endPos);
			download.start();
			beginPos=endPos;
		}
		while (tg.activeCount() > 0) {//��������Ƿ����ͬʱ�����ļ���С���������  
			//out.println("��߳���"+tg.activeCount());
			out.println("���������"+progressReport.length()/fileLength+"%");
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
	}
	public int getThreads() {
		return threads;
	}
	public void setThreads(int threads) {
		this.threads = threads;
	}
	/*public static void main(String args[]) throws IOException{
		cp mycp=new cp("http://mylzu.net/src12.mp3","/home/dell/workspace/copy_java/src/des.mp3");
		
		out.println("ok");
	}*/
}
