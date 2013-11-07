/*
 * author:idevcod@gmail.com
 * date:2013/10/26
 * description:绘制下载任务进度栏，文件名，文件信息等
 * version:0.2
 * 
 * */

package downloadUI;

import downloadcore.cp;
import actionListener.PanelMouseListener;

import java.awt.Color;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JLabel;

public class DownloadPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private String filedownloadHead=new String("文件已下载:");
	private JProgressBar downloadProgress;
	private cp newcp;
	private MonitorProgress monitorProgress;
	private JLabel fileTypeIconLabel,fileNameLabel,downloadInfoLabel,freeLabel;
	private boolean isDownloaded=false;
	private PanelMouseListener panelMouseListener;
	protected void initDownloadPanel(){
		panelMouseListener=new PanelMouseListener();
		downloadProgress=new JProgressBar(0,100);
		fileTypeIconLabel=new JLabel("test");
		fileNameLabel=new JLabel("fileNameLabel");
		downloadInfoLabel=new JLabel("downloadInfoLabel");
		freeLabel=new JLabel("freeLabel");
		downloadProgress.setBounds(80, 44, 400, 18);
		downloadProgress.setStringPainted(true);
		//downloadProgress.setString("88%");
		fileTypeIconLabel.setBounds(0, 0, 80, 80);
		fileTypeIconLabel.setBackground(Color.cyan);//测试用，检测图标位置，可删除
		fileTypeIconLabel.setOpaque(true);//测试用，检测位置，可删除
		fileNameLabel.setBounds(80,0,400,22);
		fileNameLabel.setBackground(Color.black);//测试用，检测位置，可删除
		fileNameLabel.setOpaque(true);//测试用，检测位置，可删除
		downloadInfoLabel.setBounds(80, 22, 400, 22);
		downloadInfoLabel.setBackground(Color.red);//测试用，检测位置，可删除
		downloadInfoLabel.setOpaque(true);//测试用，检测位置，可删除
		//downloadProgress.setValue(50);
		freeLabel.setBounds(80, 62, 400, 18);
		freeLabel.setBackground(Color.lightGray);//测试用，检测位置，可删除
		freeLabel.setOpaque(true);//测试用，检测位置，可删除
		addMouseListener(panelMouseListener);
		add(downloadProgress);
		add(fileTypeIconLabel);
		add(fileNameLabel);
		add(downloadInfoLabel);
		add(freeLabel);	
	}
	
	public void setInitInfo(String fileName,long downloaded,long fileLength,String progressString,int progressValue){//当从配置文件读取数据文件时或者新建一个下载时设置文件信息
		fileNameLabel.setText(fileName);
		downloadInfoLabel.setText((filedownloadHead+downloaded+"/"+fileLength));
		downloadProgress.setValue(progressValue);
		downloadProgress.setString(progressString+"%");
	}
	
	public DownloadPanel(String url) throws IOException, InterruptedException{
		setLayout(null);
		//setBounds(0,0,350,82);
		initDownloadPanel();
		monitorProgress=new MonitorProgress();
		newcp=new cp(url);
		setInitInfo(newcp.getHttpdown().getFileName(), 0, newcp.getFileLength(), "0.00", 0);
	}
	
	public DownloadPanel() throws IOException{
		System.out.println("ok");
		setLayout(null);
		//setBounds(0,0,350,82);
		initDownloadPanel();
	}
	
	public MonitorProgress getMonitorProgress() {
		return monitorProgress;
	}

	public JProgressBar getDownloadProgress() {
		return downloadProgress;
	}

	public boolean isDownloaded() {
		return isDownloaded;
	}

	public void setDownloaded(boolean isDownloaded) {
		this.isDownloaded = isDownloaded;
	}

	public class MonitorProgress extends Thread{
		public void run(){
			while(!isDownloaded){
				while(newcp.getTg().activeCount()>0||newcp.getProgressReport().length()<newcp.getFileLength()){
					newcp.monitorDownload();
					setInitInfo(newcp.getHttpdown().getFileName(), newcp.getProgressReport().length(), newcp.getFileLength(), newcp.getProgressString(), newcp.getProgressValue());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				setDownloaded(true);
			}
			newcp.monitorDownload();
			setInitInfo(newcp.getHttpdown().getFileName(), newcp.getProgressReport().length(), newcp.getFileLength(), newcp.getProgressString(), newcp.getProgressValue());
		}
	}
}
