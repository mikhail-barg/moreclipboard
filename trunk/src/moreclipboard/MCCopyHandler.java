package moreclipboard;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

public class MCCopyHandler extends AbstractHandler {

	/**
	 * The constructor.
	 */
	public MCCopyHandler() {
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		//System.out.println(event.getCommand().getId());
		
		//execute default copy command
		Object result = null;
		IHandlerService handlerService = (IHandlerService)PlatformUI.getWorkbench().getService(IHandlerService.class);
		try {
			result = handlerService.executeCommand("org.eclipse.ui.edit.copy", null);
		} catch (NotDefinedException e){
			//System.out.println(e);
		} catch (NotEnabledException e){
			//System.out.println(e);
		} catch (NotHandledException e){
			//System.out.println(e);
		}
		
		// copy contents from clipboard
		MCPlugin.getDefault().getContents().getFromClipboard();
		
		return result;
	}

}
