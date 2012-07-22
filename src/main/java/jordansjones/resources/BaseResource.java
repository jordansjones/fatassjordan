package jordansjones.resources;

import com.google.common.eventbus.EventBus;
import jordansjones.entities.StatusUpdate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
abstract class BaseResource {

	protected final DateTimeFormatter dateFormatter = DateTimeFormat.mediumDate();
	protected final DateTimeFormatter timeFormatter = DateTimeFormat.mediumTime();

	protected final ConcurrentHashMap<Long, StatusUpdate> statusUpdates = new ConcurrentHashMap<Long, StatusUpdate>();

	protected final EventBus eventBus;

	BaseResource(EventBus eventBus) {
		this.eventBus = eventBus;
		this.eventBus.register(this);
	}

}
