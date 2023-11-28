package umk.mat.pajda.ProjektZespolowy.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Tag(name = "Example Endpoint", description = "Controller for handling ExampleEndpoint API requests")
public class ExampleEndpointClass {

    @GetMapping("/hello")
    @ResponseBody
    @Operation(summary = "Get hellow", description = "Returns hellow")
    public String sayHello(){
        return "hello";
    }
}
