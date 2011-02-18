/*global Ext, nelson, vardb */
Ext.define('vardb.controls.Announcements',
{
	extend: 'Ext.grid.GridPanel',
	title: 'Announcements',
	enableHdMenu: false,
	hideHeaders: true,
	stripeRows: true,
	frame: true,
	height: 200, //145,
	width: '95%',
	loadingText: 'Loading announcements...',
	emptyText: 'No new announcements',
	iconCls: 'icon-rss',
	
	initComponent:function()
	{
		/*
		var fields=
		[
			{name: 'title'},
			{name: 'author'},
			{name: 'pubDate', type:'date'},
			{name: 'link'},
			{name: 'description'},
			{name: 'content'}
		];
		
		var expander = new Ext.ux.RowExpander({
	        rowBodyTpl : new Ext.Template(
	            '<p>{description}</p>'
	        )
	    });
		*/
		
		Ext.regModel('announcement', {
		    fields:
			[
				{name: 'title'},
				{name: 'author'},
				{name: 'pubDate', type:'date'},
				{name: 'link'},
				{name: 'description'},
				{name: 'content'}
			]
		});
		
		/*
		var proxy = new Ext.data.AjaxProxy({
		    model: 'User',
		    reader: {
		        type: 'xml',
		        root: 'users'
		    }
		});
		*/
		
		var store=new Ext.data.Store({
		    model: 'announcement',
		    proxy: {
		        type: 'ajax',
		        url : 'ajax/announcements.xml',
		        reader: {
			        type: 'xml',
			        root: 'item'
			    }
		    },
		    autoLoad: true
		});
		
		
		
		/*
		var store = new Ext.data.Store({
			url: 'ajax/announcements.xml',
			reader: new Ext.data.XmlReader({record: 'item'},fields),
			sortInfo: {field: 'pubDate', direction: 'DESC'}
		});
		*/
		
		/*
		var expander = new Ext.ux.RowExpander({
	        rowBodyTpl : new Ext.Template(
	            '<p>{description}</p>'
	        )
	    });
		*/
		
		/*
		store.on('load', function(){
			expander.expandRow(0);
		});
		*/
		store.load();

		var config=
		{
			store: store,
			viewConfig: {forceFit: true},
			columns:
			[
				//expander,
				{header: "Title", sortable: true, dataIndex: 'title', width: 250, renderer: this.renderTitle},
				{header: "Date", dataIndex: 'pubDate', width: 50, renderer: Ext.util.Format.dateRenderer('M j, Y')},
				{header: "Link", dataIndex: 'link', width: 20, renderer: this.renderLink}
			],
			/*
			plugins:
			[
				{
		            ptype: 'rowexpander',
		            rowBodyTpl : '<p>{description}</p>'
        		}
        	],
        	*/
			bbar: new Ext.PagingToolbar(
			{
				pageSize: 1,
				store: store,
				displayInfo: true,
				displayMsg: 'Displaying news {0} - {1} of {2}',
				emptyMsg: "No news to display"
			})
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		vardb.controls.Announcements.superclass.initComponent.apply(this, arguments);
	},
	
	renderTitle:function(value, p, record)
	{
		return '<span style=\'color: #15428b; font:bold 11px tahoma,arial,sans-serif;\'>'+ record.data.title +'</span>';
	},

	renderLink:function(value, p, record)
	{
		var link = '' + value;
		if(link.length > 0)
			{return '<a href=\''+link+'\'>Link</a>';}
		return link;
	}
});
