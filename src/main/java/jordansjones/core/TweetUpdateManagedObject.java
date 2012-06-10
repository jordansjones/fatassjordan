package jordansjones.core;

import com.yammer.dropwizard.lifecycle.Managed;
import com.yammer.dropwizard.logging.Log;

import javax.inject.Inject;

public class TweetUpdateManagedObject implements Managed {

	private static final Log logger = Log.forClass(TweetUpdateManagedObject.class);

	private final TweetUpdateService tweetUpdateService;

	@Inject
	public TweetUpdateManagedObject(final TweetUpdateService tweetUpdateService) {
		this.tweetUpdateService = tweetUpdateService;
	}

	@Override
	public void start() throws Exception {
		if (!this.tweetUpdateService.isRunning()) {
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
