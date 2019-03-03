

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
	
	
	

}
