/*global Ext, vardb */
vardb.analysis.MafftForm = Ext.extend(vardb.util.AbstractForm,
{
	title: 'Align sequences using MAFFT',
	width: 600,
	standardSubmit: true,
	fileUpload: true,
	url: utils.webapp+'/mafft.html',
	
	strategy: 'AUTO',
	scorematrix: 'BLOSUM45',
	op: '2.0',
	ep: '0.0',

	initComponent:function()
	{
		var config=
		{
			defaults: {allowBlank: false},
			initialConfig:
			{
				standardSubmit: true,
				fileUpload: true
			},
			items:
			[
				this.paramFieldset(),
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
		vardb.analysis.MafftForm.superclass.initComponent.apply(this, arguments);
	},
	
	paramFieldset:function()
	{
		var strategies=[];
		strategies.push(['AUTO','Auto (Moderately accurate)']);
		strategies.push(['FFT_NS_1','FFT-NS-1 (Very fast)']);
		strategies.push(['FFT_NS_2','FFT-NS-2 (Fast; progressive)']);
		strategies.push(['FFT_NS_i2','FFT-NS-i2 (Medium; iterative)']);	
		strategies.push(['FFT_NS_i','FFT-NS-i (Slow)']);
		strategies.push(['L_INS_i','L-INS-i (Very slow)']);
		strategies.push(['E_INS_i','E-INS-i (Very slow)']);
		
		return this.createFieldset({title: 'Parameters'},
		[
			this.createRow(
			[
				this.createTextControl({name: 'name', fieldLabel: 'Title', emptyText: 'User alignment', width: 400})
			]),
			this.createRow(
			[
				this.createSelectList(
				{
					data: strategies,
					hiddenName: 'strategy',
					fieldLabel: 'Strategy',
					value: this.strategy,
					width: 180
				}),
				this.createSelectList(
				{
					data: 'BLOSUM30,BLOSUM45,BLOSUM62,BLOSUM80,JTT100,JTT200',
					hiddenName: 'matrix',
					fieldLabel: 'Score matrix',
					value: this.scorematrix,
					width: 90
				})
			]),
			this.createRow(
			[
				this.createSelectList(
				{
					hiddenName: 'sequenceType',
					fieldLabel: 'Sequence type',
					data: [['AA','Protein alignment'],['NT','Nucleotide alignment']],
					value: 'AA'			
				})
			]),
			this.createRow(
			[
				this.createTextControl({name: 'op', fieldLabel: 'Gap open', value: this.op,  width: 50}),
				this.createTextControl({name: 'ep', fieldLabel: 'Gap extension', value: this.ep,  width: 50})
			])
		]);
	},
	
	msaFieldset:function()
	{
		return this.createFieldset({title: 'Sequences to align'},
		[
			this.createRow(
			[
				this.createTextAreaControl({name: 'sequences', value: this.sequences, fieldLabel: 'Enter sequences in FASTA format'})
			]),
			this.createRow(
			[
				this.createFileUploadControl({name: 'file', fieldLabel: 'File', emptyText: 'Upload a FASTA sequence file'})
			])
		]);
	},
	
	checkValidation:function()
	{
		if (!this.checkNotEmpty('sequences,file','Please enter FASTA-formatted sequences or select a file to upload'))
			{return false;}
		return true;
	},
	
	showExampleHandler:function()
	{
		var form=this.getForm();
		vardb.ajaxRequest('/demo/mafft.json',{},function(json)
		{
			form.findField('sequences').setValue(json.sequences);
		});
	}
});
