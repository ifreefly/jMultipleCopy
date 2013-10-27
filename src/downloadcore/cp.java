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
import static java.lang.System.*;
import java.text.DecimalFormat;//���ø��������ʽ������İ�

//import time.timetask;

public class cp {
	private String srcFile,desPath,desFile;
	private long fileLength=0;
	int threads=1;//Ĭ���߳���5
	long blocks=0;
	private long beginPos=0,endPos=0;
	private httpDownload httpdown;
	private File spaceDetect,progressReport;
	private DecimalFormat decimalFormate=new DecimalFormat("0.00");
	private int progressValue;
	private String progressString;
	

	ThreadGroup tg;
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
		//if(srcFile.length()==0){//ע����Ҫ��������ƥ�书��
			httpdown=new httpDownload(srcFile);
			desFile=desPath+"/"+httpdown.getFileName();
			httpdown=new httpDownload(srcFile);
			spaceDetect=new File(desPath);
			progressReport=new File(desFile);
			cp_file();
		//}
	}
	
	protected void monitorDownload(){
		setProgressValue();
		setProgressString();
	}
	
	protected void cp_file() throws IOException{
		fileLength=new Long(httpdown.getContentLength());
		// TODO Auto-generated catch block					
		tg=new ThreadGroup("download");
		//out.println("�ļ�����Ϊ"+fileLength);
		long starttime=System.currentTimeMillis();//�����
		blocks=fileLength/threads;
		if(fileLength<=0){//�޷��ӷ�������ȡ�ļ����ȣ����õ��߳�����
			//out.println("���߳�����");
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
				//out.println(i+"��"+endPos);
			}
			save_thread download=new save_thread(tg,Integer.toString(i),srcFile,desFile,beginPos,endPos);
			download.start();
			beginPos=endPos;
		}
		/*while (tg.activeCount() > 0) {//��������Ƿ����ͬʱ�����ļ���С���������  
			//out.println("��߳���"+tg.activeCount());
			out.println("��������ֽ���"+progressReport.length());
			out.println("���������"+(decimalFormate.format(progressReport.length()*100/(float)fileLength))+"%");
            try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
        }*/  
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

	public int getProgressValue() {
		return progressValue;
	}

	public void setProgressValue() {
		this.progressValue = (int) (progressReport.length()*100/fileLength);
	}
	public String getProgressString() {
		return progressString;
	}
	public void setProgressString() {
		this.progressString = decimalFormate.format(progressReport.length()*100/(float)fileLength);
	}
	public httpDownload getHttpdown() {
		return httpdown;
	}
	public long getFileLength() {
		return fileLength;
	}
	
}
