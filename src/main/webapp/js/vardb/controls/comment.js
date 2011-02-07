/*global Ext, vardb, utils */
vardb.controls.CommentWindow = Ext.extend(Ext.Window,
{
	title: 'Enter a comment',
	//width: 500,
	closable: true,
	resizable: true,
	bodyStyle: 'padding: 0',
	layout: 'fit',
	iconCls: 'icon-user_comment',

	initComponent:function()
	{
		var commentField=
		{
			xtype: 'htmleditor',
			id: 'comment-window-text-htmleditor',
			hideLabel: true,
			name: 'text',
			anchor: '0',
			enableAlignments: false
		};
		
		var form=new Ext.form.FormPanel(
		{
			collapsible: false,
			frame: false,
			width: 500,
			height: 150,
			bodyStyle: 'padding: 0',
			items: [commentField],
			buttons:
			[
				{
					id: 'vardb-comment-window-clear-btn',
					text: 'Clear',
					formBind: true,
					scope: this,
					handler: function(){this.form.getForm().reset();}
				},
				{
					id: 'vardb-comment-window-submit-btn',
					text: 'Submit',
					formBind: true,
					scope: this,
					handler: this.submitComment
				}
			]
		});
		var config=
		{
			items: form
		};
		
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		vardb.controls.CommentWindow.superclass.initComponent.apply(this, arguments);
		this.form=form;
		this.show();
	},
	
	submitComment:function()
	{
		var self=this;
		// get rid of the annoying extra character
		var value=this.form.getForm().findField('text').getValue().trim();
		this.form.getForm().findField('text').setValue(value);
		this.form.getForm().submit(
		{
			method: 'post',
			waitTitle: 'Connecting',
			waitMsg: 'Submitting comment...',
			url : utils.webapp+'/ajax/comments/submit.json',
			params : {type: self.type, identifier: self.identifier},
			failure: utils.onFailure,
			success: function(form,action)
			{
				var json=Ext.decode(action.response.responseText);
				if (self.callback)
					{self.callback();}
				self.close();
				Ext.MessageBox.alert('Success', 'Your comment was successfully submitted');
			}			
		});
	}
});
