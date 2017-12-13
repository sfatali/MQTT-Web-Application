function incrementMessageQueue(deviceId) {
    for(var i=0; i<devices.length; i++) {
        if (devices[i].deviceId == deviceId) {
            devices[i].messagesInQueueCount += 1;
            $('#row-'+deviceId+'-messagesInQueueCount').html(devices[i].messagesInQueueCount);
        }
    }
}

function decrementMessageQueue(deviceId) {
    for(var i=0; i<devices.length; i++) {
        if (devices[i].deviceId == deviceId) {
            console.log("decrementing!!!");
            devices[i].messagesInQueueCount -= 1;
            console.log(devices[i].messagesInQueueCount);
            $('#row-'+deviceId+'-messagesInQueueCount').html(devices[i].messagesInQueueCount);
        }
    }
}

function updateDeviceStatus(deviceId, status) {
    var currentStatus = getDeviceStatus(deviceId);
    console.log("device current status: "+currentStatus);
    console.log("device future status: "+status);

    if (status == 1 && (currentStatus == 'Disconnected' || currentStatus == 'Never connected')) {
        console.log("getting connected");
        setDeviceStatus(deviceId, 'Connected');
        // activating buttons
        $("#1-"+deviceId).removeClass("disabled");
        $("#1-"+deviceId).addClass("active");
        $("#3-"+deviceId).removeClass("disabled");
        $("#3-"+deviceId).addClass("active");
        $("#2-"+deviceId).removeClass("disabled");
        $("#2-"+deviceId).addClass("active");
        $("#submit-"+deviceId).removeClass("disabled");
        $("#submit-"+deviceId).addClass("active");
    }
    else if(status == 0 && currentStatus == 'Connected') {
        console.log("getting disconnected");
        setDeviceStatus(deviceId, 'Disconnected');
        // disabling buttons
        $("#1-"+deviceId).removeClass("active");
        $("#1-"+deviceId).addClass("disabled");
        $("#3-"+deviceId).removeClass("active");
        $("#3-"+deviceId).addClass("disabled");
        $("#2-"+deviceId).removeClass("active");
        $("#2-"+deviceId).addClass("disabled");
        $("#submit-"+deviceId).removeClass("active");
        $("#submit-"+deviceId).addClass("disabled");
    }
}

function clearMessagePlaceHolder(deviceId) {
    console.log("clearing text holder");
    document.getElementById("messageText-"+deviceId).value = '';
}

function clearSettings(deviceId, command) {
    console.log("clearing settings");
    var settings = getNavigationParams(deviceId, command);

    var frequencySlider = getMessageFrequencySliderByDeviceId(deviceId);
    frequencySlider.slider.value = settings.frequency;
    frequencySlider.output.innerHTML = settings.frequency;

    var durationSlider = getMessageDurationSliderByDeviceId(deviceId);
    durationSlider.slider.value = settings.duration;
    durationSlider.output.innerHTML = settings.duration;
}

function addMessageToPendingList(deviceId, messageId) {
    for(var i=0; i<devices.length; i++) {
        if (devices[i].deviceId == deviceId) {
            var element = {};
            element.messageId = messageId;
            devices[i].pendingMessagesList.push(element);
            console.log("device after new message: "+devices[i]);
            return;
        }
    }
}

function removeMessageFromPendingList(deviceId, messageId, status) {
    for(var i=0; i<devices.length; i++) {
        if (devices[i].deviceId == deviceId) {
            console.log("Yay, device found");
            console.log("pending list before removing: "+JSON.stringify(devices[i].pendingMessagesList));

            for(var j=0; j<devices[i].pendingMessagesList.length; j++) {
                if(devices[i].pendingMessagesList[j].messageId == messageId) {
                    console.log("Message found.");
                    //console.log("pending list before removing: "+JSON.stringify(devices[i].pendingMessagesList));
                    devices[i].pendingMessagesList.splice(j, 1);
                    //console.log("pending list after removing message: "+JSON.parse(devices[i].pendingMessagesList));
                    decrementMessageQueue(deviceId);

                    if(status == 1) {
                        $.notify("Message successfully processed", "info");
                    } else {
                        $.notify("Error occurred while message processing", "warn");
                    }

                    return;
                }
            }
        }
    }
}

function updateNavigation(deviceId, type, frequency, duration) {
    $('#setting'+type+'-freq-'+deviceId).html(frequency);
    $('#setting'+type+'-dur-'+deviceId).html(duration);

    if(type == 4) {
        var frequencySlider = getMessageFrequencySliderByDeviceId(deviceId);
        frequencySlider.slider.value = frequency;
        frequencySlider.output.innerHTML = frequency;

        var durationSlider = getMessageDurationSliderByDeviceId(deviceId);
        durationSlider.slider.value = duration;
        durationSlider.output.innerHTML = duration;
    }
}

