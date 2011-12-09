package org.vardb.web;

import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Controller;

import ch.ralscha.extdirectspring.controller.RouterController;
import ch.ralscha.extdirectspring.util.JsonHandler;

@Controller
public class ExtDirectRouterController extends RouterController {

	public ExtDirectRouterController(ApplicationContext context,
			ConversionService conversionService, JsonHandler jsonHandler) {
		super(context, conversionService, jsonHandler);//context, conversionService);
		// TODO Auto-generated constructor stub
	}

}
