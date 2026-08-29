package nwl.projetos.deploytest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeployTestController {

    @GetMapping("/home")
    public String hello(){
        return "Te amo Jessica";
    }
}
