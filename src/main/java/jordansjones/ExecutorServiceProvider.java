package jordansjones;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.yammer.dropwizard.config.Environment;
import jordansjones.config.TweetUpdateServiceConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

class ExecutorServiceProvider implements Provider<ExecutorService> {

	private final ExecutorService executorService;

	@Inject
	ExecutorServiceProvider(final TweetUpdateServiceConfig serviceConfig, final Environment environment) {
		executorService = environment.managedExecutorService("executorService-%d", serviceConfig.getMinThreads(), serviceConfig.getMaxThreads(), 60, TimeUnit.SECONDS);
	}

	@Override
	public ExecutorService get() {
		return executorService;
	}
}
