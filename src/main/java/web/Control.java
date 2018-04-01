package web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class Control {
    @RequestMapping(value = "/agent",method = RequestMethod.GET)
    public String index() {
     //   System.out.println("asdsad");
        return "agent/user.html";
    }


    @RequestMapping(value = "/user",method = RequestMethod.GET)
    public String usersPage2() {
        return "alpha/user.html";
    }
}
