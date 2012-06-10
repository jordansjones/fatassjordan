package jordansjones;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.yammer.dropwizard.config.Environment;
import jordansjones.config.TweetUpdateServiceConfig;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

class EventBusProvider implements Provider<EventBus> {

	private final AsyncEventBus eventBus;

	@Inject
	EventBusProvider(final TweetUpdateServiceConfig updateServiceConfig, final Environment environment) {
		final ExecutorService executorService = environment.managedExecutorService("eventbus-%d", updateServiceConfig.getMinThreads(), updateServiceConfig.getMaxThreads(), 60, TimeUnit.SECONDS);
		this.eventBus = new AsyncEventBus(executorService);
	}

	@Override
	public EventBus get() {
		return eventBus;
	}
}
