import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class main {

    public static void main(String[] args) {

        String filePathInput = "test.wav";
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
        }
        catch(Exception error)
        {
            error.printStackTrace();
        };
    }
}
