package pewee.quests;

import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;

public class QuestTest {
	public static void main(String[] args) throws InterruptedException {
		QuestDispatcher instance = QuestDispatcher.getInstance();
		QuestContext questContext = instance.createJobAndTriggerWithStragty(new RoundDoubleTimeStragty(5, 1000L), 0, "手机银行组", "手机银行任务名",QueryJob.class);
		instance.runJobByContext(questContext);
		
		Thread.currentThread().wait();
		
		
		/*String str = "{\"baseDelay\":1000,\"count\":3,\"group\":\"手机银行组\",\"interval\":-1,\"maxtimes\":5,\"name\":\"手机银行任务名-1-2-3\",\"stragrtName\":\"pewee.quests.RoundDoubleTimeStragty\"}";
		TaskDescribe deserialize = TaskDescribe.deserialize(str);
		System.out.println(deserialize);*/
	}
}	
