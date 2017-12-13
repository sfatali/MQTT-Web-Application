console.log("I'm here, I'm working!");
var devices = [];
var frequencyMessageSliderCollection = [];
var durationMessageSliderCollection = [];
var client;

$(document).ready(function() {
    $.ajax({
        url: 'http://localhost:8080/dashboard',
        dataType: 'json',
        success: function(data)
        {
            devices = data;
        },
        complete: function (data) {
            appendDashboardTable();
            appendDevices(devices);
        }
    });
});

function appendDashboardTable() {
    $(".container").append("<table id=\"dashboard\" class=\"table table-hover table-expandable table-striped\">" +
        "<thead>"+
        "<tr>" +
        "<th>Device ID</th>" +
        "<th>Status</th>" +
        "<th>Status Date</th>" +
        "<th>Messages queued</th>" +
        "</tr>" +
        "</thead>" +
        "<tbody id=\"dashboardBody\">" +
        "</tbody>" +
        "</table>");
}

function appendDevices(devices) {
    var trHTML = '';
    for(var i=0; i<devices.length; i++) {
        trHTML += '<tr>'+
            '<td id="row-'+devices[i].deviceId+'-deviceId">'+devices[i].deviceId+'</td>'+
            '<td id="row-'+devices[i].deviceId+'-status">'+devices[i].status+'</td>' +
            '<td id="row-'+devices[i].deviceId+'-statusDate">'+devices[i].statusDate+'</td>'+
            '<td id="row-'+devices[i].deviceId+'-messagesInQueueCount">'+devices[i].messagesInQueueCount+'</td>'+
            '</tr>';

        trHTML += '<tr>'+
            '<td colspan="6"><h4 align="right">'+getHeaders(devices[i].deviceId)+'</h4>';
        trHTML += controlMode(devices[i]);
        trHTML += '<div style="height: 231.67px;">'+ settingsMode(devices[i]) +'</div>';
        trHTML += '</td></tr>';

        $("#dashboardBody").append(trHTML);
        $("#settings-mode-"+devices[i].deviceId).hide();
        trHTML = '';
    }



    $.getScript('../js/content_modification.js', function() {
    });

    $.getScript('../js/content_initiation.js', function() {
    });

    $.getScript('../js/data_submission.js', function() {
    });

    $.getScript('../js/getters.js', function() {
    });

    $.getScript('../js/mqtt.js', function() {
    });

    $.getScript('../js/setters.js', function() {
    });

    manageHeaders();
    manageMessageSliders();
    manageEditButton();
    manageMorseCodeMessageSubmission();
    manageNavigationCommandSubmission();
    manageEditSubmission();
    manageEditCancellation();

    $.getScript('../js/bootstrap-table-expandable.js', function() {
    });

    runMqttClient();
}

function getHeaders(deviceId) {
    return '<a class="nav-link" style="text-decoration: none; font-weight: bold;" id="control-'+deviceId+'" href="#"> control </a>' +
        ' /  <a class="nav-link" style="text-decoration: none; font-weight: normal;" id="settings-'+deviceId+'" href="#">settings</a>';
}

function controlMode(device) {
    return '<div id="control-mode-'+device.deviceId+'">'+
        '<div style="float: left;">'+
        '<div style="width: 302px;">'+ getNavigationBar(device) + getVideo()+ '</div>' +
        '</div>'+
        //'<div style="float: left;">'+
        //' '+
        //'</div>'+
        '<div style="float: left; margin-left: 50px; width: 35%;">'+
        getTextPlaceholder(device)+
        '</div>'+
        '</div>';
}

