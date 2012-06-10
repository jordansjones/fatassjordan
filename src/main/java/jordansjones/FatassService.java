package jordansjones;

import com.google.common.base.Strings;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.bundles.AssetsBundle;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.views.ViewBundle;
import jordansjones.core.TweetUpdateManagedObject;
import jordansjones.health.TemplateHealthCheck;
import jordansjones.resources.IndexResource;
import jordansjones.resources.TweetsResource;

public class FatassService extends Service<ServiceConfiguration> {

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
		final Injector injector = Guice.createInjector(new GuiceConfig(configuration, environment));

		environment.addResource(injector.getInstance(IndexResource.class));
		environment.addResource(injector.getInstance(TweetsResource.class));

		environment.addHealthCheck(injector.getInstance(TemplateHealthCheck.class));
		environment.manage(injector.getInstance(TweetUpdateManagedObject.class));
	}
}
