/*global Ext, utils, alert, VardbDirect */
Ext.define('vardb.Vardb',
{	
	// init code
	//Ext.BLANK_IMAGE_URL = webapp+'/images/extjs/s.gif';
	//Ext.ux.GridPrinter.stylesheetPath=webapp+'/css/printgrid.css';
	//Ext.Ajax.timeout=600000;
	
	statics:
	{
		onReady:function()
		{
			//Ext.QuickTips.init();
			//utils.createSpinner();
			//Ext.ux.Lightbox.register('a[rel^=lightbox]');
			//Ext.Direct.addProvider(VardbDirect);
		},
		
		getTerm:function(identifier)
		{
			VardbDirect.term(identifier, function(result,evt)
			{
				alert(result);
				//var popup=new vardb.popups.TermPopup({term: response.result});
		    });
		}
	}
});


