package net.sereneproject.collector.dto;

import javax.annotation.Nullable;
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

    @Nullable
    private String name;

    @NotNull
    private String value;

}
