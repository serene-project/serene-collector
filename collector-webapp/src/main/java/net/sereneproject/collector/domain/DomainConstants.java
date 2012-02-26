package net.sereneproject.collector.domain;

/**
 * Constant used in the domain package.
 * 
 * In particular, fields size are defined here.
 * 
 * @author gehel
 */
public final class DomainConstants {

    /** Minimum size of a name. */
    public static final int NAME_MIN_SIZE = 3;
    /** Maximum size of a name. */
    public static final int NAME_MAX_SIZE = 20;
    /** Maximum size of a hostname. */
    public static final int HOSTNAME_MAX_SIZE = 100;
    /** Minimum size of a URL. */
    public static final int URL_MIN_SIZE = 6;
    /** Maximum size of a URL. */
    public static final int URL_MAX_SIZE = 255;

    /**
     * Constant class should not be instantiated.
     */
    private DomainConstants() {
    }
}
