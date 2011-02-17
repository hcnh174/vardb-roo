/*global Ext, vardb */
Ext.define('vardb.controls.Navigation',
{
	constructor: function(config)
	{
		Ext.applyIf(config,
		{
			cur: 'none'
		});
		var tpl = new Ext.XTemplate(
			'<tpl for="sections">',
				'<h5>{label}</h5>',
				'<ul>',
				'<tpl for="links">',
					'<li <tpl if="name==this.cur">class="current"</tpl>>',
					'<a href="{webapp}/{href}" title="{tooltip}">{[this.replaceSpaces(values.label)]}</a>',
					'</li>',
				'</tpl>',
				'</ul>',
			'</tpl>',
			'<h5>&nbsp;</h5>',
			{
				cur: config.cur,
				webapp: utils.webapp,
				replaceSpaces: function(value)
				{
					return value.split(' ').join('&nbsp;');
				}
			}
		);
		if (Ext.get(config.renderTo)!==null && config.data!==null)
			{tpl.overwrite(config.renderTo, config.data);}
		vardb.controls.Navigation.superclass.constructor.call(this, config);
	}
});
