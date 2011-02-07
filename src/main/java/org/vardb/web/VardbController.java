package org.vardb.web;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    
    @RequestMapping(value = "/contact.html", method = RequestMethod.GET)
	public String contact(Model model)
	{
		//CUserDetails user = (CUserDetails)acegiService.getUserDetails();
		//CFeedback feedback = new CFeedback(user);
		//model.addAttribute("feedback", feedback);
		return "users/contact";
	}

	@RequestMapping(value = "/contact.html", method = RequestMethod.POST)
	public String contactSubmitted(Model model,
			@RequestParam("name") String name,
			@RequestParam("affiliation") String affiliation,
			@RequestParam("email") String email,
			@RequestParam("purpose") String purpose,
			@RequestParam("comments") String comments)
	{
		//resourceService.contact(name, affiliation, email, purpose, comments);
		return "users/contactsubmitted";
	}
	
	/////////////////////////////////////////////////////////////
	
	@RequestMapping("/ajax/announcements.xml")
	public void announcementsRss(HttpServletResponse response)
	{
		response.setContentType(CWebHelper.ContentType.XML);
		String rssFeed="http://groups.google.com/group/vardb-announce/feed/rss_v2_0_msgs.xml";
		int rssMax=10;
		CWebHelper.write(response,CWebHelper.readRss(rssFeed,rssMax));
	}
}
