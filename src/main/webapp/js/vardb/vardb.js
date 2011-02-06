/*global Ext, nelson, utils, alert, VardbDirect */
(vardb.Vardb=function()
{	
	var webapp=utils.webapp;
	
	// init code
	Ext.BLANK_IMAGE_URL = webapp+'/images/extjs/s.gif';
	Ext.ux.GridPrinter.stylesheetPath=webapp+'/css/printgrid.css';
	Ext.Ajax.timeout=600000;
	
	return {
	
	//webapp: utils.webapp,
	chartswf: this.webapp+'/images/extjs/charts.swf',
	
	onReady:function()
	{
		Ext.QuickTips.init();
		utils.createSpinner();
		Ext.ux.Lightbox.register('a[rel^=lightbox]');
		Ext.Direct.addProvider(VardbDirect);
	},
	
	getTerm:function(identifier)
	{
		VardbDirect.term(identifier, function(provider, response)
		{
			var popup=new nelson.vardb.popups.TermPopup({term: response.result});
	    });
	}
};}());

