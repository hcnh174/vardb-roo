/*global Ext, vardb */
vardb.analysis.SelectonForm = Ext.extend(vardb.util.AbstractForm,
{	
	title: 'Selecton',
	width: 580,
	bodyStyle: 'padding: 10px 10px 0 10px;',
	labelWidth: 30,
	standardSubmit: true,
	fileUpload: true,
	url: utils.webapp+'/analysis/selecton.html',
	
	initComponent:function()
	{	
		var config=
		{
			initialConfig:
			{
				standardSubmit: true,
				fileUpload: true
			},
			items:
			[
				this.createRow(
				[
					this.createTextAreaControl({name: 'sequences', fieldLabel: 'Enter a codon alignment in FASTA or CLUSTALW format'})
				]),
				this.createRow(
				[
					this.createFileUploadControl({name: 'file', fieldLabel: 'File', emptyText: 'Upload an alignment file'})
				])
			],
			buttons:
			[
				this.createExampleButton(),
				this.createClearButton(),
				this.createStandardSubmitButton()
			]
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		vardb.analysis.SelectonForm.superclass.initComponent.apply(this, arguments);
	},
	
	checkValidation:function()
	{
		if (!this.checkNotEmpty('sequences,file','Please enter an alignment or select a file to upload'))
			{return false;}
		return true;
	},
	
	showExampleHandler:function()
	{
		var form=this.getForm();
		vardb.ajaxRequest('/demo/selecton.json',{},function(json)
		{
			form.findField('sequences').setValue(json.sequences);
		});
	}
});