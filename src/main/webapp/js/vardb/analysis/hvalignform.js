/*global Ext, vardb */
vardb.analysis.HvAlignForm = Ext.extend(vardb.util.AbstractForm,
{	
	title: 'Align protein hypervariable regions',
	width: 580,
	labelWidth: 50,
	standardSubmit: true,
	fileUpload: true,
	url: utils.webapp+'/analysis/hvalign.html',
	
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
				this.gblocksFieldset(),
				this.mafftFieldset(),
				this.blastclustFieldset(),
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
		vardb.analysis.HvAlignForm.superclass.initComponent.apply(this, arguments);
	},
	
	gblocksFieldset:function()
	{
		var gblocks_gaps=new vardb.SelectList(
		{
			data: [['NONE','None'],['HALF','With half'],['ALL','All']],
			hiddenName: 'gblocks_gaps',
			fieldLabel: 'Allowed gap positions',
			width: 100,
			value: this.gblocks_gaps,
			anchor: '-4',
			editable: false,
			typeAhead: true
		});
				
		var gblocks_minBlock=
		{
			xtype: 'textfield',
			fieldLabel: 'Min block',
			name: 'gblocks_minBlock',
			value: this.gblocks_minBlock,
			width: 30
		};
			
		var gblocks_minConserved=
		{
			xtype: 'textfield',
			fieldLabel: 'Min conserved',
			name: 'gblocks_minConserved',
			value: this.gblocks_minConserved,
			width: 30
		};

		var gblocks_minFlanking=
		{
			xtype: 'textfield',
			fieldLabel: 'Min flanking',
			name: 'gblocks_minFlanking',
			value: this.gblocks_minFlanking,
			width: 30
		};
		
		var gblocks_maxNonconserved=
		{
			xtype: 'textfield',
			fieldLabel: 'Max non-conserved',
			name: 'gblocks_maxNonconserved',
			value: this.gblocks_maxNonconserved,
			width: 30
		};
			
		var fieldset=
		{
			xtype:'fieldset',					
			title: 'GBlocks settings',
			autoHeight: true,
			collapsible: true,
			collapsed: false,
			items:
			[						
				{
					layout:'column',
					items:
					[
						{
							width: 250,
							layout: 'form',
							labelWidth: 130,
							items:[gblocks_gaps]
						},
						{
							width: 150,
							layout: 'form',
							labelWidth: 115,
							items:[gblocks_maxNonconserved]
						}											
					]
				},
				{
					layout:'column',
					items:
					[
						{
							width: 110,
							layout: 'form',
							labelWidth: 60,
							items:[gblocks_minBlock]
						},
						{
							width: 140,
							layout: 'form',
							labelWidth: 90,
							items:[gblocks_minConserved]
						},
						{
							width: 130,
							layout: 'form',
							labelWidth: 75,
							items:[gblocks_minFlanking]
						}						
					]
				}				 
			]
		};		
		return fieldset;
	},
	
	comboField:function(name, fieldLabel, value, data)
	{
		if (!(data instanceof Array))
			{data=data.split(',');}		
		var field=
		{
			xtype:'combo',
			hiddenName: name,
			fieldLabel: fieldLabel,
			store: data,
			valueField: 'value',
			displayField: 'label',
			mode: 'local',
			triggerAction: 'all',
			forceSelection: true,
			selectOnFocus: true,
			typeAhead: true,
			width: 90,
			listWidth: 200,
			value: value
		};
		return field;
	},
	
	mafftFieldset:function()
	{
		var strategies=[];
		strategies.push(['AUTO','Auto (Moderately accurate)']);
		strategies.push(['FFT_NS_1','FFT-NS-1 (Very fast)']);
		strategies.push(['FFT_NS_2','FFT-NS-2 (Fast; progressive)']);
		strategies.push(['FFT_NS_i2','FFT-NS-i2 (Medium; iterative)']);	
		strategies.push(['FFT_NS_i','FFT-NS-i (Slow)']);
		strategies.push(['L_INS_i','L-INS-i (Very slow)']);
		strategies.push(['E_INS_i','E-INS-i (Very slow)']);

		var strategyField=this.comboField('strategy','Strategy',this.mafft_strategy,strategies);
		
		var matrixField=this.comboField('mafft_scorematrix','Matrix',this.mafft_scorematrix,
			'BLOSUM30,BLOSUM45,BLOSUM62,BLOSUM80,JTT100,JTT200');
		
		var opField=
		{
			xtype: 'textfield',
			fieldLabel: 'Op',
			name: 'mafft_op',
			value: this.mafft_op,
			width: 40
		};
		
		var epField=
		{
			xtype: 'textfield',
			fieldLabel: 'Ep',
			name: 'mafft_ep',
			value: this.mafft_ep,
			width: 40
		};
		
		var fieldset=
		{
			xtype: 'fieldset',					
			title: 'Mafft settings',
			autoHeight: true,
			collapsible: true,
			collapsed: false,
			items:
			[						
				{
					layout: 'column',
					items:
					[
						{
							width: 160,
							layout: 'form',
							labelWidth: 50,
							items: [strategyField]
						},
						{
							width: 160,
							layout: 'form',
							labelWidth: 50,
							items:[matrixField]
						},
						{
							width: 80,
							layout: 'form',
							labelWidth: 25,
							items: [opField]
						},
						{
							width: 80,
							layout: 'form',
							labelWidth: 25,
							items: [epField]
						}
					]
				}					 
			]
		};		
		return fieldset;
	},
	
	blastclustFieldset:function()
	{
		var blastclust_minimumLength_field=
		{
			xtype:'textfield',
			fieldLabel: 'Min length',
			name: 'blastclust_minimumLength',
			value: this.blastclust_minimumLength,
			width: 30
		};
		
		var blastclust_blastclust_minimumSize_field=
		{
			xtype:'textfield',
			fieldLabel: 'Min size',
			name: 'blastclust_minimumSize',
			value: this.blastclust_minimumSize,
			width: 30
		};
		
		var fieldset=
		{
			xtype:'fieldset',					
			title: 'Blastclust settings',
			autoHeight: true,
			collapsible: true,
			collapsed: false,
			items:
			[						
				{
					layout:'column',
					items:
					[
						{
							width: 120,
							layout: 'form',
							labelWidth: 70,
							items:[blastclust_minimumLength_field]
						},
						{
							width: 120,
							layout: 'form',
							labelWidth: 50,
							items:[blastclust_blastclust_minimumSize_field]
						}
					]
				}					 
			]
		};		
		return fieldset;
	},
	
	msaFieldset:function()
	{
		return this.createFieldset({title: 'Multiple sequence alignment'},
		[
			this.createRow(
			[
				this.createTextAreaControl({name: 'sequences', value: this.sequences, fieldLabel: 'Enter a multiple sequence alignment in FASTA or CLUSTALW format'})
			]),
			this.createRow(
			[
				this.createFileUploadControl({name: 'file', fieldLabel: 'File', emptyText: 'Upload an alignment file'})
			])
		]);
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
		vardb.ajaxRequest('/demo/hvalign.json',{},function(json)
		{
			form.findField('sequences').setValue(json.sequences);
		});
	}
});