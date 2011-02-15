/*global Ext, vardb */
vardb.analysis.AlignmentViewerForm = Ext.extend(vardb.util.AbstractForm,
{	
	title: 'Inspect alignments using a simple browser-based alignment viewer',
	width: 580,
	labelWidth: 50,
	standardSubmit: true,
	fileUpload: true,
	url: util.webapp+'/alignments/view.html',
	
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
					this.createFileUploadControl({name: 'file', fieldLabel: 'File', emptyText: 'Or upload an alignment file (FASTA or ClustalW)'})
				])
			],
			buttons:
			[
				this.createClearButton(),
				this.createStandardSubmitButton()
			]
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		vardb.analysis.AlignmentViewerForm.superclass.initComponent.apply(this, arguments);
	},
	
	checkValidation:function()
	{
		if (!this.checkNotEmpty('sequences,file,alignmentIdentifier','Please enter an alignment or select a file to upload'))
			{return false;}
		return true;
	}
	
	/*
	checkValidation:function()
	{	
		if (this.getForm().findField('sequences').getValue().trim()==='' && 
				this.getForm().findField('file').getValue().trim()==='')
		{
			Ext.MessageBox.alert('Incomplete','Please enter an alignment or select a file to upload');
			return false;
		}
		return true;
	}
	*/
});