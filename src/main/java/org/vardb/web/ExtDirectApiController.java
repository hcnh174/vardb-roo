package org.vardb.web;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import ch.ralscha.extdirectspring.api.ApiController;
import ch.ralscha.extdirectspring.util.JsonHandler;

@Controller
public class ExtDirectApiController extends ApiController
{
	public ExtDirectApiController(ApplicationContext context, JsonHandler jsonHandler)
	{
		super(context, jsonHandler);
	}
}
