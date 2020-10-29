package utadborda.application.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class GeneralExceptions {
    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "video not found")
    public static class RestaurantNotFoundException extends RuntimeException {
        public RestaurantNotFoundException() {
            super();
        }

        public RestaurantNotFoundException(String s, Throwable throwable) {
            super(s, throwable);
        }

        public RestaurantNotFoundException(String s) {
            super(s);
        }

        public RestaurantNotFoundException(Throwable throwable) {
            super(throwable);
        }
    }

    @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "User already exists")
    public static class UserAlreadyExistsException extends Exception {
        public UserAlreadyExistsException(String s) {
            super(s);
        }

        public UserAlreadyExistsException(String s, Throwable throwable) {
            super(s, throwable);
        }

        public UserAlreadyExistsException(Throwable throwable) {
            super(throwable);
        }
    }
}
