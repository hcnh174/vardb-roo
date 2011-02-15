/*global Ext, vardb */
vardb.analysis.CodonUsageTable=Ext.extend(Ext.TabPanel,
{
	title: 'Codon usage',
	frame: true,
	autoHeight: true,
	activeTab: 0,
	deferredRender: true,
	layoutOnTabChange: true,
	percentFormat: '0.00',

	initComponent:function()
	{	
		this.calculateFrequencies();
		
		var config=
		{
			defaults: {autoHeight: true, bodyStyle: 'padding: 5px'}, //hideMode: 'offsets',
			hidden: this.data.rows.length<1,
			items:
			[
				this.createGcTab(),
				this.createCodonTab(),
				this.createSynonymousCodonTab(),
				this.createAminoAcidTab(),
				this.createGroupTab()
			]
		};
		
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		vardb.analysis.CodonUsageTable.superclass.initComponent.apply(this, arguments);
	},
	
	createGcTab:function()
	{
		var table=
		{
			layout: 'table',
			layoutConfig: {columns: 2},
			border: false,
			bodyStyle: 'font-family: verdana;',
			defaults: {bodyStyle: 'padding-left: 2px; padding-right: 2px; font-size: 8pt;'},
			items:
			[
				{html: '<b>G content</b>'},{html: Ext.util.Format.number(this.data.gcContent.g,this.percentFormat)+' %'},
				{html: '<b>C content</b>'},{html: Ext.util.Format.number(this.data.gcContent.c,this.percentFormat)+' %'},
				{html: '<b>A content</b>'},{html: Ext.util.Format.number(this.data.gcContent.a,this.percentFormat)+' %'},
				{html: '<b>T content</b>'},{html: Ext.util.Format.number(this.data.gcContent.t,this.percentFormat)+' %'},
				{html: '<b>GC</b>'},{html: Ext.util.Format.number(this.data.gcContent.gc,this.percentFormat)+' %'},
				{html: '<b>GC3</b>'},{html: Ext.util.Format.number(this.data.gcContent.gc3,this.percentFormat)+' %'},
				{html: '<b>GC3 skew</b>'},{html: Ext.util.Format.number(this.data.gcContent.gc3skew,this.percentFormat)}
			]
		};		
		var tab={title: 'GC content', items: table};
		return tab;
	},
	
	createCodonTab:function()
	{
		var table=
		{
			layout: 'table',			
			layoutConfig: {columns: 4},
			border: false,
			bodyStyle: 'font-family: verdana;',
			defaults: {bodyStyle: 'padding-left: 2px; padding-right: 2px; font-size: 8pt;'},
			items: []
		};
		
		var i,j,codon,aa,codons;
		table.items.push({html: '<b>Codon</b>'});	
		table.items.push({html: '<b>Amino acid</b>'});
		table.items.push({html: '<b>Count</b>'});
		table.items.push({html: '<b>Percent</b>'});	

		for (i=0;i<this.data.rows.length;i++)
		{
			codon=this.data.rows[i];
			aa=this.getAminoAcidByCode(codon.aa);
			table.items.push({html: codon.codon});
			table.items.push({html: aa.longname});
			table.items.push({html: codon.count, bodyStyle: 'text-align: right;'});
			table.items.push({html: Ext.util.Format.number(100*codon.freq,'0.0')+' %', bodyStyle: 'text-align: right;'});
		}
		var tab={title: 'Codons', items: table};
		return tab;
	},
	
	createSynonymousCodonTab:function()
	{
		var table=
		{
			layout: 'table',			
			layoutConfig: {columns: 8},
			border: false,
			bodyStyle: 'font-family: verdana;',
			defaults: {bodyStyle: 'padding-left: 2px; padding-right: 2px; font-size: 8pt;'},
			items: []
		};
		
		var i,j,codon,aa,codons;
		table.items.push({html: '<b>Amino acid</b>'});
		table.items.push({html: '<b>Percent</b>'});
		for (i=1;i<=6;i++)
		{
			table.items.push({html: '<b>Codon '+i+'</b>'});
		}		
		
		for (i=0;i<this.aminoAcids.length;i++)
		{
			aa=this.aminoAcids[i];
			if (aa.code==='*' && aa.count===0)
				{continue;}
			codons=this.getCodonsByAminoAcid(aa.code);
			table.items.push({html: aa.longname+'<br/>&nbsp;'});
			table.items.push({html: '<div qtip="'+aa.count+'">'+aa.percent+'<br/>&nbsp;</div>', bodyStyle: 'text-align: right;'});
			for (j=0;j<codons.length;j++)
			{
				codon=codons[j];
				table.items.push({html: '<div qtip="'+codon.count+'">'+codon.codon+'<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+codon.rscu+'</div>'});
			}
			if (codons.length<6)
				{table.items.push({html: '&nbsp;<br/>&nbsp;', colspan: 6-codons.length, bodyStyle: 'background-color: #EEEEEE'});}
		}
		
		var tab={title: 'Synonymous Codons', items: table};
		return tab;	
	},
	
	createAminoAcidTab:function()
	{
		var table=
		{
			layout: 'table',			
			layoutConfig: {columns: 3},
			border: false,
			bodyStyle: 'font-family: verdana; text-align: right;',
			defaults: {bodyStyle: 'padding-left: 2px; padding-right: 2px; font-size: 8pt;'},
			items:
			[
				{html: '<b>Amino acid</b>', bodyStyle: 'text-align: left;'},
				{html: '<b>Count</b>'},
				{html: '<b>Percentage</b>'}				
			]
		};
		
		var i,aa;
		for (i=0;i<this.aminoAcids.length;i++)
		{
			aa=this.aminoAcids[i];
			if (aa.code==='*')
				{continue;}
			table.items.push({html: aa.longname, bodyStyle: 'text-align: left;'});
			table.items.push({html: aa.count});
			table.items.push({html: aa.percent});
		}
		
		var tab={title: 'Amino acids', items: [table]};
		return tab;
	},
	
	createGroupTab:function()
	{
		var table=
		{
			layout: 'table',			
			layoutConfig: {columns: 3},
			border: false,
			bodyStyle: 'font-family: verdana; text-align: right;',
			defaults: {bodyStyle: 'padding-left: 2px; padding-right: 2px; font-size: 8pt;'},
			items:
			[
				{html: '<b>Group</b>', bodyStyle: 'text-align: left;'},
				{html: '<b>Count</b>'},
				{html: '<b>Percentage</b>'}				
			]
		};
		
		var i,group,count;		
		for (i=0;i<this.aminoAcidGroups.length;i++)
		{
			group=this.aminoAcidGroups[i];
			table.items.push({html: group.longname, bodyStyle: 'text-align: left;'});
			table.items.push({html: group.count});
			table.items.push({html: group.percent});
		}
		
		var tab={title: 'Amino acid groups', items: [table]};
		return tab;
	},
	
	getCodonsByAminoAcid:function(aa,sortfield)
	{
		if (!sortfield)
			{sortfield='rscu';}
		var codons=[], index, codon;
		for (index=0;index<this.data.rows.length;index++)
		{
			codon=this.data.rows[index];
			if (codon.aa===aa)
				{codons.push(codon);}
		}
		codons.sort(function(codon1,codon2){return codon2[sortfield]-codon1[sortfield];});
		return codons;
	},
	
	getTotal:function()
	{
		var index, total=0, codon;
		for (index=0;index<this.data.rows.length;index++)
		{
			codon=this.data.rows[index];
			total+=codon.count;
		}
		return total;
	},
	
	sumAminoAcidCounts:function(aa)
	{
		var sum=0, index, codon;
		for (index=0;index<this.data.rows.length;index++)
		{
			codon=this.data.rows[index];
			if (codon.aa===aa)
				{sum+=codon.count;}
		}
		return sum;
	},
	
	sumGroupCounts:function(group)
	{
		var sum=0, i, aa;
		for (i=0;i<this.aminoAcids.length;i++)
		{
			aa=this.aminoAcids[i];
			if (aa.group===group)
				{sum+=this.sumAminoAcidCounts(aa.code);}
		}
		return sum;
	},
	
	// calculate counts and frequencies for amino acids and groups and sort in descending order
	calculateFrequencies:function()
	{
		this.data.rows.sort(function(codon1,codon2){return codon2.count-codon1.count;});
		var i, aa, group;
		var total=this.getTotal();	
		for (i=0;i<this.aminoAcids.length;i++)
		{
			aa=this.aminoAcids[i];
			aa.count=this.sumAminoAcidCounts(aa.code);
			aa.percent=Ext.util.Format.number(100*aa.count/total,this.percentFormat)+' %';
		}
		this.aminoAcids.sort(function(aa1,aa2){return aa2.count-aa1.count;});

		for (i=0;i<this.aminoAcidGroups.length;i++)
		{
			group=this.aminoAcidGroups[i];
			group.count=this.sumGroupCounts(group.code);
			group.percent=Ext.util.Format.number(100*group.count/total,this.percentFormat)+ '%';
		}
		this.aminoAcidGroups.sort(function(g1,g2){return g2.count-g1.count;});
	},
	
	getAminoAcidByCode:function(code)
	{
		var i,aa;
		for (i=0;i<this.aminoAcids.length;i++)
		{
			aa=this.aminoAcids[i];
			if (aa.code===code)
				{return aa;}
		}
		return null;
	},
	
	aminoAcids:
	[	
		{code: 'G', shortname: 'Gly', longname: 'Glycine', group: 'SMALL', type: 'TINY'},
		{code: 'A', shortname: 'Ala', longname: 'Alanine', group: 'SMALL', type: 'TINY'},
		{code: 'S', shortname: 'Ser', longname: 'Serine', group: 'NUCLEOPHILIC', type: 'TINY'},
		{code: 'T', shortname: 'Thr', longname: 'Threonine', group: 'NUCLEOPHILIC', type: 'TINY'},
		{code: 'C', shortname: 'Cys', longname: 'Cysteine', group: 'NUCLEOPHILIC', type: 'CYSTEINE'},
		{code: 'V', shortname: 'Val', longname: 'Valine', group: 'HYDROPHOBIC', type: 'ALIPHATIC'},
		{code: 'L', shortname: 'Leu', longname: 'Leucine', group: 'HYDROPHOBIC', type: 'ALIPHATIC'},
		{code: 'I', shortname: 'Ile', longname: 'Isoleucine', group: 'HYDROPHOBIC', type: 'ALIPHATIC'},
		{code: 'M', shortname: 'Met', longname: 'Methionine', group: 'HYDROPHOBIC', type: 'OTHER'},
		{code: 'P', shortname: 'Pro', longname: 'Proline', group: 'HYDROPHOBIC', type: 'OTHER'},
		{code: 'F', shortname: 'Phe', longname: 'Phenylalanine', group: 'AROMATIC', type: 'AROMATIC'},
		{code: 'Y', shortname: 'Tyr', longname: 'Tyrosine', group: 'AROMATIC', type: 'POLAR'},
		{code: 'W', shortname: 'Trp', longname: 'Tryptophan', group: 'AROMATIC', type: 'AROMATIC'},
		{code: 'D', shortname: 'Asp', longname: 'Aspartic Acid', group: 'ACIDIC', type: 'POLAR'},
		{code: 'E', shortname: 'Glu', longname: 'Glutamic Acid', group: 'ACIDIC', type: 'POLAR'},
		{code: 'N', shortname: 'Asn', longname: 'Asparagine', group: 'AMIDE', type: 'POLAR'},
		{code: 'Q', shortname: 'Gln', longname: 'Glutamine', group: 'AMIDE', type: 'POLAR'},
		{code: 'H', shortname: 'His', longname: 'Histidine', group: 'BASIC', type: 'POLAR'},
		{code: 'K', shortname: 'Lys', longname: 'Lysine', group: 'BASIC', type: 'POLAR'},
		{code: 'R', shortname: 'Arg', longname: 'Arginine', group: 'BASIC', type: 'POLAR'},
		{code: '*', shortname: 'Stop', longname: 'Stop'}
	],
	
	aminoAcidGroups:
	[
		{code: 'SMALL', longname: 'Small'},
		{code: 'NUCLEOPHILIC', longname: 'Nucleophilic'},
		{code: 'HYDROPHOBIC', longname: 'Hydrophobic'},
		{code: 'AROMATIC', longname: 'Aromatic'},
		{code: 'ACIDIC', longname: 'Acidic'},
		{code: 'AMIDE', longname: 'Amide'},
		{code: 'BASIC', longname: 'Basic'}
	]
});