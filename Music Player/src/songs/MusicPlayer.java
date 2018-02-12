package songs;

import java.awt.BorderLayout;
import java.awt.Color;

/*
 * Music Player
 *
 * This instructor-provided file implements the graphical user interface (GUI)
 * for the Music Player program and allows you to test the behavior
 * of your Song class.
 */

import java.awt.Container;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Toolkit;
import java.awt.Font;

public class MusicPlayer implements ActionListener, ChangeListener, StdAudio.AudioEventListener {

    // instance variables
    private Song song;
    private boolean playing, loaded, stopped, setTempo, changeO, changeT; // whether a song is currently playing or loaded
    private MainFrame frame;
    private JFileChooser fileChooser;
    private JTextField tempoText;
    private JSlider currentTimeSlider, tempoS;
    private JPanel fileP, songP, userP, octaveP, tempoP;
    private Container cp;
    private JLabel lbDesigner, lbFrame;
    private JButton exitB, smallB, loadB, playB, pauseB, stopB, reverseB, octaveB, upB, downB, tempoB, tenterB, tresetB;
    private double tempo = 1.0;
    private DecimalFormat df = new DecimalFormat("#0.00");
    
    //these are the two labels that indicate time
    // to the right of the slider
    private JLabel currentTimeLabel;
    private JLabel totalTimeLabel;
    private JLabel infoLabel, pinfoLabel;
    
    //this the label that says 'welcome to the music player'
    private JLabel statusLabel, artistLabel; 
    private JLabel startLabel; 
   
    /*
     * Creates the music player GUI window and graphical components.
     */
    /**
     * @wbp.parser.entryPoint
     */
    public MusicPlayer() {
        song = null;
        createComponents();
        doLayout();
        StdAudio.addAudioEventListener(this);
        frame.setVisible(true);
    }

    /*
     * Called when the user interacts with graphical components, such as
     * clicking on a button.
     */
    public void actionPerformed(ActionEvent event) {
    	String cmd = event.getActionCommand();
        if (cmd.equals("Play")) {
           //fill this
        	octaveP.setVisible(false);
        	tempoP.setVisible(false);
        	stopped = false;
        	setCurrentTime(getCurrentTime());
        	infoLabel.setText("play song");
        	playSong();
        } else if (cmd.equals("Pause")) {
            StdAudio.setPaused(!StdAudio.isPaused());
        	pinfoLabel.setText(infoLabel.getText());
        	infoLabel.setText("paused");
        } else if (cmd == "Stop") {
        	stopped = true;
        	StdAudio.setMute(true);
            StdAudio.setPaused(false);
        } else if (cmd == "Load") {
        	pinfoLabel.setText(infoLabel.getText());
        	infoLabel.setText("load file");
        	octaveP.setVisible(false);
        	tempoP.setVisible(false);
            try {
                loadFile();
            } catch (IOException ioe) {
            	System.out.println("not able to load from the file\n");
            }
        } else if (cmd == "Reverse") {
            //TODO - fill this
        	octaveP.setVisible(false);
        	tempoP.setVisible(false);
        	song.reverse();
        	currentTimeSlider.setValue(0);
        	currentTimeLabel.setText(String.format("%09.2f /", 0.0));
        	pinfoLabel.setText(infoLabel.getText());
        	infoLabel.setText("song reversed");
        } else if (cmd == "Octave") {
        	//TODO - fill this
        	pinfoLabel.setText(infoLabel.getText());
        	infoLabel.setText("set octave");
        	octaveP.setVisible(true);
        	tempoP.setVisible(false);
        	changeO = true;
        } else if (cmd == "Up") {
           //TODO - fill this
        	if(song.octaveUp()){
        		pinfoLabel.setText(infoLabel.getText());
            	infoLabel.setText("octave up");
            	changeO = false;
        	}
        	else{
        		pinfoLabel.setText(infoLabel.getText());
            	infoLabel.setText("octave is at top");
        	}
        } else if (cmd == "Down") {
            //TODO - fill this
        	if(song.octaveDown()){
        		pinfoLabel.setText(infoLabel.getText());
            	infoLabel.setText("octave down");
            	changeO = false;
        	}
        	else{
        		pinfoLabel.setText(infoLabel.getText());
            	infoLabel.setText("octave is at bottom");
        	}
        } else if (cmd == "Change Tempo") {
            //TODO - fill this
        	pinfoLabel.setText(infoLabel.getText());
        	infoLabel.setText("change tempo");
        	this.setTempo = true;
        	octaveP.setVisible(false);
        	tempoP.setVisible(true);
        	changeT = true;
        } else if (cmd == "EnterT") {
        	double t = Double.parseDouble(tempoText.getText());
        	if(t == 0){
        		pinfoLabel.setText(infoLabel.getText());
            	infoLabel.setText("tempo can't be 0");
        	}
        	else{
        		song.changeTempo(1.0 / this.tempo);
        		this.tempo = t;
            	song.changeTempo(tempo);
            	pinfoLabel.setText(infoLabel.getText());
            	infoLabel.setText("tempo set to " + this.tempo);
            	this.setTempo = false;
            	tempoS.setValue(0);
            	this.setTempo = true;
            	updateTotalTime();
            	changeT = false;
        	}
        } else if (cmd == "ResetT") {
        	song.changeTempo(1.0 / this.tempo);
        	this.tempo = 1.0;
        	song.changeTempo(tempo);
        	pinfoLabel.setText(infoLabel.getText());
        	infoLabel.setText("tempo reset to 1.0");
        	tempoText.setText("1.0");
        	this.setTempo = false;
        	tempoS.setValue(0);
        	this.setTempo = true;
        	updateTotalTime();
        	changeT = false;
        } else if (cmd == "Small") {
        	pinfoLabel.setText(infoLabel.getText());
        	infoLabel.setText("minimized");
        	octaveP.setVisible(false);
        	tempoP.setVisible(false);
        	frame.setExtendedState(JFrame.ICONIFIED);
        } else if (cmd == "Exit"){
        	System.exit(0);
        }
    }
    
