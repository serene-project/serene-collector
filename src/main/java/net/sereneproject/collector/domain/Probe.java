package net.sereneproject.collector.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity(finders = { "findProbesByUuidMostSigBitsNotEqualsAndUuidLeastSigBitsEquals" })
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

    public void setUuid(String uuid) {
        setUuid(UUID.fromString(uuid));
    }

    public void setUuid(UUID uuid) {
        this.uuidLeastSigBits = uuid.getLeastSignificantBits();
        this.uuidMostSigBits = uuid.getMostSignificantBits();
    }

    public UUID getUuid() {
        return new UUID(this.uuidMostSigBits, this.uuidLeastSigBits);
    }

    public static Probe findProbeByUuidEquals(String uuid) {
        if (uuid == null)
            throw new IllegalArgumentException("The uuid argument is required");
        return findProbeByUuidEquals(UUID.fromString(uuid));
    }

    public static Probe findProbeByUuidEquals(UUID uuid) {
        if (uuid == null)
            throw new IllegalArgumentException("The uuid argument is required");
        return Probe
                .findProbesByUuidMostSigBitsNotEqualsAndUuidLeastSigBitsEquals(
                        uuid.getMostSignificantBits(),
                        uuid.getLeastSignificantBits()).getSingleResult();
    }

}
