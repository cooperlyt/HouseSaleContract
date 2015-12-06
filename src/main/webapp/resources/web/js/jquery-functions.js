var itemTimer;

jQuery(function ($) {

    function floatFN(d,b,o){
        if(d&&b){o.addClass("float_left_margin");}else if(!d&&b){o.addClass("float_right_margin");}
    }
    $("#mainContent img").each(function(i){
        $(this).css("float")=="left"?floatFN(true,true,$(this)):floatFN(true,false,$(this));
        $(this).css("float")=="right"?floatFN(false,true,$(this)):floatFN(false,false,$(this));
    })

    $("#mainContent").fontResizer({minFont:13,fontSet:13,maxFont:17,increment:2,cookieName:'_hacsc01sind',$decreaseClickItem:$('#textDecrease'),$increaseClickItem:$('#textIncrease')});
    $("#page_heading").fontResizer({minFont:16,fontSet:16,maxFont:20,increment:2,cookieName:'_hacsc02sind',$decreaseClickItem:$('#textDecrease'),$increaseClickItem:$('#textIncrease')});

    // equalize heights of children within columnRow class
    $(".columnRow").each(function () {
        $(this).find(".accountBlocks").matchHeights();
    });

    $(".truncate").truncate({
        max_length: 400,
        more: "read more",
        less: "read less"
    });

    // dialog windows
    $(".uiModal").dialog({
        autoOpen: false,
        width: "720px",
        bgiframe: true,
        modal: true,
        resizable: false,
        live: false,
        buttons: {
            Close: function () {
                $(this).dialog("close");
            }
        }
    });

    $(".uiDialog").dialog({
        autoOpen: false,
        width: "720px",
        bgiframe: true,
        modal: false,
        resizable: false,
        buttons: {
            Close: function () {
                $(this).dialog("close");
            }
        }
    });

    // END dialog windows

    // carousels
    /* This was moved to /cms/includes/js/modules/carouselScrollable.js
     $('div.carousel div.scrollable').each(function () {
     //var size = parseFloat($(this).parent().attr('class').split(' ').slice(-1).join().substring(11));
     var size = parseFloat($(this).attr('class').match(/\d\b/g));

     var $carousel = $(this).scrollable({
     size: size,
     items: ".items",
     clickable: false
     });

     var $items = $(this).children(':first').children().matchHeights();
     if ($items.length > size) { $(this).parent().find(".nextPage").removeClass('disabled'); };

     var itemHeight = $items.outerHeight();
     $(this).css({ 'height': itemHeight }).nextAll().each(function () {
     $(this).css({ 'top': (itemHeight - $(this).height()) / 2 });
     });
     });
     */
    // END carousels

    $(".tabModule").tabs();
    $(".mod_landing_gal li").matchHeights();

    //EP - Fix for Webkit issue with AjaxControlToolkit
    if (typeof Sys != 'undefined') { Sys.Browser.WebKit = {}; if (navigator.userAgent.indexOf('WebKit/') > -1) { Sys.Browser.agent = Sys.Browser.WebKit; Sys.Browser.version = parseFloat(navigator.userAgent.match(/WebKit\/(\d+(\.\d+)?)/)[1]); Sys.Browser.name = 'WebKit'; } }
});