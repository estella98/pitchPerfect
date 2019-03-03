import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.awt.SystemColor;
import java.awt.Toolkit;



public class main {

    public static void main(String[] args) {
        String filePathInput = "wav/c1.wav";
        float sampleRate = 44100;
        int audioBufferSize = 2048;
        int bufferOverlap = 0;

        // Create an AudioInputStream from my .wav file
        try{
            PitchDetectionHandler handler = new PitchDetectionHandler() {
                @Override
                public void handlePitch(PitchDetectionResult pitchDetectionResult,
                                        AudioEvent audioEvent) {
                	Note n = Converter.HztoNote(pitchDetectionResult.getPitch());
                    if(pitchDetectionResult.getPitch()!= -1.0)System.out.println(audioEvent.getTimeStamp() + " " + pitchDetectionResult.getPitch());
                    n.print(); 
                }
            };
            AudioDispatcher adp = AudioDispatcherFactory.fromDefaultMicrophone(44100, 2048, 0);
            adp.addAudioProcessor(new PitchProcessor(PitchEstimationAlgorithm.AMDF, 44100, 2048, handler));
            adp.run();
        }
        catch(Exception error)
        {
            error.printStackTrace();
        };
    }
}
