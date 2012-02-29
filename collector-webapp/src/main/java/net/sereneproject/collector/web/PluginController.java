package net.sereneproject.collector.web;

import net.sereneproject.collector.domain.Plugin;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/plugins")
@Controller
@RooWebScaffold(path = "plugins", formBackingObject = Plugin.class)
@RooWebJson(jsonObject = Plugin.class)
public class PluginController {
}
