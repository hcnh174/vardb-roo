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
			vardbDirect.getTerm(identifier, function(result,evt)
			{
				var popup=new vardb.popups.TermPopup({term: result});
		    });
		},
		
		testFeature:function()
		{
			//new vardb.graphics.Alignment({data: alignmentdata});
			new vardb.graphics.Sequence({data: {}});
			//new vardb.graphics.Location('1..5382,6287..7528',400);
		}
	}
});
