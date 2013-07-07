package moreclipboard;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * A clipboard contents View
 * 
 */
public class ContentsView extends ViewPart implements SelectionListener
{
	////////////////////////////////////////////////////////////////////
	class RemoveCurrentItemAction extends Action
	{
		RemoveCurrentItemAction()
		{
			super(Messages.ContentsView_RemoveCurrentActionName);

			setImageDescriptor(Plugin.getImage("/icons/enabl/rem.gif")); //$NON-NLS-1$
			setDisabledImageDescriptor(Plugin.getImage("/icons/disabl/rem.gif")); //$NON-NLS-1$
		}

		@Override
		public void run()
		{
			removeCurrentItem();
		}
	}

	////////////////////////////////////////////////////////////////////	
	static class ClearContentsAction extends Action
	{
		public ClearContentsAction()
		{
			super(Messages.ContentsView_RemoveAllActionName);

			setImageDescriptor(Plugin.getImage("/icons/enabl/remall.gif")); //$NON-NLS-1$
			setDisabledImageDescriptor(Plugin.getImage("/icons/disabl/remall.gif")); //$NON-NLS-1$
		}

		@Override
		public void run()
		{
			Plugin.getInstance().getContents().clear();
		}
	}
	///////////////////////////////////////////////////////////////////////////////////////
	
	
	private org.eclipse.swt.widgets.List m_listView;
	private Action m_removeCurAction;
	private Action m_removeAllAction;

	@Override
	public void createPartControl(Composite parent)
	{
		m_listView = new org.eclipse.swt.widgets.List(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		m_listView.addSelectionListener(this);
		createActions();
		createContextMenu();
		initToolBar();

		Contents contents = Plugin.getInstance().getContents();
		contents.registerView(this);
	}

	private void createActions()
	{
		m_removeCurAction = new RemoveCurrentItemAction();
		m_removeAllAction = new ClearContentsAction();
		updateRemoveCurAction();
		updateRemoveAllAction();
	}
	
	private void createContextMenu()
	{
		MenuManager manager = new MenuManager();
		manager.setRemoveAllWhenShown(true);
		manager.addMenuListener(new IMenuListener()
			{
				@Override
				public void menuAboutToShow(IMenuManager manager)
				{
					manager.add(m_removeCurAction);
					manager.add(m_removeAllAction);
				}
			});
		m_listView.setMenu(manager.createContextMenu(m_listView));
	}

	private void initToolBar()
	{
		IToolBarManager tm = getViewSite().getActionBars().getToolBarManager();
		tm.add(m_removeCurAction);
		tm.add(m_removeAllAction);
	}

	private void updateRemoveCurAction()
	{
		m_removeCurAction.setEnabled(m_listView.getSelectionIndex() >= 0);
	}

	private void updateRemoveAllAction()
	{
		m_removeCurAction.setEnabled(m_listView.getItemCount() > 0);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus()
	{
		m_listView.setFocus();
	}

	@Override
	public void dispose()
	{
		Plugin.getInstance().getContents().removeView(this);
		super.dispose();
	}

	public void setElements(String elements[])
	{
		String[] preparedElements = new String[elements.length];
		for (int i = 0; i < elements.length; ++i)
		{
			preparedElements[i] = "(" + String.valueOf(i + 1) + "): " + elements[i];  
		}
		
		m_listView.setItems(preparedElements);
		updateRemoveCurAction();
		updateRemoveAllAction();
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e)
	{
		if (e.widget != m_listView)
		{
			return;
		}
		
		final int itemIndex = m_listView.getSelectionIndex();
		if (itemIndex < 0)
		{
			return;
		}
		Plugin.getInstance().getContents().setCurrentElement(itemIndex);
	}

	@Override
	public void widgetSelected(SelectionEvent e)
	{
		updateRemoveCurAction();
	}

	public void removeCurrentItem()
	{
		final int itemIndex = m_listView.getSelectionIndex();
		if (itemIndex < 0)
		{
			return;
		}
		Plugin.getInstance().getContents().removeElement(itemIndex);
	}
}
