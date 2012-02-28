package net.sereneproject.collector.web;

import net.sereneproject.collector.domain.ProbeValue;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/probevalues")
@Controller
@RooWebScaffold(path = "probevalues", formBackingObject = ProbeValue.class)
public class ProbeValueController {
}
