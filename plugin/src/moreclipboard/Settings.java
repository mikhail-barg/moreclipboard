package moreclipboard;

/**
 * The class for storing various settings for the plugin
 * 
 * This all could be configurable, but for now I want to keep everything as simple as possible.
 * 
 * @author Mike
 *
 */
public class Settings
{
	/** to prevent instantiation */
	private Settings() {}
	
	/** maximal number of elements in the plugin*/
	static final int MAX_ELEMENTS = 16;
	
	static final boolean USE_FIXED_WIDTH_FONT = true;
	
	/** maximal number of characters displayed in the pop-up dialog*/
	//static final int MAX_DISPLAYED_STRING_LENGTH = 120;
	static final int MAX_DISPLAYED_STRING_LENGTH = 80; // for the fixed-width font we need a smaller length
	
	/** maximal number of characters displayed in the pop-up dialog*/
	static final String LONG_STRING_TERMINATION = "[...]";	
}
