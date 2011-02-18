/*global Ext, vardb */
Ext.define('vardb.popups.TermPopup',
{
	extend: 'Ext.window.Window',
	layout: 'fit',
	width: 400,
	height: 250,
	plain: true,
	title: 'Definition',
	
	initComponent:function()
	{
		var self=this;
		var config=
		{
			items: new Ext.tab.TabPanel(
			{
				activeTab: 0,
				deferredRender: false,
				border: false,
				items:
				[
					{
						title: this.term.term,
						html: this.term.definition,
						autoScroll: true
					}
				]
			}),
	
			buttons:
			[
				{
					text: 'Close',
					handler: function(){self.hide();}
				}
			]
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		vardb.popups.TermPopup.superclass.initComponent.apply(this, arguments);
		this.show();
	}
});
