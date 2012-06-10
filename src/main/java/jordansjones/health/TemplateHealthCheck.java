package jordansjones.health;

import com.yammer.metrics.core.HealthCheck;

public class TemplateHealthCheck extends HealthCheck {

	public TemplateHealthCheck() {
		super("template");
	}

	@Override
	protected Result check() throws Exception {
		final String saying = "TEST";
		if (!saying.contains("TEST")) {
			return Result.unhealthy("template doesn't include a name");
		}
		return Result.healthy();
	}
}
