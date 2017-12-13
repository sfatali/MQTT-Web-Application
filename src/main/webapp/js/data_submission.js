function generateMorseCodeMessage(deviceId, frequency, duration, messageText) {
    var message = {};
    message.deviceId = deviceId;
    message.messageId = generateUUID();
    message.command = 4;
    message.frequency = parseInt(frequency);
    message.pulseDuration = parseInt(duration);
    message.messageLength = messageText.length;
    message.message = messageText;
    message.publishDate = getFormattedDate();

    console.log(JSON.stringify(message));
    return message;
}

function generateNavigationMessage(deviceId, command) {
    console.log("deviceId"+deviceId);
    console.log('');
    var message = {};
    message.deviceId = deviceId;
    message.messageId = generateUUID();
    message.command = command;
    var navigationParams = getNavigationParams(deviceId, command);
    message.frequency = parseInt(navigationParams.frequency);
    message.pulseDuration = parseInt(navigationParams.duration);
    message.publishDate = getFormattedDate();

    console.log(JSON.stringify(message));
    return message;
}

function manageNavigationCommandSubmission() {
    $(".navigation-button").click(function() {
        var id = $(this).attr('id');
        var command = id.substr(0, id.indexOf('-'));
        var deviceId = id.substr(id.indexOf('-')+1, id.length);
        console.log("id: "+id);
        var message = generateNavigationMessage(deviceId, command);
        publishMessage(deviceId, JSON.stringify(message), 'message');
        incrementMessageQueue(deviceId);
        addMessageToPendingList(deviceId, message.messageId);
    });
}

function manageMorseCodeMessageSubmission() {
    $(".morse-button").click(function(){
        console.log("Submitted");
        var id = $(this).attr('id');
        var deviceId = id.substr(id.indexOf("-")+1, id.length);
        var frequency = getMessageFrequencySliderByDeviceId(deviceId).slider.value;
        var duration= getMessageDurationSliderByDeviceId(deviceId).slider.value;
        var messageText = document.getElementById("messageText-"+deviceId).value;

        if(messageText.length > 0 && isAsciiOnly(messageText)) {
            var message = generateMorseCodeMessage(deviceId, frequency, duration, messageText);
            publishMessage(deviceId, JSON.stringify(message), 'message');
            incrementMessageQueue(deviceId);
            addMessageToPendingList(deviceId, message.messageId);
        } else {
            $.notify("Error in message text", "error");
        }

        // temp
        clearMessagePlaceHolder(deviceId);
        clearSettings(deviceId, 4);
    });
}

function manageEditSubmission() {
    $(".edit-button-modal").click(function() {
        console.log("Edit submitted!!");
        var id = $(this).attr('id');
        var deviceId = id.substr(id.indexOf("-")+1, id.length);
        var type = id.substring(11, 12);
        console.log("device: "+deviceId);
        console.log("type: "+type);

        var slider = document.getElementById("edit"+type+"-frequency-input-"+deviceId);
        var frequency = slider.value;
        slider = document.getElementById("edit"+type+"-duration-input-"+deviceId);
        var duration = slider.value;

        var message = generateSettingsEditMessage(deviceId, type, frequency, duration);
        publishMessage(deviceId, JSON.stringify(message), 'edit');
        setNavigationParams(deviceId, type, frequency, duration);
        updateNavigation(deviceId, type, frequency, duration);
        $.notify("Changes saved", "success");

    });
}

function generateSettingsEditMessage(deviceId, type, frequency, duration) {
    var message = {};
    message.deviceId = deviceId;
    message.type = parseInt(type);
    message.frequency = parseInt(frequency);
    message.duration = parseInt(duration);

    console.log(JSON.stringify(message));
    return message;
}

function manageEditCancellation() {
    $(".edit-button-modal-cancel").click(function() {
        var id = $(this).attr('id');
        var deviceId = id.substr(id.indexOf("-")+1, id.length);
        var type = id.substring(11, 12);
        console.log("device: "+deviceId);
        console.log("type: "+type);

        var navigation = getNavigationParams(deviceId, type);

        var slider = document.getElementById("edit"+type+"-frequency-input-"+deviceId);
        slider.value = navigation.frequency;
        slider = document.getElementById("edit"+type+"-duration-input-"+deviceId);
        slider.value = navigation.duration;
    });
}

function isAsciiOnly(str) {
    for (var i = 0; i < str.length; i++)
        if (str.charCodeAt(i) > 127)
            return false;
    return true;
}