function settingsMode(device) {
    return '<table style="margin-top: 29px; " id="settings-mode-'+device.deviceId+'" class="table table-bordered table-hover table-sm table-responsive">'+
        '<thead><tr><th>Direction</th><th>Frequency (Hz)</th><th>Duration (ms)</th><th>Action</th></tr></thead>'+
        '<tbody>'+
        '<tr><td>'+device.navigationParams[0].direction+'</td><td id="setting1-freq-'+device.deviceId+'">'+device.navigationParams[0].frequency+'</td><td id="setting1-dur-'+device.deviceId+'">'+device.navigationParams[0].duration+'</td><th>'
        +getEditButton(device.deviceId, device.navigationParams[0].type)+'</th></tr>'+getModal(device.deviceId, device.navigationParams[0].type, device.navigationParams[0].frequency, device.navigationParams[0].duration)+
        '<tr><td>'+device.navigationParams[1].direction+'</td><td id="setting2-freq-'+device.deviceId+'">'+device.navigationParams[1].frequency+'</td><td id="setting2-dur-'+device.deviceId+'">'+device.navigationParams[1].duration+'</td><th>'
        +getEditButton(device.deviceId, device.navigationParams[1].type)+'</th></tr>'+getModal(device.deviceId, device.navigationParams[1].type, device.navigationParams[1].frequency, device.navigationParams[1].duration)+
        '<tr><td>'+device.navigationParams[2].direction+'</td><td id="setting3-freq-'+device.deviceId+'">'+device.navigationParams[2].frequency+'</td><td id="setting3-dur-'+device.deviceId+'">'+device.navigationParams[2].duration+'</td><th>'
        +getEditButton(device.deviceId, device.navigationParams[2].type)+'</th>'+getModal(device.deviceId, device.navigationParams[2].type, device.navigationParams[2].frequency, device.navigationParams[2].duration)+
        '<tr><td>'+device.navigationParams[3].direction+'</td><td id="setting4-freq-'+device.deviceId+'">'+device.navigationParams[3].frequency+'</td><td id="setting4-dur-'+device.deviceId+'">'+device.navigationParams[3].duration+'</td><th>'
        +getEditButton(device.deviceId, device.navigationParams[3].type)+'</th>'+getModal(device.deviceId, device.navigationParams[3].type, device.navigationParams[3].frequency, device.navigationParams[3].duration)+
        '</tr>'+
        '</tbody>'+
        '</table>';
}

function getNavigationBar(device) {
    if(device.status == 'Connected') {
        return '<div class="btn-group">'+
            '<button type="button" id="1-'+device.deviceId+'" class="btn btn-default btn-lg navigation-button">'+
            '<span class="glyphicon glyphicon-chevron-left"></span> <b>LEFT</b>'+
            '</button>'+
            '<button type="button" id="3-'+device.deviceId+'" class="btn btn-danger btn-lg navigation-button">'+
            '<span class="glyphicon glyphicon-chevron-stop"></span> <b>STOP</b>'+
            '</button>'+
            '<button type="button" id="2-'+device.deviceId+'" class="btn btn-default btn-lg navigation-button">'+
            '<b>RIGHT</b> <span class="glyphicon glyphicon-chevron-right"></span>'+
            '</button>'+
            '</div>';
    } else {
        return '<div class="btn-group">'+
            '<button id="1-'+device.deviceId+'" type="button" class="btn btn-default btn-lg disabled navigation-button">'+
            '<span class="glyphicon glyphicon-chevron-left"></span> <b>LEFT</b>'+
            '</button>'+
            '<button id="3-'+device.deviceId+'" type="button" class="btn btn-danger btn-lg disabled navigation-button">'+
            '<span class="glyphicon glyphicon-chevron-stop"></span> <b>STOP</b>'+
            '</button>'+
            '<button id="2-'+device.deviceId+'" type="button" class="btn btn-default btn-lg disabled navigation-button">'+
            '<b>RIGHT</b> <span class="glyphicon glyphicon-chevron-right"></span>'+
            '</button>'+
            '</div>';
    }
}

function getVideo() {
    return '<video width="301" height="200" poster="../images/poster.PNG" controls>'+
        '<source src="../images/video.mp4" type="video/mp4">'+
        '</video>';
}

