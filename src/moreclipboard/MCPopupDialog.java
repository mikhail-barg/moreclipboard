/**
 * 
 */
package moreclipboard;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.PopupDialog;
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
public class MCPopupDialog extends PopupDialog implements SelectionListener, KeyListener {

	private List m_listView;
	
	public MCPopupDialog(Shell parent) {
		super(parent,
				SWT.TOOL,
				true,
				false,
				false,
				false,
				false,
				null,
				null);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.PopupDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite)super.createDialogArea(parent);
		
		m_listView = new List(composite, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		m_listView.addSelectionListener(this);
		m_listView.addKeyListener(this);
		m_listView.setItems(MCPlugin.getDefault().getContents().getElements());
		m_listView.select(0);
		return composite;
	}

	private void processPasteSelectedElement() {
		int itemIndex = m_listView.getSelectionIndex(); 
		if (itemIndex < 0) {
			return;
		}
		MCPlugin.getDefault().getContents().setCurrentElement(itemIndex);
		this.close();
		try {
			MCPasteHandler.executePaste();
		} catch (ExecutionException ex) {
		}
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
		if (e.widget != m_listView)	{
			return;
		}
		processPasteSelectedElement();
	}

	public void widgetSelected(SelectionEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if (e.character == SWT.CR) {
			processPasteSelectedElement();
		}
	}

	public void keyReleased(KeyEvent e) {
	}
}
