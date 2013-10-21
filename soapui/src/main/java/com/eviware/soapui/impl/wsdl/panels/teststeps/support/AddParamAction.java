/*
 *  SoapUI, copyright (C) 2004-2013 smartbear.com
 *
 *  SoapUI is free software; you can redistribute it and/or modify it under the
 *  terms of version 2.1 of the GNU Lesser General Public License as published by
 *  the Free Software Foundation.
 *
 *  SoapUI is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *  even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU Lesser General Public License for more details at gnu.org.
 */

package com.eviware.soapui.impl.wsdl.panels.teststeps.support;

import com.eviware.soapui.impl.wsdl.MutableTestPropertyHolder;
import com.eviware.soapui.model.TestPropertyHolder;
import com.eviware.soapui.support.UISupport;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Prakash
 * Date: 2013-10-15
 * Time: 13:02
 * To change this template use File | Settings | File Templates.
 */
public class AddParamAction extends AbstractAction
{
	public static final String EMPTY_STRING = "";
	private TestPropertyHolder propertyHolder;
	private JTable parameterTable;

	public AddParamAction( JTable parameterTable, TestPropertyHolder propertyHolder, String description )
	{
		this.parameterTable = parameterTable;
		this.propertyHolder = propertyHolder;
		putValue( Action.SMALL_ICON, UISupport.createImageIcon( "/add_property.gif" ) );
		putValue( Action.SHORT_DESCRIPTION, description );
	}

	public void actionPerformed( ActionEvent e )
	{
		( ( MutableTestPropertyHolder )propertyHolder ).addProperty( EMPTY_STRING );

		final int row = propertyHolder.getPropertyNames().length - 1;
		SwingUtilities.invokeLater( new Runnable()
		{
			public void run()
			{
				editTableCell( row, 0 );

				final TableCellEditor cellEditor1 = parameterTable.getCellEditor( row, 0 );
				cellEditor1.addCellEditorListener( new CellEditorListener()
				{
					@Override
					public void editingStopped( ChangeEvent e )
					{
						cellEditor1.removeCellEditorListener( this );
						if( parameterTable.getRowCount() > row &&
								parameterTable.getValueAt( row, 0 ).toString().equals( EMPTY_STRING ) )
						{
							( ( MutableTestPropertyHolder )propertyHolder ).removeProperty( EMPTY_STRING );
							return;
						}
						editTableCell( row, 1 );
					}

					@Override
					public void editingCanceled( ChangeEvent e )
					{
						cellEditor1.removeCellEditorListener( this );
						( ( MutableTestPropertyHolder )propertyHolder ).removeProperty( EMPTY_STRING );
					}
				} );
			}
		} );
	}

	private void editTableCell( final int row, final int column )
	{
		TableCellEditor cellEditor = parameterTable.getCellEditor();
		if( cellEditor != null )
		{
			cellEditor.stopCellEditing();
		}

		SwingUtilities.invokeLater( new Runnable()
		{
			public void run()
			{
				parameterTable.requestFocusInWindow();
				if( parameterTable.getRowCount() > row )
				{
					parameterTable.setRowSelectionInterval( row, row );
				}
				parameterTable.scrollRectToVisible( parameterTable.getCellRect( row, column, true ) );
				SwingUtilities.invokeLater( new Runnable()
				{
					public void run()
					{
						parameterTable.editCellAt( row, column );
						Component editorComponent = parameterTable.getEditorComponent();
						if( editorComponent != null )
							editorComponent.requestFocusInWindow();
					}
				} );
			}
		} );
	}

}
