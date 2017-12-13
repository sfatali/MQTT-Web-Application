function setDeviceStatus(deviceId, status) {
    for(var i=0; i<devices.length; i++) {
        if (devices[i].deviceId == deviceId) {
            devices[i].status = status;
            console.log("Device status set to: "+devices[i].status);
            // updating the row
            $('#row-'+deviceId+'-status').html(devices[i].status);
            devices[i].statusDate = getFormattedDate();
            $('#row-'+deviceId+'-statusDate').html(devices[i].statusDate);
            return;
        }
    }
}

function setSlider(bundle) {
    //console.log("Setting value to output from slider");
    //console.log("slider: "+ bundle.slider.value);
    bundle.slider.oninput = function() {
        bundle.output.innerHTML = bundle.slider.value;
        //console.log("output: "+ bundle.output.value);
    };
}

function setNavigationParams(deviceId, type, frequency, duration) {
    for(var j=0;j<devices.length;j++) {
        if(devices[j].deviceId == deviceId) {
            for (var i=0; i<devices[j].navigationParams.length; i++) {
                if (devices[j].navigationParams[i].type == type) {
                    devices[j].navigationParams[i].frequency = frequency;
                    devices[j].navigationParams[i].duration = duration;
                    console.log("navigation params updated!")
                }
            }
        }
    }
}