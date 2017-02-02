package poussecafe.sample.app;

import java.time.Duration;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import poussecafe.consequence.CommandHandlingResult;
import poussecafe.consequence.CommandProcessor;
import poussecafe.sample.domain.ProductKey;
import poussecafe.sample.domain.ProductRepository;
import poussecafe.sample.workflow.CreateProduct;

@Path("/")
public class RestResource {

    @Autowired
    private CommandProcessor commandProcessor;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductConverter productConverter;

    @POST
    @Path("product")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public void createProduct(CreateProductView input,
            @Suspended final AsyncResponse asyncResponse) {
        ProductKey productKey = new ProductKey(input.key);
        CommandHandlingResult result = commandProcessor.processCommand(new CreateProduct(productKey)).get(Duration.ofSeconds(10));
        if (result.isSuccess()) {
            asyncResponse.resume(productConverter.convert(productRepository.get(productKey)));
        } else {
            asyncResponse.resume(new RuntimeException("Unable to create product: " + result.getFailureDescription()));
        }
    }

}
