/*global Ext, vardb */
Ext.ux.vardb.OligoRepForm = Ext.extend(Ext.ux.vardb.AbstractForm,
{	
	title: 'Oligo Repeat Finder',
	width: 580,
	bodyStyle: 'padding: 10px 10px 0 10px;',
	labelWidth: 30,
	standardSubmit: true,
	fileUpload: true,
	url: vardb.webapp+'/analysis/oligorep.html',
	
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
				this.createFieldset({title: 'Parameters'},
				[
					this.createRow(
					[
						this.createCheckbox({name: 'directRepeats', fieldLabel: 'Direct repeats', checked: true}),
						this.createCheckbox({name: 'complementaryRepeats', fieldLabel: 'Complementary repeats', checked: true})
					]),
					this.createRow(
					[
						this.createCheckbox({name: 'symmetricRepeats', fieldLabel: 'Symmetric repeats', checked: true}),
						this.createCheckbox({name: 'invertedRepeat', fieldLabel: 'Inverted repeats', checked: true})
					]),
					this.createRow(
					[
						this.createNumberControl({name: 'minLength', fieldLabel: 'Min. length', value: 7}),
						this.createNumberControl({name: 'maxLength', fieldLabel: 'Max length', value: 20}),
						this.createNumberControl({name: 'maxMismatches', fieldLabel: 'Max. mismatches', value: 5}),
						this.createTextControl({name: 'limit', fieldLabel: 'Limit', value: '0.0001'})
					])
				]),
				this.createFieldset({title: 'Sequences'},
				[
					this.createRow(
					[
						this.createTextAreaControl({name: 'text', fieldLabel: 'Enter sequences in FASTA or CLUSTALW format'})
					]),
					this.createRow(
					[
						this.createFileUploadControl({name: 'file', fieldLabel: 'File', emptyText: 'Upload an alignment file'})
					])
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
		Ext.ux.vardb.OligoRepForm.superclass.initComponent.apply(this, arguments);
	},
	
	checkValidation:function()
	{
		if (!this.checkNotEmpty('text,file','Please enter an alignment or select a file to upload'))
			{return false;}
		return true;
	},
	
	showExampleHandler:function()
	{
		var form=this.getForm();
		vardb.ajaxRequest('/demo/oligorep.json',{},function(json)
		{
			form.findField('text').setValue(json.sequences);
		});
	}
});