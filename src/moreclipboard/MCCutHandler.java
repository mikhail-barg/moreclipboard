package moreclipboard;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

public class MCCutHandler extends AbstractHandler
{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		// System.out.println(event.getCommand().getId());

		// execute default copy command
		Object result = null;
		final IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
		try
		{
			result = handlerService.executeCommand("org.eclipse.ui.edit.cut", null);
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

		// copy contents from clipboard
		MCPlugin.getInstance().getContents().getFromClipboard();

		return result;
	}

}
