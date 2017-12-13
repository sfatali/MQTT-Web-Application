function runMqttClient() {
    client = new Paho.MQTT.Client("iot.eclipse.org", Number(80), "/ws", "clientId");

    // set callback handlers
    client.onConnectionLost = function (responseObject) {
        console.log("Connection Lost: "+responseObject.errorMessage);
    };

    client.onMessageArrived = function (message) {
        console.log("Message Arrived: " + message.payloadString);
        console.log("Topic:     " + message.destinationName);
        console.log("QoS:       " + message.qos);
        console.log("Retained:  " + message.retained);
        //console.log("Duplicate:  " + message.duplicate);

        handleMessage(message.destinationName, message.payloadString);
    };

    // Connect the client, providing an onConnect callback
    client.connect({
        onSuccess: mqttSubscribe
    });
}

// subscription
function mqttSubscribe() {
    console.log("Connected!");

    for(i=0; i<devices.length; i++) {
        client.subscribe(devices[i].deviceId+'/deviceStatus');
        //console.log("client subscribed to: "+devices[i].deviceId+'/deviceStatus2');
        client.subscribe(devices[i].deviceId+'/messageStatus');
        //console.log("client subscribed to: "+devices[i].deviceId+'/messageStatus2');
    }
}

function handleMessage(topic, messageString) {
    var subtopicArray = topic.split('/');

    if (subtopicArray.length == 2 && messageString.length > 0) {
        var message = JSON.parse(messageString);
        var device = {};
        device = getDeviceById(subtopicArray[0]);
        if (device.deviceId == undefined) {
            console.log("Could not find the device"); // well, that's impossible, but just in case
        } else {
            console.log("Device found");
            action = subtopicArray[1];
            if(action == "deviceStatus") {
                // handling device
                console.log("handling device");
                if(isDeviceStatusValid(message)) {
                    updateDeviceStatus(device.deviceId, message.status);

                    var status = '';
                    if(message.status == 1) {
                        status = 'connected';
                        $.notify("Device "+status, "info");
                    } else {
                        status = 'disconnected';
                        $.notify("Device "+status, "info");
                    }
                }
            } else if(action == "messageStatus") {
                // handling message status
                console.log("handling message status");
                removeMessageFromPendingList(device.deviceId, message.messageId, message.commandStatus);
            }
        }
    }
}

function isDeviceStatusValid(message) {
    // TODO
    return true;
}

function publishMessage(deviceId, messagePayload, topic) {
    // Publish a Message
    var message = new Paho.MQTT.Message(messagePayload);
    message.destinationName = deviceId+'/'+topic; // topic
    message.retained = false;
    message.qos = 0;

    //if(topic)

    console.log("Publishing!");
    console.log("topic: "+message.destinationName);
    console.log("payload: "+messagePayload);
    client.send(message);

    if(topic == message) {
        $.notify("Message sent", "success");
    }
}

