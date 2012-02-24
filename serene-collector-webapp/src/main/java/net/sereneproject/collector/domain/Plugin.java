package net.sereneproject.collector.domain;

import java.net.URI;
import java.net.URISyntaxException;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class Plugin {

    /** URL of the plugin */
    @Size(min = 6, max = 255)
    private String url;

    /** Saved state for this plugin. */
    private String savedState;

    /** Last status of the probe for this plugin. */
    private String status;

    /** Probe owning this sample. */
    @NotNull
    @ManyToOne
    private Probe probe;

    /**
     * Get the URI for this plugin.
     * 
     * Returns the URI representation of {@link Plugin#getUrl()}.
     * @return the URI
     */
    public final URI getUri() {
        // TODO: syntax check should be done when setting value, unchecked
        // exception should be thrown when reading
        try {
            return new URI(this.url);
        } catch (URISyntaxException e) {
            throw new IllegalStateException(
                    "URL is invalid [" + this.url + "]", e);
        }
    }
}
