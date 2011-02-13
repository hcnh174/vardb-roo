/*global Ext, vardb */
vardb.analysis.VariabilityForm = Ext.extend(vardb.util.AbstractForm,
{	
	title: 'Variability',
	width: 580,
	bodyStyle: 'padding: 10px 10px 0 10px;',
	labelWidth: 30,
	standardSubmit: true,
	fileUpload: true,
	url: utils.webapp+'/analysis/variability.html',
	
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
					this.createControl(new vardb.UserAlignmentSelectList({hiddenName: 'alignmentIdentifier'}))
				]),
				this.createRow(
				[
					this.createTextAreaControl({name: 'sequences', fieldLabel: 'Enter a multiple sequence alignment in FASTA or CLUSTALW format'})
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
		vardb.analysis.VariabilityForm.superclass.initComponent.apply(this, arguments);
	},
	
	checkValidation:function()
	{
		if (!this.checkNotEmpty('sequences,file,alignmentIdentifier','Please enter an alignment or select a file to upload'))
			{return false;}
		return true;
	},
	
	showExampleHandler:function()
	{
		var form=this.getForm();
		vardb.ajaxRequest('/demo/variability.json',{},function(json)
		{
			form.findField('sequences').setValue(json.sequences);
		});
	}
});