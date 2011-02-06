package org.vardb.setup;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.batch.item.database.JpaItemWriter;
import org.vardb.sequences.Sequence;

public class SequenceItemWriter extends JpaItemWriter<Sequence>
{
	//@PersistenceContext
    //transient EntityManager entityManager;
	
	/*
	public SequenceItemWriter(EntityManager entityManager)	{
		this.entityManager=entityManager;
	}
	*/

	/*
	public void write(List<? extends Sequence> sequences) throws Exception {

		for(Sequence sequence : sequences) {
			System.out.println("writing sequence: "+sequence.toString());
			entityManager.merge(sequence);//entityManager.persist(sequence);
			//sequence.persist();
		}
		//entityManager.flush();
	}
	*/
}