/*global Ext, nelson, utils, window */
vardb.controls.Login = Ext.extend(Ext.form.FormPanel,
{
	title: 'Login',
	labelWidth: 100,
	url: utils.webapp+'/j_spring_security_check',
	frame: false, //true
	width: 300,
	bodyStyle: 'padding:5px 5px 0',
	defaultType: 'textfield',
	monitorValid: false,
	iconCls: 'login-icon',
	
	initComponent:function()
	{
		var self=this;
		var config=
		{
			items:
			[
				{
					fieldLabel: 'Username',
					name: 'j_username',
					allowBlank: false,
					value: this.username
				},
				{
					fieldLabel: 'Password',
					name: 'j_password',
					allowBlank: false,
					inputType: 'password',
					listeners:
					{
						specialkey: function(field,e)
						{
							if (e.getKey()===e.ENTER)
								{self.submitForm();}
						}
					}
				},
				{
					fieldLabel: 'Remember me',
					name: '_spring_security_remember_me',
					xtype: 'checkbox'
				}
			],			
			buttons:
			[
				{
					text: 'Login',
					formBind: true,
					scope: this,
					handler: this.submitForm
				}
			]
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		vardb.controls.Login.superclass.initComponent.apply(this, arguments);
	},
	
	onRender:function()
	{
		nelson.users.Login.superclass.onRender.apply(this, arguments);
		this.on('afterlayout', function()
		{
			if (this.username)
				{this.getForm().findField('j_password').focus();}
			else {this.getForm().findField('j_username').focus();}	
		},this);
	},
	
	submitForm:function()
	{
		var self=this;
		//if (!this.isValid())
		//	{return;}
		this.getForm().submit(
		{
			method: 'post',
			waitTitle: 'Connecting',
			waitMsg: 'Sending data...',
			url: utils.webapp+'/j_spring_security_check',
			success: function(form,action)
			{
				var json=Ext.decode(action.response.responseText);
				var redirect=(!json.redirect) ? utils.webapp+'/index.html' : json.redirect; 
				window.location=redirect;
			},
			failure: function(form,action)
			{
				var json=Ext.decode(action.response.responseText);
				Ext.MessageBox.alert('Failed',json.message);
				self.getForm().reset();
			}
		});
	}
});
