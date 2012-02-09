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
import net.sereneproject.collector.domain.Probe;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Probe_Roo_Entity {
    
    declare @type: Probe: @Entity;
    
    @PersistenceContext
    transient EntityManager Probe.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Probe.id;
    
    @Version
    @Column(name = "version")
    private Integer Probe.version;
    
    public Long Probe.getId() {
        return this.id;
    }
    
    public void Probe.setId(Long id) {
        this.id = id;
    }
    
    public Integer Probe.getVersion() {
        return this.version;
    }
    
    public void Probe.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void Probe.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Probe.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Probe attached = Probe.findProbe(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Probe.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void Probe.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public Probe Probe.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Probe merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager Probe.entityManager() {
        EntityManager em = new Probe().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Probe.countProbes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Probe o", Long.class).getSingleResult();
    }
    
    public static List<Probe> Probe.findAllProbes() {
        return entityManager().createQuery("SELECT o FROM Probe o", Probe.class).getResultList();
    }
    
    public static Probe Probe.findProbe(Long id) {
        if (id == null) return null;
        return entityManager().find(Probe.class, id);
    }
    
    public static List<Probe> Probe.findProbeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Probe o", Probe.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
