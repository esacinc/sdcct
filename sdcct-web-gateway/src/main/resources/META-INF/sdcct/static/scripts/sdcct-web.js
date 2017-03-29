(function ($) {
    $.extend($, {
        "encodeJson": function (value, replacer, space) {
            return JSON.stringify(value, replacer, (space || "    "));
        },
        "isBoolean": function (value) {
            return !$.isNull(value) && !$.isUndefined(value) && (value.__proto__ == Boolean.prototype);
        },
        "isNull": function (value) {
            return value === null;
        },
        "isUndefined": function (value) {
            return value === undefined;
        }
    });

    $.extend($.fn, {
        "disableClass": function (classesArg) {
            return classesArg ? this.each(function () {
                    $(this).removeClass(classesArg);
                }) : this;
        },
        "enableClass": function (classesArg) {
            if (classesArg) {
                this.filter(function () {
                    return !$(this).hasClass(classesArg);
                }).each(function () {
                    $(this).addClass(classesArg);
                });
            }

            return this;
        },
        "disable": function () {
            return this.attr("disabled", "disabled");
        },
        "enable": function () {
            return this.removeAttr("disabled");
        }
    });

    $.extend($, {
        "sdcct": function () {
            return this;
        }
    });

    $.extend($.sdcct, {
        "poll": {
            "lastPollTimestamp": new Date().getTime(),
            "pollInterval": 15000,
            "pollIntervalId": undefined
        },
        "roles": {
            "FORM_ARCHIVER": "FORM_ARCHIVER",
            "FORM_FILLER": "FORM_FILLER",
            "FORM_MANAGER": "FORM_MANAGER",
            "FORM_RECEIVER": "FORM_RECEIVER"
        }
    });

    $.extend($.fn, {
        "sdcct": function (opts) {
            return this.each(function () {
                var elem = $(this);

                elem.data("sdcct", $.extend((elem.data("sdcct") || {}), {
                    "opts": (opts || {})
                }));
            });
        }
    });
})(jQuery);