(function ($) {
    $.extend($.sdcct, {
        "ihe": $.extend(function () {
            return this;
        }, {
            "processIheTestcase": function (formElem) {
                $.ajax({
                    "beforeSend": function () {
                        iheTestcaseResultsEmptyWellElem.hide();
                        iheTestcaseResultsProcessingWellElem.show();
                    },
                    "cache": false,
                    "complete": function (jqXhr, status) {
                        iheTestcaseResultsProcessingWellElem.hide();
                    },
                    "contentType": "application/json",
                    "data": $.encodeJson(testcaseIheSubmission),
                    "dataType": "json",
                    "error": function (jqXhr, status, error) {
                        $.sdcct.form.addErrors(formTestcasesIhe, $.parseJSON(jqXhr["responseText"]));
                        $.sdcct.form.inputGroupAddons(formTestcasesIhe).show();
                    },
                    "success": function (resp, respHttpStatusText, req) {
                        $(formElem).sdcct.testcases.buildTestcaseResults(testcaseIheResultsAccordion, resp);
                    },
                    "type": formElem.attr("method"),
                    "url": formElem.attr("action")
                });
            }
        })
    });
    
    var formTestcasesIhe, testcasesIheSelect, testcaseIheEndpointAddr, testcaseIheFormId, testcaseIheSubmit, testcaseIheReset, testcaseIheDesc, testcaseIheDescFormGroup, testcaseIheResults, testcaseIheSubmission, testcaseIheResultsAccordion, iheTestcaseInfo, iheRoleTested, iheRoleTestedSelection, testcaseIheSelectionGroup, testcaseIheSdcctInitiatedFormGroup, formIdOptions, iheTestcaseResultsEmptyWellElem, iheTestcaseResultsProcessingWellElem, selectedIheTestcase;
    
    $(document).ready(function () {
        iheTestcaseInfo = {
            "FORM_ARCHIVER": {
                "action": IHE_TESTCASES_PROCESS_URLS[$.sdcct.roles.FORM_ARCHIVER],
                "testcases": IHE_TESTCASES[$.sdcct.roles.FORM_ARCHIVER],
                "testcaseSubmissionType": "iheFormArchiverTestcaseSubmission"
            },
            "FORM_FILLER": {
                "testcases": IHE_TESTCASES[$.sdcct.roles.FORM_FILLER]
            },
            "FORM_MANAGER": {
                "action": IHE_TESTCASES_PROCESS_URLS[$.sdcct.roles.FORM_MANAGER],
                "testcases": IHE_TESTCASES[$.sdcct.roles.FORM_MANAGER],
                "testcaseSubmissionType": "iheFormManagerTestcaseSubmission"
            },
            "FORM_RECEIVER": {
                "action": IHE_TESTCASES_PROCESS_URLS[$.sdcct.roles.FORM_RECEIVER],
                "testcases": IHE_TESTCASES[$.sdcct.roles.FORM_RECEIVER],
                "testcaseSubmissionType": "iheFormReceiverTestcaseSubmission"
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
        testcaseIheResultsAccordion = $("div#testcase-ihe-results-accordion", testcaseIheResults);
        
        testcaseIheResultsAccordion.accordion({
            "collapsible": true,
            "heightStyle": "content",
            "icons": {
                "activeHeader": "",
                "header": ""
            }
        });
        testcaseIheResultsAccordion.empty();
        
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
                "testcase": testcasesIheSelect.val(),
                "endpointAddr": testcaseIheEndpointAddr.val(),
                "formId": testcaseIheFormId.val()
            };
            
            $.sdcct.ihe.processIheTestcase(formTestcasesIhe);
        });
        
        testcasesIheSelect.change(function (event) {
            selectedIheTestcase =
                $(event.target).sdcct.testcases.selectTestcase(event, formTestcasesIhe, IHE_TESTCASES, iheRoleTestedSelection, formIdOptions, testcaseIheDesc);
            
            if (!$.isUndefined(selectedIheTestcase)) {
                if (iheRoleTestedSelection == $.sdcct.roles.FORM_FILLER) {
                    testcaseIheDesc.prepend($.fn.sdcct.testcases.buildTestcaseItem("Endpoint Address", IHE_ENDPOINT_ADDRESSES[selectedIheTestcase["sdcctRole"]]));
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
            testcaseIheResultsAccordion.empty();
            testcaseIheEndpointAddr.disable();
            testcaseIheFormId.disable();
            
            $.sdcct.form.clearErrorMessages(formTestcasesIhe);
        });
        
        $.sdcct.poll.pollIntervalId = setInterval(function () {
            $.fn.sdcct.testcases.pollIncomingIheTestcaseEvents(testcaseIheResultsAccordion, iheTestcaseResultsEmptyWellElem);
        }, $.sdcct.poll.POLL_INTERVAL);
    });
})(jQuery);
