package org.mkgroup.zaga.authorizationservice.quartzJob;

import org.mkgroup.zaga.authorizationservice.model.PasswordResetToken;
import org.mkgroup.zaga.authorizationservice.repository.PasswordResetTokenRepository;
import org.quartz.InterruptableJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.UnableToInterruptJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class DeleteResetPassToken  extends QuartzJobBean implements InterruptableJob {
	
	@Autowired
	PasswordResetTokenRepository passResetRepo;
	
	@Autowired
	Scheduler scheduler;

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getMergedJobDataMap();
		long id = jobDataMap.getLong("id");
		PasswordResetToken prt = passResetRepo.getOne(id);
		passResetRepo.delete(prt);
		try {
			interrupt();
		} catch (UnableToInterruptJobException e) {
			e.printStackTrace();
		}
	}

}
