Ext.define('vardb.graphics.Location',
{	
	numbases: 0,
	scale: 1,
	
	constructor: function(str, width)
	{
		var parts=[];
		var arr=str.split(',');
		var index,i,part,start,end,bases;
		for (index=0;index<arr.length;index++)
		{
			i=arr[index].indexOf('..');
			start=parseInt(arr[index].substring(0,i),10);
			end=parseInt(arr[index].substring(i+2));
			bases=end-start+1;
			//alert('start: '+start+', end: '+end+', bases: '+bases);
			parts.push({start: start, end: end, bases: bases, type: 'exon'});
		}
		
		this.parts=[];
		if (parts[0].start!=1)
		{
			var start=1;
			var end=parts[0].end;
			var bases=end;
			this.parts.push({start: start, end: end, bases: bases, type: 'intergenic'});
		}
		this.parts.push(parts[0]); // push the first exon so that you can always add the second exon in the loop
		for (index=0;index<parts.length-1;index++)
		{
			var exon1=parts[index];
			var exon2=parts[index+1];
			var start=exon1.end+1;
			var end=exon2.start-1;
			var bases=end-start+1;
			//alert('intron. start: '+start+', end: '+end+', bases: '+bases);
			this.parts.push({start: start, end: end, bases: bases, type: 'intron'});
			this.parts.push(exon2);
		}
		
		
		//alert(this.parts.length);
		this.numbases=this.parts[this.parts.length-1].end-this.parts[0].start+1;
		this.scale=width/this.numbases;
		//alert('numbases: '+this.numbases+', scale: '+this.scale);
	},
	
	convert: function(x)
	{
		x=x-1;
		var value=Math.round(x*this.scale);
		//alert('x: '+x+' scale: '+this.scale+', value='+value);
		return value;
	}
});