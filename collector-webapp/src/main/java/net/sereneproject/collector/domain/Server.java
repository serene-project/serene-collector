package net.sereneproject.collector.domain;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findServersByUuidMostSigBitsEqualsAndUuidLeastSigBitsEquals" })
@RooJson
public class Server {

    @NotNull
    @Column(unique = true)
    @Size(min = 3, max = 50)
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

    public static net.sereneproject.collector.domain.Server findServerByUuidEquals(String uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("The uuid argument is required");
        }
        return findServerByUuidEquals(UUID.fromString(uuid));
    }

    public static net.sereneproject.collector.domain.Server findServerByUuidEquals(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("The uuid argument is required");
        }
        return Server.findServersByUuidMostSigBitsEqualsAndUuidLeastSigBitsEquals(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits()).getSingleResult();
    }
}
