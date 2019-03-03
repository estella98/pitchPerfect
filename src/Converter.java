
public class Converter {
	
	public static final int STANDARD_A4 = 440;
	
	public static int HztoKeyNumber(double frequency) {
		double a = frequency/ STANDARD_A4;
		double b = Math.log(a)/Math.log(2);
		double n = 12 * b + 49;
		return (int) ((n - (int)n > 0.5)? Math.ceil(n):Math.floor(n));
	}
	
	public static Note KeyNumbertoNote(int keyNumber) {
		int octave = (keyNumber + 8) / 12;
		int noteNum = (keyNumber + 8) % 12;
		String note = "";
		
		if (noteNum == 0)note = "C";
		else if(noteNum == 1)note = "C#/Db";
		else if(noteNum == 2)note = "D";
		else if(noteNum == 3)note = "D#/Eb";
		else if(noteNum == 4)note = "E";
		else if(noteNum == 5)note = "F";
		else if(noteNum == 6)note = "F#/Gb";
		else if(noteNum == 7)note = "G";
		else if(noteNum == 8)note = "G#/Ab";
		else if(noteNum == 9)note = "A";
		else if(noteNum == 10)note = "A#/Bb";
		else if(noteNum == 11)note = "B";
		return new Note(keyNumber,octave, note);
		
	}
	
	public static Note HztoNote(int frequency) {
		return KeyNumbertoNote(HztoKeyNumber(frequency));
	}

}
