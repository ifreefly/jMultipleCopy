/* author:idevcod@gmail.com
 * date:2013/10/25
 * description:多线程复制
 * version:0.2
 * nextVersionDescription:实现多线程下载
 */

package downloadUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class FrameUI extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String url=null;
	public String getUrl() {
		return url;
	}

	public FrameUI(String name){
		super(name);
		setLayout(null);
		JMenuBar menuBar=new JMenuBar();
		JMenuItem newMenu=new JMenuItem("New");
		JMenuItem helpMenu=new JMenuItem("Help");
		setJMenuBar(menuBar);
		menuBar.add(newMenu);
		menuBar.add(helpMenu);
		menuListener menulistener=new menuListener();
		newMenu.addActionListener(menulistener);
		helpMenu.addActionListener(menulistener);
		//addNewDownloadPanel();
		setSize(500,400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void addNewDownloadPanel(DownloadPanel newDownload,int i){
		newDownload.setBounds(0,82*i,480,82);
		add(newDownload);
	}
	
	class menuListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stubv
			String cmd=e.getActionCommand();
			//System.out.println("hello world");
			if(cmd.equals("New")){
				url=JOptionPane.showInputDialog(null,"请输入一个链接");
				//System.out.println(url);
				try {
					/*DownloadPanel newDownloadtest=null;//=new DownloadPanel();
					for(int i=0;i<2;i++){
						newDownloadtest=new DownloadPanel();
						addNewDownloadPanel(newDownloadtest,i,30);
						repaint();
					}
					remove(newDownloadtest);
					newDownloadtest.getDownloadProgress().setString("hello world");*/
					if(url!=null){
						DownloadPanel newDownload=new DownloadPanel(url);
						addNewDownloadPanel(newDownload,0);
						repaint();
						newDownload.getMonitorProgress().start();
						System.out.println("奇怪的线程");
						Thread threadCp=new Thread(newDownload.getNewcp());
						System.out.println("write!");
						threadCp.start();
					}
				} catch (IOException | InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else if(cmd.equals("Help")){
				JOptionPane.showMessageDialog(FrameUI.this, "test");
			}
		}	
	}
	
	
}
