/**
 *
 */
package moreclipboard;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * The main part of the plugin - the pop-up dialog showing the contents of the plugin and allowing pasting
 *
 */
public class PastePopupDialog extends org.eclipse.jface.dialogs.PopupDialog
							  implements SelectionListener, KeyListener
{
	private static PastePopupDialog s_instance = null;
	
	static PastePopupDialog getInstance() 
	{
		return s_instance;
	}
	
	static void CreateInstance()
	{
		if (s_instance != null)
		{
		}
		else
		{
			s_instance = new PastePopupDialog(Display.getCurrent().getActiveShell());
			s_instance.open();
		}
		
	}
	
	
	private List m_listView;

	private PastePopupDialog(Shell parent)
	{
		super(parent, INFOPOPUP_SHELLSTYLE, true, false, false, false, false, null, null);
	}

	@Override
	protected Control createDialogArea(Composite parent)
	{
		final Composite clientArea = (Composite) super.createDialogArea(parent);

		m_listView = new List(clientArea, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		m_listView.addSelectionListener(this);
		m_listView.addKeyListener(this);
		m_listView.setItems(Plugin.getInstance().getContents().getElements());
		m_listView.select(0);

		if (Settings.USE_FIXED_WIDTH_FONT)
		{
			m_listView.setFont(JFaceResources.getTextFont());
		}
		return clientArea;
	}

	private static void processEvents()
	{
		//see https://bugs.eclipse.org/bugs/show_bug.cgi?id=424576
		//http://sourceforge.net/p/practicalmacro/bugs/17/
	    Display display = PlatformUI.getWorkbench().getDisplay();
	    if (display != null)
	    {
	        while (display.readAndDispatch())
	        {

	        }
	    }
	}

	private void processPasteSelectedElement()
	{
		final int itemIndex = m_listView.getSelectionIndex();
		if (itemIndex < 0)
		{
			return;
		}

		Plugin.getInstance().getContents().setCurrentElement(itemIndex);

		this.close();
		processEvents();	//probably helps to actually assure closing is finished. See links in the method

		try
		{
			PasteHandler.executePaste();
		}
		catch (ExecutionException e)
		{
			//TODO: do something about the exception..
			e.printStackTrace();
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e)
	{
		if (e.widget != m_listView)
		{
			return;
		}

		processPasteSelectedElement();
	}

	@Override
	public void widgetSelected(SelectionEvent e)
	{
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		//handling should be in the keyPressed, not keyReleased, to prevent
		// using "Return" key to be handled as a search key in list, which cause a bug..
		if (e.keyCode == SWT.CR
				|| e.keyCode == SWT.KEYPAD_CR
				)
		{
			processPasteSelectedElement();
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
	}
	
	@Override
	public boolean close() 
	{
		s_instance = null;
		return super.close();
	}
	
	void MoveSelectionToNextItem()
	{
		if (m_listView.getItemCount() <= 0)
		{
			return;
		}
		int selectedIndex = m_listView.getSelectionIndex();
		if (selectedIndex < 0)
		{
			selectedIndex = 0;
		}
		else
		{
			selectedIndex = (selectedIndex + 1) % m_listView.getItemCount();
		}
		m_listView.setSelection(selectedIndex);
	}
}
