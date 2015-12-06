(function (mainNavigation, $, undefined) {
    mainNavigation.init = function() {
        var navAdd="<div style='display:block; position:relative; top:-6px; font-size:12px; color:#ff0000;'>All waiting lists currently closed</div>";
        var navAddAfter="Waiting Lists Applicants";
        $("#nav .mainNav>ul>li").each(function(i) {
            var rollover = $(this);
            var sub = $('>a',rollover).siblings(".sub");
            $(this).hoverIntent(function(){
                sub.fadeIn(250);
            }, function() {
                sub.fadeOut(200);
            });
        }).find("a:contains("+navAddAfter+")").after(navAdd);
        $("#railNav").find("a:contains("+navAddAfter+")").after("<div style='padding-left:10px; padding-right:10px;'>"+navAdd+"</div>");
    };
}(window.mainNavigation = window.mainNavigation || {}, jQuery));
jQuery(function ($) {
    mainNavigation.init();
//	BaseModule.addModuleRebindFunction("mainNavigation", mainNavigation.init);
});