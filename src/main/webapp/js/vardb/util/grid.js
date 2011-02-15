/*global Ext, vardb, alert */
vardb.util.Grid = Ext.extend(Ext.grid.GridPanel,
{
	stripeRows: true,

	initComponent:function()
	{
		vardb.util.Grid.superclass.initComponent.apply(this, arguments);
	},
	
	selectAll:function()
	{
		this.getSelectionModel().selectAll();
	},
	
	unselectAll:function()
	{
		this.getSelectionModel().clearSelections();
	},
	
	invertSelection:function()
	{
		var rows=this.getSelectionModel().getSelections();
		var indexes=[],index, i;
		for (i=0;i<rows.length;i++)
		{
			indexes.push(this.getStore().indexOf(rows[i]));
		}
		this.selectAll();
		for (i=0;i<indexes.length;i++)
		{
			index=indexes[i];
			this.getSelectionModel().deselectRow(index);
		}
	},
	
	getSelectedIds:function()
	{
		var rows=this.getSelectionModel().getSelections();
		var ids=[],index;
		for (index=0;index<rows.length;index++)
		{
			ids.push(rows[index].id);
		}
		return ids;
	},
	
	getSelectedId:function()
	{
		var ids=this.getSelectedIds();
		if (ids.length<1)
		{
			alert('Please select an item');
			throw 'param';
		}
		if (ids.length>1)
		{
			alert('Please select only one item');
			throw 'param';
		}
		return ids[0];
	},
	
	createSelectMenu:function()
	{
		var grid=this;
		var menu=
		{
			text: 'Select',
			enableScrolling: false,
			menu:
			{
				items:
				[
					{text: 'Select all', handler: function(btn){grid.selectAll();}},
					{text: 'Unselect all', handler: function(btn){grid.unselectAll();}},
					'-',{text: 'Invert selection', handler: function(btn){grid.invertSelection();}}
				]
			}
		};
		return menu;
	},
	
	createCartMenu:function(type)
	{
		var grid=this;
		function addSequences(list_id)
		{
			Ext.ux.vardb.Cart.addResourcesToCart(grid,type,list_id);
		}
		
		var menu=
		{
			text: 'Cart',
			enableScrolling: false,
			menu:
			{
				items:
				[
					{text: 'Add to cart', handler: function(btn){addSequences('cart');}},
					{text: 'Add as new cart', handler: function(btn){addSequences('new');}},
					'-',
					{text: 'Open cart', handler: function(btn){Ext.ux.vardb.Cart.cart();}},
					{text: 'Empty cart',handler: function(btn){Ext.ux.vardb.Cart.emptyCart();}}
				]
			}
		};
		return menu;
	},
	
	createResourceToolbar:function(type)
	{
		var self=this;
		var tbar=new Ext.Toolbar(
		{
			items:
			[
				self.createPrintButton(),
				'-',self.createSelectMenu(),
				'-',self.createCartMenu(type)
			]
		});
		return tbar;
	},

	createPrintButton:function()
	{
		return {
			//text: 'Print',
			iconCls: 'icon-print',
			scope: this,
			handler: this.printHandler
		};
	},
	
	/*
	createExportButton:function()
	{
		var button = new Ext.ux.Exporter.Button({
			store: this.store,
			text: 'Export'
		});
		return button;
	},
	*/
	
	printHandler:function()
	{
		Ext.ux.GridPrinter.print(this);		
	},
	
	createReloadButton:function()
	{
		var self=this;
		var button=Ext.ux.vardb.Vardb.createReloadButton(function()
		{
			self.store.reload();
		});
		return button;
	}
	
	/*
	addCopyListener:function()
	{
		var self=this;
		this.addListener('keydown',function(evnt)
		{
			var keyPressed=evnt.getKey();
			if (evnt.ctrlKey)
			{
				// the ctrl+c combination seems to be code 67
				if (keyPressed===67)
				{
					// get a list of all visible columns
					var visibleCols=self.getColumnModel().getColumnsBy(function(columnConfig, index)
					{
						return !columnConfig.hidden;
					});
					var str='';
					var rec,col,colIdxName;
					var selRecords=self.getSelectionModel().getSelections();
					for (rec=0;rec<selRecords.length;rec++)
					{
						for (col=0;col<visibleCols.length;col++)
						{
							colIdxName=''+visibleCols[col].dataIndex;
							str+=selRecords[rec].get(visibleCols[col].dataIndex)+'\t';
						}
						str+='\n';
					}
					alert(str);
					copy(str);
				}
			}
		});
	}
	*/
});
