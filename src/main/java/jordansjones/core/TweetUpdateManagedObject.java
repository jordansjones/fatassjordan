package jordansjones.core;

import com.yammer.dropwizard.lifecycle.Managed;
import com.yammer.dropwizard.logging.Log;
import jordansjones.config.TweetUpdateServiceConfig;

import javax.inject.Inject;

public class TweetUpdateManagedObject implements Managed {

	private static final Log logger = Log.forClass(TweetUpdateManagedObject.class);

	private final TweetUpdateService tweetUpdateService;
	private final TweetUpdateServiceConfig serviceConfig;

	@Inject
	public TweetUpdateManagedObject(final TweetUpdateService tweetUpdateService, final TweetUpdateServiceConfig serviceConfig) {
		this.tweetUpdateService = tweetUpdateService;
		this.serviceConfig = serviceConfig;
	}

	@Override
	public void start() throws Exception {
		if (!this.tweetUpdateService.isRunning() && this.serviceConfig.isEnabled()) {
			logger.info("Starting {}", serviceName());
			this.tweetUpdateService.start();
		}
	}

	@Override
	public void stop() throws Exception {
		if (this.tweetUpdateService.isRunning()) {
			logger.info("Stopping {}", serviceName());
			this.tweetUpdateService.stop();
		}
	}

	private String serviceName() {
		return this.tweetUpdateService.getClass().getSimpleName();
	}

}
