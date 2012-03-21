/**
 * Copyright (c) 2012, Serene Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.sereneproject.collector.rrd;

import java.io.IOException;
import java.util.Arrays;

import net.sereneproject.collector.domain.Probe;

import org.apache.log4j.Logger;
import org.rrd4j.core.RrdByteArrayBackend;
import org.springframework.dao.EmptyResultDataAccessException;

public class RrdJpaBackend extends RrdByteArrayBackend {

    /** Logger. */
    private static final Logger LOG = Logger.getLogger(RrdJpaBackend.class);

    private volatile boolean dirty = false;

    public RrdJpaBackend(String path) {
        super(path);
        try {
            Probe probe = Probe.findProbeByUuidEquals(path);
            if (probe.getRrd() != null) {
                this.buffer = Arrays.copyOf(probe.getRrd(),
                        probe.getRrd().length);
            }
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalStateException("The probe [" + path
                    + "] should exist before its RRD database is accessed.");
        }
    }

    @Override
    protected synchronized void write(final long offset, final byte[] bytes)
            throws IOException {
        super.write(offset, bytes);
        dirty = true;
    }

    @Override
    public void close() throws IOException {
        if (dirty) {
            Probe probe = Probe.findProbeByUuidEquals(getPath());
            probe.setRrd(buffer);
        }
    }
}
