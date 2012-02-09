package net.sereneproject.collector.dto;

import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class ProbeValueDto {
    @NotNull
    private String uuid;

    private String name;

    private String value;

}
