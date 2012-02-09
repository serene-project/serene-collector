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
