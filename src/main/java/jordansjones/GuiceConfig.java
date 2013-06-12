package jordansjones;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.yammer.dropwizard.client.JerseyClient;
import com.yammer.dropwizard.client.JerseyClientFactory;
import com.yammer.dropwizard.config.Environment;
import jordansjones.config.Pop3UpdateServiceConfig;
import jordansjones.config.TweetUpdateServiceConfig;
import jordansjones.core.Pop3UpdateService;
import jordansjones.core.TweetUpdateService;
import jordansjones.core.UpdateServiceManagedObject;
import jordansjones.health.TemplateHealthCheck;
import jordansjones.resources.EntriesResource;
import jordansjones.resources.IndexResource;
import jordansjones.resources.TweetsResource;

import java.util.concurrent.ExecutorService;

class GuiceConfig extends AbstractModule {

	public static final TypeLiteral<UpdateServiceManagedObject<TweetUpdateService, TweetUpdateServiceConfig>> TweetManagedTypeLiteral = new TypeLiteral<UpdateServiceManagedObject<TweetUpdateService, TweetUpdateServiceConfig>>() {};
	public static final TypeLiteral<UpdateServiceManagedObject<Pop3UpdateService, Pop3UpdateServiceConfig>> Pop3ManagedTypeLiteral = new TypeLiteral<UpdateServiceManagedObject<Pop3UpdateService, Pop3UpdateServiceConfig>>() {};

	private final ServiceConfiguration serviceConfig;
	private final Environment environment;

	GuiceConfig(ServiceConfiguration serviceConfig, Environment environment) {
		this.serviceConfig = serviceConfig;
		this.environment = environment;
	}

	@Override
	protected void configure() {
		bind(ServiceConfiguration.class).toInstance(this.serviceConfig);
		bind(TweetUpdateServiceConfig.class).toInstance(this.serviceConfig.getTweetUpdateServiceConfiguration());
		bind(Pop3UpdateServiceConfig.class).toInstance(this.serviceConfig.getPop3UpdateServiceConfiguration());
		bind(Environment.class).toInstance(this.environment);

		final JerseyClientFactory clientFactory = new JerseyClientFactory(this.serviceConfig.getHttpClientConfiguration());
		bind(JerseyClient.class).toInstance(clientFactory.build(this.environment));

		bind(IndexResource.class).in(Scopes.NO_SCOPE);
		bind(TweetsResource.class).in(Scopes.NO_SCOPE);
		bind(EntriesResource.class).in(Scopes.NO_SCOPE);

		bind(TemplateHealthCheck.class).in(Scopes.NO_SCOPE);

		bind(TweetUpdateService.class).in(Scopes.NO_SCOPE);
		bind(TweetManagedTypeLiteral).in(Scopes.NO_SCOPE);

		bind(Pop3UpdateService.class).in(Scopes.NO_SCOPE);
		bind(Pop3ManagedTypeLiteral).in(Scopes.NO_SCOPE);

		bind(ExecutorService.class).toProvider(ExecutorServiceProvider.class).asEagerSingleton();
		bind(EventBus.class).toProvider(EventBusProvider.class).asEagerSingleton();
	}

}
