package jordansjones;

import com.google.common.base.Strings;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.bundles.AssetsBundle;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.views.ViewBundle;
import jordansjones.config.Pop3UpdateServiceConfig;
import jordansjones.config.TweetUpdateServiceConfig;
import jordansjones.health.TemplateHealthCheck;
import jordansjones.resources.EntriesResource;
import jordansjones.resources.IndexResource;
import jordansjones.resources.TweetsResource;

public class FatassService extends Service<ServiceConfiguration> {

	private static final String ENV_USERNAME = "EMAIL_USER";
	private static final String ENV_PASSWORD = "EMAIL_PWD";
	private static final String ENV_SCREENNAME = "TWEET_USER";

	private static final String PortJvmArgName = "dw.http.port";
	private static final String AdminPortJvmArgName = "dw.http.adminPort";
	private static final String defaultPort = "8080";

	public static void main(String[] args) throws Exception {
		String port = System.getenv("PORT");
		if (Strings.isNullOrEmpty(port))
			port = defaultPort;
		System.setProperty(PortJvmArgName, port);
		System.setProperty(AdminPortJvmArgName, port);

		new FatassService().run(args);
	}

	private FatassService() {
		super("fatass");
		addBundle(new AssetsBundle());
		addBundle(new ViewBundle());
	}

	@Override
	protected void initialize(final ServiceConfiguration configuration, final Environment environment) throws Exception {
		initializeConfig(configuration);

		final Injector injector = Guice.createInjector(new GuiceConfig(configuration, environment));

		environment.addResource(injector.getInstance(IndexResource.class));
		environment.addResource(injector.getInstance(TweetsResource.class));
		environment.addResource(injector.getInstance(EntriesResource.class));

		environment.addHealthCheck(injector.getInstance(TemplateHealthCheck.class));
		environment.manage(injector.getInstance(Key.get(GuiceConfig.TweetManagedTypeLiteral)));
		environment.manage(injector.getInstance(Key.get(GuiceConfig.Pop3ManagedTypeLiteral)));
	}

	private void initializeConfig(final ServiceConfiguration config) {
		final Pop3UpdateServiceConfig pop3UpdateServiceConfig = config.getPop3UpdateServiceConfiguration();
		pop3UpdateServiceConfig.setUsername(System.getenv(ENV_USERNAME));
		pop3UpdateServiceConfig.setPassword(System.getenv(ENV_PASSWORD));

		final TweetUpdateServiceConfig tweetUpdateServiceConfig = config.getTweetUpdateServiceConfiguration();
		tweetUpdateServiceConfig.setScreenName(System.getenv(ENV_SCREENNAME));

	}
}
