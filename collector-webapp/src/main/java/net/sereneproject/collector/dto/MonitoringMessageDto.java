/**
 * Copyright (c) 2012, Serene Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.sereneproject.collector.dto;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONSerializer;

/**
 * DTO exposing a message sent from a probe agent to the collector.
 * 
 * @author gehel
 */
@RooJavaBean
@RooToString
@RooJson
public class MonitoringMessageDto {

    /** String representation of the UUID of the server. */
    @NotNull
    private String uuid;

    /** Hostname of the server. */
    @Nullable
    private String hostname;

    /** Group in which this server is located. */
    @Nullable
    private String group;

    /** List of probe values for this server. */
    @Nullable
    @Valid
    private List<ProbeValueDto> probeValues;

    /**
     * Serialize this object as JSON.
     * 
     * The ROO generated serializer doesnt do a
     * {@link JSONSerializer#deepSerialize(Object)}, so we override it.
     * 
     * @return this object serialized as JSON
     */
    public final String toJson() {
        return new JSONSerializer().exclude("*.class").deepSerialize(this);
    }

    /**
     * Serialize a collection of {@link MonitoringMessageDto}s to JSON.
     * 
     * The ROO generated serializer doesnt do a
     * {@link JSONSerializer#deepSerialize(Object)}, so we override it.
     * 
     * @param collection
     *            the collection to serialize
     * @return a JSON representation of the collection
     */
    public static String toJsonArray(
            final Collection<MonitoringMessageDto> collection) {
        return new JSONSerializer().exclude("*.class")
                .deepSerialize(collection);
    }

}
