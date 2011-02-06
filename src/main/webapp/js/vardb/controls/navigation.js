/*global Ext, nelson, vardb */
vardb.controls.Navigation = Ext.extend(Ext.util.Observable,
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
					'<a href="{utils.webapp}/{href}" title="{tooltip}">{[this.replaceSpaces(values.label)]}</a>',
					'</li>',
				'</tpl>',
				'</ul>',
			'</tpl>',
			'<h5>&nbsp;</h5>',
			{
				cur: config.cur,
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
