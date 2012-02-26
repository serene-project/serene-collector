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

import java.lang.Integer;
import java.lang.Long;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import net.sereneproject.collector.domain.Server;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Server_Roo_Entity {
    
    declare @type: Server: @Entity;
    
    @PersistenceContext
    transient EntityManager Server.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Server.id;
    
    @Version
    @Column(name = "version")
    private Integer Server.version;
    
    public Long Server.getId() {
        return this.id;
    }
    
    public void Server.setId(Long id) {
        this.id = id;
    }
    
    public Integer Server.getVersion() {
        return this.version;
    }
    
    public void Server.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void Server.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Server.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Server attached = Server.findServer(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Server.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void Server.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public Server Server.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Server merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager Server.entityManager() {
        EntityManager em = new Server().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Server.countServers() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Server o", Long.class).getSingleResult();
    }
    
    public static List<Server> Server.findAllServers() {
        return entityManager().createQuery("SELECT o FROM Server o", Server.class).getResultList();
    }
    
    public static Server Server.findServer(Long id) {
        if (id == null) return null;
        return entityManager().find(Server.class, id);
    }
    
    public static List<Server> Server.findServerEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Server o", Server.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}