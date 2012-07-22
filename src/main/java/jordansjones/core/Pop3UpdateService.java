package jordansjones.core;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.inject.Inject;
import com.yammer.dropwizard.logging.Log;
import com.yammer.dropwizard.util.Duration;
import jordansjones.config.Pop3UpdateServiceConfig;
import org.apache.commons.net.pop3.POP3Client;
import org.apache.commons.net.pop3.POP3MessageInfo;
import org.apache.commons.net.pop3.POP3SClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class Pop3UpdateService extends AbstractScheduledService implements UpdateService {

	private static final Log logger = Log.forClass(Pop3UpdateService.class);

	private final Duration disabledDuration = Duration.milliseconds(0);

	private final Pop3UpdateServiceConfig config;
	private final EventBus eventBus;

	@Inject
	public Pop3UpdateService(final Pop3UpdateServiceConfig config, final EventBus eventBus) {
		this.config = config;
		this.eventBus = eventBus;
	}

	@Override
	protected void runOneIteration() throws Exception {
		final POP3Client client = this.createClient();
		final String server = this.config.getServer();
		final int port = this.config.getPort();
		try {
			client.connect(server, port);
		}
		catch (IOException e) {
			logger.error(e, "Failed to connect to server {}:{}", server, port);
			return;
		}

		try {
			if (!client.login(this.config.getUsername(), this.config.getPassword())) {
				logger.error("Authentication failed");
				return;
			}

			final POP3MessageInfo[] pop3MessageInfos = client.listMessages();
			if (pop3MessageInfos == null || pop3MessageInfos.length == 0) {
				return;
			}

			for (POP3MessageInfo messageInfo : pop3MessageInfos) {
				final int msgNumber = messageInfo.number;
				final String identifier = messageInfo.identifier;
				final BufferedReader reader = (BufferedReader) client.retrieveMessageTop(msgNumber, 0);
				printMessageInfo(reader, msgNumber);
			}

		}
		catch (IOException e) {
			logger.error(e, "Failed to fetch emails.");
		}
		finally {
			try {
				client.logout();
			}
			catch (IOException ignored) {}

			if (client.isConnected()) {
				try {
					client.disconnect();
				}
				catch (IOException ignored) {}
			}
		}
	}

	@Override
	protected Scheduler scheduler() {
		final long initialDelay = config.getInitialDelay().toSeconds();
		final long period = config.getPeriod().toSeconds();
		return Scheduler.newFixedRateSchedule(initialDelay, period, TimeUnit.SECONDS);
	}

	private POP3Client createClient() {
		POP3Client client = null;

		if (config.isUseSSL()) {
			client = new POP3SClient(true);
		}
		else {
			client = new POP3Client();
		}

		final Duration socketTimeout = config.getSocketTimeout();
		if (socketTimeout != null && !disabledDuration.equals(socketTimeout)) {
			client.setDefaultTimeout((int) socketTimeout.toMilliseconds());
		}

		return client;
	}

	public static final void printMessageInfo(BufferedReader reader, int id) throws IOException  {
		String from = "";
		String subject = "";
		String line;
		while ((line = reader.readLine()) != null)
		{
			String lower = line.toLowerCase(Locale.ENGLISH);
			if (lower.startsWith("from: ")) {
				from = line.substring(6).trim();
			}  else if (lower.startsWith("subject: ")) {
				subject = line.substring(9).trim();
			}
		}

		System.out.println(Integer.toString(id) + " From: " + from + "  Subject: " + subject);
	}
}
