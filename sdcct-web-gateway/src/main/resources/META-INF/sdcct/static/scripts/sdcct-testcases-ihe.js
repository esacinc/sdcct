(function ($) {
    $.extend($.sdcct, {
        "ihe": $.extend(function () {
            return this;
        }, {
            "PROCESS_IHE_TESTCASE_ERROR_MSG_FORMAT": "Unable to process IHE testcase (id=%s, name=%s) submission (statusCode=%s, statusText=%s, error=%s):\n%s",
            
            "processIheTestcase": function (formElem) {
                var testcaseIheProcess = testcaseIheSubmission["testcase"], testcaseIheProcessId = testcaseIheProcess["id"], testcaseIheProcessName =
                    testcaseIheProcess["name"];
                
                $.ajax({
                    "beforeSend": function () {
                        formTestcasesIhe.find(".form-control").attr("disabled", "disabled");
                        
                        testcaseIheResults.find("div.panel div.panel-collapse").collapse("hide");
                        
                        iheTestcaseResultsProcessingWellElem.show();
                    },
                    "cache": false,
                    "complete": function (jqXhr, statusText) {
                        formTestcasesIhe.find(".form-control").removeAttr("disabled");
                        
                        iheTestcaseResultsProcessingWellElem.hide();
                    },
                    "contentType": "application/json",
                    "data": $.encodeJson(testcaseIheSubmission),
                    "dataType": "json",
                    "error": function (jqXhr, statusText, error) {
                        var respText = (jqXhr["responseText"] || ""), resp, errorMsg =
                            sprintf($.sdcct.ihe.PROCESS_IHE_TESTCASE_ERROR_MSG_FORMAT, testcaseIheProcessId, testcaseIheProcessName, jqXhr["status"], statusText, error, respText);
                        
                        console.error(errorMsg);
                        
                        if (respText) {
                            resp = $.parseJSON(respText);
                        } else {
                            resp = {
                                "global": {
                                    "msgs": [ errorMsg ]
                                }
                            };
                        }
                        
                        $.sdcct.form.addErrors(formTestcasesIhe, resp);
                        $.sdcct.form.inputGroupAddons(formTestcasesIhe).show();
                    },
                    "headers": {
                        "Accept": "application/json"
                    },
                    "type": formElem.attr("method"),
                    "url": formElem.attr("action")
                });
            }
        })
    });
    
    var formTestcasesIhe, testcasesIheSelect, testcaseIheEndpointAddr, testcaseIheFormId, testcaseIheSubmit, testcaseIheReset, testcaseIheDesc, testcaseIheDescFormGroup, testcaseIheResults, testcaseIheSubmission, iheTestcaseInfo, iheRoleTested, iheRoleTestedSelection, testcaseIheSelectionGroup, testcaseIheSdcctInitiatedFormGroup, formIdOptions, iheTestcaseResultsEmptyWellElem, iheTestcaseResultsProcessingWellElem, selectedIheTestcase;
    
    $(document).ready(function () {
        iheTestcaseInfo = {
            "FORM_ARCHIVER": {
                "action": IHE_TESTCASES_PROCESS_URLS[$.sdcct.roles.FORM_ARCHIVER],
                "testcases": IHE_TESTCASES[$.sdcct.roles.FORM_ARCHIVER],
                "testcaseSubmissionType": "iheFormArchiverTestcaseSubmission",
                "testcaseType": "iheFormArchiverTestcase"
            },
            "FORM_FILLER": {
                "testcases": IHE_TESTCASES[$.sdcct.roles.FORM_FILLER]
            },
            "FORM_MANAGER": {
                "action": IHE_TESTCASES_PROCESS_URLS[$.sdcct.roles.FORM_MANAGER],
                "testcases": IHE_TESTCASES[$.sdcct.roles.FORM_MANAGER],
                "testcaseSubmissionType": "iheFormManagerTestcaseSubmission",
                "testcaseType": "iheFormManagerTestcase"
            },
            "FORM_RECEIVER": {
                "action": IHE_TESTCASES_PROCESS_URLS[$.sdcct.roles.FORM_RECEIVER],
                "testcases": IHE_TESTCASES[$.sdcct.roles.FORM_RECEIVER],
                "testcaseSubmissionType": "iheFormReceiverTestcaseSubmission",
                "testcaseType": "iheFormReceiverTestcase"
            }
        };
        
        iheRoleTested = $("input[name=\"role-tested\"]");
        iheRoleTestedSelection = iheRoleTested.filter(":checked").val();
        
        formTestcasesIhe = $("form[id=\"form-testcases-ihe\"]");
        testcaseIheSelectionGroup = $(".ihe-testcase-selection-group", formTestcasesIhe);
        testcaseIheSdcctInitiatedFormGroup = $(".ihe-sdcct-initiated-form-group", formTestcasesIhe);
        testcasesIheSelect = $("select#testcase-ihe-select", formTestcasesIhe);
        testcaseIheEndpointAddr = $("input#testcase-ihe-endpoint-addr", formTestcasesIhe);
        testcaseIheFormId = $("input#testcase-ihe-form-id", formTestcasesIhe);
        formIdOptions = $("datalist#formIds");
        
        iheTestcaseResultsEmptyWellElem = $("div#well-testcase-ihe-results-empty");
        iheTestcaseResultsProcessingWellElem = $("div#well-testcase-ihe-results-processing");
        
        testcaseIheSubmit = $("button#testcase-ihe-submit", formTestcasesIhe);
        testcaseIheReset = $("button#testcase-ihe-reset", formTestcasesIhe);
        testcaseIheDesc = $("div#testcase-ihe-desc", formTestcasesIhe);
        testcaseIheDescFormGroup = $(".ihe-testcase-desc-form-group", formTestcasesIhe);
        testcaseIheResults = $("div#testcase-ihe-results", formTestcasesIhe);
        
        testcaseIheSelectionGroup.hide();
        testcaseIheSdcctInitiatedFormGroup.hide();
        testcaseIheDescFormGroup.hide();
        
        $(iheRoleTested).on("change", function () {
            iheRoleTestedSelection = $(this).filter(":checked").val();
            
            testcasesIheSelect.empty();
            testcasesIheSelect.append($("<option/>").attr("value", "").text("-- No testcase selected --"));
            
            $.each(iheTestcaseInfo[iheRoleTestedSelection]["testcases"], function (index, testcase) {
                testcasesIheSelect.append($("<option/>").attr("value", testcase["id"]).text(testcase["name"]));
            });
            
            formTestcasesIhe.attr("action", iheTestcaseInfo[iheRoleTestedSelection]["action"]);
            testcasesIheSelect.trigger("change");
            
            testcaseIheSelectionGroup.show();
            testcaseIheEndpointAddr.val("");
            testcaseIheFormId.val("");
            
            if (iheRoleTestedSelection != $.sdcct.roles.FORM_FILLER) {
                testcaseIheSdcctInitiatedFormGroup.show();
            } else {
                testcaseIheSdcctInitiatedFormGroup.hide();
            }
        });
        
        formTestcasesIhe.submit(function (event) {
            event.preventDefault();
            
            $.sdcct.form.clearErrorMessages(formTestcasesIhe);
            
            testcaseIheSubmission = {
                "@type": iheTestcaseInfo[iheRoleTestedSelection]["testcaseSubmissionType"],
                "endpointAddr": testcaseIheEndpointAddr.val(),
                "formId": testcaseIheFormId.val(),
                "testcase": {
                    "@type": iheTestcaseInfo[iheRoleTestedSelection]["testcaseType"],
                    "id": testcasesIheSelect.val()
                }
            };
            
            $.sdcct.ihe.processIheTestcase(formTestcasesIhe);
        });
        
        testcasesIheSelect.change(function (event) {
            selectedIheTestcase =
                $(event.target).sdcct.testcases.selectTestcase(event, formTestcasesIhe, IHE_TESTCASES, iheRoleTestedSelection, formIdOptions, testcaseIheDesc);
            
            if (!$.isUndefined(selectedIheTestcase)) {
                if (iheRoleTestedSelection == $.sdcct.roles.FORM_FILLER) {
                    testcaseIheDesc.prepend($.fn.sdcct.testcases.buildTestcaseItem("Endpoint Address", IHE_TESTCASES_ENDPOINT_ADDRESSES[selectedIheTestcase["sdcctRole"]]));
                }
                
                testcaseIheDescFormGroup.show();
                testcaseIheEndpointAddr.enable();
                
                if (selectedIheTestcase["negative"] && selectedIheTestcase["formIds"].length == 0) {
                    testcaseIheFormId.val("");
                    testcaseIheFormId.disable();
                } else {
                    testcaseIheFormId.enable();
                }
                
                testcaseIheSubmit.enable();
            } else {
                testcaseIheEndpointAddr.disable();
                testcaseIheFormId.disable();
                testcaseIheSubmit.disable();
            }
            
            $.sdcct.form.clearErrorMessages(formTestcasesIhe);
        });
        
        testcaseIheReset.click(function (event) {
            testcaseIheSelectionGroup.hide();
            testcaseIheSdcctInitiatedFormGroup.hide();
            testcaseIheDescFormGroup.hide();
            iheTestcaseResultsEmptyWellElem.show();
            testcaseIheResults.empty();
            testcaseIheEndpointAddr.disable();
            testcaseIheFormId.disable();
            
            $.sdcct.form.clearErrorMessages(formTestcasesIhe);
        });
        
        var testcasesIheResultsSockJsClient = new SockJS(IHE_TESTCASES_RESULTS_WEBSOCKET_URL);
        testcasesIheResultsSockJsClient.debug = function (msg) {
            console.debug(sprintf("SockJS WebSocket client: %s", msg));
        };
        
        var testcasesIheResultsStompClient = Stomp.over(testcasesIheResultsSockJsClient);
        testcasesIheResultsStompClient.debug = function (msg) {
            console.debug(sprintf("STOMP WebSocket client: %s", msg));
        };
        
        window.addEventListener("beforeunload", $.proxy(function (event) {
            if (testcasesIheResultsStompClient) {
                try {
                    testcasesIheResultsStompClient.unsubscribe();
                } catch (e) {
                    console.warn("Unable to unsubscribe IHE testcases results STOMP WebSocket client.", e);
                }
                
                try {
                    testcasesIheResultsStompClient.disconnect();
                } catch (e) {
                    console.warn("Unable to disconnect IHE testcases results STOMP WebSocket client.", e);
                }
            }
            
            return null;
        }, this), false);
        
        testcasesIheResultsStompClient.connect({}, $.proxy(function (frame) {
            console.info(sprintf("IHE testcases results STOMP WebSocket client connected (url=%s):\n%s", IHE_TESTCASES_RESULTS_WEBSOCKET_URL, $.encodeJson(frame)));
            
            testcasesIheResultsStompClient.subscribe(IHE_TESTCASES_RESULTS_TOPIC_WEBSOCKET_ENDPOINT, $.proxy(function (msg) {
                var testcaseResult = $.parseJSON(msg.body)["payload"], testcaseTxId = testcaseResult["txId"], testcaseSubmission = testcaseResult["submission"], testcase =
                    testcaseSubmission["testcase"];
                
                $.fn.sdcct.testcases.buildTestcaseResults(iheTestcaseResultsEmptyWellElem, testcaseIheResults, testcaseResult, testcaseTxId);
                
                iheTestcaseResultsEmptyWellElem.hide();
                
                console.info(sprintf("Added IHE testcase (id=%s, name=%s) result (txId=%s, submittedTimestamp=%s, processedTimestamp=%s).", (testcase ? testcase["id"] : null), (testcase ? testcase["name"] : null), testcaseTxId, new Date(
                    testcaseSubmission["submittedTimestamp"]).toString(), new Date(testcaseResult["processedTimestamp"]).toString()));
            }, this));
            
            console.info(sprintf("IHE testcases results STOMP WebSocket client subscribed to topic (endpoint=%s).", IHE_TESTCASES_RESULTS_TOPIC_WEBSOCKET_ENDPOINT));
        }, this), $.proxy(function (frame) {
            console.error(sprintf("IHE testcases results STOMP WebSocket client failed to connect (url=%s):\n%s", IHE_TESTCASES_RESULTS_WEBSOCKET_URL, $.encodeJson(frame)));
        }, this));
    });
})(jQuery);
