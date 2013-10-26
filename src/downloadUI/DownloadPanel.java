/*
 * author:idevcod@gmail.com
 * date:2013/10/26
 * description:��������������������ļ������ļ���Ϣ��
 * version:0.2
 * 
 * */

package downloadUI;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JLabel;

public class DownloadPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private JProgressBar downloadProgress;
	private JLabel fileTypeIconLabel,fileNameLabel,downloadInfoLabel,freeLabel;
	protected void initDownloadPanel(){
		downloadProgress=new JProgressBar(0,100);
		fileTypeIconLabel=new JLabel("test");
		fileNameLabel=new JLabel("fileNameLabel");
		downloadInfoLabel=new JLabel("downloadInfoLabel");
		freeLabel=new JLabel("freeLabel");
		System.out.println("��ʼ�����");
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
	}
	public DownloadPanel(){
		System.out.println("ok");
		setLayout(null);
		setBounds(0,0,350,82);
		initDownloadPanel();
		add(downloadProgress);
		add(fileTypeIconLabel);
		add(fileNameLabel);
		add(downloadInfoLabel);
		add(freeLabel);	
	}
}
