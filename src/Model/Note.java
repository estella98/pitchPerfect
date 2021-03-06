package Model;


public class Note {
	private final int STANDARD_A4 = 440;
	int keyNumber;
	int octave;
	String note;
	
	public void print() {
		if (keyNumber == 0)return;
		System.out.println("keyNumber: " + keyNumber);
		System.out.println("octave: " + octave);
		System.out.println("note: " + note);
		System.out.println();
	}

	public Note(int keyNumber, int octave, String note) {
		super();
		this.keyNumber = keyNumber;
		this.octave = octave;
		this.note = note;
	}

	public int getKeyNumber (){
		return keyNumber;
	}

	public int getOctave(){
		return octave;
	}

	public String getNote(){
		return note;
	}

	public int toMidiID(){
		return keyNumber + 20;
	}


}
