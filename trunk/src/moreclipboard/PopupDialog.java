/**
 * 
 */
package moreclipboard;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

/**
 * @author MVPI
 * 
 */
public class PopupDialog extends org.eclipse.jface.dialogs.PopupDialog implements SelectionListener, KeyListener
{
	private List m_listView;

	public PopupDialog(Shell parent)
	{
		super(parent, SWT.TOOL, true, false, false, false, false, null, null);
	}

	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite composite = (Composite) super.createDialogArea(parent);

		m_listView = new List(composite, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		m_listView.addSelectionListener(this);
		m_listView.addKeyListener(this);
		m_listView.setItems(Plugin.getInstance().getContents().getElements());
		m_listView.select(0);
		return composite;
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
		if (e.character == SWT.CR)
		{
			processPasteSelectedElement();
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
	}
}