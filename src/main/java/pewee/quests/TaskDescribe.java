package pewee.quests;

import org.quartz.JobKey;

import com.alibaba.fastjson.JSON;

public class TaskDescribe {
	
	private String name;
	
	private String group;
	
	private int maxtimes;
	
	private int count;
	
	private Long interval;
	
	private Long baseDelay;
	
	private String stragrtName;
	
	private String jobName; 

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public int getMaxtimes() {
		return maxtimes;
	}

	public void setMaxtimes(int maxtimes) {
		this.maxtimes = maxtimes;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Long getInterval() {
		return interval;
	}

	public void setInterval(Long interval) {
		this.interval = interval;
	}

	public String getStragrtName() {
		return stragrtName;
	}

	public void setStragrtName(String stragrtName) {
		this.stragrtName = stragrtName;
	}
	
	public Long getBaseDelay() {
		return baseDelay;
	}

	public void setBaseDelay(Long baseDelay) {
		this.baseDelay = baseDelay;
	}

	public TaskDescribe() {
		super();
	}

	public TaskDescribe(String name, String group, int maxtimes, int count, Long baseDelay, Long interval, String stragrtName ,String jobName) {
		super();
		this.name = name;
		this.group = group;
		this.maxtimes = maxtimes;
		this.count = count;
		this.baseDelay = baseDelay;
		this.interval = interval;
		this.stragrtName = stragrtName;
		this.jobName = jobName;
	}
	
	public TaskDescribe(int count,JobKey jobKey,IStragty stragty) {
		super();
		this.name = jobKey.getName();
		this.group = jobKey.getGroup();
		this.maxtimes = stragty.getMaxtimes();
		this.count = count;
		if(stragty instanceof RoundIntrevalStragty) {
			this.interval = ((RoundIntrevalStragty) stragty).getInterval();
			this.baseDelay = -1L;
		}
		if(stragty instanceof RoundDoubleTimeStragty) {
			this.interval = -1L;
			this.baseDelay = ((RoundDoubleTimeStragty)stragty).getBaseDelay();
		}
		this.stragrtName = stragty.getClass().getName();
	}
	
	public String serialize() {
		return JSON.toJSONString(this);
	}
	
	public static  TaskDescribe deserialize(String str) {
		TaskDescribe describe = JSON.parseObject(str, TaskDescribe.class);
		return describe;
	}
	
	
}
