/*global Ext, vardb */
vardb.analysis.BlastClustForm = Ext.extend(vardb.util.AbstractForm,
{
	title: 'BLASTclust',
	width: 580,
	labelWidth: 35,
	url: utils.webapp+'/analysis/blastclust.html',
	
	initComponent:function()
	{
		var self=this;		
		var config=
		{
			defaults: {allowBlank: false},
			initialConfig: {standardSubmit: true, fileUpload: true},
			monitorValid: true,
			items:
			[
				this.createRow(
				[
					this.createTextControl({name: 'name', fieldLabel: 'Title', value: 'BLASTClust results', anchor: '-4'},{width: '100%'})
				]),
				this.createRow(
				[				 
					this.createSelectList(
					{
						data: [['true','Protein'],['false','DNA']],
						hiddenName: 'protein',
						fieldLabel: 'Type',
						value: ''+this.protein,
						width: 100
					}),
					this.createTextControl({name: 'minimumLength', fieldLabel: 'Minimum length', value: this.minimumLength, width: 50}),
					this.createTextControl({name: 'similarityThreshold', fieldLabel: 'Similary Threshold', value: this.similarityThreshold, width: 50})
				]),
				this.createRow(
				[
					this.createTextAreaControl({name: 'sequences', fieldLabel: 'Enter sequences in FASTA format'})
				]),
				this.createRow(
				[
					this.createFileUploadControl({name: 'file', fieldLabel: 'File', emptyText: 'Or upload an a FASTA-formatted sequence file'})
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
		vardb.analysis.BlastClustForm.superclass.initComponent.apply(this, arguments);		
	},
	
	checkValidation:function()
	{
		if (!this.checkNotEmpty('sequences,file','Please enter sequences or select a file to upload'))
			{return false;}
		return true;
	},
	
	showExampleHandler:function()
	{
		var form=this.getForm();
		vardb.ajaxRequest('/demo/blastclust.json',{},function(json)
		{
			form.findField('sequences').setValue(json.sequences);
		});
	}
});
