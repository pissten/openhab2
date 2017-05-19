/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.irtrans.handler;

import org.openhab.binding.irtrans.IRcommand;

/**
 * The {@link TransceiverStatusListener} is interface that is to be implemented
 * by all classes that wish to be informed of events happening to a infrared
 * transceiver
 *
 * @author Karel Goderis - Initial contribution
 * @since 2.1.0
 *
 */
public interface TransceiverStatusListener {

    /**
     *
     * Called when the ethernet transceiver/bridge receives an infrared command
     *
     * @param bridge
     * @param command - the infrared command
     */
    public void onCommandReceived(EthernetBridgeHandler bridge, IRcommand command);

    /**
     *
     * Called when the connection with the remote transceiver is lost
     *
     * @param bridge
     */
    public void onBridgeDisconnected(EthernetBridgeHandler bridge);

    /**
     * Called when the connection with the remote transceiver is established
     *
     * @param bridge
     */
    public void onBridgeConnected(EthernetBridgeHandler bridge);

}