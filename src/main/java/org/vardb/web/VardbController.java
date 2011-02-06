package org.vardb.web;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vardb.util.CDateHelper;
import org.vardb.util.CFileHelper;
import org.vardb.util.CStringHelper;
import org.vardb.util.CWebHelper;

import com.google.common.collect.Maps;

@Controller
public class VardbController {

	@ModelAttribute("config")
	public Map<String,Object> preloadConfig(HttpServletRequest request, HttpServletResponse response, Principal user)
	{
		//System.out.println("loading config into model");
		Map<String,Object> config=Maps.newHashMap();
		config.put("webapp",CWebHelper.getWebapp(request));
		config.put("extjsurl","http://extjs.cachefly.net/ext-3.3.0");
		config.put("version","Release 6");
		if (user!=null && user.getName()!=null)
			config.put("username",user);
		config.put("lastUpdated", getLastUpdatedDate());
		return config;
	}
	
	public static Date getLastUpdatedDate()
	{
		String str=CFileHelper.getResource("classpath:META-INF/maven/org.vardb/vardb-roo/pom.properties");
		List<String> lines=CStringHelper.splitAsList(str,"\n");//#Sun Feb 06 09:34:48 JST 2011
		Date date=CDateHelper.parse(lines.get(1).substring(1),CDateHelper.EXCEL_PATTERN);
		return date;
	}
	
    @RequestMapping("/homepage.html")
    public String homepage() {
        return "homepage";
    }
    
    @RequestMapping("/login.html")
    public String login() {
        return "login";
    }
}
