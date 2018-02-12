package songs;

//TODO: write this class

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * @author CNelson£¬ Colin Wu
 * A class that deals with a collection of notes.
 * A collection of notes are implemented using an array.
 *
 */
public class Song {
	
	protected Note[] songNotes;
	private String songName;
	private String Author;
	protected int noteNum;
	private double duration;
	private DecimalFormat df = new DecimalFormat("#0.00");
	public String playNote;
	
	/**
	 * constructor, read from a file
	 * @param filename
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public Song(String filename) throws NumberFormatException, IOException{
		BufferedReader file = new BufferedReader(new FileReader(filename));
		this.songName = file.readLine();
		this.Author = file.readLine();
		this.noteNum = Integer.parseInt(file.readLine());
		this.songNotes = new Note[this.noteNum];
		String piece;
		int pieceNum = 0;
		while((piece = file.readLine()) != null){
			String[] oneNote = piece.split(" ");
			if(oneNote.length == 5){
				if(oneNote[4].equals("true")){
					this.songNotes[pieceNum] = new Note(Double.parseDouble(oneNote[0]), Pitch.getValueOf(oneNote[1].toUpperCase()),
					Integer.parseInt(oneNote[2]), Accidental.getValueOf(oneNote[3].toUpperCase()), true);
				}
				else{
					this.songNotes[pieceNum] = new Note(Double.parseDouble(oneNote[0]), Pitch.getValueOf(oneNote[1].toUpperCase()), 
					Integer.parseInt(oneNote[2]), Accidental.getValueOf(oneNote[3].toUpperCase()), false);
				}
			}
			else{
				if(oneNote[2].equals("true")){
					this.songNotes[pieceNum] = new Note(Double.parseDouble(oneNote[0]), true);
				}
				else{
					this.songNotes[pieceNum] = new Note(Double.parseDouble(oneNote[0]), false);
				}
			}
			pieceNum++;
		}
		file.close();
	}
	
	/**
	 * @return the title of the song
	 */
	public String getTitle(){
		return this.songName;
	}
	
	/**
	 * @return the artist of the song
	 */
	public String getArtist(){
		return this.Author;
	}
	
	/**
	 * @return the total length of the song
	 */
	public double getTotalDuration(){
		int re = 0;
		double total = 0;
		for(int i = 0; i < this.noteNum; i++){
			if(this.songNotes[i].isRepeat() && re == 0){
				total = total + 2.0 * this.songNotes[i].getDuration();
				re = 1;
			}
			else if(this.songNotes[i].isRepeat() && re == 1){
				total = total + 2.0 * this.songNotes[i].getDuration();
				re = 0;
			}
			else if(re == 1){
				total = total + 2.0 * this.songNotes[i].getDuration();
			}
			else{
				total = total + 1.0 * this.songNotes[i].getDuration();
			}
		}
		df.format(total);
		this.duration = total;
		return this.duration;
	}
	
	/**
	 * play the song by notes
	 */
	public void play(){
		int re = 0, reNote = 0, rePlay = 0;
		for(int i = 0; i < this.noteNum; i++){
			this.playNote = df.format(this.songNotes[i].getDuration()) + " " + this.songNotes[i].getPitch() + (this.songNotes[i].getPitch() == Pitch.R ? "" : (" " 
			+ this.songNotes[i].getOctave() + " " + this.songNotes[i].getAccidental().toString().toLowerCase())) + " " + this.songNotes[i].isRepeat();
			if(this.songNotes[i].isRepeat() && re == 0 && rePlay == 0){
				this.songNotes[i].play();
				re = 1;
				reNote = i;
			}
			else if(this.songNotes[i].isRepeat() && re == 1 && rePlay == 0){
				this.songNotes[i].play();
				re = 0;
				i = reNote - 1;
				rePlay = 1;
			}
			else if(this.songNotes[i].isRepeat() && re == 0 && rePlay == 1){
				this.songNotes[i].play();
				re = 1;
			}
			else if(this.songNotes[i].isRepeat() && re == 1 && rePlay == 1){
				this.songNotes[i].play();
				re = 0;
				rePlay = 0;
			}
			else{
				this.songNotes[i].play();
			}
		}
	}
	
	/**
	 * modify down the octave by 1
	 * @return whether octave is down
	 */
	public boolean octaveDown(){
		Note[] songNotesDown = new Note[this.noteNum];
		for(int i = 0; i < this.noteNum; i++){
			songNotesDown[i] = this.songNotes[i];
			if(this.songNotes[i].getPitch() != Pitch.R){
				if(this.songNotes[i].getOctave() > 1){
					songNotesDown[i].setOctave(this.songNotes[i].getOctave() - 1);
				}
				else{
					return false;
				}
			}
		}
		this.songNotes = songNotesDown;
		return true;
	}
	
	/**
	 * modify up the octave by 1
	 * @return whether octave is up
	 */
	public boolean octaveUp(){
		Note[] songNotesUp = new Note[this.noteNum];
		for(int i = 0; i < this.noteNum; i++){
			songNotesUp[i] = this.songNotes[i];
			if(this.songNotes[i].getPitch() != Pitch.R){
				if(this.songNotes[i].getOctave() < 10){
					songNotesUp[i].setOctave(this.songNotes[i].getOctave() + 1);
				}
				else{
					return false;
				}
			}
		}
		this.songNotes = songNotesUp;
		return true;
	}
	
	/**
	 * change the speed of the song
	 * @param ratio
	 */
	public void changeTempo(double ratio){
		for(int i = 0; i < this.noteNum; i++){
			this.songNotes[i].setDuration(this.songNotes[i].getDuration() * ratio);
		}
	}
	
	 /**
	 * reverse the song
	 */
	public void reverse(){
		 if(this.noteNum % 2 == 0){
			 for(int i = 0; i < this.noteNum / 2; i++){
					Note temp = this.songNotes[this.noteNum - 1 - i];
					this.songNotes[this.noteNum - 1 - i] = this.songNotes[i];
					this.songNotes[i] = temp;
				}
		 }
		 else{
			 for(int i = 0; i < (this.noteNum - 1) / 2; i++){
					Note temp = this.songNotes[this.noteNum - 1 - i];
					this.songNotes[this.noteNum - 1 - i] = this.songNotes[i];
					this.songNotes[i] = temp;
				}
		 }
	 }
	
	public String toString(){
		String songInfo = "";
		songInfo = songInfo + "Song Title: " + this.songName + "\nSong Artist: " + this.Author + "\nSong Duration: " + df.format(getTotalDuration())
		+ "\nSong Note Number: " + String.valueOf(this.noteNum) + "\nSong notes: " + Arrays.toString(this.songNotes);
		return songInfo;
	}
}







