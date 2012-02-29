package net.sereneproject.collector.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findProbesByUuidMostSigBitsEqualsAndUuidLeastSigBitsEquals" })
@RooJson
public class Probe {

    @NotNull
    @Column(unique = true)
    @Size(min = 3, max = 20)
    private String name;

    @NotNull
    @Column
    private Long uuidMostSigBits;

    @NotNull
    @Column
    private Long uuidLeastSigBits;

    private String savedState;

    @NotNull
    @ManyToOne
    private Server server;

    @NotNull
    @OneToMany
    private Set<Plugin> plugins = new HashSet<Plugin>();

    public final void setUuid(String uuid) {
        setUuid(UUID.fromString(uuid));
    }

    public final void setUuid(UUID uuid) {
        this.uuidLeastSigBits = uuid.getLeastSignificantBits();
        this.uuidMostSigBits = uuid.getMostSignificantBits();
    }

    public final UUID getUuid() {
        return new UUID(this.uuidMostSigBits, this.uuidLeastSigBits);
    }

    public static net.sereneproject.collector.domain.Probe findProbeByUuidEquals(String uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("The uuid argument is required");
        }
        return findProbeByUuidEquals(UUID.fromString(uuid));
    }

    public static net.sereneproject.collector.domain.Probe findProbeByUuidEquals(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("The uuid argument is required");
        }
        return Probe.findProbesByUuidMostSigBitsEqualsAndUuidLeastSigBitsEquals(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits()).getSingleResult();
    }
}
