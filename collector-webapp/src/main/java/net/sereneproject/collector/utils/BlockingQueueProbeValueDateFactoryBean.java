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
package net.sereneproject.collector.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import net.sereneproject.collector.dto.ProbeValueDateDto;

import org.springframework.beans.factory.FactoryBean;

public class BlockingQueueProbeValueDateFactoryBean implements
        FactoryBean<BlockingQueue<ProbeValueDateDto>> {

    private final int capacity;

    public BlockingQueueProbeValueDateFactoryBean(final int capacity) {
        this.capacity = capacity;
    }

    @Override
    public BlockingQueue<ProbeValueDateDto> getObject() throws Exception {
        return new ArrayBlockingQueue<ProbeValueDateDto>(getCapacity());
    }

    @Override
    public Class<BlockingQueue> getObjectType() {
        return BlockingQueue.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public final int getCapacity() {
        return this.capacity;
    }

}
