package poussecafe.sample.app;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import poussecafe.context.MetaApplicationContext;
import poussecafe.sample.command.CreateProduct;
import poussecafe.sample.domain.ProductKey;
import poussecafe.sample.workflow.ProductManagement;

@Path("/")
public class RestResource {

    @Autowired
    private MetaApplicationContext metaApplicationContext;

    @POST
    @Path("product")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public void createProduct(CreateProductView input) {
        ProductKey productKey = new ProductKey(input.key);
        metaApplicationContext.getProcess(ProductManagement.class).createProduct(new CreateProduct(productKey));
    }

}
