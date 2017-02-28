(function ($) {
    $.extend($.fn.sdcct, {
        "testcases": $.extend(function () {
            return this;
        }, {
            "selectTestcase": function (event, form, testcases, roleSelected, formIdOptions, testcaseDescElem) {
                var elem = $(event.target), testcaseId = elem.val(), testcase;
                
                testcaseDescElem.empty();
                
                if (testcaseId != "" && testcaseId && !$.isEmptyObject(testcase = testcases[roleSelected].filter(function (testcase) {
                    return testcase["id"] == testcaseId;
                })) && (testcase = testcase[0])) {
                    testcaseDescElem.append(elem.sdcct.testcases.buildTestcaseDescription(testcase));
                    form.sdcct.testcases.setTestcaseFormId(testcase, formIdOptions);
                } else {
                    testcaseDescElem.append("<strong><em>None</em></strong>");
                }
                
                return testcase;
            },
            "setTestcaseFormId": function (testcase, options) {
                options.empty();
                
                var formIds = testcase["formIds"];
                
                if (!$.isNull(formIds)) {
                    $.each(formIds, function (index, value) {
                        options.append($("<option/>").text(value));
                    });
                }
                
                return formIds;
            },
            "buildTestcaseDescription": function (testcase) {
                var elem = $(this), testcaseDesc = testcase["desc"], testcaseDescElem = $("<div/>");
                
                testcaseDescElem.append(elem.sdcct.testcases.buildTestcaseItem("Description", testcaseDesc["text"]));
                
                if (!$.isNull(testcase["endpointAddr"])) {
                    testcaseDescElem.append(elem.sdcct.testcases.buildTestcaseItem("Endpoint Address", testcase["endpointAddr"]));
                }
                
                // TODO: determine how to present form ids for various testcases
                if (!$.isNull(testcase["formIds"])) {
                    testcaseDescElem.append(elem.sdcct.testcases.buildTestcaseItem("Form IDs", testcase["formIds"]));
                }
                
                testcaseDescElem.append(elem.sdcct.testcases.buildTestcaseItem("Content Type", testcase["contentType"]));
                testcaseDescElem.append(elem.sdcct.testcases.buildTestcaseItem("Negative", testcase["negative"]));
                testcaseDescElem.append(elem.sdcct.testcases.buildTestcaseItem("Optional", testcase["optional"]));
                testcaseDescElem.append(elem.sdcct.testcases.buildTestcaseItem("Transaction", testcase["transaction"]));
                testcaseDescElem.append(elem.sdcct.testcases.buildTestcaseMessageDetails("Message Request Details", testcase["requestInfo"]));
                testcaseDescElem.append(elem.sdcct.testcases.buildTestcaseMessageDetails("Message Response Details", testcase["responseInfo"]));
                testcaseDescElem.append(elem.sdcct.testcases.buildTestcaseItem("Underlying Specification References", testcaseDesc["specs"]));
                testcaseDescElem.append(elem.sdcct.testcases.buildTestcaseItem("Instructions", testcaseDesc["instructions"]));
                
                return testcaseDescElem;
            },
            "buildTestcaseSteps": function (testcaseStepsLbl, testcaseSteps) {
                // TODO: update with status and messages
                var testcaseStepsList = $("<ol/>");
                
                testcaseSteps.forEach(function (testcaseStep) {
                    testcaseStepsList.append($("<li/>").append($.fn.sdcct.testcases.buildTestcaseItem(testcaseStep["desc"]["text"], [ $.fn.sdcct.testcases.buildTestcaseItem("Specification Type", testcaseStep["specType"]) ])));
                });
                
                return $.fn.sdcct.testcases.buildTestcaseItem(testcaseStepsLbl, testcaseStepsList);
            },
            "buildTestcaseMessageDetails": function (testcaseMessageDetailsLbl, testcaseMessageInfo) {
                var testcaseMessageDetailsElem = $("<div/>");
                
                testcaseMessageDetailsElem.append($.fn.sdcct.testcases.buildTestcaseItemLabel(testcaseMessageDetailsLbl));
                testcaseMessageDetailsElem.append($("<ul/>").append($("<li/>").append($.fn.sdcct.testcases.buildTestcaseItem(testcaseMessageInfo["messageName"], $.map(testcaseMessageInfo["messageDetails"], function (
                    value, key) {
                    return (key + ": " + value);
                })))));
                
                return testcaseMessageDetailsElem;
            },
            "buildTestcaseMessage": function (testcaseMsgLevel, testcaseMsg) {
                var testcaseMsgLevelClassName = "text-";
                
                switch (testcaseMsgLevel) {
                    case "ERROR":
                        testcaseMsgLevelClassName += "danger";
                        break;
                    
                    case "WARN":
                        testcaseMsgLevelClassName += "warning";
                        break;
                    
                    case "INFO":
                        testcaseMsgLevelClassName += "info";
                        break;
                    
                    default:
                        testcaseMsgLevelClassName += "default";
                        break;
                }
                
                return $("<span/>").append($("<strong/>", {
                    "class": testcaseMsgLevelClassName
                }).text(testcaseMsgLevel), ": ", testcaseMsg);
            },
            "buildTestcaseItemLabel": function (testcaseItemLbl) {
                var testcaseItemLblElem = $("<span/>");
                testcaseItemLblElem.append($("<strong/>").text(testcaseItemLbl), ": ");
                
                return testcaseItemLblElem;
            },
            "buildTestcaseItem": function (testcaseItemLbl, testcaseItemValues) {
                var testcaseItemElem = $("<div/>");
                var testcaseItemLblElem = $.fn.sdcct.testcases.buildTestcaseItemLabel(testcaseItemLbl);
                testcaseItemElem.append(testcaseItemLblElem);
                
                if (!$.isBoolean(testcaseItemValues) && !$.isNumeric(testcaseItemValues) && (!testcaseItemValues || $.isEmptyObject(testcaseItemValues))) {
                    testcaseItemLblElem.append($("<i/>").text("None"));
                } else if ($.isArray(testcaseItemValues)) {
                    var testcaseItemValuesList = $("<ul/>");
                    
                    testcaseItemValues.forEach(function (testcaseItemValue) {
                        testcaseItemValuesList.append($("<li/>").append(testcaseItemValue));
                    });
                    
                    testcaseItemElem.append(testcaseItemValuesList);
                } else {
                    testcaseItemLblElem.append(($.isBoolean(testcaseItemValues) || $.isNumeric(testcaseItemValues)) ? testcaseItemValues.toString() : testcaseItemValues);
                }
                
                return testcaseItemElem;
            },
            "buildTestcaseResults": function (testcaseResultsAccordion, testcaseResult) {
                var testcaseSuccess = !$.isUndefined(testcaseResult["success"]) ? testcaseResult["success"] : false;
                var testcaseSuccessStr = testcaseSuccess ? "success" : "error";
                var testcaseSubmission = testcaseResult["submission"];
                var testcaseName = testcaseSubmission["testcase"]["name"];
                
                var testcaseResultHeaderElem = $("<h3/>");
                testcaseResultHeaderElem.enableClass("testcase-result-header");
                testcaseResultHeaderElem.enableClass("testcase-result-header-" + testcaseSuccessStr);
                testcaseResultHeaderElem.append($.fn.sdcct.testcases.buildTestcaseItem("Testcase", testcaseName));
                testcaseResultHeaderElem.append($.fn.sdcct.testcases.buildTestcaseItem("Endpoint Address", testcaseSubmission["endpointAddr"]));
                testcaseResultHeaderElem.append($.fn.sdcct.testcases.buildTestcaseItem("Form ID", testcaseSubmission["formId"]));
                
                testcaseResultsAccordion.append(testcaseResultHeaderElem);
                
                var testcaseResultBodyElem = $("<div/>");
                testcaseResultBodyElem.append($.fn.sdcct.testcases.buildTestcaseItem("Success", testcaseSuccess));
                
                var testcaseResultMsgs = [];
                
                $.each(testcaseResult["msgs"], function (testcaseMsgLevel, testcaseMsgs) {
                    var mappedTestcaseMsgs = $.map(testcaseMsgs, function (testcaseMsg) {
                        return $.fn.sdcct.testcases.buildTestcaseMessage(testcaseMsgLevel, testcaseMsg);
                    });
                    
                    $.each(mappedTestcaseMsgs, function (index, msg) {
                        testcaseResultMsgs.push(msg);
                    });
                });
                
                testcaseResultBodyElem.append($.fn.sdcct.testcases.buildTestcaseItem("Message(s)", testcaseResultMsgs));
                testcaseResultBodyElem.append($.fn.sdcct.testcases.buildTestcaseSteps("Testcase Steps", testcaseSubmission["testcase"]["steps"]));
                
                // TODO: update to include HttpRequestEvent and HttpResponseEvent
                if (!$.isUndefined(testcaseResult["wsRequestEvent"]) || !$.isUndefined(testcaseResult["wsResponseEvent"])) {
                    var numTestcaseResults = testcaseResultsAccordion.find("h3.testcase-result-header").length;
                    testcaseResultBodyElem.append($.fn.sdcct.testcases.buildTestcaseResultEventTabs(numTestcaseResults, testcaseResult));
                }
                
                testcaseResultsAccordion.append(testcaseResultBodyElem);
                
                testcaseResultsAccordion.accordion("refresh");
                testcaseResultsAccordion.accordion({
                    "active": -1
                });
                
                $("h3.testcase-result-header", testcaseResultsAccordion).each(function () {
                    var testcaseResultHeaderElem = $(this);
                    
                    var testcaseResultHeaderIcon = $("span.ui-accordion-header-icon", testcaseResultHeaderElem);
                    testcaseResultHeaderIcon.disableClass("ui-icon");
                    testcaseResultHeaderIcon.enableClass("glyphicon");
                    
                    if (testcaseResultHeaderElem.hasClass("testcase-result-header-success")) {
                        testcaseResultHeaderIcon.enableClass("glyphicon-ok-sign");
                        testcaseResultHeaderIcon.enableClass("glyphicon-type-success");
                    } else {
                        testcaseResultHeaderIcon.enableClass("glyphicon-remove-sign");
                        testcaseResultHeaderIcon.enableClass("glyphicon-type-error");
                    }
                });
            },
            "buildTestcaseResultEventTabs": function (numTestcaseResults, testcaseResult) {
                var testcaseResultEventElem = $("<div/>").attr({
                    "id": "testcase-result-events-tabs-" + numTestcaseResults,
                    "class": "tabs"
                });
                var testcaseResultEventTabsHeaderElem =
                    $("<ul/>").append($("<li/>").append($("<a/>").attr("href", "#ws-event-request-" + numTestcaseResults).text("Web Service Request Event"))).append($("<li/>").append($("<a/>").attr("href", "#ws-event-response-"
                        + numTestcaseResults).text("Web Service Response Event")));
                
                testcaseResultEventElem.append(testcaseResultEventTabsHeaderElem);
                testcaseResultEventElem.append($.fn.sdcct.testcases.buildTestcaseWsEvent("ws-event-request-" + numTestcaseResults, testcaseResult["wsRequestEvent"]));
                testcaseResultEventElem.append($.fn.sdcct.testcases.buildTestcaseWsEvent("ws-event-response-" + numTestcaseResults, testcaseResult["wsResponseEvent"]));
                
                return testcaseResultEventElem;
            },
            "buildTestcaseWsEvent": function (testcaseWsEventTabId, testcaseWsEvent) {
                // TODO: determine better grouping of items
                var elem = $(this), testcaseWsEventElem = $("<div/>").attr("id", testcaseWsEventTabId);
                
                // TODO: add additional request info
                if ($.isUndefined(testcaseWsEvent)) {
                    return testcaseWsEventElem.append("<strong><em>None</em></strong>");
                }
                
                testcaseWsEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Message Type", testcaseWsEvent["messageType"]));
                testcaseWsEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Endpoint Type", testcaseWsEvent["endpointType"]));
                testcaseWsEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Binding Name", testcaseWsEvent["bindingName"]));
                testcaseWsEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Operation Name", testcaseWsEvent["operationName"]));
                testcaseWsEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Direction", testcaseWsEvent["direction"]));
                testcaseWsEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Endpoint Address", testcaseWsEvent["endpointAddress"]));
                testcaseWsEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Endpoint Name", testcaseWsEvent["endpointName"]));
                testcaseWsEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Port Name", testcaseWsEvent["portName"]));
                testcaseWsEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Port Type Name", testcaseWsEvent["portTypeName"]));
                testcaseWsEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Service Name", testcaseWsEvent["serviceName"]));
                testcaseWsEventElem.append(elem.sdcct.testcases.buildTestcaseItem("SOAP Headers", testcaseWsEvent["soapHeaders"]));
                testcaseWsEventElem.append(elem.sdcct.testcases.buildTestcaseItem("SOAP Fault", $.isUndefined(testcaseWsEvent["soapFault"])));
                testcaseWsEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Transaction ID", testcaseWsEvent["txId"]));
                testcaseWsEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Payload", $("<pre/>").text(!$.isUndefined(testcaseWsEvent["prettyPayload"]) ? testcaseWsEvent["prettyPayload"] : testcaseWsEvent["payload"])));
                
                return testcaseWsEventElem;
            }
        })
    });
})(jQuery);
