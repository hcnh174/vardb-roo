package org.vardb.util.services;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.springframework.stereotype.Repository;
import org.vardb.util.CAbstractDaoImpl;

import freemarker.cache.TemplateLoader;

//http://nurkiewicz.blogspot.com/2010/01/writing-custom-freemarker-template.html
@Repository
public class DatabaseTemplateLoader<T> extends CAbstractDaoImpl implements TemplateLoader
{
	public Object findTemplateSource(String name) throws IOException
	{
		System.out.println("CDatabaseTemplateLoader.findTemplateSource: "+name);
		return super.get(FreemarkerTemplate.class, name);
	}

	public long getLastModified(Object templateSource)
	{
		System.out.println("CDatabaseTemplateLoader.getLastModified: "+templateSource.getClass().getName());
		final FreemarkerTemplate template = (FreemarkerTemplate) templateSource;
		getSession().refresh(template);
		return template.getUpdated().getTimeInMillis();
	}

	public Reader getReader(Object templateSource, String encoding) throws IOException
	{
		FreemarkerTemplate template=(FreemarkerTemplate)templateSource;
		System.out.println("CDatabaseTemplateLoader.getReader: "+template.getContent());
		return new StringReader(template.getContent());
	}

	public void closeTemplateSource(Object templateSource) throws IOException
	{
	}
}