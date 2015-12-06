// Master Control for site overlays

jQuery(function ($) {
    // metadata allows array of data to be applied inline and be used wihin function
    $("body").on("click", ".trigger-overlay", function (e) {
        overlayDisplay(null, $(this).data());
        e.preventDefault();
    });
});


function overlayDisplay(content, data) {
    if (data === undefined) {
        data = "";
        data.options = "";
    };
    var $mainContent = $("#mainOverlayContent");

    // check to see if data.options are present
    if (data.options && data.options) {
        // sets data type for request
        var dataType = (data.options.dataType) ? data.options.dataType : "json";
        // sets directory for ajax
        var queryTool = (data.options.QueryTool) ? data.options.QueryTool : "store";
        // enables direct ajax URL control
        var ajaxUrl = (data.options.ajaxUrl) ? data.options.ajaxUrl : "/ajax/" + queryTool + "/ajax.aspx?";
    };

    // checks to see if content is being passed as a string to be used instead of ajax request
    if (content != null) {
        if (content.constructor == String) {
            // injects static content
            $mainContent.html(content);
            buildOverlay();
            return false;
        };
    };

    // checks to see if modal should use static content embedded within page instead of ajax request
    (data.options && data.options.StaticId) ? buildOverlay() : buildAjax();

    function buildAjax(cacheResults) {
        // query path built from data array pulled from object
        var query = ajaxUrl + $.param(data.query);

        // check for cache; process and output ajax query
        var cacheResp = window[query];
        if (!data.options.noCache && cacheResp) {
            handleOverlayResult(cacheResp, data, query);
        } else {
            //$.getJSON(query, function(o) { handleOverlayResult(o, data, query); });
            // above has been replaced with below for more control over requests
            $.ajax({
                "type": "GET",
                "data": "",
                "async": "false",
                "contentType": "application/json; charset=utf-8",
                "url": query,
                "dataType": dataType,
                "success": function (o) {
                    handleOverlayResult(o, data, query);
                }
            });

        };
    };

    function handleOverlayResult(o, data, query) {
        // handle ajax query results
        var ac = '';
        if (!o.Success) {
            ac += '<div class="errorMessage" style="margin:30px 30px 20px 30px;">';
            ac += '	<table cellspacing="0" cellpadding="10" border="0">';
            ac += '		<tr>';
            ac += '			<td valign="top" style="width: 30px;"><img width="24" height="24" alt="Error" style="border-style: none; padding-top: 6px;" src="/cms/images/exclam.gif"></td>';
            ac += '			<td style="width: 100%;"><p class="bold red">This was not processed due to the following reasons:</p>&raquo; ' + o.Error + '</td>';
            ac += '		</tr>';
            ac += '	</table>';
            ac += '</div>';

        } else {
            if (!data.options.noCache) window[query] = o;

            switch (data.query.f) {
                case 'BuyNow': ac = processBuyNow(o); break;
                case 'GetVideo': ac = o.VideoHtml; break;
                case 'GetProductVideo': ac = processGetVideo(o); break;
                case 'GetSizeChart': ac = processGetSizeChart(o); break;
                default: ac = (o.html) ? o.html : o.HTML;
            };
        };

        // injects ajax content inside of overlay container and triggers overlay
        $mainContent.html(ac);

        switch (data.query.f) {
            case 'BuyNow': buildBuyNow(o); break;
            case 'GetProductVideo': buildGetVideo(o); break;
        };

        $("div.sizeChartA").accordion({
            header: "h3",
            autoHeight: false,
            collapsible: true
        });

        buildOverlay();
    };

    function buildOverlay() {
        var $overlayId = $("#overlay-main");
        if (data.options && data.options.StaticId) {
            $overlayId = $("#" + data.options.StaticId);
            $mainContent = $("#" + data.options.StaticId + "Content");
        }
        if (data.options && data.options.oWidth) $overlayId.css('width', data.options.oWidth + 'px');

        // initializes and loads overlay.
        $overlayId.overlay({
            top: 'center',
            closeOnClick: true,
            oneInstance: false,
            expose: {
                color: '#000',
                loadSpeed: 0,
                closeSpeed: 0,
                maskId: 'exposeMask',
                opacity: 0.90
            },
            onLoad: function() { $("#exposeMask").css("height", $(document).height()); },
            onClose: function() { $overlayId.removeAttr('style'); },
            api: true
        }).load();

        // binds closing functions
        $overlayId.bgIframe().find(".overlayClose, .modalClose").click(function() {
            $overlayId.overlay().close();

            //if (!data.options.StaticId) $mainContent.html('');
        });
    };
};

function processGetSizeChart(o) {
    return o.HTML;
}

function processBuyNow(o) {
    var sBuyNow = '';
    sBuyNow += o.HTML

    return sBuyNow;
};

function buildBuyNow(o) {
    //alert(o.Selections.Variable);
    window[o.Selections.Variable] = new IdevSelections(o.Selections);
    window[o.Selections.Variable].Create();

    for (var i = 0; i < o.Selections.Classifications.length; i++) {
        var c = o.Selections.Classifications[i];
        if (c.s.length === 1) {
            window[o.Selections.Variable].Select(c.i, c.s[0].i, true);
        }
    }

    if (window.addthis){ window.addthis = null; }
    $.getScript('http://s7.addthis.com/js/250/addthis_widget.js#pubid=xxx',function(){

        var add_this_html =
            '<div class="addthis_toolbox addthis_default_style" addthis:url="' + o.ItemUrl + '">'+
            '<a class="addthis_button_twitter"></a> '+
            '<a class="addthis_button_preferred_1"></a>'+
            '<a class="addthis_button_preferred_2"></a>'+
            '<a class="addthis_button_facebook_like"></a>'+
            '</div>';

        $("#loadhere").html(add_this_html);
        addthis.init();
    });

    $(".siteOverlay select:not(.noStyle)").peek("selectmenu").hide();
    $(".siteOverlay .tabModule .tabdiv").peek("jScrollPane");

    var overlayTabs = $('.siteOverlay .tabModule').tabs();
    overlayTabs.find(".tabnav").children().first().addClass('firstTab');
    overlayTabs.find(".tabnav").children().last().addClass('lastTab');
};

function processGetVideo(o) {
    var s = '';
    s += ' <div class="pvInner">';
    s+= o.VideoCode;
    s += ' </div>';

    return s;
};

function buildGetVideo(o) {
    var flashvars = {};
    flashvars.datasrc = "/GetVideoPlayerXml.aspx?VideoId=" + o.VideoId + "%26Preview%3dFalse";
    var params = {};
    params.quality = "best";
    params.autostart = "true";
    params.videoid = o.VideoId;
    params.wmode = "transparent";
    params.allowfullscreen = "true";
    params.allowscriptaccess = "always";
    var attributes = {};
    swfobject.embedSWF("/flash/video_player.swf", "videoDiv", "384", "253", "9.0.0", false, flashvars, params, attributes);
}