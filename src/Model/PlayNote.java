package Model;
import java.util.Random;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

public class PlayNote {
    private static int INSTRUMENT = 3;
    private static int VOLUME = 300;
    private static MidiChannel[] channels;

    public static Note RandomNoteGenerator() {
        Random rand= new Random();
        int key = rand.nextInt(26) + 30;
        return Converter.KeyNumbertoNote(key);

    }

    public static void playNotes(Note note, int duration) throws InterruptedException, MidiUnavailableException{
        // * start playing a note
        Synthesizer synth = MidiSystem.getSynthesizer();
        System.out.println(synth.getMaxPolyphony());
        synth.open();
        channels = synth.getChannels();
        channels[INSTRUMENT].noteOn(note.toMidiID(), VOLUME );
        // * wait
        Thread.sleep( duration );
        // * stop playing a note
        channels[INSTRUMENT].noteOff(note.toMidiID());
    }


//    public static void main(String[] args) {
//        for(int i = 0; i< 10 ; i++) {
//            Note n = RandomNoteGenerator();
//            n.print();
//            try {
//                playNotes(n,500);
//            }catch(Exception e) {
//            };
//        }
//
//    }


}
