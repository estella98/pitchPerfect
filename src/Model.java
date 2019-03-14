import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;


public class Model implements Runnable {
    Note n;
    private void model() {

    }

    public void run()
    {
        try{
            PitchDetectionHandler handler = new PitchDetectionHandler() {
                @Override
                public void handlePitch(PitchDetectionResult pitchDetectionResult,
                                        AudioEvent audioEvent) {
                    n = Converter.HztoNote(pitchDetectionResult.getPitch());
                    if(pitchDetectionResult.getPitch()!= -1.0)System.out.println(audioEvent.getTimeStamp() + " " + pitchDetectionResult.getPitch());
                    n.print();
                }
            };
            AudioDispatcher adp = AudioDispatcherFactory.fromDefaultMicrophone(44100, 2048, 0);
            adp.addAudioProcessor(new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.AMDF, 44100, 2048, handler));
            adp.run();
        }
        catch(Exception error)
        {
            error.printStackTrace();
        };
        System.out.println(Thread.currentThread().getName()
                + ", executing run() method!");
    }

    public Note getNote(){
        return n;
    }

}
