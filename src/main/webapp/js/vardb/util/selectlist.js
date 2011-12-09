/*global Ext, vardb */
Ext.define('vardb.util.SelectList',
{	
	extend: 'Ext.form.ComboBox',
	valueField: 'value',
	displayField: 'display',
	width: 150,
	queryMode: 'local',
	typeAhead: true,
	//triggerAction: 'all',
	//selectOnFocus: true,
	//forceSelection: true,

	initComponent:function()
	{
		Ext.regModel('selectlistmodel', {
		    fields: [
		        {type: 'string', name: 'value'},
		        {type: 'string', name: 'display'}
		    ]
		});
		
		var store = new Ext.data.Store({
		    model: 'selectlistmodel',
		    data: this.prepareData(this.data)
		});
		
		var config=
		{
			store: new Ext.data.Store({
			    model: 'selectlistmodel',
			    data: this.prepareData(this.data)
			})
		};
		
		
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		vardb.util.SelectList.superclass.initComponent.apply(this, arguments);
	},
	
	prepareData: function(data)
	{
		if (!(data instanceof Array))
		{
			var arr=data.split(',');
			var index,value,display;
			data=[];
			for (index=0;index<arr.length;index++)
			{
				value=display=arr[index];
				if (display===' ')
					{display='&nbsp;';}
				data.push([value,display]);
			}
		}
		return data;
	}
});

