Ext.define('vardb.graphics.Sequence',
{
	constructor: function(config)
	{
		var items=[];
		var track_width=500;
		var track_height=10;
		
		this.plotGene(
		{
			accession: 'PFE00005w',
			location: '1..5382,6287..7528',
			width: track_width,
			height: track_height,
			x: 0,
			y: 0
		}, items);
		
		var drawing = Ext.create('Ext.draw.Component',
		{
			autoSize: true,
			padding: 40,
			draggable: {constrain: false},
			floating: true,
			viewBox: false,
			renderTo: Ext.getBody(),
			items: items
		});
	},
	
	plotGene: function(config, items)
	{
		var location=new vardb.graphics.Location(config.location,config.width);
		items.push(
		{
			type: 'path',
			path: utils.getRectPath(config.x, config.y, config.width, config.height),
			fill: 'lightgrey'
		});
		var exon_height=5;
		var vert_offset=exon_height/2;
		var ymid=config.y+config.height/2;
		var y1=ymid-vert_offset;
		var y2=ymid+vert_offset;
		var x1, x2;
		var intron_offset=3
		//alert('ymid: '+ymid+', y1: '+y1+', y2: '+y2);
		for (var index=0;index<location.parts.length;index++)
		{
			var part=location.parts[index];
			x1=location.convert(part.start);
			x2=location.convert(part.end);
			if (part.type==='exon')
			{
				items.push(
				{
					type: 'path',
					path: utils.getRectPath(x1, y1, x2-x1, y2-y1),
					fill: 'blue'
				});
			}
			else if (part.type==='intron')
			{
				var xmid=(x2-x1)/2;
				var paths=[];
				paths.push(utils.getLinePath(x1, ymid, x1+xmid, ymid-intron_offset));
				paths.push(utils.getLinePath(x1+xmid, ymid-intron_offset, x2, ymid));
				items.push(
				{
					type: 'path',
					path: paths.join(' '),
					stroke: 'gray'
				});
			}
		}
	}
});
