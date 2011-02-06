package org.vardb.web;

import java.util.List;

import org.springframework.stereotype.Service;
import org.vardb.sequences.Sequence;

import ch.ralscha.extdirectspring.annotation.ExtDirectMethod;
import ch.ralscha.extdirectspring.annotation.ExtDirectMethodType;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreReadRequest;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreResponse;

@Service
public class VardbDirect {

	@ExtDirectMethod
	public String doEcho(String message) {
		return message;
	}
	
	@ExtDirectMethod(ExtDirectMethodType.STORE_READ)
	public ExtDirectStoreResponse<Sequence> loadSequences(ExtDirectStoreReadRequest request) {
		int start=request.getStart();
		int limit=request.getLimit();
		int total=(int)Sequence.countSequences();
		List<Sequence> sequences=Sequence.findSequenceEntries(start, limit);
		return new ExtDirectStoreResponse<Sequence>(total,sequences);
	}
}
