package org.example.app.web.validator;

public interface ParamValidator<T> {

    ValidateErrors validate(T request);

}
