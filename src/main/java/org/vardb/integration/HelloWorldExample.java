package org.vardb.integration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class HelloWorldExample
{
	public static void main(String args[])
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/integration-demo.xml");
//		MessageChannel channel = context.getBean("names", MessageChannel.class);
//		Message<String> message = MessageBuilder.withPayload("World").build();
//		channel.send(message);
		
		HelloService helloService =	context.getBean("helloGateway", HelloService.class);
		System.out.println(helloService.sayHello("World"));
	}
}