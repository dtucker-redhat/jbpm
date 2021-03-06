/*
 * Copyright 2013 JBoss by Red Hat.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpm.executor.ejb.impl.test;

import java.util.concurrent.atomic.AtomicLong;

import org.kie.internal.executor.api.CommandCallback;
import org.kie.internal.executor.api.CommandContext;
import org.kie.internal.executor.api.ExecutionResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SimpleIncrementCallback implements CommandCallback {
    
    private static final Logger logger = LoggerFactory.getLogger(SimpleIncrementCallback.class);

    public void onCommandDone(CommandContext ctx, ExecutionResults results) {
        String businessKey = (String) ctx.getData("businessKey");
        logger.info("Before Incrementing = {}", ((AtomicLong) BasicExecutorIntegrationTest.cachedEntities.get(businessKey)).get());
        ((AtomicLong) BasicExecutorIntegrationTest.cachedEntities.get(businessKey)).incrementAndGet();
        logger.info("After Incrementing = {}", BasicExecutorIntegrationTest.cachedEntities.get(businessKey));

    }

    @Override
    public void onCommandError(CommandContext ctx, Throwable exception) {
        logger.info("Command for request with business key {} failed an no more retries will be performed",
                ctx.getData("businessKey"));
        
    }
}
