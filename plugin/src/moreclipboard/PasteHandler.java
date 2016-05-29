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
 * Handler for a PasteFromMoreClipboard command - shows the popup
 */
public class PasteHandler extends AbstractHandler
{
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		PastePopupDialog dialog = PastePopupDialog.getInstance();
		if (dialog != null)
		{
			dialog.MoveSelectionToNextItem();
			return null;
		}

		// copy contents from clipboard - in case there was something else in the clipboard, e.g. copied from the system buffer.
		Plugin.getInstance().getContents().getFromClipboard();

		//create and open a new dialog
		PastePopupDialog.CreateInstance();
		
		return null;
	}

	/**
	 * Performs an actual paste command: takes the first element from the Contents,
	 *  moves it to the system clipboard and tries to issue a regular Paste command
	 * @throws ExecutionException if the underlying API call fails
	 */
	public static void executePaste() throws ExecutionException
	{
		// move contents to clipboard
		Plugin.getInstance().getContents().setToClipboard();
		
		try
		{
			// run regular paste command
			final IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
			handlerService.executeCommand("org.eclipse.ui.edit.paste", null); //result is ignored..
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
	}
}
