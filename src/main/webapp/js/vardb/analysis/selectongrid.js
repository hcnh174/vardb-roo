/*global Ext, vardb */
vardb.analysis.SelectonGrid = Ext.extend(vardb.util.Grid,
{	
	frame: true,
	width: 500,
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
			root: 'positions',
			idProperty: 'number',
			fields:
			[
				{name: 'column', type: 'int'},
				{name: 'aa'},
				{name: 'kaKs', type: 'float'},
				{name: 'level'}
			]
		});

		var store=new Ext.data.Store(
		{
			reader: reader,
			data: this.data,
			sortInfo: {field: 'column', direction: 'ASC'}
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
				{header: "Position", width: 20, sortable: true, dataIndex: 'column'},
				{header: "Consensus", sortable: true, dataIndex: 'aa'},
				{header: "Ka/Ks", xtype: 'numbercolumn', sortable: true, dataIndex: 'kaKs', align: 'right', tooltip: 'Ka/Ks', format: format},
				{header: "Level", sortable: true, dataIndex: 'level', align: 'center', tooltip: 'selection level', renderer: this.levelRenderer}
			],
			viewConfig: {forceFit: true},
			tbar: new Ext.Toolbar(
			{		 
				items:
				[
					self.createSelectMenu()
				]
			})
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		vardb.analysis.SelectonGrid.superclass.initComponent.apply(this, arguments);
	},
	
	levelRenderer:function(value, p, r)
	{
		var bgcolor='white';
		var description='';
		if (value==='S1')
			{bgcolor='ffbd00'; description='Positive selection - high';}
		else if (value==='S2')
			{bgcolor='ffff78'; description='Positive selection - intermediate';}
		else if (value==='S3')
			{bgcolor='ffffff'; description='Positive selection - low';}
		else if (value==='S4')
			{bgcolor='fcedf4'; description='No selection';}
		else if (value==='S5')
			{bgcolor='fac9de'; description='Purifying selection - low';}
		else if (value==='S6')
			{bgcolor='f07dab'; description='Purifying selection - intermediate';}
		else if (value==='S7')
			{bgcolor='a02560'; description='Purifying selection - high';}
		return '<span style="background-color:#'+bgcolor+'">'+description+'</span>';
	}
});

