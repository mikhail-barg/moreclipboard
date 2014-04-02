package moreclipboard;

import org.eclipse.ui.IStartup;

/**
 * see https://wiki.eclipse.org/FAQ_Can_I_activate_my_plug-in_when_the_workbench_starts%3F
 *
 */
public class StartupAssurer implements IStartup
{
	@Override
	public void earlyStartup()
	{
		//just implementing this interface and specifying this class in org.eclipse.ui.startup extension point
		//causes it to be loaded and therefore - plugin to be activated

		//System.out.println("Startup!");
	}
}
