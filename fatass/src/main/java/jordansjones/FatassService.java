package jordansjones;

import com.google.common.base.Strings;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.bundles.AssetsBundle;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.views.ViewBundle;
import jordansjones.resources.IndexResource;

public class FatassService extends Service<FatassServiceConfiguration> {

	private static final String PortJvmArgName = "dw.http.port";
	private static final String defaultPort = "8080";

	public static void main(String[] args) throws Exception {
		String port = System.getenv("PORT");
		if (Strings.isNullOrEmpty(port))
			port = defaultPort;
		System.setProperty(PortJvmArgName, port);
		new FatassService().run(args);
	}

	private FatassService() {
		super("fatass");
		addBundle(new AssetsBundle());
		addBundle(new ViewBundle());
	}

	@Override
	protected void initialize(final FatassServiceConfiguration configuration, final Environment environment) throws Exception {
		final String template = configuration.getTemplate();
		final String defaultName = configuration.getDefaultName();

		environment.addResource(new IndexResource(template, defaultName));
	}
}
