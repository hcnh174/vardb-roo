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
			//new vardb.controls.NewUserForm({renderTo: Ext.getBody()});
			
			//alert('testing');
			/*
			var drawing = Ext.create('vardb.graphics.Demo',
			{
				width: 400,
				height: 400,			
				padding: 10,
				draggable: {
					constrain: true
				},
				floating: true,
				renderTo: Ext.getBody(),
				items: [{
				    type: "path",
					path: "M-122.304 84.285C-122.304 84.285 -122.203 86.179 -123.027 86.16C-123.851 86.141 -140.305 38.066 -160.833 40.309C-160.833 40.309 -143.05 32.956 -122.304 84.285z","stroke-width":"0.172",
					stroke:"#000",
					fill:"#fff"
				}]
			});
			*/
		}
	}
});


