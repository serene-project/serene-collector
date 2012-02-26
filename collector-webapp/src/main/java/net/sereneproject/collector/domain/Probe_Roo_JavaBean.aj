/**
 * Copyright (c) 2012, Serene Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package net.sereneproject.collector.domain;

import java.lang.Long;
import java.lang.String;
import java.util.Set;
import net.sereneproject.collector.domain.Plugin;
import net.sereneproject.collector.domain.Server;

privileged aspect Probe_Roo_JavaBean {
    
    public String Probe.getName() {
        return this.name;
    }
    
    public void Probe.setName(String name) {
        this.name = name;
    }
    
    public Long Probe.getUuidMostSigBits() {
        return this.uuidMostSigBits;
    }
    
    public void Probe.setUuidMostSigBits(Long uuidMostSigBits) {
        this.uuidMostSigBits = uuidMostSigBits;
    }
    
    public Long Probe.getUuidLeastSigBits() {
        return this.uuidLeastSigBits;
    }
    
    public void Probe.setUuidLeastSigBits(Long uuidLeastSigBits) {
        this.uuidLeastSigBits = uuidLeastSigBits;
    }
    
    public String Probe.getSavedState() {
        return this.savedState;
    }
    
    public void Probe.setSavedState(String savedState) {
        this.savedState = savedState;
    }
    
    public Server Probe.getServer() {
        return this.server;
    }
    
    public void Probe.setServer(Server server) {
        this.server = server;
    }
    
    public Set<Plugin> Probe.getPlugins() {
        return this.plugins;
    }
    
    public void Probe.setPlugins(Set<Plugin> plugins) {
        this.plugins = plugins;
    }
    
}
