package debug;

import javax.swing.JLabel;

public class Avaliability {
	public AvaliabilityError isAvaliable = AvaliabilityError.AVALIABLE;
	public Exception error = null;
	public Avaliability() {
		// TODO Auto-generated constructor stub
	}
	
	public Avaliability(AvaliabilityError err) {
		isAvaliable = err;
	}
	
	public void corrupt(Exception error) {
		isAvaliable = AvaliabilityError.CORRUPT;
		this.error = error;
	}
	public void noexist(Exception error) {
		isAvaliable = AvaliabilityError.NOEXISTENCE;
		this.error = error;
	}
	
	public void formatLabel(String contents, JLabel label) {
		label.setText(contents);
		if(isAvaliable.isAvaliable()) {
			label.setEnabled(true);
		}else {
			label.setEnabled(false);
		}
	}
	
	

}
