/*global Ext, vardb */
/*global Ext, vardb */
Ext.define('vardb.util.AbstractForm',
{
	extend: 'Ext.form.FormPanel',
	collapsible: false,
	frame: true,
	bodyStyle: 'padding: 5px 5px 0 5px;',

	createFieldset:function(config,items)
	{
		Ext.applyIf(config,
		{
			xtype: 'fieldset',
			collapsible: true,
			collapsed: false,
			items: items
		});
		return config;
	},
	
	createRow:function(items)
	{
		var column=
		{
			layout: 'column',
			items: items
		};
		return column;
	},
	
	createControl:function(controlConfig,formConfig)
	{
		if (!controlConfig.fieldLabel)
		{
			if (controlConfig.name)
				{controlConfig.fieldLabel=controlConfig.name;}
			else if (controlConfig.hiddenName)
				{controlConfig.fieldLabel=controlConfig.hiddenName;}
		}
		if (!controlConfig.value && this.data)
		{
			 if (controlConfig.name && this.data[controlConfig.name])
				{controlConfig.value=this.data[controlConfig.name];}
			 else if (controlConfig.hiddenName && this.data[controlConfig.hiddenName])
				{controlConfig.value=this.data[controlConfig.hiddenName];}
		}
		formConfig=formConfig || {};
		//var rmargin=(controlConfig.helpText) ? 25 : 5;
		var rmargin=0;
		Ext.applyIf(formConfig,
		{
			layout: 'form',
			labelWidth: this.getTextMetrics().getWidth(controlConfig.fieldLabel)+rmargin, //25,//+5,
			bodyStyle: 'padding-right: 7px',
			items: [controlConfig]
		});
		return formConfig;
	},
	
	createDateControl:function(controlConfig,formConfig)
	{
		Ext.applyIf(controlConfig,
		{
			xtype: 'datefield',
			width: 100
		});
		return this.createControl(controlConfig,formConfig);
	},
	
	createTextControl:function(controlConfig,formConfig)
	{
		Ext.applyIf(controlConfig,
		{
			xtype: 'textfield',
			width: 80
		});
		return this.createControl(controlConfig,formConfig);
	},
	
	createTextAreaControl:function(controlConfig,formConfig)
	{
		Ext.applyIf(controlConfig,
		{
			xtype: 'textarea',
			anchor: '-4',
			height: 100
		});
		formConfig=formConfig || {};
		Ext.applyIf(formConfig,
		{
			labelAlign: 'top',
			width: '100%'
		});
		return this.createControl(controlConfig,formConfig);
	},
	
	createNumberControl:function(controlConfig,formConfig)
	{
		Ext.applyIf(controlConfig,
		{
			xtype: 'numberfield',
			width: 30
		});
		return this.createControl(controlConfig,formConfig);
	},
	
	createCheckbox:function(controlConfig,formConfig)
	{
		Ext.applyIf(controlConfig,
		{
			xtype: 'checkbox'
		});
		return this.createControl(controlConfig,formConfig);
	},
		
	createSelectList:function(controlConfig,formConfig)
	{
		return this.createControl(new Ext.ux.vardb.SelectList(controlConfig),formConfig);
	},

	createTextBox:function(controlConfig,formConfig)
	{
		Ext.applyIf(controlConfig,
		{
			xtype: 'textbox',
			width: this.getTextMetrics().getWidth(controlConfig.text)
		});
		return this.createControl(controlConfig,formConfig);
	},
	
	createFileUploadControl:function(controlConfig,formConfig)
	{
		Ext.applyIf(controlConfig,
		{
			xtype: 'fileuploadfield',		
			//emptyText: 'Upload a file',
			fieldLabel: 'File',
			anchor: '-4',
			buttonText: '',
			buttonCfg: {iconCls: 'upload-icon'}
		});
		formConfig=formConfig || {};
		Ext.applyIf(formConfig,
		{
			width: '100%'
		});
		return this.createControl(controlConfig,formConfig);
	},
	
	getTextMetrics:function()
	{
		if (this.textMetrics)
			{return this.textMetrics;}
		if (this.renderTo)
			{this.textMetrics=Ext.util.TextMetrics.createInstance(this.renderTo);}
		else {this.textMetrics=Ext.util.TextMetrics.createInstance(document.body);}
		return this.textMetrics;
	},
	
	createClearButton:function()
	{
		var btn=
		{
			text: 'Clear',
			formBind: true,
			scope: this,
			handler: this.resetHandler
		};
		return btn;
	},
	
	createStandardSubmitButton:function()
	{
		var btn=
		{
			text: 'Submit',
			formBind: true,
			scope: this,
			handler: this.standardSubmitHandler
		};
		return btn;
	},
	
	createExampleButton:function()
	{
		var btn=
		{
			text: 'Example',
			formBind: true,
			scope: this,
			handler: this.showExampleHandler
		};
		return btn;
	},
	
	checkValidation:function()
	{
		return true;
	},
	
	standardSubmitHandler:function()
	{
		if (!this.checkValidation())
			{return;}
		var form=this.getForm().getEl().dom;
		form.action=this.url;
		form.method='post';
		form.submit();
	},
	
	resetHandler:function()
	{
		this.getForm().reset();
	},
	
	showExampleHandler:function()
	{
		// do nothing - override to add functionality
	},
	
	checkNotEmpty:function(fieldnames,message)
	{
		var arr=fieldnames.split(',');
		var index, fieldname;
		for (index=0;index<arr.length;index++)
		{
			fieldname=arr[index];
			if (this.getForm().findField(fieldname).getValue().trim()!=='')
				{return true;}
		}
		Ext.MessageBox.alert('Field validation',message);
		return false;
	}
});