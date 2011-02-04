package org.vardb.web;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Maps;

@Controller
public class VardbController {

	@ModelAttribute("config")
	public Map<String,Object> preloadConfig(HttpServletRequest request, HttpServletResponse response, Principal user)
	{
		//System.out.println("loading config into model");
		Map<String,Object> config=Maps.newHashMap();
		config.put("extjsurl","http://extjs.cachefly.net/ext-3.3.0");
		config.put("version","Release 6");
		config.put("username",user.getName());
		return config;
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
