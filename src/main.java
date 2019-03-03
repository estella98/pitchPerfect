import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import com.sun.prism.Image;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;


public class main extends JFrame{
	
	private static final long serialVersionUID = 12L;
	private static JPanel contentPane;
	private static JFrame frame;
	static BufferedImage background;
	private static JTextField title;
	static int width = 500;
	static int height = 500;
	static Timer timer;
	
    public static void main(String[] args) {
    	
    	String filePathInput = "test.wav";
    	frame = new JFrame();
    	contentPane = new ImagePanel();
//    	try {
//    	    final BufferedImage backgroundImage = javax.imageio.ImageIO.read(new File("Background.jpg"));
//    	    contentPane = new ImagePanel(new BorderLayout()) {
//				@Override 
//				public void paintComponent(Graphics g) {
//    	            g.drawImage(backgroundImage, 0, 0, null);
//    	        }
//    	    };
    	frame.setContentPane(contentPane);
//    	} catch (IOException e) {
//    	    throw new RuntimeException(e);
//    	}
    	
    	contentPane.setVisible(true);
    	title = new JTextField("Audio Processor");
    	title.setBounds(200, 100, 100, 50);
    			
    	frame.setTitle("test");
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setBounds(650, 300, 1000, 1000);
    	frame.setSize(width, height + 20);
    	frame.setVisible(true);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(title);
		
		
		JButton loadButton = new JButton("Start");
		loadButton.setBounds(200, 200, 100, 40);
		contentPane.add(loadButton);
		
//		timer = new Timer(6, null);
//		timer.start();
		
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
    }
    
//public void paintComponent(Graphics g) {
//	try {
//		background = ImageIO.read(new File("./asset/background.jpg"));
//	} catch (IOException ex) {
//		System.err.println("image not found");
//	}
//	g.drawImage(background, 0, 0, width, height, this);
//    }
}

