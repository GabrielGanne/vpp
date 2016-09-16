/*
 * Copyright (c) 2016 Cisco and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openvpp.jvpp.core.test;

import org.openvpp.jvpp.JVpp;
import org.openvpp.jvpp.JVppRegistry;
import org.openvpp.jvpp.JVppRegistryImpl;
import org.openvpp.jvpp.VppCallbackException;
import org.openvpp.jvpp.core.JVppCoreImpl;
import org.openvpp.jvpp.callback.ControlPingCallback;
import org.openvpp.jvpp.dto.ControlPing;
import org.openvpp.jvpp.dto.ControlPingReply;

public class ControlPingTest {

    private static void testControlPing() throws Exception {
        System.out.println("Testing ControlPing using Java callback API");
        JVppRegistry registry = new JVppRegistryImpl("ControlPingTest");
        JVpp jvpp = new JVppCoreImpl();

        registry.register(jvpp, new ControlPingCallback() {
            @Override
            public void onControlPingReply(final ControlPingReply reply) {
                System.out.printf("Received ControlPingReply: %s\n", reply);
            }

            @Override
            public void onError(VppCallbackException ex) {
                System.out.printf("Received onError exception: call=%s, reply=%d, context=%d ", ex.getMethodName(),
                        ex.getErrorCode(), ex.getCtxId());
            }

        });
        System.out.println("Successfully connected to VPP");
        Thread.sleep(1000);

        System.out.println("Sending control ping using JVppRegistry");
        registry.controlPing(jvpp.getClass());

        Thread.sleep(2000);

        System.out.println("Sending control ping using JVpp plugin");
        jvpp.send(new ControlPing());

        Thread.sleep(2000);

        System.out.println("Disconnecting...");
        registry.close();
        Thread.sleep(1000);
    }

    public static void main(String[] args) throws Exception {
        testControlPing();
    }
}
