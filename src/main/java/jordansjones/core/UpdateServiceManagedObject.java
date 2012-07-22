package jordansjones.core;

import com.google.inject.Inject;
import com.yammer.dropwizard.lifecycle.Managed;
import com.yammer.dropwizard.logging.Log;
import jordansjones.config.UpdateServiceConfig;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public class UpdateServiceManagedObject<SVC extends UpdateService, CNF extends UpdateServiceConfig> implements Managed {

	private final Log logger;

	private final SVC updateService;
	private final CNF serviceConfig;

	@Inject
	public UpdateServiceManagedObject(final SVC updateService, final CNF serviceConfig) {
		this.updateService = checkNotNull(updateService);
		this.serviceConfig = checkNotNull(serviceConfig);

		this.logger = Log.named(initLoggerName());
	}

	private String initLoggerName() {
		return String.format("%s<%s, %s>",
			UpdateServiceManagedObject.class.getSimpleName(),
			serviceName(),
			serviceConfig.getClass().getSimpleName()
		);
	}

	@Override
	public void start() throws Exception {
		if (!this.updateService.isRunning() && this.serviceConfig.isEnabled()) {
			logger.info("Starting {}", serviceName());
			this.updateService.start();
		}
	}

	@Override
	public void stop() throws Exception {
		if (this.updateService.isRunning()) {
			logger.info("Stopping {}", serviceName());
			this.updateService.stop();
		}
	}

	private String serviceName() {
		return this.updateService.getClass().getSimpleName();
	}
}
