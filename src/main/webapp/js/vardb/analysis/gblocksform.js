/*global Ext, vardb */
vardb.analysis.GblocksForm = Ext.extend(vardb.util.AbstractForm,
{	
	title: 'Gblocks',
	width: 580,
	bodyStyle: 'padding: 10px 10px 0 10px;',
	labelWidth: 50,
	standardSubmit: true,
	fileUpload: true,
	url: utils.webapp+'/analysis/gblocks.html',	
	
	initComponent:function()
	{
		this.data=this.data || {};
		Ext.applyIf(this.data,
		{
			gaps: 'HALF',
			type: 'PROTEIN',
			minBlock: 10,
			minConserved: 0.5,
			minFlanking: 0.5,
			maxNonconserved: 8
		});
	
		var config=
		{
			initialConfig:
			{
				standardSubmit: true,
				fileUpload: true
			},
			items:
			[
				this.gblocksFieldset(),
				this.msaFieldset()
			],
			buttons:
			[
				this.createExampleButton(),
				this.createClearButton(),
				this.createStandardSubmitButton()
			]
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		vardb.analysis.GblocksForm.superclass.initComponent.apply(this, arguments);
	},

	gblocksFieldset:function()
	{
		return this.createFieldset({title: 'Parameters'},
		[	
			this.createRow(
			[
				this.createTextControl({name: 'minBlock', fieldLabel: 'Min block', width: 30}),//, helpText: 'Minimum length of a block'}),
				this.createSelectList(
				{
					data: [['NONE','None'],['HALF','Half'],['ALL','All']],
					hiddenName: 'gaps',
					fieldLabel: 'Allowed gaps',
					width: 80
				}),
				this.createSelectList(
				{
					data: [['PROTEIN','Protein'],['DNA','DNA'],['CODONS','Codons']],
					hiddenName: 'type',
					fieldLabel: 'Type',
					width: 80
				})
			]),
			this.createRow(
			[
				this.createTextControl({name: 'minConserved', fieldLabel: 'Min conserved', width: 30}),//, helpText: 'Minimum number of sequences for a conserved position'}),				
				this.createTextControl({name: 'minFlanking', fieldLabel: 'Min flanking', width: 30}),//, helpText: 'Minimum number of sequences for a flank position'}),
				this.createTextControl({name: 'maxNonconserved', fieldLabel: 'Max non-conserved', width: 30})//, helpText: 'Maximum number of contiguous nonconserved positions'})
			])
		]);
	},
	
	msaFieldset:function()
	{
		return this.createFieldset({title: 'Multiple sequence alignment'},
		[
			this.createRow(
			[
				this.createControl(new vardb.UserAlignmentSelectList())
			]),
			this.createRow(
			[
				this.createTextAreaControl({name: 'sequences', fieldLabel: 'Enter a multiple sequence alignment in FASTA or CLUSTALW format'})
			]),
			this.createRow(
			[
				this.createFileUploadControl({name: 'file', fieldLabel: 'File'})
			])
		]);
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
		vardb.ajaxRequest('/demo/gblocks.json',{},function(json)
		{
			form.findField('sequences').setValue(json.sequences);
		});
	}
});