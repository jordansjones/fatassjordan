package jordansjones.resources;


import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.yammer.dropwizard.logging.Log;
import jordansjones.entities.StatusUpdate;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

@Path("/entries")
@Produces(MediaType.APPLICATION_JSON)
public class EntriesResource extends BaseResource {

	private static final Log logger = Log.forClass(EntriesResource.class);

	@Inject
	public EntriesResource(final EventBus eventBus) {
		super(eventBus);
	}

	@GET
	public Collection<StatusUpdate> get() {
		logger.info("Returning {} Status updates", statusUpdates.size());
		return statusUpdates.values();
	}

//	@Subscribe
//	@AllowConcurrentEvents
//	public void handleNewEntries(Tweet[] tweets) {
//		if (tweets == null)
//			tweets = new Tweet[0];
//
//		logger.info("{} new tweets on the event bus.", tweets.length);
//
//		final List<StatusUpdate> updates = Lists.newArrayList();
//		for (Tweet tweet : tweets) {
//			final DateTime createdAt = tweet.getCreatedAt();
//			final String weightText = tweet.getText();
//			float weight = 0.0f;
//			try {
//				weight = Float.parseFloat(weightText);
//			}
//			catch (NumberFormatException nfe) {
//				continue;
//			}
//
//			if (weight < 1) {
//				continue;
//			}
//
//			final StatusUpdate statusUpdate = new StatusUpdate(
//				tweet.getIdStr(),
//				dateFormatter.print(createdAt),
//				timeFormatter.print(createdAt),
//				weight,
//				createdAt.toInstant().getMillis()
//			);
//			if (this.statusUpdates.putIfAbsent(tweet.getId(), statusUpdate) == null)
//				updates.add(statusUpdate);
//		}
//
//		// TODO: Push status updates to the client
//	}
}
