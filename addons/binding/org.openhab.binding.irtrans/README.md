# IRtrans Binding

This binding integrates infrared receivers and blasters manufactured by IRtrans (www.irtrans.de)

## Supported Things

The *ethernet* Bridge supports the Ethernet (PoE) IRtrans transceiver equipped with an on-board IRDB database. Blasters and receivers are defined as Channels on the Bridge, but one can also define blasters as a *blaster* child Thing on the Bridge.

## Discovery

There is no Discovery feature available.

## Binding Configuration

There is no specific binding configuration required.

## Thing Configuration

The *ethernet* Bridge requires an *ipAddress* IP address and *portNumber* TCP port number in order to configure it. Optionally, one can add the following parameters to the configuration:

*bufferSize* : Buffer size used by the TCP socket when sending and receiving commands to the transceiver (default: 1024)
*refreshInterval* : Specifies the refresh interval, in milliseconds, for status updates (default: 50)
*responseTimeOut* : Specifies the time milliseconds to wait for a response from the transceiver when sending a command (default: 100)
*pingTimeOut* : Specifies the time milliseconds to wait for a response from the transceiver when pinging the device (default: 1000)
*reconnectInterval* : Specifies the time seconds to wait before reconnecting to a transceiver after a communication failure (default: 10)

The *blaster* Thing requires a *led* parameter to specify on which infrared commands will be emitted, *remote* the remote or manufacturer name which's commands will be allowed, as defined in the IRtrans server database that is flashed into the transceiver (can be '*' for 'any' remote), and *command* the name of the command will be allowed, as defined in the IRtrans server database that is flashed into the transceiver (can be '*' for 'any' command).

## Channels

The *ethernet* Thing supports the following Channel Types:

| Channel Type ID | Item Type | Description                                                                         |
|-----------------|-----------|-------------------------------------------------------------------------------------|
| blaster         | String    | Send (filtered) infrared commands over the specified blaster led of the transceiver |
| receiver        | String    | Receive (filtered) infrared commands on the receiver led of the transceiver         |

The *blaster* Channel Type requires a *led* configuration parameter to specify on which infrared commands will be emitted, *remote* the remote or manufacturer name which's commands will be allowed, as defined in the IRtrans server database that is flashed into the transceiver (can be '*' for 'any' remote), and *command* the name of the command will be allowed, as defined in the IRtrans server database that is flashed into the transceiver (can be '*' for 'any' command).

The *receiver* Channel Type requires *remote* the remote or manufacturer name which's commands will be allowed, as defined in the IRtrans server database that is flashed into the transceiver (can be '*' for 'any' remote), and *command* the name of the command will be allowed, as defined in the IRtrans server database that is flashed into the transceiver (can be '*' for 'any' command).

The *blaster* Thing supports a *io* Channel (of Item Type String) that allows to read infrared commands received by the blaster, as well as to write infrared commands to be sent by the blaster.

The IRtrans transceivers store infrared commands in a "remote,command" table, e.g. "telenet,power". Sending the literal text string "telenet,power" to the transceiver will make the transceiver "translate" that into the actual infrared command that will be emitted by the transceiver.  A "remote,command" string sent to a Channel that does not match the defined filter will be ignored. 

## Full Example

demo.things:
```
Bridge irtrans:ethernet:kitchen [ ipAddress="192.168.0.56", portNumber=21000, bufferSize=1024, refreshInterval=50, responseTimeOut=100, pingTimeOut=2000, reconnectInterval=10 ]
{
Channels:
Receiver : any [remote="*", command="*"]
Receiver : telenet_power [remote="telenet", command="power"]
Blaster : samsung [led="E", remote="samsung", command="*"]
}
```

In the above example, the first channel will be updated when any IR command from any type of device is received. The second channel will only be updated if a "power" infrared command from the remote/device type "telenet" is received. The third channel can be used to feed any type of infrared command to a Samsung television by means of the "E" emitter of the IRtrans device.

```
Bridge irtrans:ethernet:technicalfacilities [ ipAddress="192.168.0.58", portNumber=21000, bufferSize=1024, refreshInterval=50, responseTimeOut=100, pingTimeOut=2000, reconnectInterval=10 ]
{
Channels:
Receiver : any [remote="*", command="*"]
Blaster : telenet1 [led="2", remote="telenet", command="*"]
Blaster : telenet2 [led="1", remote="telenet", command="*"]
Blaster : appletv [led="3", remote="appletv", command="*"]
}
```

In the above channel a single IRtrans transceiver has 3 output leds in use, 2 to drive 2 DTV SetTopBoxes, and a third one to drive an Apple TV device.

demo.items:
```
String KitchenIRReceiverAny {channel="irtrans:ethernet:kitchen:any"}
String KitchenIRReceiverTelenetPower {channel="irtrans:ethernet:kitchen:telenet_power"}
String KitchenIRBlasterSamsung {channel="irtrans:ethernet:kitchen:samsung"}

String TechnicalFacilitiesIRReceiverAny {channel="irtrans:ethernet:technicalfacilities:any"}
String TechnicalFacilitiesIRBlasterTelenet2 {channel="irtrans:ethernet:technicalfacilities:telenet2"}
String TechnicalFacilitiesIRBlasterTelenet1 {channel="irtrans:ethernet:technicalfacilities:telenet1"}
String TechnicalFacilitiesIRBlasterAppleTV {channel="irtrans:ethernet:technicalfacilities:appletv"}
```

demo.rules:

```
rule "Kitchen switch IR rule"
when
    Item KitchenIRReceiverTelenetPower received update 
then
    createTimer(now.plusSeconds(5)) [|
        KitchenIRBlasterSamsung.sendCommand("samsung,power")
        ]
end
```