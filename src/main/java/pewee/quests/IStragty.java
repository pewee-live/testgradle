package pewee.quests;

import java.util.concurrent.TimeUnit;

/***
 * quest执行策略
 * @author pewee
 *
 */
public interface IStragty {
	//时间单位毫秒
	static public TimeUnit timeunit = TimeUnit.MILLISECONDS;
	
	/***
	 * times从0开始,即第一次传0
	 * @param times
	 * @return
	 */
	public long doStragty(int times);//执行该策略,返回long>0,继续在long ms后执行,返回-1不执行
	
	public int getMaxtimes();
	
}
