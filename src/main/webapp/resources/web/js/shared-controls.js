//EP - Shared controls, used in admin and front-end.

//EP - Limit Text Box
(function (LimitTextBoxModule, $, undefined) {
    LimitTextBoxModule.init = function () {
        function generateGuid() {
            var ret = [];
            var chars = "0123456789abcdef";
            for (var i = 0; i < 32; i++) {
                var beginPos = parseInt(Math.random() * chars.length);
                ret.push(chars.substring(beginPos, beginPos + 1));
            }
            return ret.join("");
        }

        if (jQuery().limit !== "undefined") {
            $("textarea[maxlength], input[type='text'][maxlength][data-limit-box]").each(function () {
                //EP - something with unobtrusive validation, html encoding, and rendering updates, causes there to be
                //a leftover newline character which looks bad and messes up the count.
                var $this = $(this);
                if ($this.val() == "\n")
                    $this.val("");

                $this.limit();
            });

            $(".limitTextBoxSpellWrpr").each(function (i) {
                elem = $(this);
                var textId = elem.data("text-id");
                var spellId = textId + "_UltimateSpell";

                if (elem.data("spell-bound")) {

                    // We deleted the reference to the init'd ultimatespell so we have to unwind the operations and re-do them
                    elem.parent().find("span").remove();
                    elem.off("click");
                    //if (window.UltimateSpells != undefined) delete window.UltimateSpells[spellId];
                }
                var textName = elem.data("text-name");
                var spellName = textName + "$UltimateSpell";

                var generatedGuid = generateGuid();
                var outerSpan = $("<span/>", { "id": spellId + "Click" });
                outerSpan.append($("<span />", { "onclick": "UltimateSpellCheck('/UltimateSpellInclude/UltimateSpell.dialog.htm?cid=" + spellId + "','u" + generatedGuid + "');return false;" }));

                elem.on("click", function () {
                    UltimateSpellClick(spellId + "Click");
                }).parent().append(outerSpan);

                InitiateUltimateSpell(spellId, "", function () {
                    new UltimateSpell(spellId, spellName, true, '/UltimateSpellInclude/',
                        'en-US', '', 1, 10, textId, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 1, 'Ignore', 'Ignore All', 'Delete',
                        'Add to Dictionary', '(no spelling suggestions)', 'Delete Repeated Word', false,
                        300, false, 'http://dictionary.reference.com/browse/[WORD]', 'Look up meaning',
                        false, textId, 'font-weight:bold;color:#FF0000;', 0, 0, 'Not in dictionary:',
                        'Mixed case:', 'Repeated word:', 'Spell check complete.',
                        'Click OK to apply changes.', 'Loading...', 'Change to:', 'Suggestions:', 'Change',
                        'Change All', 'Delete All', 'Show Options...', 'Hide Options...', 'OK', 'Cancel',
                        'Ignore Words In Uppercase', 'Ignore Words In Mixed Case',
                        'Ignore Words With Numbers', 'Ignore Repeated Words', 'Ignore Internet Addresses',
                        'Ignore Email Addresses', 'Ignore Filenames', 'Ignore HTML Tags');
                });

                elem.data("spell-bound", true);
            });
        } else {
            logger.log("Limit Text Box plugin not found, could not bind Limit Text Box.");
        }
    }
}(window.LimitTextBoxModule = window.LimitTextBoxModule || {}, jQuery));

//EP - One Click Button - currently only works with a single OneClickButton per page.
(function (OneClickButtonModule, $, undefined) {
    var ctrl_to_disable;

    OneClickButtonModule.init = function () {
        //$('input').off('invalid').on('invalid', function () {
        //triggers when browser HTML5 validation fires, from like e-mail/date field types.
        //use if needed. by default, browser validation is disabled at the submit button level (formnovalidate attr).
        //});
    }

    OneClickButtonModule.PleaseWait = function (ctrl, msg) {
        var $ctrl = $(ctrl);
        $ctrl.data('oneclick-prevmsg', $ctrl.val());

        var msg_to_display = msg ? msg_to_display = msg : msg_to_display = 'Please wait...';

        $ctrl.data('oneclick-msg', msg_to_display);

        ctrl_to_disable = ctrl;

        window.setTimeout(function () { PleaseWaitTimeout($ctrl) }, 10);
    }

    OneClickButtonModule.PleaseWaitImageButton = function (ctrl) {
        ctrl_to_disable = ctrl
        window.setTimeout(function () { PleaseWaitTimeout($ctrl) }, 10);
    }

    function PleaseWaitTimeout(ctrl) {
        if (ctrl.attr('type') == 'image') {
            ctrl.attr('src', '/images/spacer.gif');
            ctrl.attr('onclick', 'return false');
        } else {
            ctrl.val(ctrl.data('oneclick-msg'));
            ctrl.prop("disabled", true);
        }
    }

    function PleaseWaitReset(ctrl) {
        var $ctrl = $(ctrl);
        $ctrl.prop("disabled", false);
        $ctrl.val($ctrl.data('oneclick-prevmsg'));
    }
}(window.OneClickButtonModule = window.OneClickButtonModule || {}, jQuery));

//END ONE CLICK BUTTON

//EP - Rate It Ajax/UpdatePanel Rebinding
(function (StarRatingModule, $, undefined) {
    StarRatingModule.init = function () {
        $('div.rateit').rateit();
    }
}(window.StarRatingModule = window.StarRatingModule || {}, jQuery));

//END ONE CLICK BUTTON

jQuery(function () {
    LimitTextBoxModule.init();
    BaseModule.addModuleRebindFunction("LimitTextBoxModule", LimitTextBoxModule.init);

    //Rate It automatically binds, so we only need to rebind if needed.
    BaseModule.addModuleRebindFunction("StarRatingModule", StarRatingModule.init);

    //OneClickButtonModule.init();
    //BaseModule.addModuleRebindFunction("OneClickButtonModule", OneClickButtonModule.init);
});