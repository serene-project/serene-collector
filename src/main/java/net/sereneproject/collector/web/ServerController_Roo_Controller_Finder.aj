// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package net.sereneproject.collector.web;

import java.lang.Long;
import java.lang.String;
import net.sereneproject.collector.domain.Server;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

privileged aspect ServerController_Roo_Controller_Finder {
    
    @RequestMapping(params = { "find=ByUuidMostSigBitsEqualsAndUuidLeastSigBitsEquals", "form" }, method = RequestMethod.GET)
    public String ServerController.findServersByUuidMostSigBitsEqualsAndUuidLeastSigBitsEqualsForm(Model uiModel) {
        return "servers/findServersByUuidMostSigBitsEqualsAndUuidLeastSigBitsEquals";
    }
    
    @RequestMapping(params = "find=ByUuidMostSigBitsEqualsAndUuidLeastSigBitsEquals", method = RequestMethod.GET)
    public String ServerController.findServersByUuidMostSigBitsEqualsAndUuidLeastSigBitsEquals(@RequestParam("uuidMostSigBits") Long uuidMostSigBits, @RequestParam("uuidLeastSigBits") Long uuidLeastSigBits, Model uiModel) {
        uiModel.addAttribute("servers", Server.findServersByUuidMostSigBitsEqualsAndUuidLeastSigBitsEquals(uuidMostSigBits, uuidLeastSigBits).getResultList());
        return "servers/list";
    }
    
}
