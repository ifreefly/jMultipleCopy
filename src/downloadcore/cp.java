/*
 * author:idevcod@gmail.com
 * date:2013/10/25
 * description:多线程复制
 * version:0.2
 * nextVersionDescription:实现多线程下载
 * */
package downloadcore;

import java.io.File;
import java.io.IOException;
import static java.lang.System.*;
import java.text.DecimalFormat;//设置浮点运算格式而导入的包

//import time.timetask;

public class cp {
	private String srcFile,desPath,desFile;
	private long fileLength=0;
	int threads=1;//默认线程是5
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
		//if(srcFile.length()==0){//注：需要增加链接匹配功能
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
		//out.println("文件长度为"+fileLength);
		long starttime=System.currentTimeMillis();//毫秒记
		blocks=fileLength/threads;
		if(fileLength<=0){//无法从服务器获取文件长度，采用单线程下载
			//out.println("单线程下载");
			setThreads(1);
		}
		if(fileLength>0&&fileLength>spaceDetect.getFreeSpace()){
			out.println("磁盘空间不足！");
		}
		for(int i=0;i<threads;i++){
			if(i!=threads-1)
				endPos=beginPos+blocks;
			else{
				endPos=fileLength;
				//out.println(i+"个"+endPos);
			}
			save_thread download=new save_thread(tg,Integer.toString(i),srcFile,desFile,beginPos,endPos);
			download.start();
			beginPos=endPos;
		}
		/*while (tg.activeCount() > 0) {//监测任务是否完成同时计算文件大小并报告进度  
			//out.println("活动线程有"+tg.activeCount());
			out.println("下载完成字节数"+progressReport.length());
			out.println("下载已完成"+(decimalFormate.format(progressReport.length()*100/(float)fileLength))+"%");
            try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
        }*/  
		long endtime=System.currentTimeMillis();
		long usetime=(endtime-starttime)/1000;
		out.println("复制用时"+usetime);
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
