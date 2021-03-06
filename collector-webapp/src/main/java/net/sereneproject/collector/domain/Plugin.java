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
package net.sereneproject.collector.domain;

import java.net.URI;
import java.net.URISyntaxException;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

/**
 * Instance of a plugin, configured for a specific {@link Probe}.
 * 
 * @author gehel
 */
@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooJson
public class Plugin {

    /** URL of the plugin. */
    @Size(min = 6, max = 255)
    private String url;

    /**
     * Saved state of the plugin.
     * 
     * This state is sent to the plugin each time it is called and updated with
     * the response from the plugin.
     */
    private String savedState;

    /** The {@link Probe} for which this plugin is configured. */
    @NotNull
    @ManyToOne
    private Probe probe;
    
    /**
     * Get the {@link URI} of the plugin from its internal representation.
     * 
     * @return the {@link URI}
     */
    public final URI getUri() {
        try {
            return new URI(this.url);
        } catch (URISyntaxException e) {
            throw new IllegalStateException(
                    "URL is invalid [" + this.url + "]", e);
        }
    }
}
