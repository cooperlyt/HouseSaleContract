//EP - Shared controls, used in admin and front-end.
//EP - Placeholder Polyfill
(function (PlaceholderPolyfillModule, $, undefined) {
    PlaceholderPolyfillModule.init = function () {
        if (!Modernizr.input.placeholder)
            $('input[placeholder], textarea[placeholder]').placeHolder();
    }
}(window.PlaceholderPolyfillModule = window.PlaceholderPolyfillModule || {}, jQuery));

jQuery(function () {
    Modernizr.load({
        test: Modernizr.input.placeholder,
        nope: [
            '/cms/includes/polyfill/placeholder_polyfill.css',
            '/cms/includes/polyfill/placeholder_polyfill.jquery.min.js'
        ],
        complete: function () {
            PlaceholderPolyfillModule.init();
        }
    });
    BaseModule.addModuleRebindFunction("PlaceholderPolyfillModule", PlaceholderPolyfillModule.init);

    Modernizr.load({
        test: Modernizr.mq('only all'),
        nope: '/cms/includes/polyfill/respond.min.js'
    });
});