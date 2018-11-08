package pewee.quests;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import pewee.quests.business.AutoStartSingletonIntervalJob;
import pewee.service.TestService;


/**
 * 处理调度任务
 * @author pewee
 *
 */
@Component
public class QuestDispatcher implements InitializingBean,ApplicationContextAware{
	
	private static QuestDispatcher questDispatcher = new QuestDispatcher();
	
	private static final Logger logger = LoggerFactory.getLogger(QuestDispatcher.class);
	
	static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
	static Scheduler sche;
	public static ReentrantLock rl = new ReentrantLock();
	
	private static ApplicationContext applicationContext;
	
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}


	public static QuestDispatcher getInstance(){
		return questDispatcher;
	}
	
	static{
		
		try {
			System.out.println("启动任务调度");
			sche = StdSchedulerFactory.getDefaultScheduler();
			resumeJob(sche);
			sche.start();
		} catch (Exception e) {
			logger.error("数字胶片初始化任务调度器失败",e);
		}
	}
	
	public boolean hasThisJob(String group,String name) {
		try {
			return sche.checkExists(new JobKey(name, group));
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}
		return false;
	}
	
	/**
	 * 从数据库中找到已经存在的job，并重新开户调度
	 */
	public static void resumeJob(Scheduler scheduler) {
		try {
			// ①获取调度器中所有的触发器组
			List<String> triggerGroups = scheduler.getTriggerGroupNames();
			// ②重新恢复在tgroup1组中，名为trigger1_1触发器的运行
			for (int i = 0; i < triggerGroups.size(); i++) {
				List<String> triggers = scheduler.getTriggerGroupNames();
				for (int j = 0; j < triggers.size(); j++) {
					Trigger tg = scheduler.getTrigger(new TriggerKey(triggers
							.get(j), triggerGroups.get(i)));
					// ②-1:根据名称判断
					/*if (tg instanceof SimpleTrigger
							&& tg.getDescription().equals("tgroup1.trigger1_1")) {
						// ②-1:恢复运行
						
					}*/
					scheduler.resumeJob(new JobKey(triggers.get(j),
							triggerGroups.get(i)));
				}

			}
			scheduler.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public QuestContext createJobAndTriggerWithStragty(IStragty stragty,int count,String group,String name,Class<? extends Job> clazz) {
		if(stragty instanceof RoundIntrevalStragty) {
			/**
			 * RoundIntrevalStragty 只会建立一个job和trigger,执行多次直到结束
			 */
			System.out.println("round roll interval!!");
			RoundIntrevalStragty roundIntrevalStragty = (RoundIntrevalStragty)stragty;
			JobKey jobKey = new JobKey(name, group);
			JobDetail jobDetail = createNewJob(count,jobKey,stragty,clazz);
			SimpleTrigger createIntrvalSimpleTrigger = createIntrvalSimpleTrigger(jobKey, roundIntrevalStragty.getMaxtimes(), roundIntrevalStragty.getInterval());
			 return new QuestContext(jobDetail,createIntrvalSimpleTrigger);
		} else {
			/**
			 * RoundDoubleTimeStragty 只会建立一个job和trigger,执行一次,若还需要执行会建立新的
			 */
			System.out.println("round roll double!!");
			RoundDoubleTimeStragty roundDoubleTimeStragty = (RoundDoubleTimeStragty)stragty;
			JobKey jobKey = new JobKey(name, group);
			JobDetail jobDetail = createNewJob(count,jobKey,stragty,clazz);
			SimpleTrigger trigger = createDelaySimpleTrigger(jobKey, roundDoubleTimeStragty.doStragty(count));
			return new QuestContext(jobDetail,trigger);
		}
	}
	
	public QuestContext createJobAndTriggerWithTaskDescribe(TaskDescribe taskDescribe) {
		System.out.println("taskDescribe >> QuestContext");
		String stragrtName = taskDescribe.getStragrtName();
		Class<?> clazz = null;
		try {
			clazz = Class.forName(stragrtName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			return null;
		}
		Object instance;
		try {
			instance = clazz.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			return null;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		String jobName = taskDescribe.getJobName();
		Class<? extends Job> clazz1 = null;
		try {
			clazz1 = (Class<? extends Job>) Class.forName(jobName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		int count = taskDescribe.getCount();
		JobKey jobKey = new JobKey(taskDescribe.getName() + "-" + count , taskDescribe.getGroup());
		if(instance instanceof RoundIntrevalStragty) {
			RoundIntrevalStragty  roundIntrevalStragty	= (RoundIntrevalStragty)instance;
			roundIntrevalStragty.setInterval(taskDescribe.getInterval());
			roundIntrevalStragty.setMaxtimes(taskDescribe.getMaxtimes());
			return createJobAndTriggerWithStragty(roundIntrevalStragty, count, jobKey.getGroup(), jobKey.getName(),clazz1);
		} else {
			RoundDoubleTimeStragty roundDoubleTimeStragty = (RoundDoubleTimeStragty)instance;
			roundDoubleTimeStragty.setBaseDelay(taskDescribe.getBaseDelay());
			roundDoubleTimeStragty.setMaxtimes(taskDescribe.getMaxtimes());
			return createJobAndTriggerWithStragty(roundDoubleTimeStragty, count, jobKey.getGroup(), jobKey.getName(),clazz1);
		}
	}
	
	public JobDetail createNewJob(int count,JobKey jobKey,IStragty stragty,Class<? extends Job> clazz){
		JobDetailImpl job = new JobDetailImpl();
		job.setKey(jobKey);
		job.setJobClass(clazz);
		job.getJobDataMap().put("taskDescribe", new TaskDescribe(count,jobKey,stragty).serialize());
		return job;
	}
	
	public void stopJob(JobDetail job){
		try {
			sche.deleteJob(job.getKey());
		} catch (SchedulerException e) {
			
		}
	}
	
	public SimpleTrigger createIntrvalSimpleTrigger(JobKey jobKey,int time,Long interval){
		/*SimpleTrigger simpleTrigger = new SimpleTriggerImpl("simpleIntrvalTrigger", "triggerGroup-s1");
		long now = System.currentTimeMillis();
		//long start = now + 30 * 1000;
		// ((SimpleTriggerImpl) simpleTrigger).setStartTime(new Date(start));
		 ((SimpleTriggerImpl) simpleTrigger).setRepeatInterval(1000);
		 ((SimpleTriggerImpl) simpleTrigger).setRepeatCount(10);
		 return simpleTrigger;*/
		Trigger trigger=TriggerBuilder.newTrigger()
		        .startNow()
		        .withIdentity(jobKey.getName(),jobKey.getGroup())
		        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
		            .withIntervalInMilliseconds(interval)
		            .withRepeatCount(time-1))
		        .build();
		return (SimpleTrigger)trigger;
	}
	
	
	public SimpleTrigger createDelaySimpleTrigger(JobKey jobKey, Long delay){
		 
		SimpleTrigger simpleTrigger = new SimpleTriggerImpl(jobKey.getName(), jobKey.getGroup());
		long now = System.currentTimeMillis();
		long start = now + delay;
		 ((SimpleTriggerImpl) simpleTrigger).setStartTime(new Date(start));
		 return simpleTrigger;
	}
	
	
	public void runJobByContext(QuestContext context) {
		runJobByTrigger(context.getJobDetail(), context.getTrigger());
	}
	
	public void runJobByTrigger(JobDetail job,Trigger trigger){
		rl.lock();
		if(null == sche){
			try {
				sche = StdSchedulerFactory.getDefaultScheduler();
				sche.start();
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}
		try {
			if(sche.isShutdown()){
				sche = StdSchedulerFactory.getDefaultScheduler();
				sche.start();
			}
		} catch (SchedulerException e1) {
			// TODO 自动生成 catch 块
			e1.printStackTrace();
		}
		rl.unlock();
		
		try {
			System.out.println("任务加入调度器");
			Date date = sche.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("开始初始化认读调度器");
		QuestDispatcher.getInstance();
		
		//QuestDispatcher instance = QuestDispatcher.getInstance();
		//QuestContext questContext = instance.createJobAndTriggerWithStragty(new RoundDoubleTimeStragty(5, 1000L), 0, "手机银行组", "手机银行任务名");
		//instance.runJobByContext(questContext);
		/**
		 * spring
		 * 启动
		 */
		AutoStartSingletonIntervalJob.runThisJobIfNotExist();
	}


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext = applicationContext;
		TestService service = applicationContext.getBean(TestService.class);
		System.out.println(this);
		System.out.println("setApplicationContext>>>>>>>>>" + service.getById(1));
	}
	
}
