// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package net.sereneproject.collector.domain;

import java.lang.String;
import net.sereneproject.collector.domain.Probe;

privileged aspect Plugin_Roo_JavaBean {
    
    public String Plugin.getUrl() {
        return this.url;
    }
    
    public void Plugin.setUrl(String url) {
        this.url = url;
    }
    
    public String Plugin.getSavedState() {
        return this.savedState;
    }
    
    public void Plugin.setSavedState(String savedState) {
        this.savedState = savedState;
    }
    
    public String Plugin.getStatus() {
        return this.status;
    }
    
    public void Plugin.setStatus(String status) {
        this.status = status;
    }
    
    public Probe Plugin.getProbe() {
        return this.probe;
    }
    
    public void Plugin.setProbe(Probe probe) {
        this.probe = probe;
    }
    
}