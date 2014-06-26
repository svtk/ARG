package audio.recognizer.google;

/**
 * Response listeners for URL connections.
 * @author Skylion
 *
 */
public interface GSpeechResponseListener {
	
	public void onResponse(GoogleResponse gr);
	
}
