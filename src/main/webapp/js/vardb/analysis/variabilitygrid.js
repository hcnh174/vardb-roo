/*global Ext, vardb */
Ext.define('vardb.analysis.VariabilityGrid',
{
	extend: 'vardb.util.Grid',
	frame: true,
	width: 600,
	height: 500,
	//autoHeight: true,
	trackMouseOver:false,
	collapsible: false,
	title: 'Variability',

	initComponent:function()
	{
		var self=this;
		
		var reader=new Ext.data.JsonReader(
		{
			root: 'rows',
			idProperty: 'number',
			fields:
			[
				{name: 'number', type: 'int'},
				{name: 'consensus'},
				{name: 'shannon', type: 'float'},
				{name: 'simpson', type: 'float'},
				{name: 'wukabat', type: 'float'}
			]
		});

		var store=new Ext.data.Store(
		{
			reader: reader,
			data: this.data,
			sortInfo: {field: 'number', direction: 'ASC'}
		});
		
		var sm=new Ext.grid.CheckboxSelectionModel({
			sortable: true,
			width: 20
		});
		
		var r=vardb.Renderer;
		var format='0.000';
		var config=
		{
			sm: sm,
			store: store,
			columns:
			[
				sm,
				{header: "Position", width: 20, sortable: true, dataIndex: 'number'},
				{header: "Consensus", sortable: true, dataIndex: 'consensus'},
				{header: "Shannon", xtype: 'numbercolumn', sortable: true, dataIndex: 'shannon', align: 'right', tooltip: 'Shannon entropy', format: format},
				{header: "Simpson", xtype: 'numbercolumn', sortable: true, dataIndex: 'simpson', align: 'right', tooltop: 'Simpson diversity', format: format},
				{header: "Wu-Kabat", xtype: 'numbercolumn', sortable: true, dataIndex: 'wukabat', align: 'right', tooltip: 'Wu-Kabat coefficient', format: format}
			],
			viewConfig: {forceFit: true},
			/*
			view: new Ext.ux.grid.BufferView(
			{				
				rowHeight: 34, // custom row height				
				scrollDelay: false // render rows as they come into viewable area.
			}),
			*/
			tbar: new Ext.Toolbar(
			{		 
				items:
				[
					this.createPrintButton(),
					'-',self.createSelectMenu()
					//'-',self.createCartMenu('GENOME')
				]
			})
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		vardb.analysis.VariabilityGrid.superclass.initComponent.apply(this, arguments);
	}
});
