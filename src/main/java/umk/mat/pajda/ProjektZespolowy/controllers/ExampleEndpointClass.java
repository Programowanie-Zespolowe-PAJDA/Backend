package umk.mat.pajda.ProjektZespolowy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ExampleEndpointClass {

    @GetMapping("/hello")
    @ResponseBody
    public String sayHello(){
        return "hello";
    }
}
