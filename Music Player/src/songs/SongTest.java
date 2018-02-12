package songs;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

//test the Song class
public class SongTest {

	private Song testSong;
	
	@Before
	public void setUp() throws Exception {
		testSong = new Song("source/TestSong.txt");
	}

	@Test
	public void testSong() throws NumberFormatException, IOException {
		Song newSong = new Song("source/TestSong.txt");
		assertArrayEquals(newSong.songNotes, testSong.songNotes);
		assertEquals(17, newSong.noteNum);
		assertEquals(true, newSong.songNotes[0].isRepeat());
		//fail("Not yet implemented");
	}

	@Test
	public void testGetTitle() {
		assertEquals("Game", testSong.getTitle());
		//fail("Not yet implemented");
	}

	@Test
	public void testGetArtist() {
		assertEquals("Ramin", testSong.getArtist());
		//fail("Not yet implemented");
	}

	@Test
	public void testGetTotalDuration() {
		assertEquals(10,testSong.getTotalDuration(), 0.0001);
		//fail("Not yet implemented");
	}

	@Test
	public void testPlay() {
		testSong.play();
		//fail("Not yet implemented");
	}

	@Test
	public void testOctaveDown() {
		testSong.octaveDown();
		assertEquals(5,testSong.songNotes[7].getOctave());
		assertEquals(4,testSong.songNotes[8].getOctave());
		//testSong.play();
		//fail("Not yet implemented");
	}

	@Test
	public void testOctaveUp() {
		testSong.octaveUp();
		assertEquals(7,testSong.songNotes[7].getOctave());
		assertEquals(6,testSong.songNotes[8].getOctave());
		testSong.octaveUp();
		//testSong.play();
		//fail("Not yet implemented");
	}

	@Test
	public void testChangeTempo() {
		testSong.changeTempo(0.25);
		assertEquals(2.5,testSong.getTotalDuration(), 0.0001);
		//testSong.play();
		//fail("Not yet implemented");
	}

	@Test
	public void testReverse() {
		testSong.reverse();
		assertEquals("F",testSong.songNotes[1].getPitch().toString());
		assertEquals("E",testSong.songNotes[2].getPitch().toString());
		//testSong.play();
		//fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		assertEquals(testSong.toString(),"Song Title: Game\nSong Artist: Ramin\nSong Duration: 10.00\nSong Note Number: 17\n"
				+ "Song notes: [0.4 G 6 NATURAL true, 0.4 C 6 NATURAL false, 0.2 E 6 FLAT false, 0.2 F 6 NATURAL true, 0.4 G 6 NATURAL true, "
				+ "0.4 C 6 NATURAL false, 0.2 E 6 NATURAL false, 0.2 F 6 NATURAL true, 1.2 G 5 NATURAL false, 1.2 C 5 NATURAL false, "
				+ "0.2 E 5 FLAT false, 0.2 F 5 NATURAL false, 0.8 G 5 NATURAL false, 0.8 C 5 NATURAL false, 0.2 E 5 FLAT false, "
				+ "0.2 F 5 NATURAL false, 0.4 D 5 NATURAL false]");
		//fail("Not yet implemented");
	}

}
