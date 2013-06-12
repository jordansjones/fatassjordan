package jordansjones;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.concurrent.ExecutorService;

class EventBusProvider implements Provider<EventBus> {

	private final AsyncEventBus eventBus;

	@Inject
	EventBusProvider(final ExecutorService executorService) {
		this.eventBus = new AsyncEventBus(executorService);
	}

	@Override
	public EventBus get() {
		return eventBus;
	}
}
