import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import java.awt.*;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Time;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;


public class Audio_Prosessor extends JPanel{
	
	private static final long serialVersionUID = 12L;
	private static JFrame frame;
	static BufferedImage background;
	static int width = 500;
	static int height = 500;
	static Timer timer;
	
    public static void main(String[] args) {
    	
    	String filePathInput = "test.wav";
    	
    	// initiates the panel
    	Audio_Prosessor program = new Audio_Prosessor();
    	program.setVisible(true);
    	program.setBorder(new EmptyBorder(5, 5, 5, 5));
    	program.setLayout(new FlowLayout());
    	
    	// initiates the frame
    	frame = new JFrame();
    	frame.setTitle("test");
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setBounds(650, 300, 1000, 1000);
    	frame.setSize(width, height);
    	frame.setVisible(true);
		frame.setLocationRelativeTo(null);
    	frame.setContentPane(program);
		
    	BufferedImage background;
    	try {
			background = ImageIO.read(new File("Background2.jpg"));
			Image scaled_bg = background.getScaledInstance(500, 500, Image.SCALE_SMOOTH);
			JLabel picLabel = new JLabel(new ImageIcon(scaled_bg), JLabel.CENTER);
			picLabel.setVisible(true);
			picLabel.setBounds(250, 0, 1000, 1000);
			program.add(picLabel);
			frame.pack();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
//    	JTextField title = new JTextField("Audio Processor");
//    	program.add(title);
    	
		JButton loadButton = new JButton("Start");
		loadButton.setBounds(200, 200, 100, 40);
		program.add(loadButton);
		
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        float sampleRate = 44100;
		        int audioBufferSize = 4096;
		        int bufferOverlap = 0;

		        // Create an AudioInputStream from my .wav file
		        try{
		            AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filePathInput));
		            // Convert into TarsosDSP API
		            JVMAudioInputStream audioStream = new JVMAudioInputStream(stream);
		            AudioDispatcher dispatcher = new AudioDispatcher(audioStream, audioBufferSize, bufferOverlap);
		            MyPitchDetector myPitchDetector = new MyPitchDetector();
		            dispatcher.addAudioProcessor(new PitchProcessor(PitchEstimationAlgorithm.YIN, sampleRate,
		                    audioBufferSize, myPitchDetector));
		            dispatcher.run();
		        } catch(Exception error)
		        {
		            error.printStackTrace();
		        };
			}
		});
		
//		Timer t = new Timer(6, program);
//		t.start();
    }
    
    

   
}
