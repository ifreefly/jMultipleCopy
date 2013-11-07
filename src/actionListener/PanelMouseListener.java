package actionListener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PanelMouseListener implements MouseListener{
	private boolean isPause=false;
	public boolean getIsPause() {
		return isPause;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(2==e.getClickCount()){
			//System.out.println("hello world");
			if(false==isPause)
				isPause=true;
			else
				isPause=false;
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
