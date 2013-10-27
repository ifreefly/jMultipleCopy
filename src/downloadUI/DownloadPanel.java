/*
 * author:idevcod@gmail.com
 * date:2013/10/26
 * description:��������������������ļ������ļ���Ϣ��
 * version:0.2
 * 
 * */

package downloadUI;

import downloadcore.cp;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JLabel;

public class DownloadPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private String filedownloadHead=new String("�ļ�������:");
	private JProgressBar downloadProgress;
	private cp newcp;
	public JProgressBar getDownloadProgress() {
		return downloadProgress;
	}



	private JLabel fileTypeIconLabel,fileNameLabel,downloadInfoLabel,freeLabel;
	protected void initDownloadPanel(){
		downloadProgress=new JProgressBar(0,100);
		fileTypeIconLabel=new JLabel("test");
		fileNameLabel=new JLabel("fileNameLabel");
		downloadInfoLabel=new JLabel("downloadInfoLabel");
		freeLabel=new JLabel("freeLabel");
		downloadProgress.setBounds(80, 44, 400, 18);
		downloadProgress.setStringPainted(true);
		downloadProgress.setString("88%");
		fileTypeIconLabel.setBounds(0, 0, 80, 80);
		fileTypeIconLabel.setBackground(Color.cyan);//�����ã����ͼ��λ�ã���ɾ��
		fileTypeIconLabel.setOpaque(true);//�����ã����λ�ã���ɾ��
		fileNameLabel.setBounds(80,0,400,22);
		fileNameLabel.setBackground(Color.black);//�����ã����λ�ã���ɾ��
		fileNameLabel.setOpaque(true);//�����ã����λ�ã���ɾ��
		downloadInfoLabel.setBounds(80, 22, 400, 22);
		downloadInfoLabel.setBackground(Color.red);//�����ã����λ�ã���ɾ��
		downloadInfoLabel.setOpaque(true);//�����ã����λ�ã���ɾ��
		downloadProgress.setValue(50);
		freeLabel.setBounds(80, 62, 400, 18);
		freeLabel.setBackground(Color.lightGray);//�����ã����λ�ã���ɾ��
		freeLabel.setOpaque(true);//�����ã����λ�ã���ɾ��
		//containProgress.set
		add(downloadProgress);
		add(fileTypeIconLabel);
		add(fileNameLabel);
		add(downloadInfoLabel);
		add(freeLabel);	
	}
	
	public void setInitInfo(String fileName,long downloaded,long fileLength,String progressString,int progressValue){//���������ļ���ȡ�����ļ�ʱ�����½�һ������ʱ�����ļ���Ϣ
		fileNameLabel.setText(fileName);
		downloadInfoLabel.setText((filedownloadHead+downloaded+"/"+fileLength));
		downloadProgress.setValue(progressValue);
		downloadProgress.setString(progressString);
	}
	
	public DownloadPanel(String url) throws IOException{
		setLayout(null);
		//setBounds(0,0,350,82);
		initDownloadPanel();
		newcp=new cp(url);
		setInitInfo(newcp.getHttpdown().getFileName(), 0, newcp.getFileLength(), "0.00", 0);
	}
	
	public DownloadPanel() throws IOException{
		System.out.println("ok");
		setLayout(null);
		//setBounds(0,0,350,82);
		initDownloadPanel();
	}
}
