package jordansjones;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.yammer.dropwizard.client.JerseyClient;
import com.yammer.dropwizard.client.JerseyClientFactory;
import com.yammer.dropwizard.config.Environment;
import jordansjones.config.TweetUpdateServiceConfig;
import jordansjones.core.TweetUpdateManagedObject;
import jordansjones.core.TweetUpdateService;
import jordansjones.health.TemplateHealthCheck;
import jordansjones.resources.IndexResource;
import jordansjones.resources.TweetsResource;

class GuiceConfig extends AbstractModule {

	private final ServiceConfiguration serviceConfig;
	private final Environment environment;

	GuiceConfig(ServiceConfiguration serviceConfig, Environment environment) {
		this.serviceConfig = serviceConfig;
		this.environment = environment;
	}

	@Override
	protected void configure() {
		bind(ServiceConfiguration.class).toInstance(this.serviceConfig);
		bind(TweetUpdateServiceConfig.class).toInstance(this.serviceConfig.getUpdateServiceConfiguration());
		bind(Environment.class).toInstance(this.environment);

		final JerseyClientFactory clientFactory = new JerseyClientFactory(this.serviceConfig.getHttpClientConfiguration());
		bind(JerseyClient.class).toInstance(clientFactory.build(this.environment));

		bind(IndexResource.class).in(Scopes.NO_SCOPE);
		bind(TweetsResource.class).in(Scopes.NO_SCOPE);

		bind(TemplateHealthCheck.class).in(Scopes.NO_SCOPE);

		bind(TweetUpdateService.class).in(Scopes.NO_SCOPE);
		bind(TweetUpdateManagedObject.class).in(Scopes.NO_SCOPE);

		bind(EventBus.class).toProvider(EventBusProvider.class).asEagerSingleton();
	}

}
