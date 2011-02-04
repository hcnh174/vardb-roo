package org.vardb.web;

import org.springframework.stereotype.Service;

import ch.ralscha.extdirectspring.annotation.ExtDirectMethod;

@Service
public class VardbDirect {

	@ExtDirectMethod
	public String doEcho(String message)
	{
		return message;
	}
}
