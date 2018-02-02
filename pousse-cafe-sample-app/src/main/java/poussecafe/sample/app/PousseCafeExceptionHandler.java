package poussecafe.sample.app;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import poussecafe.exception.NotFoundException;

@ControllerAdvice
public class PousseCafeExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ErrorView handleNotFound(HttpServletRequest req, Exception ex) {
        ErrorView view = new ErrorView();
        view.message = ex.getMessage();
        return view;
    }
}
