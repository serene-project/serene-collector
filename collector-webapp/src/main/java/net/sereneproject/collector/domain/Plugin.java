package net.sereneproject.collector.domain;

import java.net.URI;
import java.net.URISyntaxException;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooJson
public class Plugin {

    @Size(min = 6, max = 255)
    private String url;

    private String savedState;

    private String status;

    @NotNull
    @ManyToOne
    private Probe probe;

    public final URI getUri() {
        try {
            return new URI(this.url);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("URL is invalid [" + this.url + "]", e);
        }
    }
}
