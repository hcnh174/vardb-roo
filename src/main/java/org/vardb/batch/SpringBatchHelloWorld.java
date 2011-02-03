package org.vardb.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringBatchHelloWorld {

	public static void main(String ... args) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("batch-infrastructure.xml");
		JobLauncher jobLauncher = context.getBean(
		JobLauncher.class);
		Job job = context.getBean(Job.class);
		JobExecution jobExecution = jobLauncher.run(job,new JobParameters());
		System.out.println("Job ended with status: "+jobExecution.getExitStatus());
	}
}
