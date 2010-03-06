package moreclipboard;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Mikhail Barg
 * 
 */
public class Plugin extends AbstractUIPlugin
{

	/**
	 * Default instance of the receiver
	 */
	private static Plugin INSTANCE;
	private Contents m_contents;

	/**
	 * Creates the MCPlugin and caches its default instance
	 */
	public Plugin()
	{
		if (INSTANCE == null)
		{
			INSTANCE = this;
		}
		else
		{
			assert (false);
		}
	}

	/**
	 * Gets the plugin singleton.
	 * 
	 * @return the default instance
	 */
	static public Plugin getInstance()
	{
		return INSTANCE;
	}

	public Contents getContents()
	{
		return m_contents;
	}

	public static final String ClearContentsAction_text = "Remove all";
	public static final String RemoveCurrentItemAction_text = "Remove current";

	@Override
	public void start(BundleContext context) throws Exception
	{
		super.start(context);

		m_contents = new Contents();
		assert (m_contents != null);

		// m_contents.addString("AAAA!");
		// m_contents.addString("BBBB!");
		// m_contents.addString("ccc!");
	}

	@Override
	public void stop(BundleContext context) throws Exception
	{
		if (m_contents != null)
		{
			m_contents.dispose();
			m_contents = null;
		}
		super.stop(context);
	}
}
