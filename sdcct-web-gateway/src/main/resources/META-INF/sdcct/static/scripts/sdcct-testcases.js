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
                
                var forms = testcase["forms"];
                
                if (testcase["roleTested"] == $.sdcct.roles.FORM_RECEIVER || testcase["roleTested"] == $.sdcct.roles.FORM_ARCHIVER) {
                    var formId;
                    
                    if ($.isUndefined(formIds) && !$.isUndefined(forms)) {
                        $.each(forms, function (index, value) {
                            formId = value["identifier"];
                            options.append($("<option/>").text(formId));
                        });
                    }
                }
                
                return formIds;
            },
            "buildTestcaseDescription": function (testcase) {
                var elem = $(this), testcaseDesc = testcase["desc"], testcaseDescElem = $("<div/>");
                
                testcaseDescElem.append(elem.sdcct.testcases.buildTestcaseItem("Description", testcaseDesc["text"]));
                
                if (!$.isNull(testcase["endpointAddr"])) {
                    testcaseDescElem.append(elem.sdcct.testcases.buildTestcaseItem("Endpoint Address", testcase["endpointAddr"]));
                }
                
                var formIds = testcase["formIds"];
                
                if (!$.isNull(formIds) && !$.isUndefined(formIds)) {
                    testcaseDescElem.append(elem.sdcct.testcases.buildTestcaseItem("Form IDs", testcase["formIds"]));
                } else {
                    var forms = testcase["forms"];
                    formIds = [];
                    
                    if (!$.isUndefined(forms)) {
                        $.each(forms, function (index, value) {
                            formIds.push(value["identifier"]);
                        });
                    }
                    
                    testcaseDescElem.append(elem.sdcct.testcases.buildTestcaseItem("Form IDs", formIds));
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
                var testcaseStepsList = $("<ol/>");
                
                if (!$.isEmptyObject(testcaseSteps)) {
                    testcaseSteps.forEach(function (testcaseStep) {
                        testcaseStepsList.append($("<li/>").append($.fn.sdcct.testcases.buildTestcaseItem(testcaseStep["desc"]["text"], [ $.fn.sdcct.testcases.buildTestcaseItem("Specification Type", testcaseStep["specType"]) ])));
                    });
                } else {
                    testcaseStepsList.append("None");
                }
                
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
            "buildTestcaseResults": function (testcaseResultsEmptyWellElem, testcaseResultsElem, testcaseResult) {
                var testcaseSuccess = !$.isUndefined(testcaseResult["success"]) ? testcaseResult["success"] : false;
                var testcaseSuccessStr = testcaseSuccess ? "success" : "error";
                var testcaseSubmission = testcaseResult["submission"];
                var testcase = testcaseSubmission["testcase"];
                var testcaseTxId = testcaseResult["txId"];
                var testcaseName = !$.isUndefined(testcase) ? testcase["name"] : "None";
                
                var testcaseResultPanelElem = $("<div/>", {
                    "class": ("panel panel-" + (testcaseSuccess ? "success" : "danger")),
                    "id": ("testcase-result-panel-" + testcaseTxId)
                });
                
                var testcaseResultPanelHeaderElem = $("<div/>", {
                    "class": ("panel-heading testcase-result-header testcase-result-header-" + testcaseSuccessStr),
                    "id": ("testcase-result-panel-header" + testcaseTxId),
                    "role": "tab"
                });
                testcaseResultPanelElem.append(testcaseResultPanelHeaderElem);
                
                var testcaseResultPanelTitleElem = $("<div/>", {
                    "class": "panel-title"
                });
                testcaseResultPanelHeaderElem.append(testcaseResultPanelTitleElem);
                
                var testcaseResultPanelCloseButtonContentElem = $("<span/>", {
                    "aria-hidden": true
                });
                testcaseResultPanelCloseButtonContentElem[0].innerHTML += "&#215;";
                testcaseResultPanelTitleElem.append($("<button/>", {
                    "aria-label": "Close",
                    "class": "close",
                    "type": "button"
                }).click($.proxy(function (event) {
                    testcaseResultPanelElem.remove();
                    
                    if (testcaseResultsElem.find("div.panel").length == 0) {
                        testcaseResultsEmptyWellElem.show();
                    }
                }, this)).append(testcaseResultPanelCloseButtonContentElem));
                
                var testcaseResultPanelHeaderCollapseElem = $("<a/>", {
                    "aria-controls": ("#testcase-result-panel-" + testcaseTxId),
                    "data-parent": ("#" + testcaseResultsElem.attr("id")),
                    "data-toggle": "collapse",
                    "href": ("#testcase-result-panel-collapse-" + testcaseTxId),
                    "role": "button"
                }).append($("<span/>").append($("<i/>", {
                    "class": ("fa fa-fw fa-" + (testcaseSuccess ? "check" : "times") + "-circle-o")
                })));
                testcaseResultPanelTitleElem.append(testcaseResultPanelHeaderCollapseElem);
                
                var testcaseResultPanelHeaderCollapseContentElem = $("<span/>");
                testcaseResultPanelHeaderCollapseElem.append(testcaseResultPanelHeaderCollapseContentElem);
                
                testcaseResultPanelHeaderCollapseContentElem.append($.fn.sdcct.testcases.buildTestcaseItem("Transaction ID", testcaseTxId));
                testcaseResultPanelHeaderCollapseContentElem.append($.fn.sdcct.testcases.buildTestcaseItem("Testcase", testcaseName));
                testcaseResultPanelHeaderCollapseContentElem.append($.fn.sdcct.testcases.buildTestcaseItem("Endpoint Address", testcaseSubmission["endpointAddr"]));
                testcaseResultPanelHeaderCollapseContentElem.append($.fn.sdcct.testcases.buildTestcaseItem("Form ID", testcaseSubmission["formId"]));
                testcaseResultPanelHeaderCollapseContentElem.append($.fn.sdcct.testcases.buildTestcaseItem("Submitted Timestamp", new Date(
                    testcaseSubmission["submittedTimestamp"]).toString()));
                testcaseResultPanelHeaderCollapseContentElem.append($.fn.sdcct.testcases.buildTestcaseItem("Processed Timestamp", new Date(
                    testcaseResult["processedTimestamp"]).toString()));
                
                var testcaseResultCollapseElem = $("<div/>", {
                    "aria-labelledby": ("testcase-result-panel-header" + testcaseTxId),
                    "class": "collapse in panel-collapse",
                    "id": ("testcase-result-panel-collapse-" + testcaseTxId),
                    "role": "tabpanel"
                });
                testcaseResultPanelElem.append(testcaseResultCollapseElem);
                
                var testcaseResultPanelBodyElem = $("<div/>", {
                    "class": "panel-body"
                });
                testcaseResultCollapseElem.append(testcaseResultPanelBodyElem);
                
                testcaseResultPanelBodyElem.append($.fn.sdcct.testcases.buildTestcaseItem("Success", testcaseSuccess));
                
                var testcaseResultMsgs = [];
                
                $.each(testcaseResult["msgs"], function (testcaseMsgLevel, testcaseMsgs) {
                    var mappedTestcaseMsgs = $.map(testcaseMsgs, function (testcaseMsg) {
                        return $.fn.sdcct.testcases.buildTestcaseMessage(testcaseMsgLevel, testcaseMsg);
                    });
                    
                    $.each(mappedTestcaseMsgs, function (index, msg) {
                        testcaseResultMsgs.push(msg);
                    });
                });
                
                testcaseResultPanelBodyElem.append($.fn.sdcct.testcases.buildTestcaseItem("Message(s)", testcaseResultMsgs));
                testcaseResultPanelBodyElem.append($.fn.sdcct.testcases.buildTestcaseSteps("Testcase Steps", !$.isUndefined(testcase) ? testcase["steps"] : []));
                
                if (!$.isUndefined(testcaseResult["wsRequestEvent"]) || !$.isUndefined(testcaseResult["wsResponseEvent"])) {
                    testcaseResultPanelBodyElem.append($.fn.sdcct.testcases.buildTestcaseResultEventTabs(testcaseTxId, testcaseResult));
                }
                
                testcaseResultsElem.append(testcaseResultPanelElem);
            },
            "buildTestcaseResultEventTabs": function (testcaseTxId, testcaseResult) {
                var testcaseResultEventTabsElem = $("<div/>", {
                    "id": ("testcase-result-event-tabs-" + testcaseTxId)
                });
                
                var testcaseResultEventTabsHeaderElem =
                    $("<ul/>", {
                        "class": "nav nav-tabs",
                        "role": "tablist"
                    }).append($.fn.sdcct.testcases.buildTestcaseResultEventTabHeaderElem(testcaseTxId, ("ws-event-request-" + testcaseTxId), "Web Service Request Event")).append($.fn.sdcct.testcases.buildTestcaseResultEventTabHeaderElem(testcaseTxId, ("ws-event-response-" + testcaseTxId), "Web Service Response Event", true)).append($.fn.sdcct.testcases.buildTestcaseResultEventTabHeaderElem(testcaseTxId, ("http-event-request-" + testcaseTxId), "HTTP Request Event")).append($.fn.sdcct.testcases.buildTestcaseResultEventTabHeaderElem(testcaseTxId, ("http-event-response-" + testcaseTxId), "HTTP Response Event"));
                testcaseResultEventTabsElem.append(testcaseResultEventTabsHeaderElem);
                
                var testcaseResultEventTabsContentElem =
                    $("<div/>", {
                        "class": "tab-content"
                    }).append($.fn.sdcct.testcases.buildTestcaseWsEvent(("ws-event-request-" + testcaseTxId), testcaseResult["wsRequestEvent"])).append($.fn.sdcct.testcases.buildTestcaseWsEvent(("ws-event-response-" + testcaseTxId), testcaseResult["wsResponseEvent"], true)).append($.fn.sdcct.testcases.buildTestcaseHttpRequestEvent(("http-event-request-" + testcaseTxId), testcaseResult["httpRequestEvent"])).append($.fn.sdcct.testcases.buildTestcaseHttpResponseEvent(("http-event-response-" + testcaseTxId), testcaseResult["httpResponseEvent"]));
                testcaseResultEventTabsElem.append(testcaseResultEventTabsContentElem);
                
                return testcaseResultEventTabsElem;
            },
            "buildTestcaseResultEventTabHeaderElem": function (testcaseTxId, testcaseResultEventTabId, testcaseResultEventTabHeaderText,
                testcaseResultEventTabActive) {
                return $("<li/>", {
                    "class": (testcaseResultEventTabActive ? "active" : ""),
                    "role": "presentation"
                }).append($("<a/>", {
                    "aria-controls": testcaseResultEventTabId,
                    "data-toggle": "tab",
                    "href": ("#" + testcaseResultEventTabId),
                    "role": "tab"
                }).text(testcaseResultEventTabHeaderText));
            },
            "buildTestcaseHttpEvent": function (testcaseHttpEventElem, testcaseHttpEvent) {
                var elem = $(this);
                
                testcaseHttpEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Endpoint Type", testcaseHttpEvent["endpointType"]));
                testcaseHttpEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Event Type", testcaseHttpEvent["eventType"]));
                testcaseHttpEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Transaction ID", testcaseHttpEvent["txId"]));
                testcaseHttpEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Character Encoding", testcaseHttpEvent["characterEncoding"]));
                testcaseHttpEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Content Length", testcaseHttpEvent["contentLength"]));
                testcaseHttpEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Content Type", testcaseHttpEvent["contentType"]));
                
                var headers = testcaseHttpEvent["headers"];
                var headersList = $("<ul/>");
                
                if (!$.isEmptyObject(headers)) {
                    $.each(headers, function (key, val) {
                        headersList.append($("<li/>").append($.fn.sdcct.testcases.buildTestcaseItem(key, val.join())));
                    });
                } else {
                    headersList.append("None");
                }
                
                testcaseHttpEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Headers", headersList));
                
                return testcaseHttpEventElem;
            },
            "buildTestcaseHttpRequestEvent": function (testcaseHttpEventTabId, testcaseHttpEvent, testcaseResultEventTabActive) {
                var elem = $(this), testcaseHttpEventElem = $("<div/>", {
                    "class": ((testcaseResultEventTabActive ? "active " : "") + "tab-pane"),
                    "id": testcaseHttpEventTabId,
                    "role": "tabpanel"
                });
                
                if ($.isUndefined(testcaseHttpEvent)) {
                    return testcaseHttpEventElem.append("<strong><em>None</em></strong>");
                }
                
                testcaseHttpEventElem = $.fn.sdcct.testcases.buildTestcaseHttpEvent(testcaseHttpEventElem, testcaseHttpEvent);
                
                testcaseHttpEventElem.append(elem.sdcct.testcases.buildTestcaseItem("HTTP Method", testcaseHttpEvent["method"]));
                testcaseHttpEventElem.append(elem.sdcct.testcases.buildTestcaseItem("HTTP Protocol", testcaseHttpEvent["protocol"]));
                testcaseHttpEventElem.append(elem.sdcct.testcases.buildTestcaseItem("HTTP Scheme", testcaseHttpEvent["scheme"]));
                testcaseHttpEventElem.append(elem.sdcct.testcases.buildTestcaseItem("URI", testcaseHttpEvent["uri"]));
                testcaseHttpEventElem.append(elem.sdcct.testcases.buildTestcaseItem("URL", testcaseHttpEvent["url"]));
                testcaseHttpEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Local Name", testcaseHttpEvent["localName"]));
                testcaseHttpEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Local Port", testcaseHttpEvent["localPort"]));
                testcaseHttpEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Remote Address", testcaseHttpEvent["remoteAddr"]));
                testcaseHttpEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Remote Host", testcaseHttpEvent["remoteHost"]));
                testcaseHttpEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Remote Port", testcaseHttpEvent["remotePort"]));
                testcaseHttpEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Server Name", testcaseHttpEvent["serverName"]));
                testcaseHttpEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Server Port", testcaseHttpEvent["serverPort"]));
                
                return testcaseHttpEventElem;
            },
            "buildTestcaseHttpResponseEvent": function (testcaseHttpEventTabId, testcaseHttpEvent, testcaseResultEventTabActive) {
                var elem = $(this), testcaseHttpEventElem = $("<div/>", {
                    "class": ((testcaseResultEventTabActive ? "active " : "") + "tab-pane"),
                    "id": testcaseHttpEventTabId,
                    "role": "tabpanel"
                });
                
                if ($.isUndefined(testcaseHttpEvent)) {
                    return testcaseHttpEventElem.append("<strong><em>None</em></strong>");
                }
                
                testcaseHttpEventElem = $.fn.sdcct.testcases.buildTestcaseHttpEvent(testcaseHttpEventElem, testcaseHttpEvent);
                testcaseHttpEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Status Code", testcaseHttpEvent["statusCode"]));
                testcaseHttpEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Status Message", testcaseHttpEvent["statusMessage"]));
                
                return testcaseHttpEventElem;
            },
            "buildTestcaseWsEvent": function (testcaseWsEventTabId, testcaseWsEvent, testcaseResultEventTabActive) {
                var elem = $(this), testcaseWsEventElem = $("<div/>", {
                    "class": ((testcaseResultEventTabActive ? "active " : "") + "tab-pane"),
                    "id": testcaseWsEventTabId,
                    "role": "tabpanel"
                });
                
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
                testcaseWsEventElem.append(elem.sdcct.testcases.buildTestcaseItem("SOAP Fault", !$.isUndefined(testcaseWsEvent["soapFault"])));
                testcaseWsEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Transaction ID", testcaseWsEvent["txId"]));
                testcaseWsEventElem.append(elem.sdcct.testcases.buildTestcaseItem("Payload", $("<pre/>").text(!$.isUndefined(testcaseWsEvent["prettyPayload"]) ? testcaseWsEvent["prettyPayload"] : testcaseWsEvent["payload"])));
                
                return testcaseWsEventElem;
            },
            "pollTestcaseResults": function (resultsEmptyWellElem, resultsElem) {
                $.ajax({
                    "complete": function (jqXhr, statusText) {
                        var statusCode = jqXhr["status"];
                        
                        try {
                            if (statusCode == 200) {
                                resultsEmptyWellElem.hide();
                                
                                var processedTestcaseResultTxIds = [];
                                
                                $.each($.parseJSON(jqXhr["responseText"]), function (resultIndex, testcaseResult) {
                                    $.fn.sdcct.testcases.buildTestcaseResults(resultsEmptyWellElem, resultsElem, testcaseResult);
                                    
                                    processedTestcaseResultTxIds.push(testcaseResult["txId"]);
                                });
                                
                                console.info(("Testcase result(s) poll AJAX query was successful (statusCode=" + statusCode + ", statusText=" + statusText
                                    + "): txIds=[" + processedTestcaseResultTxIds.join(", ") + "]"));
                            } else if (statusCode == 204) {
                                console.info(("No testcase result(s) received from poll AJAX query (statusCode=" + statusCode + ", statusText=" + statusText + ")."));
                            } else {
                                console.error(("Testcase result(s) poll AJAX query failed (statusCode=" + statusCode + ", statusText=" + statusText + ")."));
                            }
                        } finally {
                            $.sdcct.poll.lastPollTimestamp = new Date().getTime();
                        }
                    },
                    "contentType": "application/json",
                    "error": function (jqXhr, statusText, error) {
                        console.error(("Testcase result(s) poll AJAX query failed (statusCode=" + jqXhr["status"] + ", statusText=" + statusText + "): " + error));
                    },
                    "headers": {
                        "Accept": "application/json"
                    },
                    "type": "GET",
                    "url": (IHE_TESTCASES_RESULTS_POLL_URL + "?submittedAfterTimestamp=" + $.sdcct.poll.lastPollTimestamp)
                });
            }
        })
    });
})(jQuery);
