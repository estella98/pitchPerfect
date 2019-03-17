package Model;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import java.lang.*;

public class ADPModel implements Runnable {
    Note n;
    Note standardNote;
    boolean flag = true;
    private void model() {

    }

    public void run()
    {
        int testNote = 40;

        try {
            PitchDetectionHandler handler = new PitchDetectionHandler() {
                @Override
                public void handlePitch(PitchDetectionResult pitchDetectionResult,
                                        AudioEvent audioEvent) {
                    PlayNote NotePlayer = new PlayNote();
                    if (flag) {
                        standardNote = NotePlayer.RandomNoteGenerator();
                        try {
                            NotePlayer.playNotes(standardNote, 500);
                        } catch (Exception error) {
                        }
                        ;
                        System.out.println("the standard note is" + standardNote.getKeyNumber());
                        flag = false;
                        try {
                            // thread to sleep for 1000 milliseconds
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                    n = Converter.HztoNote(pitchDetectionResult.getPitch());
                    if (pitchDetectionResult.getPitch() != -1.0) System.out.println(n.getKeyNumber());
                    if (n.getKeyNumber() == standardNote.getKeyNumber()) {
                        flag = true;
                        try {
                            // thread to sleep for 1000 milliseconds
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
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
    public Note getStandardNote(){return standardNote; }
}
