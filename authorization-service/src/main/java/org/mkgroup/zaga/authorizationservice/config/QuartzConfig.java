package org.mkgroup.zaga.authorizationservice.config;

import java.util.Date;
import java.util.UUID;

import org.mkgroup.zaga.authorizationservice.quartzJob.DeleteResetPassToken;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

	public JobDetail buildJobDetail(long id) {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("id", id);
		
		return JobBuilder.newJob(DeleteResetPassToken.class)
				.withIdentity(UUID.randomUUID().toString(), " key for delete ")
				.withDescription("Delete reset password token job")
				.usingJobData(jobDataMap)
				.storeDurably()
				.build();
	}
	
	public Trigger buildJobTrigger(JobDetail jobDetail, Date startAt) {
		return TriggerBuilder.newTrigger()
				.forJob(jobDetail)
				.withIdentity(jobDetail.getKey().getName(), " delete token - triggers")
				.withDescription("Delete reset password token trigger")
				.startAt(startAt)
				.build();
	}
}
