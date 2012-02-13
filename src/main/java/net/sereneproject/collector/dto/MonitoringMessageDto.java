package net.sereneproject.collector.dto;

import java.util.Collection;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class MonitoringMessageDto {

    @NotNull
    private String uuid;

    private String hostname;

    private String group;

    private List<ProbeValueDto> probeValues;

    public String toJson() {
        return new JSONSerializer().exclude("*.class").deepSerialize(this);
    }

    public static String toJsonArray(Collection<MonitoringMessageDto> collection) {
        return new JSONSerializer().exclude("*.class")
                .deepSerialize(collection);
    }

}