package org.vardb.web;

import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Controller;

import ch.ralscha.extdirectspring.controller.RouterController;

@Controller
public class ExtDirectRouterController extends RouterController {

	public ExtDirectRouterController(ApplicationContext context,
			ConversionService conversionService) {
		super(context, conversionService);
		// TODO Auto-generated constructor stub
	}

}
