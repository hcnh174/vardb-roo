/*global Ext, nelson, vardb, utils */
vardb.controls.MainMenu = Ext.extend(Ext.Toolbar,
{
	height: 25,
	anonymous: true,
	admin: false,
	
	initComponent:function()
	{
		var config=
		{
			defaults: {enableScrolling: false},
			items:
			[
				this.createHomepageMenu(),'-',
				this.createResourceMenu(),'-',
				this.createBlastMenu(),'-',
				this.createToolMenu(),'-',
				this.createUserMenu(),'-',
				this.createAdminMenu(),
				'->',
				this.createSearchSelect(),
				this.createSearchTextBox(),'-',
				this.createSearchButton()
			 ]
		};
		
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		vardb.controls.MainMenu.superclass.initComponent.apply(this, arguments);
	},
	
	createHomepageMenu:function()
	{
		var menu=
		{
			text: 'Homepage', handler: function(){utils.gotoUrl('/homepage.html');}
		};
		return menu;
	},
	
	createResourceMenu:function()
	{
		var menu=
		{
			text: 'Resources',
			enableScrolling: false,
			menu:
			{
				items:
				[
					{text: 'Pathogens', handler: function(){utils.gotoUrl('/pathogens.html');}},
					{text: 'Families', handler: function(){utils.gotoUrl('/families.html');}},
					{text: 'Diseases', handler: function(){utils.gotoUrl('/diseases.html');}},
					{text: 'Pfam motifs', handler: function(){utils.gotoUrl('/pfams.html');}},
					{text: 'Structures', handler: function(){utils.gotoUrl('/structures.html');}},
					{text: 'Genomes', handler: function(){utils.gotoUrl('/genomes.html');}},
					{text: 'Map', handler: function(){utils.gotoUrl('/search/map.html');}},
					{text: 'Alignments', handler: function(){utils.gotoUrl('/alignments.html');}},
					{text: 'Clinical data', handler: function(){utils.gotoUrl('/bundles.html');}},
					'-',
					{text: 'Tutorials', handler: function(){utils.gotoUrl('/tutorials.html');}},
					{text: 'Antigenic variation', handler: function(){utils.gotoUrl('/antigenicvariation.html');}},
					{text: 'Database construction', handler: function(){utils.gotoUrl('/database.html');}},
					{text: 'Terms', handler: function(){utils.gotoUrl('/terms.html');}},
					{text: 'Links', handler: function(){utils.gotoUrl('/links.html');}},
					{text: 'Images', handler: function(){utils.gotoUrl('/images.html');}},
					{text: 'References', handler: function(){utils.gotoUrl('/references.html');}}
				]
			}
		};
		return menu;
	},
	
	createBlastMenu:function()
	{	
		var menu=
		{
			text: 'BLAST',
			menu:
			{
				items:
				[
					{text: 'PSI', handler: function(){utils.gotoUrl('/blast/blast.html');}},
					{text: 'PSI-BLAST', handler: function(){utils.gotoUrl('/blast/psiblast.html');}},
					{text: 'PHI-BLAST', handler: function(){utils.gotoUrl('/blast/phiblast.html');}},
					{text: 'Netblast', handler: function(){utils.gotoUrl('/analysis/netblast.html');}}
				]
			}
		};
		return menu;
	},
	
	createToolMenu:function()
	{
		var menu=
		{
			text: 'Tools',
			menu:
			{
				items:
				[
					{text: 'Search sequences', handler: function(){utils.gotoUrl('/search/sequences.html');}},
					{text: 'PROSITE/regex search', handler: function(){utils.gotoUrl('/regex/search.html');}},
					{text: 'MAFFT alignment tool', handler: function(){utils.gotoUrl('/mafft.html');}},
					{text: 'Alignment viewer', handler: function(){utils.gotoUrl('/alignments/view.html');}},
					{text: 'Create a codon alignment', handler: function(){utils.gotoUrl('/analysis/codonalign.html');}},
					{text: 'Gblocks', handler: function(){utils.gotoUrl('/analysis/gblocks.html');}},
					{text: 'Analyze variability', handler: function(){utils.gotoUrl('/analysis/variability.html');}}
				]
			}
		};
		return menu;
	},
	
	createUserMenu:function()
	{
		var items=[];
		if (this.anonymous)
		{
			items.push({text: 'New account', handler: function(){utils.gotoUrl('/newuser.html');}});
			items.push({text: 'Login', handler: function(){utils.gotoUrl('/login.html');}});
		}
		else
		{
			items.push({text: 'Logout', handler: function(){utils.gotoUrl('/logout.html');}});
			items.push({text: 'Edit user information', handler: function(){utils.gotoUrl('/edituser.html');}});
			//items.push({text: 'User homepage', handler: function(){utils.gotoUrl('/user.html');}});
			items.push({text: 'Change password', handler: function(){utils.gotoUrl('/changepassword.html');}});
			items.push({text: 'Contact us', handler: function(){utils.gotoUrl('/contact.html');}});
		}
		items.push('-');
		items.push({text: 'Explorer', handler: function(){utils.gotoUrl('/explorer.html');}});
		items.push('-');
		items.push({text: 'My searches', handler: function(){utils.gotoUrl('/user/searches.html');}});
		items.push({text: 'My analyses', handler: function(){utils.gotoUrl('/user/analyses.html');}});
		items.push({text: 'My sequences', handler: function(){utils.gotoUrl('/user/sequences.html');}});
		items.push({text: 'My alignments', handler: function(){utils.gotoUrl('/user/alignments.html');}});	
		
		var menu=
		{
			text: 'User',
			menu: {items: items}
		};
		return menu;
	},
	
	createAdminMenu:function()
	{
		if (!this.admin)
			{return '';}
		var menu=
		{
			text: 'Admin',
			menu:
			{			
				items:
				[
					{text: 'Admin page', handler: function(){utils.gotoUrl('/admin/index.html');}},
					{text: 'Update XML', handler: function(){utils.gotoUrl('/admin/setup.html');}},
					{text: 'Update sequences', handler: function(){utils.gotoUrl('/admin/load/table.html');}},
					{text: 'Update counts', handler: function(){utils.gotoUrl('/admin/update/counts.html');}},
					{text: 'Clear cache', handler: function(){utils.gotoUrl('/admin/cache/clear.html');}},
					{text: 'Users', handler: function(){utils.gotoUrl('/admin/users.html');}}
				]
			}
		};
		return menu;
	},
	
	createSearchSelect:function()
	{
		var combo=new Ext.form.ComboBox(
		{
			store: new Ext.data.ArrayStore(
			{
				fields: ['value', 'display'],
				data: [['SEQUENCES','Sequences'],['GOOGLE','Google']]
			}),
			itemId: 'searchtype',
			hiddenName: 'type',
			valueField: 'value',
			displayField: 'display',
			width: 110,
			mode: 'local',
			triggerAction: 'all',
			value: 'SEQUENCES',
			selectOnFocus: true,
			forceSelection: true
		});
		return combo;
	},
	
	createSearchTextBox:function()
	{
		var self=this;
		
		var store = new Ext.data.Store(
		{
			url: utils.webapp+'/search/ajax/suggestions.json',
			reader: new Ext.data.JsonReader(
			{
				root: 'results',
				totalProperty: 'totalCount',
				idProperty: 'keyword'
			},
			[
				{name: 'keyword', mapping: 'keyword'},
				{name: 'type', mapping: 'type'},
				{name: 'identifier', mapping: 'identifier'}
			]),
			baseParams: {limit:20}
		});
	
		//var tpl=new Ext.XTemplate('<tpl for"."><div class="x-combo-list-item">{keyword} ({type})</div>');
		var combo=new Ext.form.ComboBox(
		{
			itemId: 'searchtextbox',
			store: store,
			minChars: 2,
			displayField: 'keyword',
			//typeAhead: true,
			//typeAheadDelay: 500,
			loadingText: 'Searching...',
			width: 150, //width: 100,
			listWidth: 200,
			hideTrigger: true,
			emptyText: 'Search...',
			queryDelay: 800,
			forceSelection: false,
			foundMatch: false,
			listeners:
			{
				select: function(field,record,index)
				{
					this.foundMatch=true;
				},
				specialkey: function(field,e)
				{
					if (e.getKey()===e.ENTER && this.foundMatch)
						{self.submitSearchHandler();}
				}
			}
		});
		return combo;
	},
	
	createSearchButton:function()
	{
		var button=new Ext.Button(
		{
			text: 'Go',
			width: 32,
			scope: this,
			handler: this.submitSearchHandler	
		});
		return button;
	},
	
	submitSearchHandler:function()
	{
		var value=this.getComponent('searchtextbox').getValue().trim();
		var type=this.getComponent('searchtype').getValue().trim();
		if (value==='')
			{return;}
		var form=document.forms.searchform;
		form.elements.query.value=value;
		if (type==='SEQUENCES')
		{
			if (value.indexOf(' ')!==-1) // if the query contains spaces, wrap it with quotes
				{form.elements.query.value='"'+value+'"';}
			//form.action=utils.webapp+'/search/sequences.html';
		}
		else if (type==='GOOGLE')
			{form.action=utils.webapp+'/search/google.html';}
		else {throw 'unsupported search type: '+type;}
		form.submit();
	}
});
