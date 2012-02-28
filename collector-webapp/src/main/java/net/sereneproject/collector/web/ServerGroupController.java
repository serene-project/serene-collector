package net.sereneproject.collector.web;

import net.sereneproject.collector.domain.ServerGroup;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "servergroups", formBackingObject = ServerGroup.class)
@RequestMapping("/servergroups")
@Controller
public class ServerGroupController {
}
