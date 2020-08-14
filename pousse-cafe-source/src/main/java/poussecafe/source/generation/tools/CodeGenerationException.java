package poussecafe.source.generation.tools;


@SuppressWarnings("serial")
public class CodeGenerationException extends RuntimeException {

    public CodeGenerationException() {

    }

    public CodeGenerationException(String message) {
        super(message);
    }

    public CodeGenerationException(Throwable cause) {
        super(cause);
    }

    public CodeGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
