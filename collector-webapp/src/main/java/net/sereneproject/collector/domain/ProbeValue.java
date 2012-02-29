package net.sereneproject.collector.domain;

import java.util.Date;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findProbeValuesByProbe" })
@RooJson
public class ProbeValue {

    @NotNull
    private Double value;

    @NotNull
    @DateTimeFormat(pattern = "yyyy.MM.dd HH.mm.ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @NotNull
    @ManyToOne
    private Probe probe;

    public static TypedQuery<net.sereneproject.collector.domain.ProbeValue> findProbeValuesByProbeUUID(
            UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("The uuid argument is required");
        }
        EntityManager em = ProbeValue.entityManager();
        TypedQuery<ProbeValue> q = em.createQuery(
                "SELECT o FROM ProbeValue AS o JOIN o.probe AS p "
                        + "  WHERE p.uuidMostSigBits = :uuidMostSigBits "
                        + "    AND p.uuidLeastSigBits = :uuidLeastSigBits",
                ProbeValue.class);
        q.setParameter("uuidMostSigBits", uuid.getMostSignificantBits());
        q.setParameter("uuidLeastSigBits", uuid.getLeastSignificantBits());
        return q;
    }
}
