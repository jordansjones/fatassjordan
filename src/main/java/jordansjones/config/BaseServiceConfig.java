package jordansjones.config;

import com.yammer.dropwizard.util.Duration;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 */
public abstract class BaseServiceConfig {

	@NotNull
	private Duration initialDelay = Duration.minutes(1);
	@NotNull
	private Duration period = Duration.minutes(30);
	@Max(16)
	@Min(1)
	private int minThreads = 1;
	@Max(16)
	@Min(1)
	private int maxThreads = 8;

	public Duration getInitialDelay() {
		return initialDelay;
	}

	public void setInitialDelay(Duration initialDelay) {
		this.initialDelay = initialDelay;
	}

	public Duration getPeriod() {
		return period;
	}

	public void setPeriod(Duration period) {
		this.period = period;
	}

	public int getMaxThreads() {
		return maxThreads;
	}

	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}

	public int getMinThreads() {
		return minThreads;
	}

	public void setMinThreads(int minThreads) {
		this.minThreads = minThreads;
	}
}
