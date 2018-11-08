package pewee.quests.business;

import java.util.Date;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import pewee.quests.QuestContext;
import pewee.quests.QuestDispatcher;
import pewee.quests.RoundIntrevalStragty;
/**
 * 注意：当使用 @PersistJobDataAfterExecution 注解时，为了避免并发时，存储数据造成混乱，强烈建议把 @DisallowConcurrentExecution 注解也加上。
 * @author pewee
 *
 */
@Component
@DisallowConcurrentExecution//禁止同一个JOBDETAIL并发执行
@PersistJobDataAfterExecution//用在 Job 实现类上，表示一个有状态的任务，意思是当正常执行完 Job 后，JobDataMap 中的数据应该被改动，以被下一次调用时用。
public class AutoStartSingletonIntervalJob implements Job{
	
	static QuestDispatcher instance = QuestDispatcher.getInstance();
	
	static final Logger logger = LoggerFactory.getLogger(AutoStartSingletonIntervalJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		logger.info("开始interval任务");
		logger.info("本次时间：{}",new Date().toGMTString());
		
		{
			//业务
			String index = context.getJobDetail().getJobDataMap().getString("index");
			logger.info("获取index：{}",index);
			context.getJobDetail().getJobDataMap().put("index", (Integer.valueOf(index)+ 1)+ "");
		}
	}
	
	public static void injectParams(String index,QuestContext context) {
		JobDataMap map = context.getJobDetail().getJobDataMap();
		map.put("index", index);
	}
	
	public static void runThisJobIfNotExist() {
		boolean flag = instance.hasThisJob("AAAA", "AAAA");
		if(flag) {
			logger.info("有了!!!!!!!!!!!");
			return;
		}
		logger.info("没有，开始！！！");
		QuestContext questContext = instance.createJobAndTriggerWithStragty(new RoundIntrevalStragty(Integer.MAX_VALUE, 5000L), 0, "AAAA", "AAAA",AutoStartSingletonIntervalJob.class);
		injectParams("1", questContext);
		instance.runJobByContext(questContext);
	}
	
}
