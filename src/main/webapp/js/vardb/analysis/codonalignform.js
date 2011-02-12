/*global Ext, vardb */
Ext.ux.vardb.AbstractCodonAlignForm = Ext.extend(Ext.ux.vardb.AbstractForm,
{	
	width: 580,
	standardSubmit: true,
	fileUpload: true,
	
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
				this.createNucleotideFieldset(),	
				this.createProteinFieldset()							
			],
			buttons:
			[
				this.createExampleButton(),
				this.createClearButton(),
				this.createStandardSubmitButton()
			]
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		Ext.ux.vardb.AbstractCodonAlignForm.superclass.initComponent.apply(this, arguments);
	},
	
	createNucleotideFieldset:function()
	{
		return this.createFieldset({title: 'Nucleotide sequences'},
		[
			this.createRow(
			[
				this.createTextAreaControl({name: 'ntsequences', fieldLabel: 'Enter unaligned nucleotide sequences in FASTA format'})
			]),
			this.createRow(
			[
				this.createFileUploadControl({name: 'ntfile', fieldLabel: 'File'})
			])
		]);
	},
	
	createProteinFieldset:function()
	{
		return this.createFieldset({title: 'Protein sequences'},
		[
			//emptyText: 'Or upload an alignment file (FASTA or ClustalW)',
			/*
			this.createRow(
			[
				this.createControl(new Ext.ux.vardb.UserAlignmentSelectList({hiddenName: 'alignmentIdentifier'}))
			]),
			*/
			this.createRow(
			[
				this.createTextAreaControl({name: 'aasequences', fieldLabel: 'Enter a multiple sequence alignment in FASTA or CLUSTALW format'})
			]),
			this.createRow(
			[
				this.createFileUploadControl({name: 'aafile', fieldLabel: 'File'})
			])
		]);
	},
	
	checkValidation:function()
	{
		//alignmentIdentifier
		if (!this.checkNotEmpty('aasequences,aafile','Please enter a protien alignment or select a file to upload'))
		{return false;}
		if (!this.checkNotEmpty('ntsequences,ntfile','Please enter nucleotide sequences or select a file to upload'))
			{return false;}
		return true;
	},
	
	showExampleHandler:function()
	{
		var form=this.getForm();
		vardb.ajaxRequest('/demo/codonalign.json',{},function(json)
		{
			form.findField('ntsequences').setValue(json.ntsequences);
			form.findField('aasequences').setValue(json.aasequences);					
		});
	}
});

Ext.ux.vardb.Pal2NalForm = Ext.extend(Ext.ux.vardb.AbstractCodonAlignForm,
{	
	title: 'PAL2NAL',
	url: vardb.webapp+'/analysis/pal2nal.html'
});

Ext.ux.vardb.RevTransForm = Ext.extend(Ext.ux.vardb.AbstractCodonAlignForm,
{	
	title: 'RevTrans',
	url: vardb.webapp+'/analysis/revtrans.html'
});

Ext.ux.vardb.CodonAlignForm = Ext.extend(Ext.TabPanel,
{	
	activeTab: 0,
	width: 600,
	frame: true,
	deferredRender: true,

	initComponent:function()
	{
		var data=this.data;
		var config=
		{
			defaults: {autoHeight: true},
			items:
			[
				new Ext.ux.vardb.RevTransForm({}),
				new Ext.ux.vardb.Pal2NalForm({})
			]
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		Ext.ux.vardb.CodonAlignForm.superclass.initComponent.apply(this, arguments);
	}
});