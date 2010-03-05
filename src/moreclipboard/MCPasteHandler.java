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
 * Handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class MCPasteHandler extends AbstractHandler
{
	/**
	 * The constructor.
	 */
	private MCPopupDialog m_popupDialog;

	public static Object executePaste() throws ExecutionException
	{
		// move contents to clipboard
		MCPlugin.getInstance().getContents().setToClipboard();

		// run regular paste command
		Object result = null;
		final IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
		try
		{
			result = handlerService.executeCommand("org.eclipse.ui.edit.paste", null);
		}
		catch (NotDefinedException e)
		{
			throw new ExecutionException("Failed to perform paste command", e);
		}
		catch (NotEnabledException e)
		{
			throw new ExecutionException("Failed to perform paste command", e);
		}
		catch (NotHandledException e)
		{
			throw new ExecutionException("Failed to perform paste command", e);
		}
		return result;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		// System.out.println(event.getCommand().getId());

		// return executePaste();

		if (m_popupDialog != null)
		{
			m_popupDialog.close();
			m_popupDialog = null;
		}

		m_popupDialog = new MCPopupDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		m_popupDialog.open();

		return null;
	}
}
