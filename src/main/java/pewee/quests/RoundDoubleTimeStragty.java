package pewee.quests;
/**
 * 时间加倍轮训
 * @author pewee
 *
 */
public class RoundDoubleTimeStragty implements IStragty {
	
	public RoundDoubleTimeStragty() {}
	
	public RoundDoubleTimeStragty(int maxtimes,Long baseDelay) {
		this.maxtimes = maxtimes;
		this.baseDelay = baseDelay;
		
	}
	
	public int getMaxtimes() {
		return maxtimes;
	}

	public void setMaxtimes(int maxtimes) {
		this.maxtimes = maxtimes;
	}

	public Long getBaseDelay() {
		return baseDelay;
	}

	public void setBaseDelay(Long baseDelay) {
		this.baseDelay = baseDelay;
	}

	//-1表示无限制次数,永远执行
	private int maxtimes;
	
	//基础延时,往后加倍
	private Long baseDelay;
	
	
	@Override
	public long doStragty(int times) {
		// TODO Auto-generated method stub
		if(-1 == this.maxtimes) {
			return getBase(times)*this.baseDelay;
		}
		if(times < this.maxtimes) {
			return getBase(times)*this.baseDelay;
		} else {
			return -1L;
		}
	}
	
	private int getBase(int i) {
		if(i == 0) {
			return 1;
		} 
		return 2*getBase(i-1);
	}
	
}
