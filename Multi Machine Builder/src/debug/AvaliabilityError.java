package debug;

public enum AvaliabilityError {
	AVALIABLE, CORRUPT, NOEXISTENCE;
	
	public boolean isAvaliable() {
		switch(this) {
		case AVALIABLE:
			return true;
		case CORRUPT:
		case NOEXISTENCE:
		default:
			return false;
		}
	}
	
	public boolean exists() {
		switch(this) {
		case AVALIABLE:
		case CORRUPT:
			return true;
		case NOEXISTENCE:
		default:
			return false;
		}
	}
}
