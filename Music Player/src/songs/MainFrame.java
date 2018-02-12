package songs;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * @author CNelson, Colin Wu
 * A class that deals with the creation and drag for undecorated frame.
 * Reference from "http://blog.csdn.net/qiantujava/article/details/10060847"
 * Tested by debug no unit tests.
 *
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private int preX, preY, endX, endY;
	
	/**
	 * constructor
	 * @throws HeadlessException
	 */
	public MainFrame() throws HeadlessException {
		// TODO Auto-generated constructor stub
		IconScale bgp = new IconScale(new ImageIcon("source/Background.jpg")); // create a scaled background, but this will not allow adding more panels so the choice depends on requirement
    	JLabel bgpL = new JLabel(bgp);
    	this.setTitle("Kaori Player");
    	this.setIconImage(Toolkit.getDefaultToolkit().getImage("source/icon.png"));
    	this.setSize(1000, 600);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.getContentPane().add(bgpL, BorderLayout.CENTER); // add the background to the content pane but adding future panels will cover the background
		this.getLayeredPane().add(bgpL, new Integer(Integer.MIN_VALUE)); // add the background directly to the layered pane so that the content pane can add more panels but the size can't change
		bgpL.setBounds(0, 0, this.getWidth(), this.getHeight()); 
		this.getContentPane().setLayout(null);
		this.setDragable(true);
	}
	
	/**
	 * get the frame moved by listening to the mouse
	 * @param set
	 */
	public void setDragable(boolean set){
		this.setUndecorated(true);
		if(set){
			this.addMouseListener(new java.awt.event.MouseAdapter(){
				@Override
				public void mousePressed(MouseEvent e){
					preX = e.getX(); preY = e.getY(); // get original point
				}
			});
			this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter(){
				@Override
				public void mouseDragged(MouseEvent e){
					endX = e.getXOnScreen(); endY = e.getYOnScreen(); // get current point
					int moveX = endX - preX;
					int moveY = endY - preY;
					MainFrame.this.setLocation(moveX, moveY); // move with the mouse
				}
			});
		}
	}
}
