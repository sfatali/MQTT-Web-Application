function manageEditButton() {
    $('.edit-button').click(function() {
        //console.log("Setting edit sliders!!!");

        var id = $(this).attr('id');
        //console.log("id: "+id);
        var deviceId = id.substr(id.indexOf('-')+1, id.length);
        var type = id.substring(4, 5);
        //console.log("device: "+deviceId);
        //console.log("type: "+type);

        var element = {};
        var slider = document.getElementById("edit"+type+"-frequency-input-"+deviceId);
        var output = document.getElementById("edit"+type+"-frequency-output-"+deviceId);
        element.slider = slider;
        element.output = output;
        output.innerHTML = slider.value;
        //console.log("freq value: : "+slider.value);
        //console.log("freq output value: : "+output.innerHTML);
        setSlider(element);

        element = {};
        slider = document.getElementById("edit"+type+"-duration-input-"+deviceId);
        output = document.getElementById("edit"+type+"-duration-output-"+deviceId);
        element.slider = slider;
        element.output = output;
        output.innerHTML = slider.value;
        //console.log("dur value: : "+slider.value);
        //console.log("dur output value: : "+output.innerHTML);
        setSlider(element);
    });
}

function manageHeaders() {
    $('.nav-link').click(function() {
        var id = $(this).attr('id');
        var mode = id.substr(0, id.indexOf('-'));
        var deviceId = id.substr(id.indexOf('-')+1, id.length);
        document.getElementById(id).style.fontWeight = 'bold';
        //var pagePosition = $(window).scrollTop();

        if(mode == 'control') {
            document.getElementById('settings-'+deviceId).style.fontWeight = 'normal';
            $("#settings-mode-"+deviceId).hide();
            $("#control-mode-"+deviceId).show();
            //$(window).scrollTop(pagePosition);
        } else if(mode == 'settings') {
            console.log("switching headers to settings");
            document.getElementById('control-'+deviceId).style.fontWeight = 'normal';
            $("#control-mode-"+deviceId).hide();
            $("#settings-mode-"+deviceId).show();
            //$(window).scrollTop(pagePosition);
        }
    });
}

function manageMessageSliders() {
    for (var j=0; j< devices.length; j++) {
        var element = {};
        var slider = document.getElementById("message-frequency-input-"+devices[j].deviceId);
        var output = document.getElementById("message-frequency-output-"+devices[j].deviceId);
        element.deviceId = devices[j].deviceId;
        element.slider = slider;
        element.output = output;
        frequencyMessageSliderCollection.push(element);
        frequencyMessageSliderCollection[j].output.innerHTML = frequencyMessageSliderCollection[j].slider.value;
        setSlider(frequencyMessageSliderCollection[j]);

        element = {};
        slider = document.getElementById("message-duration-input-"+devices[j].deviceId);
        output = document.getElementById("message-duration-output-"+devices[j].deviceId);
        element.deviceId = devices[j].deviceId;
        element.slider = slider;
        element.output = output;
        durationMessageSliderCollection.push(element);
        durationMessageSliderCollection[j].output.innerHTML = durationMessageSliderCollection[j].slider.value;
        setSlider(durationMessageSliderCollection[j]);
    }
}
