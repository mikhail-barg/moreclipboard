package moreclipboard;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

/**
 * Handler for a PasteFromMoreClipboard by Index command - instantly pastes
 */
public class PasteIndexHandler extends AbstractHandler
{
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		//in regular PasteHandler we do follwing:
		//// copy contents from clipboard - in case there was something else in the clipboard, e.g. copied from the system buffer.
		//// Plugin.getInstance().getContents().getFromClipboard();
		// but here it would break the order of elements, rendering this command unusable, 
		// so we are ok to possibly lose the clipboard content.. 
		String paramValue = event.getParameter("MoreClipboard.commands.morePasteIndexParameter");
		if (paramValue == null)
		{
			throw new ExecutionException("Failed to perform paste with index command - parameter is null");
		}
		
		int index = Integer.parseInt(paramValue);
		Plugin.getInstance().getContents().setToClipboard(index);
		
		try
		{
			// run regular paste command
			final IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
			handlerService.executeCommand("org.eclipse.ui.edit.paste", null); //result is ignored..
		}
		catch (NotDefinedException e)
		{
			throw new ExecutionException("Failed to perform paste with index command", e);
		}
		catch (NotEnabledException e)
		{
			throw new ExecutionException("Failed to perform paste with index command", e);
		}
		catch (NotHandledException e)
		{
			throw new ExecutionException("Failed to perform paste with index command", e);
		}
		

		return null;
	}
}
