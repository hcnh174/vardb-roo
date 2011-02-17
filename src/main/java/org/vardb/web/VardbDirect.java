package org.vardb.web;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.vardb.resources.Comment;
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
	
	@ExtDirectMethod
	public String getTerm(String id) {
		return "definition of "+id;
	}
	
	@ExtDirectMethod(ExtDirectMethodType.STORE_READ)
	public ExtDirectStoreResponse<Sequence> loadSequences(ExtDirectStoreReadRequest request) {
		int start=request.getStart();
		int limit=request.getLimit();
		int total=(int)Sequence.countSequences();
		List<Sequence> sequences=Sequence.findSequenceEntries(start, limit);
		return new ExtDirectStoreResponse<Sequence>(total,sequences);
	}
	
	@ExtDirectMethod(ExtDirectMethodType.STORE_READ)
	public ExtDirectStoreResponse<Comment> loadComments(ExtDirectStoreReadRequest request) {
		int start=request.getStart();
		int limit=request.getLimit();
		int total=(int)Comment.countComments();
		List<Comment> sequences=Comment.findCommentEntries(start, limit);
		return new ExtDirectStoreResponse<Comment>(total,sequences);
	}
	
	@ExtDirectMethod
	public String submitComment(String type, String identifier, String text, Principal user) {
		String username=user==null ? "anonymous" : user.getName();
		Comment comment=new Comment(username,type,identifier,text);
		comment.persist();
		return "comment submitted";
	}
}
