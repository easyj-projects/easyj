package icu.easyj.core.util.time;

import java.util.Date;

/**
 * 记号时钟包装
 *
 * @author wangliang181230
 */
public class TickClockWrapper implements ITickClock {

	protected final ITickClock tickClock;

	/**
	 * 构造函数
	 *
	 * @param tickClock 记号时钟
	 */
	public TickClockWrapper(ITickClock tickClock) {
		this.tickClock = tickClock;
	}


	//region Override

	@Override
	public Date now() {
		return tickClock.now();
	}

	@Override
	public long currentTimeMillis() {
		return tickClock.currentTimeMillis();
	}

	@Override
	public long currentTimeMicros() {
		return tickClock.currentTimeMicros();
	}

	@Override
	public long currentTimeNanos() {
		return tickClock.currentTimeNanos();
	}

	@Override
	public long getBaseEpochMicros() {
		return tickClock.getBaseEpochMicros();
	}

	@Override
	public long getBaseTickNanos() {
		return tickClock.getBaseTickNanos();
	}

	@Override
	public long getPassedNanos() {
		return tickClock.getPassedNanos();
	}

	//endregion
}
