package net.sereneproject.collector.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

/**
 * Store data about Probes.
 * 
 * @author gehel
 */
@RooJavaBean
@RooToString
@RooEntity(finders = { "findProbesByUuidMostSigBitsNotEqualsAndUuidLeastSigBitsEquals" })
public class Probe {

    /** Human readable probe name. */
    @NotNull
    @Column(unique = true)
    @Size(min = 3, max = 20)
    private String name;

    /** UUID is split in 2 {@link Long} fields. */
    @NotNull
    @Column
    private Long uuidMostSigBits;

    /** UUID is split in 2 {@link Long} fields. */
    @NotNull
    @Column
    private Long uuidLeastSigBits;

    /**
     * Saved state from the analyzer.
     * 
     * @todo: This needs to be refactored to support multiple analyzers per
     *        probe.
     */
    private String savedState;

    /** Server to which this probe belongs. */
    @NotNull
    @ManyToOne
    private Server server;

    /**
     * Set the UUID of the probe from its String representation.
     * 
     * @param uuid
     *            must conform to specifications from {@link UUID#toString()}.
     */
    public final void setUuid(final String uuid) {
        setUuid(UUID.fromString(uuid));
    }

    /**
     * Set the UUID of the probe.
     * 
     * @param uuid
     *            the UUID to set
     */
    public final void setUuid(final UUID uuid) {
        this.uuidLeastSigBits = uuid.getLeastSignificantBits();
        this.uuidMostSigBits = uuid.getMostSignificantBits();
    }

    /**
     * Get the UUID of the probe.
     * 
     * @return this probe's UUID
     */
    public final UUID getUuid() {
        return new UUID(this.uuidMostSigBits, this.uuidLeastSigBits);
    }

    /**
     * Find a probe by it's UUID.
     * 
     * @param uuid
     *            the String representation of the UUID to look for, must
     *            conform to specifications from {@link UUID#toString()}.
     * @return the probe
     * @throws EmptyResultDataAccessException
     *             in case the probe is not found
     */
    public static Probe findProbeByUuidEquals(final String uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("The uuid argument is required");
        }
        // TODO : document exception thrown when UUID does not conform to spec
        return findProbeByUuidEquals(UUID.fromString(uuid));
    }

    /**
     * Find a probe by it's UUID.
     * 
     * @param uuid
     *            the UUID to look for.
     * @return the probe
     * @throws EmptyResultDataAccessException
     *             in case the probe is not found
     */
    public static Probe findProbeByUuidEquals(final UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("The uuid argument is required");
        }
        return Probe
                .findProbesByUuidMostSigBitsNotEqualsAndUuidLeastSigBitsEquals(
                        uuid.getMostSignificantBits(),
                        uuid.getLeastSignificantBits()).getSingleResult();
    }

}
