// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package net.sereneproject.collector.web;

import java.io.UnsupportedEncodingException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import net.sereneproject.collector.domain.Server;
import net.sereneproject.collector.domain.ServerGroup;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect ServerController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST)
    public String ServerController.create(@Valid Server server, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("server", server);
            return "servers/create";
        }
        uiModel.asMap().clear();
        server.persist();
        return "redirect:/servers/" + encodeUrlPathSegment(server.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String ServerController.createForm(Model uiModel) {
        uiModel.addAttribute("server", new Server());
        List dependencies = new ArrayList();
        if (ServerGroup.countServerGroups() == 0) {
            dependencies.add(new String[]{"servergroup", "servergroups"});
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "servers/create";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String ServerController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("server", Server.findServer(id));
        uiModel.addAttribute("itemId", id);
        return "servers/show";
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String ServerController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            uiModel.addAttribute("servers", Server.findServerEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) Server.countServers() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("servers", Server.findAllServers());
        }
        return "servers/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public String ServerController.update(@Valid Server server, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("server", server);
            return "servers/update";
        }
        uiModel.asMap().clear();
        server.merge();
        return "redirect:/servers/" + encodeUrlPathSegment(server.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String ServerController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("server", Server.findServer(id));
        return "servers/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String ServerController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Server.findServer(id).remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/servers";
    }
    
    @ModelAttribute("servers")
    public Collection<Server> ServerController.populateServers() {
        return Server.findAllServers();
    }
    
    @ModelAttribute("servergroups")
    public Collection<ServerGroup> ServerController.populateServerGroups() {
        return ServerGroup.findAllServerGroups();
    }
    
    String ServerController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        }
        catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}