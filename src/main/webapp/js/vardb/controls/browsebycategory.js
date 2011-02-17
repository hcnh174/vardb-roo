/*global Ext, nelson, vardb, utils */
Ext.define('vardb.controls.BrowseByCategory',
{
	extend: 'Ext.form.FormPanel',
	labelWidth: 100,
	frame: false,
	title: 'Browse by category',
	bodyStyle: 'padding:5px 5px 0',
	width: '95%',
	iconCls: 'icon-chart_organisation',
	
	initComponent:function()
	{	
		this.ntsequenceCount = new Ext.Toolbar.TextItem('DNA sequences: '+this.data.statistics.ntsequences);
		this.aasequenceCount = new Ext.Toolbar.TextItem('Protein sequences: '+this.data.statistics.aasequences);
	
		var statusbar=new Ext.Toolbar({
			items:
			[
				'->',
				this.ntsequenceCount,
				' ',
				this.aasequenceCount
			 ]
		});
	
		var config=
		{
			defaults: {width: 350},
			bbar: statusbar,
			items:
			[
				this.comboList(this.data.families,'Gene families','family'),
				this.comboList(this.data.pathogens,'Pathogens','pathogen'),
				this.comboList(this.data.diseases,'Diseases','disease')
			]
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		vardb.controls.BrowseByCategory.superclass.initComponent.apply(this, arguments);
		
		this.on('afterrender',function()
		{
			this.ntsequenceCount.addClass('custom-status-text-panel');
			this.aasequenceCount.addClass('custom-status-text-panel');
		},this);
	},
	
	comboList:function(data, fieldLabel, type)
	{
		Ext.regModel('categorymodel', {
		    fields: [
		        {type: 'string', name: 'identifier'},
		        {type: 'string', name: 'name'}
		    ]
		});
		
		// The data store holding the states
		var store = new Ext.data.Store({
		    model: 'categorymodel',
		    data: data
		});
		
		// Simple ComboBox using the data store
		var combo = new Ext.form.ComboBox({
		    valueField: 'identifier',
			displayField: 'name',
		    width: 110,
		    fieldLabel: fieldLabel,
		    //labelWidth: 0,
		    emptyText: (data.length-1)+' '+fieldLabel.toLowerCase(),
		    store: store,
		    queryMode: 'local',
		    typeAhead: true,
		    onSelect: function(record)
			{
				if (type==='family')
				{
					if (record.data.identifier==='families')
						{utils.gotoUrl('/families.html');}
					else {utils.gotoUrl('/families/'+record.data.identifier+'.html');}
				}
				else if (type==='pathogen')
				{
					if (record.data.identifier==='pathogens')
						{utils.gotoUrl('/pathogens.html');}	
					else {utils.gotoUrl('/pathogens/'+record.data.identifier+'.html');}
				}
				else if (type==='disease')
				{
					if (record.data.identifier==='diseases')
						{utils.gotoUrl('/diseases.html');}	
					else{utils.gotoUrl('/diseases/'+record.data.identifier+'.html');}
				}
			}
		});
		return combo;
		
		/*
		var combo=new Ext.form.ComboBox(
		{
			store: new Ext.data.JsonStore(
			{
				fields: ['identifier','name'],
				data: data
			}),
			valueField: 'identifier',
			displayField: 'name',
			mode: 'local',
			triggerAction: 'all',
			emptyText: (data.length-1)+' '+fieldLabel.toLowerCase(),
			forceSelection: true,
			fieldLabel: fieldLabel,
			typeAhead: true,
			onSelect: function(record)
			{
				if (type==='family')
				{
					if (record.data.identifier==='families')
						{utils.gotoUrl('/families.html');}
					else {utils.gotoUrl('/families/'+record.data.identifier+'.html');}
				}
				else if (type==='pathogen')
				{
					if (record.data.identifier==='pathogens')
						{utils.gotoUrl('/pathogens.html');}	
					else {utils.gotoUrl('/pathogens/'+record.data.identifier+'.html');}
				}
				else if (type==='disease')
				{
					if (record.data.identifier==='diseases')
						{utils.gotoUrl('/diseases.html');}	
					else{utils.gotoUrl('/diseases/'+record.data.identifier+'.html');}
				}
			}
		});
		
		return combo;
		*/
	}	
});
