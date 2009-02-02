package moreclipboard;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Mikhail Barg
 *
 */
public class MCPlugin extends AbstractUIPlugin {

    /**
     * Default instance of the receiver
     */ 
    private static MCPlugin inst;
    private MCContents m_contents;

    /**
     * Creates the MCPlugin and caches its default instance
     */
    public MCPlugin() {
        if (inst == null) {
			inst = this;
		} else {
			assert (false);
		}
    }
    
    /**
     * Gets the plugin singleton.
     *
     * @return the default ReadmePlugin instance
     */
    static public MCPlugin getDefault() {
        return inst;
    }
    
    public MCContents getContents() {
    	return m_contents;
    }
    
    public static final String ClearContentsAction_text = "Remove all"; 
    public static final String RemoveCurrentItemAction_text = "Remove current";

	/* (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
        m_contents = new MCContents();
        assert(m_contents != null);
        
        //m_contents.addString("AAAA!");
        //m_contents.addString("BBBB!");
        //m_contents.addString("ccc!");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		if (m_contents != null){
			m_contents.dispose();
			m_contents = null;
		}
		super.stop(context);
	}
}
