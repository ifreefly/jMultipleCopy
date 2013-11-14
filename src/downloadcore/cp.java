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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Iterator;

public class cp implements Runnable{
	private String srcFile,desPath,desFile,lckFile;
	private long fileLength=0;
	int threads=1;//默认线程是5
	long blocks=0;
	private long beginPos=0,endPos=0;
	private httpDownload httpdown;
	private File spaceDetect,progressReport,detectLck;
	private DecimalFormat decimalFormate=new DecimalFormat("0.00");
	private int progressValue;
	private String progressString;
	private ThreadGroup tg;
	
	private FileInputStream finLck;
	private FileOutputStream fouLck;
	private Map<String,String> posInfoMap;
	private Set<?> keys;
	private Properties pro;
	private String posInfo;
	private save_thread[] download;
	private boolean isPause=false;
	
	public cp(String srcFile,String desPath) throws IOException{
		this.srcFile=srcFile;
		this.desPath=desPath;
		//posInfoMap=new HashMap<String,String>();
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
			desFile=desPath+httpdown.getFileName();
			httpdown=new httpDownload(srcFile);
			spaceDetect=new File(desPath);
			progressReport=new File(desFile);
			cp_file();
		//}
	}
	
	public void monitorDownload(){
		setProgressValue();
		setProgressString();
	}
	
	protected void cp_file() throws IOException{
		fileLength=new Long(httpdown.getContentLength());
		lckFile=desPath+httpdown.getFileName()+".lck";
		detectLck=new File(lckFile);
		tg=new ThreadGroup("download");
		download=new save_thread[threads];
		//out.println("文件长度为"+fileLength);
		long starttime=System.currentTimeMillis();//毫秒记
		blocks=fileLength/threads;
		if(fileLength<=0){//无法从服务器获取文件长度，采用单线程下载
			//out.println("单线程下载");
			setThreads(1);
		}
		if(fileLength>0&&fileLength>spaceDetect.getFreeSpace()){
			out.println("磁盘空间不足！");
			return ;
		}
		if(detectLck.exists()){
			int i=0;
			String key;
			String posInfo;
			String[] posInfoArray;
			posInfoMap=new HashMap<String,String>();
			pro=new Properties();
			finLck=new FileInputStream(detectLck);
			posInfoMap=new HashMap<String,String>();
			pro.load(finLck);
			finLck.close();
			fouLck=new FileOutputStream(detectLck);
			out.println("0pos是"+pro.get("0pos"));
			keys=pro.keySet();
			out.println(keys);
			for(Iterator<?> itr=keys.iterator();itr.hasNext();i++){
				key=(String)itr.next();
				posInfo=(String)pro.get(key);
				posInfoArray=posInfo.split(" ");
				if(posInfoArray[2].equals("2")){//该数据库暂时没有线程接管
                    beginPos=Long.valueOf(posInfoArray[0]);
                    endPos=Long.valueOf(posInfoArray[1]);
                    out.println("srcFile:"+srcFile+" desFile:"+desFile+" beginPos:"+beginPos+" endPos:"+endPos);
                    download[i]=new save_thread(tg,Integer.toString(i),srcFile,desFile,beginPos,endPos);
                    download[i].start();
                    posInfoArray[2]="2";
                    posInfo=posInfoArray[0]+" "+posInfoArray[1]+" "+posInfoArray[2];
                    posInfoMap.put(key, posInfo);
				}else{
					posInfoMap.put(key, posInfo);
				}
				out.println("没执行?");
			}
			pro.putAll(posInfoMap);
			pro.store(fouLck, "位置信息");
			fouLck.close();
			out.println("test");
		}else{
			pro=new Properties();
			fouLck=new FileOutputStream(detectLck);
			posInfoMap=new HashMap<String,String>();
			for(int i=0;i<threads;i++){
				if(i!=threads-1){
					endPos=beginPos+blocks;
				}
				else{
					endPos=fileLength;
					//out.println(i+"个"+endPos);
				}
				out.println("i="+i+" srcFile:"+srcFile+" desFile:"+desFile+" beginPos:"+beginPos+" endPos:"+endPos);
				System.out.println();
				download[i]=new save_thread(tg,Integer.toString(i),srcFile,desFile,beginPos,endPos);
				download[i].start();
				posInfo=Long.toString(beginPos)+" "+Long.toString(endPos)+" "+2;
				beginPos=endPos;
				posInfoMap.put(i+"pos", posInfo);
			}
			pro.putAll(posInfoMap);
			pro.store(fouLck, "fileInfo");
			fouLck.close();
		}
		long endtime=System.currentTimeMillis();
		long usetime=(endtime-starttime)/1000;
		out.println("复制用时"+usetime);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!isPause&&tg.activeCount()>0){
			try {
				writePosInfo();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected void writePosInfo() throws IOException{
		pro=new Properties();
		posInfoMap=new HashMap<String,String>();
		fouLck=new FileOutputStream(detectLck);
		out.println(download.length);
		for(int i=0;i<download.length;i++){
			out.println("download["+i+"].getCurrentPos()="+download[i].getCurrentPos()+" endPos="+download[i].getEndPos());
			posInfo=Long.toString(download[i].getCurrentPos())+" "+Long.toString(download[i].getEndPos())+" "+2;
			posInfoMap.put(i+"pos", posInfo);
		}
		pro.putAll(posInfoMap);
		pro.store(fouLck, "fileInfo");
	}
	/*public boolean monitorCp(){
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
		/*if(tg.activeCount()>0)//判别条件有待改进
			return false;//还没下载完
		else
			return true;
	}*/
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
		//System.out.println(progressValue);
	}
	public String getProgressString() {
		return progressString;
	}
	public void setProgressString() {
		this.progressString = decimalFormate.format(progressReport.length()*100/(float)fileLength);
		//System.out.println(progressString+"%");
	}
	public httpDownload getHttpdown() {
		return httpdown;
	}
	public long getFileLength() {
		return fileLength;
	}
	public File getProgressReport() {
		return progressReport;
	}
	public ThreadGroup getTg() {
		return tg;
	}

	public boolean isPause() {
		return isPause;
	}

	public void setPause(boolean isPause) {
		this.isPause = isPause;
		for(int i=0;i<download.length;i++){
			download[i].setPause(isPause);
		}
	}

}
