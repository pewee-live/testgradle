package pewee.quests;

import org.quartz.JobDetail;
import org.quartz.Trigger;

public class QuestContext {
	
	private JobDetail jobDetail;
	
	private Trigger trigger;

	public JobDetail getJobDetail() {
		return jobDetail;
	}

	public void setJobDetail(JobDetail jobDetail) {
		this.jobDetail = jobDetail;
	}

	public Trigger getTrigger() {
		return trigger;
	}

	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}

	public QuestContext() {
		super();
	}

	public QuestContext(JobDetail jobDetail, Trigger trigger) {
		super();
		this.jobDetail = jobDetail;
		this.trigger = trigger;
	}
	
	
	
}
