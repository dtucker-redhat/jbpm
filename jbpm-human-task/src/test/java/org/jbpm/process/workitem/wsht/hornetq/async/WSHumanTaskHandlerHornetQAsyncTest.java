/**
 * Copyright 2010 JBoss Inc
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
package org.jbpm.process.workitem.wsht.hornetq.async;

import org.drools.SystemEventListenerFactory;
import org.jbpm.process.workitem.wsht.AsyncWSHumanTaskHandler;
import org.jbpm.process.workitem.wsht.async.WSHumanTaskHandlerBaseAsyncTest;
import org.jbpm.task.TestStatefulKnowledgeSession;
import org.jbpm.task.service.TaskClient;
import org.jbpm.task.service.TaskServer;
import org.jbpm.task.service.hornetq.HornetQTaskClientConnector;
import org.jbpm.task.service.hornetq.HornetQTaskClientHandler;
import org.jbpm.task.service.hornetq.HornetQTaskServer;

public class WSHumanTaskHandlerHornetQAsyncTest extends WSHumanTaskHandlerBaseAsyncTest {

    private TaskServer server;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        server = new HornetQTaskServer(taskService, 5446);
        logger.debug("Waiting for the HornetQTask Server to come up");
        try {
            startTaskServerThread(server, false);
        } catch (Exception e) {
            startTaskServerThread(server, true);
        }
        setClient(new TaskClient(new HornetQTaskClientConnector("client 1",
                new HornetQTaskClientHandler(SystemEventListenerFactory.getSystemEventListener()))));
        TestStatefulKnowledgeSession ksession = new TestStatefulKnowledgeSession();
        AsyncWSHumanTaskHandler handler = new AsyncWSHumanTaskHandler(getClient(), ksession);
        handler.setConnection("127.0.0.1", 5446);
        setHandler(handler);
        setSession(ksession);
    }

    protected void tearDown() throws Exception {
        ((AsyncWSHumanTaskHandler) getHandler()).dispose();
        getClient().disconnect();
        server.stop();
        super.tearDown();
    }
}