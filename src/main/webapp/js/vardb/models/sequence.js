/*global Ext, vardb, utils */
Ext.regModel('Sequence',
{
	fields:
	[
		'identifier',   
		'accession',    
		'genome',
		'strain',
		'taxid',
		'source',
		'chromosome',
		'sequence',
		'cds',
		'translation',
		'start',
		'end',
		'strand',
		'numexons',
		'splicing',
		'pseudogene',
		'method',
		'model',
		'score',
		'evalue',
		'hmmloc',
		'description',
		{name: 'domainnum', type: 'int'},
		{name: 'totaldomainnum', type: 'int'},
		'domains'
	],
	
	proxy:
	{
	 	type: 'rest',
	 	url: '/sequences',
	 	reader: 'json'
	}
	//validations: []
});
