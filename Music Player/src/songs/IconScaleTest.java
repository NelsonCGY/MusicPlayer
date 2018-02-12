package songs;

import static org.junit.Assert.*;


import javax.swing.ImageIcon;

import org.junit.Before;
import org.junit.Test;

// test the IconScale class
public class IconScaleTest {

	private IconScale bgp;
	
	@Before
	public void setUp() throws Exception {
		bgp = new IconScale(new ImageIcon("source/Background.jpg"));
	}

	@Test
	public void testIconScale() {
		// constructor no need to test
		//fail("Not yet implemented");
	}

	@Test
	public void testGetIconHeight() {
		assertEquals(1074,bgp.getIconHeight());
		//fail("Not yet implemented");
	}

	@Test
	public void testGetIconWidth() {
		assertEquals(1788,bgp.getIconWidth());
		//fail("Not yet implemented");
	}

	@Test
	public void testPaintIcon() {
		// tested by debug
		//fail("Not yet implemented");
	}

}
