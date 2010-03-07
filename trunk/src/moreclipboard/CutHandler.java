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
 * The handler for a TransparentCut command
 */
public class CutHandler extends AbstractHandler
{
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		try
		{
			// execute default cut command
			final IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
			final Object result = handlerService.executeCommand("org.eclipse.ui.edit.cut", null);
			
			// copy contents from clipboard
			Plugin.getInstance().getContents().getFromClipboard();

			return result;
		}
		catch (NotDefinedException e)
		{
			throw new ExecutionException("Failed to perform cut command", e);
		}
		catch (NotEnabledException e)
		{
			throw new ExecutionException("Failed to perform cut command", e);
		}
		catch (NotHandledException e)
		{
			throw new ExecutionException("Failed to perform cut command", e);
		}
	}

}
