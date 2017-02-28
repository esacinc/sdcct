(function ($) {
    $.extend($.sdcct, {
        "form": $.extend(function () {
            return this;
        }, {
            "addErrors": function (form, data) {
                if (!$.isUndefined(data)) {
                    var dataErrorsGlobal = data["global"];
                    
                    if (dataErrorsGlobal) {
                        $.each(dataErrorsGlobal, function (dataErrorGlobalIndex, dataErrorGlobal) {
                            var dataErrorGlobalMsgs = dataErrorGlobal["msgs"];
                            
                            if (dataErrorGlobalMsgs) {
                                $.each(dataErrorGlobalMsgs, function (dataErrorGlobalMsgIndex, dataErrorGlobalMsg) {
                                    $.sdcct.form.addErrorMessage(form, "global", dataErrorGlobalMsg, "Global Error");
                                });
                            }
                        });
                    }
                    
                    var dataErrorsFieldsMap = data["fields"];
                    
                    if (dataErrorsFieldsMap) {
                        for ( var dataErrorFieldName in dataErrorsFieldsMap) {
                            if (dataErrorsFieldsMap.hasOwnProperty(dataErrorFieldName)) {
                                var dataErrorsField = dataErrorsFieldsMap[dataErrorFieldName];
                                
                                if (dataErrorsField) {
                                    $.each(dataErrorsField, function (dataErrorFieldIndex, dataErrorField) {
                                        var dataErrorFieldMsgs = dataErrorField["msgs"];
                                        
                                        if (dataErrorFieldMsgs) {
                                            $.each(dataErrorFieldMsgs, function (dataErrorFieldMsgIndex, dataErrorFieldMsg) {
                                                $.sdcct.form.addErrorMessage(form, "field", dataErrorFieldMsg, dataErrorFieldName);
                                            });
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            },
            "addErrorMessage": function (form, msgType, msg, fieldName) {
                var errorMsgsElem = $(form).find("div.has-error").find("ul");
                
                errorMsgsElem.append($("<li/>").append((msgType == "field") ? fieldName + " : " + msg : msg));
                
                return errorMsgsElem;
            },
            "clearErrorMessages": function (form) {
                $(form).find("div.has-error").find("ul").empty();
                $.sdcct.form.inputGroupAddons(form).hide();
            },
            "inputGroupAddons": function (form) {
                return $(form).find("div.form-group-addons div.input-group-addon");
            }
        })
    });
})(jQuery);
