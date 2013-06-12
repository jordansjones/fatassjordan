package jordansjones.core;

import com.google.common.util.concurrent.Service;
import com.google.inject.Inject;
import com.yammer.dropwizard.lifecycle.Managed;
import com.yammer.dropwizard.logging.Log;
import jordansjones.config.UpdateServiceConfig;

import java.util.concurrent.ExecutorService;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public class UpdateServiceManagedObject<SVC extends UpdateService, CNF extends UpdateServiceConfig> implements Managed {

	private final SVC updateService;
	private final CNF serviceConfig;

	@Inject
	public UpdateServiceManagedObject(final SVC updateService, final CNF serviceConfig, final ExecutorService executorService) {
		this.updateService = checkNotNull(updateService);
		this.serviceConfig = checkNotNull(serviceConfig);

		this.updateService.addListener(
			new ServiceListener(
				Log.named(initLoggerName())
			),
			executorService
		);
	}

	private String initLoggerName() {
		return serviceName();
	}

	@Override
	public void start() throws Exception {
		if (!this.updateService.isRunning() && this.serviceConfig.isEnabled()) {
			this.updateService.start();
		}
	}

	@Override
	public void stop() throws Exception {
		if (this.updateService.isRunning()) {
			this.updateService.stop();
		}
	}

	private String serviceName() {
		return this.updateService.getClass().getName();
	}

	private class ServiceListener implements Service.Listener {

		private final Log logger;

		private ServiceListener(final Log logger) {
			this.logger = logger;
		}

		/**
		 * Called when the service transitions from {@linkplain com.google.common.util.concurrent.Service.State#NEW NEW} to
		 * {@linkplain com.google.common.util.concurrent.Service.State#STARTING STARTING}. This occurs when {@link com.google.common.util.concurrent.Service#start} or
		 * {@link com.google.common.util.concurrent.Service#startAndWait} is called the first time.
		 */
		@Override
		public void starting() {
			logger.info("Entering '{}' phase...", Service.State.STARTING.name());
		}

		/**
		 * Called when the service transitions from {@linkplain com.google.common.util.concurrent.Service.State#STARTING STARTING} to
		 * {@linkplain com.google.common.util.concurrent.Service.State#RUNNING RUNNING}. This occurs when a service has successfully started.
		 */
		@Override
		public void running() {
			logger.info("Entering '{}' phase...", Service.State.RUNNING.name());
		}

		/**
		 * Called when the service transitions to the {@linkplain com.google.common.util.concurrent.Service.State#STOPPING STOPPING} state. The
		 * only valid values for {@code from} are {@linkplain com.google.common.util.concurrent.Service.State#STARTING STARTING} or
		 * {@linkplain com.google.common.util.concurrent.Service.State#RUNNING RUNNING}.  This occurs when {@link com.google.common.util.concurrent.Service#stop} is called.
		 *
		 * @param from The previous state that is being transitioned from.
		 */
		@Override
		public void stopping(final Service.State from) {
			logger.info("Entering '{}' phase from '{}' phase", Service.State.STOPPING.name(), from.name());
		}

		/**
		 * Called when the service transitions to the {@linkplain com.google.common.util.concurrent.Service.State#TERMINATED TERMINATED} state.
		 * The {@linkplain com.google.common.util.concurrent.Service.State#TERMINATED TERMINATED} state is a terminal state in the transition
		 * diagram.  Therefore, if this method is called, no other methods will be called on the
		 * {@link com.google.common.util.concurrent.Service.Listener}.
		 *
		 * @param from The previous state that is being transitioned from.  The only valid values for
		 *             this are {@linkplain com.google.common.util.concurrent.Service.State#NEW NEW}, {@linkplain com.google.common.util.concurrent.Service.State#RUNNING RUNNING} or
		 *             {@linkplain com.google.common.util.concurrent.Service.State#STOPPING STOPPING}.
		 */
		@Override
		public void terminated(final Service.State from) {
			logger.info("Entering '{}' phase from '{}' phase", Service.State.TERMINATED.name(), from.name());
		}

		/**
		 * Called when the service transitions to the {@linkplain com.google.common.util.concurrent.Service.State#FAILED FAILED} state. The
		 * {@linkplain com.google.common.util.concurrent.Service.State#FAILED FAILED} state is a terminal state in the transition diagram.
		 * Therefore, if this method is called, no other methods will be called on the {@link com.google.common.util.concurrent.Service.Listener}.
		 *
		 * @param from    The previous state that is being transitioned from.  Failure can occur in any
		 *                state with the exception of {@linkplain com.google.common.util.concurrent.Service.State#NEW NEW} or
		 *                {@linkplain com.google.common.util.concurrent.Service.State#TERMINATED TERMINATED}.
		 * @param failure The exception that caused the failure.
		 */
		@Override
		public void failed(final Service.State from, final Throwable failure) {
			logger.error(failure, "Failed during '{}' phase", from.name());
		}
	}
}
