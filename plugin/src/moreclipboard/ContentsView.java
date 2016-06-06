package moreclipboard;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandler2;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.services.IEvaluationService;

/**
 * A clipboard contents View
 * 
 */
public class ContentsView extends ViewPart implements SelectionListener
{
	private org.eclipse.swt.widgets.List m_listView;
	private IContextActivation m_contextActivation;
	private IHandler2 m_removeCurHandler;
	private IHandler2 m_removeAllHandler;
	private IHandler2 m_moveUpHandler;
	private IHandler2 m_moveDownHandler;

	@Override
	public void createPartControl(Composite parent)
	{
		IContextService contextService = (IContextService)getSite().getService(IContextService.class);
		m_contextActivation = contextService.activateContext("MoreClipboard.contexts.View");  //$NON-NLS-1$
		
		m_listView = new org.eclipse.swt.widgets.List(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		m_listView.addSelectionListener(this);
		
		IHandlerService handlerService = (IHandlerService)getSite().getService(IHandlerService.class); 
		m_removeCurHandler = new AbstractHandler() {
			@Override
			public Object execute(ExecutionEvent event) throws ExecutionException 
			{
				removeCurrentItem();
				return null;
			}
			@Override
			public void setEnabled(Object evaluationContext) 
			{
				setBaseEnabled(m_listView.getSelectionIndex() >= 0);
			}
		};
		m_removeAllHandler = new AbstractHandler() {
			@Override
			public Object execute(ExecutionEvent event) throws ExecutionException 
			{
				Plugin.getInstance().getContents().clear();
				return null;
			}
			@Override
			public void setEnabled(Object evaluationContext) 
			{
				setBaseEnabled(m_listView.getItemCount() > 0);
			}
		};
		m_moveUpHandler = new AbstractHandler() {
			@Override
			public Object execute(ExecutionEvent event) throws ExecutionException 
			{
				moveCurrentElementUp();
				return null;
			}
			@Override
			public void setEnabled(Object evaluationContext)
			{
				setBaseEnabled(m_listView.getSelectionIndex() >= 1);
			}
		};
		m_moveDownHandler = new AbstractHandler() {
			@Override
			public Object execute(ExecutionEvent event) throws ExecutionException 
			{
				moveCurrentElementDown();
				return null;
			}
			@Override
			public void setEnabled(Object evaluationContext) 
			{
				setBaseEnabled(m_listView.getSelectionIndex() < m_listView.getItemCount() - 1);
			}
		};
		handlerService.activateHandler("MoreClipboard.commands.ContentsView.deleteCurrent", m_removeCurHandler); 
		handlerService.activateHandler("MoreClipboard.commands.ContentsView.deleteAll", m_removeAllHandler);
		handlerService.activateHandler("MoreClipboard.commands.ContentsView.moveUp", m_moveUpHandler);
		handlerService.activateHandler("MoreClipboard.commands.ContentsView.moveDown", m_moveDownHandler);
		
		MenuManager manager = new MenuManager();
		Menu contextMenu = manager.createContextMenu(m_listView);
		m_listView.setMenu(contextMenu);
		getSite().registerContextMenu(manager, new ListViewer(m_listView));
		
		Contents contents = Plugin.getInstance().getContents();
		contents.registerView(this);
		
		updateHandlersEnabledState();
	}
	
	private void updateHandlersEnabledState()
	{
		boolean hasItems = m_listView.getItemCount() > 0;
		/*
		m_removeCurAction.setEnabled(m_listView.getSelectionIndex() >= 0);
		m_removeAllAction.setEnabled(hasItems);
		m_moveCurUpAction.setEnabled(hasItems && m_listView.getSelectionIndex() >= 1);
		m_moveCurDownAction.setEnabled(hasItems && m_listView.getSelectionIndex() < m_listView.getItemCount() - 1);
		*/
		//IEvaluationService evaluationService = (IEvaluationService)getSite().getService(IEvaluationService.class);
		//evaluationService.requestEvaluation(propertyName);
		m_removeCurHandler.setEnabled(null);
		m_removeAllHandler.setEnabled(null);
		m_moveUpHandler.setEnabled(null);
		m_moveDownHandler.setEnabled(null);
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
		m_removeCurHandler.dispose();
		m_removeAllHandler.dispose();
		m_moveUpHandler.dispose();
		m_moveDownHandler.dispose();
		
		IContextService contextService = (IContextService)getSite().getService(IContextService.class);
		contextService.deactivateContext(m_contextActivation);
		
		Plugin.getInstance().getContents().removeView(this);
		super.dispose();
	}

	public void setElements(String elements[])
	{
		String[] preparedElements = new String[elements.length];
		for (int i = 0; i < elements.length; ++i)
		{
			preparedElements[i] = "(" + String.valueOf(i + 1) + "): " + elements[i];   //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		m_listView.setItems(preparedElements);
		updateHandlersEnabledState();
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
		updateHandlersEnabledState();
	}

	private void removeCurrentItem()
	{
		int itemIndex = m_listView.getSelectionIndex();
		if (itemIndex < 0)
		{
			return;
		}
		Plugin.getInstance().getContents().removeElement(itemIndex);
		
		//keep some item selected if possible and update actions state
		if (itemIndex >= m_listView.getItemCount())
		{
			itemIndex = m_listView.getItemCount() - 1;
		}
		if (itemIndex >= 0)
		{
			m_listView.setSelection(itemIndex);
			updateHandlersEnabledState();
		}
	}
	
	private void moveCurrentElementUp()
	{
		final int itemIndex = m_listView.getSelectionIndex();
		if (itemIndex < 1)
		{
			return;
		}
		Plugin.getInstance().getContents().moveElementUp(itemIndex);
		
		//keep same item selected and update actions state
		m_listView.setSelection(itemIndex - 1);
		updateHandlersEnabledState();
	}
	
	private void moveCurrentElementDown()
	{
		final int itemIndex = m_listView.getSelectionIndex();
		if (itemIndex < 0 || itemIndex >= m_listView.getItemCount() - 1)
		{
			return;
		}
		Plugin.getInstance().getContents().moveElementDown(itemIndex);
		
		//keep same item selected and update actions state
		m_listView.setSelection(itemIndex + 1);
		updateHandlersEnabledState();
	}
}

