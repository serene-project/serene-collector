package net.sereneproject.collector.web;

import net.sereneproject.collector.domain.Server;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "servers", formBackingObject = Server.class)
@RequestMapping("/servers")
@Controller
public class ServerController {
}
