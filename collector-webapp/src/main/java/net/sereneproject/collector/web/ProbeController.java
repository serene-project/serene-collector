package net.sereneproject.collector.web;

import net.sereneproject.collector.domain.Probe;

import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/probes")
@Controller
@RooWebScaffold(path = "probes", formBackingObject = Probe.class)
@RooWebJson(jsonObject = Probe.class)
public class ProbeController {
}
