/*global Ext, vardb */
Ext.define('vardb.controls.Comments',
{
	extend: 'Ext.panel.Panel',
	width: '600',
	autoScroll: true,
	pagesize: 5,
	iconCls: 'icon-user_comment',
	
	initComponent:function()
	{
		var self=this;
		if (!this.type)
			{throw 'vardb.controls.Comments: type not set';}
		if (!this.identifier)
			{throw 'vardb.controls.Comments: identifier not set';}

			/*
		var store = new Ext.data.DirectStore( {
			id: 'commentstore',
			paramsAsHash: true,
			root: 'records',
			totalProperty: 'total',
			remoteSort: true,
			directFn: vardbDirect.loadComments,
			fields: ['id', 'accession', 'strain', 'description']
		});
		store.load({params:{start: 0, limit: 10}});
		*/
		

		Ext.regModel('comment', {
		    fields:
			[
				{name: 'id', type: 'int'},
				{name: 'username'},
				{name: 'type'},
				{name: 'identifier'},
				{name: 'text'},
				{name: 'date'}
			]
		});
		
		var store=new Ext.data.Store({
		    model: 'comment',
		    proxy: {
		        type: 'ajax',
		        url : 'ajax/comments.json',
		        reader: 'json',
		        extraParams: {type: this.type, identifier: this.identifier}
		    },
		    autoLoad: true		    
		});
		
		/*
		var reader = new Ext.data.JsonReader(
		{
			root: 'comments',
			totalProperty: 'totalCount',
			idProperty: 'id',
			fields:
			[
				{name: 'id', type: 'int'},
				{name: 'username'},
				{name: 'type'},
				{name: 'identifier'},
				{name: 'text'},
				{name: 'date'}
			]
		});
		
		var store=new Ext.data.Store(
		{
			url: utils.webapp+'/ajax/comments.json',
			reader: reader,
			remoteSort: true,
			sortInfo: {field: 'date', direction: 'DESC'},
			baseParams: {type: this.type, identifier: this.identifier}
		});
		*/
		
		// Custom rendering Template for the View
		var resultTpl = new Ext.XTemplate('' +
				'<tpl for=".">' +
				'<div class="comment-item">' +
				'<tpl if="username!=\'system\'">' +
				'<h3><span>{date:date("M j, Y, g:i A")}<br /></span><a href="javascript:void(0)">{username} wrote:</a></h3>' +
				'</tpl>' +
				'<p>{text}</p>' +
				'</div>' +
				'</tpl>');

		var addButton=new Ext.Button(
		{
			id: 'vardb-comments-submit-btn',
			text: 'Submit a comment',
			scope: this,
			handler: this.submitHandler
		});
		
		var config=
		{
			items: new Ext.DataView({
				tpl: resultTpl,
				store: store,
				itemSelector: 'div.comment-item',
				//emptyText: '<div class="emptyText">No comments have been submitted for this item.</div>'
				emptyText: '<div class="comment-item"><p style="margin-top:5px;margin-bottom:5px;">No comments have been submitted for this item.</p></div>'
			}),
			tbar: new Ext.Toolbar(
			{
				items:
				[
					'<span class="comments-heading">User comments</span>',
					'->','-',
					addButton
				]
			}),
			bbar: new Ext.PagingToolbar(
			{				
				pageSize: this.pagesize,
				store: store,
				displayInfo: true,
				displayMsg: '{0} - {1} of {2}',
				emptyMsg: ''//No comments
			})
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		vardb.controls.Comments.superclass.initComponent.apply(this, arguments);
		
		store.load({params: {start: 0, limit: this.pagesize}});
		this.store=store;
	},
	
	submitHandler:function()
	{
		var self=this;
		var win=new vardb.controls.CommentWindow(
		{
			type: self.type,
			identifier: self.identifier,
			callback: function()
			{
				self.store.load({params: {start: 0, limit: self.pagesize}});
				//self.store.reload();
			}
		});
	}
});	
