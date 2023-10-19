package icstar.kbdsi.apps.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"http://localhost:4200", "http://https://kbdsi-icstar-fe.vercel.app/", "https://kbdsi-icstar-g0cg82eie-lh007lucky-gmailcom.vercel.app/" })


@RestController
public class HomeController {

    @GetMapping("/")
    public String index(){
        return "API For KBDSI Accounting System (ICStar Hackathon 2023) \n\n http://https://kbdsi-icstar-fe.vercel.app/";
    }
}
