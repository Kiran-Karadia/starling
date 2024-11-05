package kiran.interview.httfilters;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import kiran.interview.exceptions.MissingAuthHeaderException;
import org.reactivestreams.Publisher;

@Filter("/starling/**")
public class AuthHeaderFilter implements HttpServerFilter {

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        if (!request.getHeaders().contains(HttpHeaders.AUTHORIZATION)) {
            throw new MissingAuthHeaderException("Authorization header is missing");
        }
        return chain.proceed(request);
    }

}

