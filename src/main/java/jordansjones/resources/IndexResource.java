package jordansjones.resources;

import jordansjones.views.IndexView;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/")
@Produces(MediaType.TEXT_HTML)
public class IndexResource {

	@GET
	public IndexView get() {
		return new IndexView("Hello World!");
	}
}
