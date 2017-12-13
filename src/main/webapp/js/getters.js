function getFormattedDate() {
    var date = new Date();
    var month = parseInt(date.getMonth()) + 1;
    return date.getDate()+"."+month+"."+date.getFullYear()+
        ' '+date.getHours()+':'+date.getMinutes();
}

function getNavigationParams(deviceId, command) {
    for(var i=0; i<devices.length; i++) {
        if (devices[i].deviceId == deviceId) {
            for(var j=0; j<devices[i].navigationParams.length; j++) {
                if(devices[i].navigationParams[j].type == command) {
                    var navigation = {};
                    navigation.frequency = devices[i].navigationParams[j].frequency;
                    navigation.duration = devices[i].navigationParams[j].duration;
                    return navigation;
                }
            }
        }
    }
}

function generateUUID() {
    var dt = new Date().getTime();
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = (dt + Math.random()*16)%16 | 0;
        dt = Math.floor(dt/16);
        return (c=='x' ? r :(r&0x3|0x8)).toString(16);
    });
    return uuid;
}

function getMessageFrequencySliderByDeviceId(deviceId) {
    for(var i=0; i<frequencyMessageSliderCollection.length; i++) {
        if (frequencyMessageSliderCollection[i].deviceId == deviceId) {
            return frequencyMessageSliderCollection[i];
        }
    }
}

function getMessageDurationSliderByDeviceId(deviceId) {
    for(var i=0; i<durationMessageSliderCollection.length; i++) {
        if (durationMessageSliderCollection[i].deviceId == deviceId) {
            return durationMessageSliderCollection[i];
        }
    }
}

function getDeviceById(deviceId) {
    for(var i=0; i<devices.length; i++) {
        if (devices[i].deviceId == deviceId) {
            return devices[i];
        }
    }
}

function getDeviceStatus(deviceId) {
    for(var i=0; i<devices.length; i++) {
        if (devices[i].deviceId == deviceId) {
            return devices[i].status;
        }
    }
}