function getEditButton(deviceId, navType) {
    return '<button id="edit'+navType+'-'+deviceId+'" type="button" data-toggle="modal" data-target="#modal'+navType+'-'+deviceId+'" class="btn btn-success btn-xs btn-block edit-button">'+
        'EDIT <span class="glyphicon glyphicon-edit"></span>'+
        '</button>';
}

function getModal(deviceId, type, frequency, duration) {

    return '<div class="modal fade" id="modal'+type+'-'+deviceId+'" role="dialog">'+
        '<div class="modal-dialog modal-md">'+
        '<div class="modal-content">'+
        '<div class="modal-header">'+
        '<button type="button" id="modalcancel'+type+'-'+deviceId+'" class="close edit-button-modal-cancel" data-dismiss="modal">&times;</button>'+
    '<h4 class="modal-title">Edit</h4>'+
        '</div>'+
        '<div class="modal-body">'+
        '<div style="margin-top: 20px;" id="edit-frequencySlider-"'+deviceId+'>'+
        '<input type="range" min="1" max="1000" value="'+frequency+'" class="slider" id="edit'+type+'-frequency-input-'+deviceId+'">'+
        '<p style="font-size: 12.5px; padding: 10px 0px;">Frequency value (Hz): <span id="edit'+type+'-frequency-output-'+deviceId+'"></span></p>'+
        '</div>' +
        '<div id="edit-durationSlider-"'+deviceId+'>'+
        '<input type="range" min="1" max="10000" value="'+duration+'" class="slider" id="edit'+type+'-duration-input-'+deviceId+'">'+
        '<p style="font-size: 12.5px; padding: 10px 0px;">Duration value (ms): <span id="edit'+type+'-duration-output-'+deviceId+'"></span></p>'+
        '</div>'+
    '</div>'+
    '<div class="modal-footer">'+
        '<button type="submit" id="modalbutton'+type+'-'+deviceId+'" class="btn btn-success edit-button-modal" data-dismiss="modal">Save</button>'+
        '<button type="button" id="modalcancel'+type+'-'+deviceId+'" class="btn btn-default edit-button-modal-cancel" data-dismiss="modal">Close</button>'+
        '</div>'+
        '</div>'+
        '</div>'+
        '</div>';
}

function getTextPlaceholder(device) {
    var html = '<div id="messageForm-'+device.deviceId+'">'+
        '<div class="form-group">'+
        '<label for="messageText'+device.deviceId+'">Morse code message</label>'+
        '<textarea class="form-control" id="messageText-'+device.deviceId+'" rows="5" maxlength="250"></textarea>'+
        '</div>'+
        '<div id="message-frequencySlider-"'+device.deviceId+'>'+
        '<input type="range" min="1" max="1000" value="'+(device.navigationParams[3].frequency)+'" class="slider" id="message-frequency-input-'+device.deviceId+'">'+
        '<p style="font-size: 11px;">Frequency value (Hz): <span id="message-frequency-output-'+device.deviceId+'"></span></p>'+
        '</div>' +
        '<div id="message-durationSlider-"'+device.deviceId+'>'+
        '<input type="range" min="1" max="10000" value="'+(device.navigationParams[3].duration)+'" class="slider" id="message-duration-input-'+device.deviceId+'">'+
        '<p style="font-size: 11px;">Duration value (ms): <span id="message-duration-output-'+device.deviceId+'"></span></p>'+
        '</div>' + '</div>';

    if(device.status == 'Connected') {
        html += '<button id="submit-'+device.deviceId+'" type="submit" class="btn btn-primary btn-block btn-sm morse-button">Send</button>'+
            '</div>';
    } else {
        html += '<button id="submit-'+device.deviceId+'" type="submit" class="btn btn-primary btn-block btn-sm disabled morse-button">Send</button>'+
            '</div>';
    }
    return html;
}





