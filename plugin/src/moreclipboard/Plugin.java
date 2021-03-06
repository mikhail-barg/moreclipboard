package moreclipboard;

import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main class of the plugin.
 *
 * @author Mikhail Barg
 *
 */
public class Plugin extends AbstractUIPlugin
{
	/** Singleton instance */
	private static Plugin INSTANCE;

	/**
	 * Gets the plugin singleton.
	 *
	 * @return the default instance
	 */
	static public Plugin getInstance()
	{
		return INSTANCE;
	}

	/**
	 * Returns an imageDescriptor for given image path
	 * @param path
	 * @return
	 * @since 1.1
	 */
	public static ImageDescriptor getImage(String path)
	{
		URL url = null;

		try
		{
			url = getInstance().getBundle().getEntry(path);
	    }
		catch (IllegalStateException e)
		{
			e.printStackTrace();
	    }
	    return ImageDescriptor.createFromURL(url);
	}

	/**
	 * Creates the Plugin singleton
	 */
	public Plugin()
	{
		if (INSTANCE == null)
		{
			INSTANCE = this;
		}
	}

	/** The instance of the Contents to store the clipboard data */
	private Contents m_contents;

	/**
	 * @return current MoreClipboard contents
	 */
	public Contents getContents()
	{
		return m_contents;
	}

	@Override
	public void start(BundleContext context) throws Exception
	{
		super.start(context);

		if (m_contents != null)
		{
			throw new IllegalStateException("Attempt to start the plugin second time!");
		}

		PlatformUI.getWorkbench().getDisplay().asyncExec(
				new Runnable()
				{
					@Override
					public void run()
					{
						IContextService contextService = ((IContextService) PlatformUI.getWorkbench().getService(IContextService.class));
						contextService.activateContext("MoreClipboard.contexts.main");
					}
				}
			);

		m_contents = new Contents();
	}
}
