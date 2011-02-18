/*global Ext, vardb */
Ext.define('vardb.controls.NewUserForm',
{
	extend: 'Ext.form.FormPanel',
	title: 'User information',
	labelWidth: 75,
	//url: utils.webapp+'/newuser.html',
	frame: true,
	width: 550,
	bodyStyle: 'padding: 5px 5px 0',
	defaultType: 'textfield',
	monitorValid: true,

	initComponent:function()
	{
		var config=
		{
			//initialConfig: {standardSubmit: true},
			paramsAsHash: true,
			fieldDefaults: {width: 300, allowBlank: false},
			items:
			[
				{
					fieldLabel: 'Username*',
					name: 'username'
					//plugins: [Ext.ux.plugins.RemoteValidator],
					//rvOptions: {url: vardb.webapp+'/ajax/validate/user.json'}
				},
				{
					fieldLabel:'Password*',
					name: 'password1',
					inputType: 'password'
				},
				{
					fieldLabel: 'Password (retype)*',
					name: 'password2',
					inputType: 'password'		
				},
				{
					fieldLabel: 'First name',
					name: 'firstname',
					allowBlank: true
				},
				{
					fieldLabel: 'Last name',
					name: 'lastname',
					allowBlank: true
				},
				{
					fieldLabel: 'Affiliation',
					name: 'affiliation',
					allowBlank: true
				},
				{
					fieldLabel: 'Email*',
					name: 'email',
					vtype: 'email'
				},
				{
					fieldLabel: 'Email (retype)*',
					name: 'email2',
					vtype: 'email'
				}
			],
			buttons:
			[
				{
					text: 'Submit',
					//formBind: true,
					scope: this,
					handler: this.submitHandler
				},
				{
					text: 'Reset',
					//formBind: true,
					scope: this,
					handler: function(){this.getForm().reset();}
				}			
			],
			initialConfig:
			{
				api:
				{
					load: vardbDirect.getNewUserForm,
					submit: vardbDirect.postNewUserForm
				}
			}
		};
		
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		vardb.controls.NewUserForm.superclass.initComponent.apply(this, arguments);
	},
	
	onRender:function()
	{
		vardb.controls.NewUserForm.superclass.onRender.apply(this, arguments);
		//this.on('afterlayout', function(){this.getForm().findField('username').focus();},this);
		this.getForm().load();
		this.getForm().findField('username').focus();
	},
	
	submitHandler:function()
	{
		var form=this.getForm();
		
		form.getFields().each(function(field) {
			field.validate();
		});
		if(!form.isValid())
			{return;}
		
		var password1=form.findField('password1').getValue();
		var password2=form.findField('password2').getValue();
		if (password1!==password2)
		{
			Ext.MessageBox.alert('Alert','Passwords do not match.');
			form.findField('password1').setValue('');
			form.findField('password2').setValue('');
			return;
		}
		// validate email.
		var email1=form.findField('email').getValue();
		var email2=form.findField('email2').getValue();
		if (email1!==email2)
		{
			Ext.MessageBox.alert('Alert','Emails do not match.');
			form.findField('email').setValue('');
			form.findField('email2').setValue('');
			return;
		}
		form.submit();
		/*
		form.getEl().dom.action=utils.webapp+'/newuser.html';
		form.getEl().dom.method='post';
		form.getEl().dom.submit();
		*/
	}
});
