package pewee.quests;
/**
 * Interval轮询
 * @author pewee
 *
 */
public class RoundIntrevalStragty implements IStragty{
	
	//-1表示无限制次数,永远执行
	private int maxtimes;
	
	//间隔时间,毫秒
	private Long interval;
	
	public int getMaxtimes() {
		return maxtimes;
	}

	public void setMaxtimes(int maxtimes) {
		this.maxtimes = maxtimes;
	}

	public RoundIntrevalStragty() {
	}
	
	public RoundIntrevalStragty(int times,Long interval) {
		this.maxtimes = times;
		this.interval = interval;
	}
	
	public Long getInterval() {
		return interval;
	}

	public void setInterval(Long interval) {
		this.interval = interval;
	}

	@Override
	public long doStragty(int times) {
		// TODO Auto-generated method stub
		if(-1 == this.maxtimes) {
			return this.interval;
		} else {
			if(times < this.maxtimes) {
				return this.interval;
			} else {
				return -1L;
			}
		}
	}

}
