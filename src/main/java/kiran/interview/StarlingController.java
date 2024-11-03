package kiran.interview;

import io.micronaut.http.annotation.*;

@Controller("/starling")
public class StarlingController {

    @Get(uri="/", produces="text/plain")
    public String index() {
        return "Example Response";
    }
}