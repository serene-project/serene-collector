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
@RooEntity(finders = { "findServersByUuidMostSigBitsEqualsAndUuidLeastSigBitsEquals" })
public class Server {

    @NotNull
    @Column(unique = true)
    @Size(min = 3, max = 100)
    private String hostname;

    @NotNull
    @Column
    private Long uuidMostSigBits;

    @NotNull
    @Column
    private Long uuidLeastSigBits;

    @NotNull
    @ManyToOne
    private ServerGroup serverGroup;

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

    public static Server findServerByUuidEquals(String uuid) {
        if (uuid == null)
            throw new IllegalArgumentException("The uuid argument is required");
        return findServerByUuidEquals(UUID.fromString(uuid));
    }

    public static Server findServerByUuidEquals(UUID uuid) {
        if (uuid == null)
            throw new IllegalArgumentException("The uuid argument is required");
        return Server
                .findServersByUuidMostSigBitsEqualsAndUuidLeastSigBitsEquals(
                        uuid.getMostSignificantBits(),
                        uuid.getLeastSignificantBits()).getSingleResult();
    }

}
