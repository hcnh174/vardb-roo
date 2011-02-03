package org.vardb.batch;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class InvoiceFieldSetMapper implements FieldSetMapper<Invoice>
{
	public Invoice mapFieldSet(FieldSet fieldSet) throws BindException {
		Invoice invoice = new Invoice();
		invoice.setId(fieldSet.readString("INVOICE_ID"));
		invoice.setCustomerId(fieldSet.readLong("CUSTOMER_ID"));
		invoice.setDescription(fieldSet.readString("DESCRIPTION"));
		invoice.setIssueDate(fieldSet.readDate("ISSUE_DATE"));
		invoice.setAmount(fieldSet.readBigDecimal("AMOUNT"));
		return invoice;
	}
}
