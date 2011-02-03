package org.vardb.batch;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
public class Invoice {
	
	private String id;
	private Long customerId;
	private String description;
	private Date issueDate;
	private BigDecimal amount;

}