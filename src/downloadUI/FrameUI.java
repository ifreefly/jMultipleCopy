/* author:idevcod@gmail.com
 * date:2013/10/25
 * description:多线程复制
 * version:0.2
 * nextVersionDescription:实现多线程下载
 */

package downloadUI;

import downloadcore.cp;

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
		JMenuBar menuBar=new JMenuBar();
		JMenuItem newMenu=new JMenuItem("New");
		JMenuItem helpMenu=new JMenuItem("Help");
		//JMenuItem testMenuItem=new JMenuItem("test");
		setJMenuBar(menuBar);
		menuBar.add(newMenu);
		menuBar.add(helpMenu);
		menuListener menulistener=new menuListener();
		newMenu.addActionListener(menulistener);
		helpMenu.addActionListener(menulistener);
		setSize(400,200);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	class menuListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stubv
			String cmd=e.getActionCommand();
			//System.out.println("hello world");
			if(cmd.equals("New")){
				url=JOptionPane.showInputDialog(null,"请输入一个链接");
				try {
					cp newcp=new cp(url);
				} catch (IOException e1) {
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
