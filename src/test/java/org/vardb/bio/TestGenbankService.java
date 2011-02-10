package org.vardb.bio;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.vardb.util.CMessageWriter;
import org.vardb.util.CStringHelper;

import com.google.common.collect.Lists;

public class TestGenbankService
{
	@Test
	public void doNothing()
	{
		GenbankService genbankService=new GenbankServiceImpl();
	}
	
	/*
	@Test
	public void testGetTaxa()
	{
		GenbankService genbankService=new GenbankServiceImpl();
		CMessageWriter writer=new CMessageWriter();
		List<Integer> ids=Lists.newArrayList();
		ids.add(5865);
		ids.add(5833);
		Collection<Taxon> roottaxa=genbankService.getTaxa(ids, writer);
		for (Taxon taxon : roottaxa)
		{
			display(taxon,0);
		}
	}
	
	private void display(Taxon taxon, int depth)
	{
		System.out.println(CStringHelper.repeatString("\t",depth)+taxon.getName()+": "+taxon.getDescription());
		for (Taxon child : taxon.getTaxa())
		{
			display(child,depth+1);
		}
	}
	
	@Test
	public void testGetRefs()
	{
		GenbankService genbankService=new GenbankServiceImpl();
		CMessageWriter writer=new CMessageWriter();
		List<Integer> ids=Lists.newArrayList();
		ids.add(20637204);
		ids.add(20576307 );
		Map<Integer,Ref> refs=genbankService.getRefs(ids, writer);
		for (Ref ref : refs.values())
		{
			System.out.println(ref.getCitation());
		}
	}
	*/	 
}