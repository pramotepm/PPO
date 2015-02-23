package sequence.compare;

@SuppressWarnings("serial")
public class SplicingPositionException extends Exception {
	public SplicingPositionException() { 
		super();
	}
	
	public SplicingPositionException(String message) { 
		super(message); 
	}
	
	public SplicingPositionException(String message, Throwable cause) { 
		super(message, cause);
	}
	
	public SplicingPositionException(Throwable cause) { 
		super(cause);
	}
}