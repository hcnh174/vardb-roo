/*global Ext, nelson, vardb */
vardb.controls.FeedbackForm = Ext.extend(Ext.form.FormPanel,
{	
	title: 'Please use the form below to send your comments or suggestions to varDB. ',
	labelWidth: 75,
	frame: true,
	width: 550,
	bodyStyle: 'padding: 5px 5px 0',
	defaultType: 'textfield',
	monitorValid: true,
	iconCls: 'icon-email',

	initComponent:function()
	{
		var purposeCombo=new nelson.extjs.SelectList(
		{
			data: [['COMMENT','Comment'],['QUESTION','Question'],['CORRECTION','Correction'],['SUBMISSION','Submission']],
			name: 'purpose',
			fieldLabel: 'Purpose',
			value: this.purpose
		});
		
		var config=
		{
			defaults: {width: 350, allowBlank: true},	
			items:
			[				
				{
					fieldLabel: 'Name',
					name: 'name',
					value: this.name
				},
				{
					fieldLabel: 'Affiliation',
					name: 'affiliation',
					value: this.affiliation
				},
				{
					fieldLabel: 'Email*',
					name: 'email',
					vtype: 'email',
					allowBlank: false,
					value: this.email
				},
				purposeCombo,
				{
					fieldLabel: 'Comments*',
					name: 'comments',
					xtype: 'textarea',
					allowBlank: false,
					grow: true		
				}
			],
			buttons:
			[
				{
					text: 'Submit',
					formBind: true,
					scope: this,
					handler: this.submitHandler
				},
				{
					text: 'Reset',
					scope: this,
					handler: function(){this.getForm().reset();}
				}			
			]
		};
		
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		vardb.controls.FeedbackForm.superclass.initComponent.apply(this, arguments);
		//form.getForm().findField('name').focus();
	},
	
	submitHandler:function()
	{
		if (!this.getForm().isValid())
			{return;}
		this.getForm().getEl().dom.action=utils.webapp+'/contact.html';
		this.getForm().getEl().dom.method='post';
		this.getForm().getEl().dom.submit();
	}
});