    /* 
     * Called when the user interacts with graphical components, such as
     * sliding on a slider.
     */
    public void stateChanged(ChangeEvent event){
    	if((JSlider)event.getSource() == tempoS && setTempo){
			double t = (double)Math.round((1 + tempoS.getValue() * 0.05) * 100) / 100;
            tempoText.setText(String.valueOf(t));
			song.changeTempo(1.0 / tempo);
		    tempo = t;
			song.changeTempo(tempo);
			pinfoLabel.setText(infoLabel.getText());
			infoLabel.setText("tempo set to " + tempo);
			updateTotalTime();
			changeT = false;
    	}
    }

    /*
     * Called when audio events occur in the StdAudio library. We use this to
     * set the displayed current time in the slider.
     */
    public void onAudioEvent(StdAudio.AudioEvent event) {
        // update current time
        if (event.getType() == StdAudio.AudioEvent.Type.PLAY
                || event.getType() == StdAudio.AudioEvent.Type.STOP) {
            setCurrentTime(getCurrentTime() + event.getDuration());
        }
    }
    
    /*
     * Sets up the graphical components in the window and event listeners.
     */
    private void createComponents() {
        //TODO - create all your components here.
        // note that you should have already defined your components as instance variables.
    	frame = new MainFrame();
		frame.addMouseListener(new java.awt.event.MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
						octaveP.setVisible(false);
						tempoP.setVisible(false);
						if(changeO){
							pinfoLabel.setText(infoLabel.getText());
							infoLabel.setText("octave no change");
						}
						if(changeT){
							pinfoLabel.setText(infoLabel.getText());
							infoLabel.setText("tempo no change");
						}
						changeO = false; changeT = false;
			}
		}); // hide the octave panel and tempo panel when click on other things
		cp =frame.getContentPane();
		userP = new JPanel();
		fileP = new JPanel();
		songP = new JPanel();
		lbDesigner = new JLabel("Designed by CNelson and Colin"); lbDesigner.setForeground(Color.WHITE); 
		lbDesigner.setFont(new Font("Arial", Font.BOLD, 10)); lbDesigner.setBounds(4, 6, 180, 40);
		lbFrame = new JLabel("Kaori Player"); lbFrame.setForeground(Color.WHITE);
		lbFrame.setFont(new Font("Arial", Font.BOLD, 12)); lbFrame.setBounds(4, -8, 180, 40);
		exitB = new JButton("Exit"); exitB.addActionListener(this); exitB.setToolTipText("exit");
		loadB = new JButton("Load"); loadB.addActionListener(this); loadB.setToolTipText("load file");
		fileChooser = new JFileChooser(); fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); fileChooser.setDialogTitle("Choose a song notes file");
		startLabel = new JLabel("Welcome to the music player!"); startLabel.setForeground(Color.WHITE);
		startLabel.setFont(new Font("Arial", Font.BOLD, 36)); startLabel.setBounds(53, 187, 520, 100);
		statusLabel = new JLabel(); statusLabel.setForeground(Color.WHITE);
		statusLabel.setFont(new Font("Arial", Font.BOLD, 17)); statusLabel.setBounds(17, 14, 500, 40);
		artistLabel = new JLabel(); artistLabel.setForeground(Color.WHITE);
		artistLabel.setFont(new Font("Arial", Font.BOLD, 14)); artistLabel.setBounds(80, 40, 500, 40);
		currentTimeLabel = new JLabel(); currentTimeLabel.setForeground(Color.WHITE);
		currentTimeLabel.setFont(new Font("Arial", Font.BOLD, 14)); currentTimeLabel.setBounds(460, 185, 100, 20);
		totalTimeLabel = new JLabel(); totalTimeLabel.setForeground(Color.WHITE);
		totalTimeLabel.setFont(new Font("Arial", Font.BOLD, 14)); totalTimeLabel.setBounds(460, 200, 100, 20);
		infoLabel = new JLabel(); infoLabel.setForeground(Color.WHITE);
		infoLabel.setFont(new Font("Arial", Font.BOLD, 13)); infoLabel.setBounds(420, 480, 140, 20);
		pinfoLabel = new JLabel(); pinfoLabel.setForeground(Color.WHITE);
		pinfoLabel.setFont(new Font("Arial", Font.BOLD, 11)); pinfoLabel.setBounds(420, 465, 140, 20);
		currentTimeSlider = new JSlider(); currentTimeSlider.setSize(455, 40); currentTimeSlider.setLocation(0, 185);
		currentTimeSlider.setOpaque(false);
		tempoText = new JTextField();
		playB = new JButton("Play"); playB.addActionListener(this); playB.setToolTipText("play");
		pauseB = new JButton("Pause"); pauseB.addActionListener(this); pauseB.setToolTipText("pause");
		stopB = new JButton("Stop"); stopB.addActionListener(this); stopB.setToolTipText("stop");
		smallB = new JButton("Small"); smallB.addActionListener(this); smallB.setToolTipText("minimize");	
		reverseB = new JButton("Reverse"); reverseB.addActionListener(this); reverseB.setToolTipText("reverse");
		octaveB = new JButton("Octave"); octaveB.addActionListener(this); octaveB.setToolTipText("octave");
		tempoB = new JButton("Change Tempo"); tempoB.addActionListener(this); tempoB.setToolTipText("tempo");
		octaveP = new JPanel();
		upB = new JButton(); 
		upB.setFont(new Font("Tahoma", Font.PLAIN, 10));upB.addActionListener(this); upB.setActionCommand("Up");
		downB = new JButton(); 
		downB.setFont(new Font("Tahoma", Font.PLAIN, 10));downB.addActionListener(this); downB.setActionCommand("Down");
		tempoP = new JPanel();
		tenterB = new JButton(); tresetB = new JButton();
		tenterB.setFont(new Font("Arial", Font.PLAIN, 11));tenterB.addActionListener(this); tenterB.setActionCommand("EnterT");
		tresetB.setFont(new Font("Arial", Font.PLAIN, 11));tresetB.addActionListener(this); tresetB.setActionCommand("ResetT");
		tempoText = new JTextField(); tempoText.setOpaque(false);
		tempoS = new JSlider(-18,18,0);tempoS.setOpaque(false); tempoS.addChangeListener(this);
        doEnabling();
    }

    /*
     * Sets whether every button, slider, spinner, etc. should be currently
     * enabled, based on the current state of whether a song has been loaded and
     * whether or not it is currently playing. This is done to prevent the user
     * from doing actions at inappropriate times such as clicking play while the
     * song is already playing, etc.
     */
    private void doEnabling() {
       //TODO - figure out which buttons need to enabled
    	if(loaded != true){
    		statusLabel.setVisible(false);
    		artistLabel.setVisible(false);
    		currentTimeLabel.setVisible(false);
    		totalTimeLabel.setVisible(false);
    		currentTimeSlider.setVisible(false);
    		playB.setVisible(false);
    		pauseB.setVisible(false);
    		stopB.setVisible(false);
    		reverseB.setVisible(false);
    		octaveB.setVisible(false);
    		tempoB.setVisible(false);
    	}
    	else if(loaded && (playing != true)){
    		startLabel.setVisible(false);
    		statusLabel.setVisible(true);
    		artistLabel.setVisible(true);
    		currentTimeLabel.setVisible(true);
    		totalTimeLabel.setVisible(true);
    		currentTimeSlider.setVisible(true);
    		playB.setVisible(true);
    		loadB.setVisible(true);
    		pauseB.setVisible(false);
    		stopB.setVisible(false);
    		reverseB.setVisible(true);
    		octaveB.setVisible(true);
    		tempoB.setVisible(true);
    	}
    	else if(playing){
    		playB.setVisible(false);
    		loadB.setVisible(false);
    		pauseB.setVisible(true);
    		stopB.setVisible(true);
    		reverseB.setVisible(false);
    		//octaveB.setVisible(false);
    		//tempoB.setVisible(false);
    	}
    }
    
    /**
     * set the button icon
     * @param file
     * @param button
     */
    private void setIcon(String file, JButton button){
    	ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(file));
    	icon.getImage();
		Image temp = icon.getImage().getScaledInstance(button.getWidth(), button.getHeight(), Image.SCALE_DEFAULT);
    	icon = new ImageIcon(temp);
    	button.setIcon(icon);
    }

    /*
     * Performs layout of the components within the graphical window. 
     * Also make the window a certain size and put it in the center of the screen.
     */
    private void doLayout() {
        //TODO - figure out how to layout the components
    	cp.setLayout(new BorderLayout()); 
		((JPanel)cp).setOpaque(false);
		userP.setOpaque(false);
		userP.setSize(cp.getWidth(), cp.getHeight());
		userP.setLocation(0, 0);
		userP.setLayout(null);
    	cp.add(userP);
    	fileP.setSize(120, 170); fileP.setLocation(5, 420);
		fileP.setOpaque(false);
		userP.add(fileP);
		songP.setSize(560, 500); songP.setLocation(420, 80);
		songP.setOpaque(false);
		userP.add(songP);
		userP.add(lbDesigner); userP.add(lbFrame);
		fileP.setLayout(null);
		exitB.setSize(30, 60); exitB.setLocation(10, 100); setIcon("source/exit.png", exitB);
		exitB.setBorderPainted(false); exitB.setContentAreaFilled(false); exitB.setFocusPainted(false); exitB.setFocusable(true); exitB.setMargin(new Insets(0, 0, 0, 0));
		fileP.add(exitB);
		smallB.setSize(40, 60);smallB.setLocation(80, 100); setIcon("source/small.png", smallB);
		smallB.setBorderPainted(false); smallB.setContentAreaFilled(false); smallB.setFocusPainted(false); smallB.setFocusable(true); smallB.setMargin(new Insets(0, 0, 0, 0));
		fileP.add(smallB);
		loadB.setSize(50, 70); loadB.setLocation(50, 20); setIcon("source/load.png", loadB);
		loadB.setBorderPainted(false); loadB.setContentAreaFilled(false); loadB.setFocusPainted(false); loadB.setFocusable(true); loadB.setMargin(new Insets(0, 0, 0, 0));
		fileP.add(loadB);
		songP.setLayout(null);
		songP.add(startLabel);songP.add(statusLabel); songP.add(artistLabel); songP.add(currentTimeLabel); songP.add(currentTimeSlider); songP.add(totalTimeLabel); songP.add(infoLabel); songP.add(pinfoLabel);
		playB.setSize(80, 70); playB.setLocation(140, 110); setIcon("source/play.png", playB);
		playB.setBorderPainted(false); playB.setContentAreaFilled(false); playB.setFocusPainted(false); playB.setFocusable(true); playB.setMargin(new Insets(0, 0, 0, 0));
		songP.add(playB);
		pauseB.setSize(80, 70); pauseB.setLocation(140, 110); setIcon("source/pause.png", pauseB);
		pauseB.setBorderPainted(false); pauseB.setContentAreaFilled(false); pauseB.setFocusPainted(false); pauseB.setFocusable(true); pauseB.setMargin(new Insets(0, 0, 0, 0));
		songP.add(pauseB);
		stopB.setSize(80, 50); stopB.setLocation(260, 130); setIcon("source/stop.png", stopB);
		stopB.setBorderPainted(false); stopB.setContentAreaFilled(false); stopB.setFocusPainted(false); stopB.setFocusable(true); stopB.setMargin(new Insets(0, 0, 0, 0));
		songP.add(stopB);
		reverseB.setSize(90, 30); reverseB.setLocation(220, 220); setIcon("source/reverse.png", reverseB);
		reverseB.setBorderPainted(false); reverseB.setContentAreaFilled(false); reverseB.setFocusPainted(false); reverseB.setFocusable(true); reverseB.setMargin(new Insets(0, 0, 0, 0));
		songP.add(reverseB);
		octaveB.setSize(50, 60); octaveB.setLocation(510, 280); setIcon("source/octave.png", octaveB);
		octaveB.setBorderPainted(false); octaveB.setContentAreaFilled(false); octaveB.setFocusPainted(false); octaveB.setFocusable(true); octaveB.setMargin(new Insets(0, 0, 0, 0));
		songP.add(octaveB);
		tempoB.setSize(50, 60); tempoB.setLocation(417, 240); setIcon("source/tempo.png", tempoB);
		tempoB.setBorderPainted(false); tempoB.setContentAreaFilled(false); tempoB.setFocusPainted(false); tempoB.setFocusable(true); tempoB.setMargin(new Insets(0, 0, 0, 0));
		songP.add(tempoB);
		octaveP.setSize(41, 40); octaveP.setLocation(518, 240); octaveP.setVisible(false);
		songP.add(octaveP);
		octaveP.setLayout(null); octaveP.setOpaque(false);
		upB.setText("+"); downB.setText("-");
		upB.setSize(41, 20); upB.setLocation(0, 0); downB.setSize(41, 20); downB.setLocation(0, 20);
		upB.setFocusPainted(false); upB.setFocusable(true); downB.setFocusPainted(false); downB.setFocusable(true); upB.setContentAreaFilled(false); downB.setContentAreaFilled(false);
		octaveP.add(upB); octaveP.add(downB);
		tempoP.setSize(320, 30); tempoP.setLocation(90, 260); tempoP.setVisible(false);
		songP.add(tempoP);
		tempoP.setLayout(null); tempoP.setOpaque(false);
		tenterB.setText("ok"); tresetB.setText("reset");
		tenterB.setSize(45, 30); tenterB.setLocation(215, 0); tenterB.setFocusPainted(false); tenterB.setFocusable(true); tenterB.setContentAreaFilled(false);
		tresetB.setSize(60, 30); tresetB.setLocation(260, 0); tresetB.setFocusPainted(false); tresetB.setFocusable(true); tresetB.setContentAreaFilled(false);
		tempoP.add(tenterB); tempoP.add(tresetB);
		tempoText.setSize(45, 30); tempoText.setLocation(170, 0); 
		tempoP.add(tempoText);
		tempoS.setSize(170, 30); tempoS.setLocation(0, 0);
		tempoP.add(tempoS);
    }

    /*
     * Returns the estimated current time within the overall song, in seconds.
     */
    private double getCurrentTime() {
        String timeStr = currentTimeLabel.getText();
        timeStr = timeStr.replace(" /", "");
        try {
            return Double.parseDouble(timeStr);
        } catch (NumberFormatException nfe) {
            return 0.0;
        }
    }

    /*
     * Pops up a file-choosing window for the user to select a song file to be
     * loaded. If the user chooses a file, a Song object is created and used
     * to represent that song.
     */
    private void loadFile() throws IOException {
        if (fileChooser.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) {
        	pinfoLabel.setText(infoLabel.getText());
        	infoLabel.setText("no file loaded");
        	return;
        }
        File selected = fileChooser.getSelectedFile();
        if (selected == null) {
        	pinfoLabel.setText(infoLabel.getText());
        	infoLabel.setText("no file loaded");
            return;
        }
        String filename = selected.getAbsolutePath();
        System.out.println("Loading song from " + selected.getName() + " ...");
        
        //TODO - create a song from the file
        try {
        	song = new Song(filename);
        } catch (NumberFormatException e) {
        	System.out.println("This is not a song notes file!\n");
        	JOptionPane.showMessageDialog(null, "This is not a song notes file!");
        	pinfoLabel.setText(infoLabel.getText());
         	infoLabel.setText("file not loaded");
        	return;
        }
        statusLabel.setText("Current song: " + song.getTitle());
        artistLabel.setText("Artist: " + song.getArtist());
        tempoText.setText("1.0");
        setCurrentTime(0.0);
        updateTotalTime();
        infoLabel.setText("file loaded");
        System.out.println("Loading complete.\n");
        System.out.println("Song Information \n" + song);
        loaded = true;
        doEnabling();
    }

    /*
     * Initiates the playing of the current song in a separate thread (so
     * that it does not lock up the GUI). 
     * You do not need to change this method.
     * It will not compile until you make your Song class.
     */
    private void playSong() {
        if (song != null) {
            setCurrentTime(0.0);
            Thread playThread = new Thread(new Runnable() {
                public void run() {
                    StdAudio.setMute(false);
                    playing = true;
                    doEnabling();
                    String title = song.getTitle();
                    String artist = song.getArtist();
                    double duration = song.getTotalDuration();
                    System.out.println("Playing \"" + title + "\", by "
                            + artist + " (" + df.format(duration) + " sec)");
                    song.play();
                    System.out.println("Playing complete.");
                    playing = false;
                    doEnabling();
                    if(stopped){
                    	currentTimeSlider.setValue(0);
                    	currentTimeLabel.setText(String.format("%09.2f /", 0.0));
                    	pinfoLabel.setText(infoLabel.getText());
                    	infoLabel.setText("playing stopped");
                    }
                    else{
                    	pinfoLabel.setText(infoLabel.getText());
                        infoLabel.setText("playing complete");
                    }
                }
            });
            playThread.start();
        }
    }

    /*
     * Sets the current time display slider/label to show the given time in
     * seconds. Bounded to the song's total duration as reported by the song.
     */
    private void setCurrentTime(double time) {
        double total = song.getTotalDuration();
        time = Math.max(0, Math.min(total, time));
        currentTimeLabel.setText(String.format("%09.2f /", time));
        currentTimeSlider.setValue((int) (100 * time / total));
        pinfoLabel.setText(infoLabel.getText());
        infoLabel.setText(song.playNote);
    }

    /*
     * Updates the total time label on the screen to the current total duration.
     */
    private void updateTotalTime() {
       //TODO - fill this
    	double total = song.getTotalDuration();
    	totalTimeLabel.setText(String.format("%09.2f", total) + " sec");
    }
}
