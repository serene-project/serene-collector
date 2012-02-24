// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package net.sereneproject.collector.dto;

import flexjson.JSONDeserializer;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.sereneproject.collector.dto.MonitoringMessageDto;

privileged aspect MonitoringMessageDto_Roo_Json {
    
    public static MonitoringMessageDto MonitoringMessageDto.fromJsonToMonitoringMessageDto(String json) {
        return new JSONDeserializer<MonitoringMessageDto>().use(null, MonitoringMessageDto.class).deserialize(json);
    }
    
    public static Collection<MonitoringMessageDto> MonitoringMessageDto.fromJsonArrayToMonitoringMessageDtoes(String json) {
        return new JSONDeserializer<List<MonitoringMessageDto>>().use(null, ArrayList.class).use("values", MonitoringMessageDto.class).deserialize(json);
    }
    
}
