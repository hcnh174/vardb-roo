package org.vardb.setup;

import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.vardb.util.CStringHelper;

public class HeaderCallbackHandler implements LineCallbackHandler {

	private DelimitedLineTokenizer tokenizer;
	private char delimiter='\t';
	
	public void setLineTokenizer(DelimitedLineTokenizer tokenizer)
	{
		//System.out.println("setting tokenizer: "+tokenizer);
		this.tokenizer=tokenizer;
	}
	
	public void setDelimiter(char delimiter)
	{
		//System.out.println("setting delimiter: "+delimiter);
		this.delimiter=delimiter;
	}
	
	@Override
	public void handleLine(String line) {
		//System.out.println("Header: "+line);
		String[] fields=CStringHelper.splitAsArray(line,""+delimiter); //"\t"
		//System.out.println("Fields: "+CStringHelper.join(fields,","));
		tokenizer.setNames(fields);
		tokenizer.setDelimiter(delimiter);
	}

}
