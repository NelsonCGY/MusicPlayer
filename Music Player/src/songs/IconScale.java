package songs;

import java.awt.Component;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;

/**
 * @author CNelson, Colin Wu
 * A class used to scale the pictures for icons.
 * Reference from "http://blog.csdn.net/zhongweijian/article/details/7668926"
 *
 */
public class IconScale implements Icon {
	
	private Icon icon;

	/**
	 * constructor
	 */
	public IconScale(Icon icon) {
		// TODO Auto-generated constructor stub
		this.icon = icon;
	}

	/* (non-Javadoc)
	 * @see javax.swing.Icon#getIconHeight()
	 * get icon height
	 */
	@Override
	public int getIconHeight() {
		// TODO Auto-generated method stub
		return icon.getIconHeight();
	}

	/* (non-Javadoc)
	 * @see javax.swing.Icon#getIconWidth()
	 * get icon width
	 */
	@Override
	public int getIconWidth() {
		// TODO Auto-generated method stub
		return icon.getIconWidth();
	}

	/* (non-Javadoc)
	 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
	 * paint scaled icon
	 */
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		// TODO Auto-generated method stub
		double comW = c.getWidth(), comH = c.getHeight(), iconW = this.icon.getIconWidth(), iconH = this.icon.getIconHeight();
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR); // reshape the picture in case of not fit pixels
		g2d.scale(comW / iconW, comH / iconH); // redraw the picture with new ratio
		icon.paintIcon(c, g2d, 0, 0); // use recursion to scale
	}

}




