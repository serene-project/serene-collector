package net.sereneproject.collector.web;

import java.util.UUID;

import net.sereneproject.collector.domain.ProbeValue;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/probevalues")
@Controller
@RooWebScaffold(path = "probevalues", formBackingObject = ProbeValue.class)
@RooWebJson(jsonObject = ProbeValue.class)
public class ProbeValueController {

    @RequestMapping(params = "find=ByProbeUUID", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindProbeValuesByProbeUUID(
            @RequestParam("probeUUID") UUID uuid) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(ProbeValue.toJsonArray(ProbeValue
                .findProbeValuesByProbeUUID(uuid).getResultList()), headers,
                HttpStatus.OK);
    }

}
