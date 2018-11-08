package pewee.quests;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pewee.entity.TestEntity;
import pewee.service.TestService;
import pewee.service.TestServiceImpl;
public class QueryJob implements Job  {
	
	static QuestDispatcher instance = QuestDispatcher.getInstance();
	

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		System.out.println("任务开始");
		System.out.println(instance);
		System.out.println(instance.getApplicationContext());
		TestEntity testEntity = instance.getApplicationContext().getBean(TestService.class).getById(2);
		System.out.println("查出"  + testEntity);
		
		JobDetail detail = context.getJobDetail();
        JobDataMap map = detail.getJobDataMap(); 
        String  string = (String) map.get("taskDescribe");
        System.out.println("获取taskDescribe:" + string);
        TaskDescribe taskDescribe = null; 
        taskDescribe = TaskDescribe.deserialize(string);
        int count = taskDescribe.getCount();
        if(count < 5){
        	//业务逻辑
        	boolean flag = false;
        	if(!flag){
        		System.out.println("第" + count + "次查询手机银行,未确定状态");
        		taskDescribe.setCount(count + 1);
	        	QuestContext context1 = instance.createJobAndTriggerWithTaskDescribe(taskDescribe);
	        	instance.runJobByContext(context1);
        	} else {
        		System.out.println("状态确定!");
        		instance.stopJob(detail);
        	}
        	
        	
        } else {
        	System.out.println("超过查询次数,终止任务!");
        	instance.stopJob(detail);
        }
		
	}

}
