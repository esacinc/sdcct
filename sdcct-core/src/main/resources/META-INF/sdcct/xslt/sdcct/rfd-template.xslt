<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:sdc="http://healthIT.gov/sdc">
    <xsl:output method="html"/>
    <xsl:template match="//sdc:FormDesign">
        <html>
            <head>
                <!-- link rel="stylesheet" href="sdctemplate.css" type="text/css"/ -->
                <style>
                    Body
                    {
                    font-family: Arial;
                    font-size: 12px;
                    width: 100%;
                    margin-left: auto;
                    margin-right: auto;
                    }

                    li
                    {
                    padding: 1px;
                    color: blue;
                    list-style-type: square;
                    }

                    .BodyGroup
                    {
                    width: 900px;
                    margin-left: auto;
                    margin-right: auto;
                    text-align: left;
                    }

                    .HeaderGroup
                    {
                    width: 99%;
                    margin-top: 5px;
                    margin-bottom: 5px;
                    margin-left: auto;
                    margin-right: auto;
                    border: solid 1px #000080;
                    padding: 5px;
                    clear: both;
                    }

                    .HeaderGroupText
                    {
                    font-size: 12px;
                    font-weight: bold;
                    background-color: #FFFFFF;
                    color: #000080;
                    padding: 8px;
                    vertical-align: bottom;
                    }

                    .HeaderInfoText
                    {
                    font-size: 12px;
                    text-align: right;
                    font-style: italic;
                    }

                    .HeaderText
                    {
                    text-align: center;
                    font-size: 18px;
                    font-weight: bold;
                    background-color: #FFFFFF;
                    color: #000080;
                    padding: 8px;
                    vertical-align: bottom;
                    }

                    .TopHeader
                    {
                    text-align: center;
                    font-size: 30px;
                    font-weight: bold;
                    background-color: #000080;
                    color: #FFFFFF;
                    padding: 2px;
                    }


                    .subSection
                    {
                    text-align: center;
                    font-size: 20px;
                    font-weight: bold;
                    background-color: #000080;
                    color: #FFFFFF;
                    padding: 2px;
                    }

                    .thinBox
                    {
                    text-align: center;
                    font-size: 14px;
                    font-weight: bold;
                    font-style:italic;
                    background-color: #000080;
                    color: #FFFFFF;
                    padding: 2px;
                    }


                    .QuestionReset
                    {
                    float: right;
                    padding-left: 2px;
                    font-size: 10px;
                    text-decoration: none;
                    font-style: normal;
                    color: Blue;
                    }


                    .QuestionInSection
                    {
                    float: left;
                    padding: 2px;
                    }

                    .QuestionInListItem
                    {
                    padding: 2px;
                    padding-left: 20px;
                    }


                    .QuestionTitle
                    {
                    padding: 2px;
                    background-color: #E9E9E9;
                    font-size: 13px;
                    font-style: italic;
                    font-weight: bold;
                    clear: both;
                    }

                    .Answer
                    {
                    float: left;
                    padding-top: 2px;
                    width: 93%;
                    }

                    .DisplayProperty
                    {
                    float: left;
                    padding-top: 2px;
                    width: 22%;
                    }

                    .DisplayProperty1
                    {
                    float: left;
                    padding-top: 2px;
                    padding-left: 5px;
                    }

                    .DisplayedItem
                    {
                    display:block;
                    }

                    .AnswerTextBox
                    {
                    height: 11px;
                    font-size: 10px;
                    padding-left: 2px;
                    margin-left: 4px;
                    width:70%;
                    }

                    .NoteText
                    {
                    padding: 2px;
                    font-weight: bold;
                    color: Blue;
                    clear: both;
                    }

                    .TextBox
                    {
                    padding: 2px;
                    width: 90%;
                    height: 15px;
                    }

                    .SubmitButton
                    {
                    padding: 2px;
                    text-align: center;
                    }

                    .MessageDataResult
                    {
                    padding-bottom: 20px;
                    padding-top: 20px;
                    clear: both;
                    padding-left: 10px;
                    padding-right: 10px;
                    }

                    .MessageDataQuestion
                    {
                    padding-left: 20px;
                    color: blue;
                    font-style: italic;
                    }

                    .MessageDataAnswer
                    {
                    padding-left: 20px;
                    color: green;
                    font-style: normal;
                    font-weight: bold;
                    }

                    .MessageDataChecklist
                    {
                    font-weight: bold;
                    color: red;
                    }


                    .header_text
                    {
                    border-bottom:1px solid black;padding-bottom:0px;text-align:left;
                    }

                    .title
                    {
                    text-align: center;
                    font-size: 18px;
                    font-weight: bold;
                    font-style:italic;
                    background-color: #FFFFFF;
                    color: #000080;
                    padding: 8px;
                    vertical-align: bottom;

                    }

                    #S1
                    {
                    font-size: 18px;
                    font-weight: bold;
                    background-color: #FFFFFF;
                    color: #000080;
                    padding: 8px;
                    vertical-align: bottom;
                    }
                    .right.noBreak{float: right}
                    .right{float: right}
                    .left
                    {

                    text-align: left;

                    }
                </style>
            </head>
            <body align="left">
                <div class="BodyGroup">
                    <div id="MessageData" style="display:none;">
                        <table class="HeaderGroup" align="center">
                            <tr>
                                <td>
                                    <div class="TopHeader">
                                        Structured Report Data
                                    </div>
                                    <div id="MessageDataResult" class="MessageDataResult"/>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div id="FormData">
                        <form id="checklist" name="checklist" method="post">
                            <xsl:apply-templates/>
                        </form>
                    </div>
                </div>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="sdc:Header">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="sdc:Body">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="sdc:ChildItems" name="ChildItemsType">
        <xsl:variable name="styleClass" select="@styleClass"/>
        <div class="{$styleClass}">
            <xsl:call-template name="ExtensionBaseType"/>
            <xsl:apply-templates/> <!-- Section, Question -->
        </div>
    </xsl:template>

    <xsl:template match="sdc:Question" name="QuestionItemType">
        <xsl:call-template name="QuestionItemBaseType"/>
    </xsl:template>

    <xsl:template name="QuestionItemBaseType">
        <xsl:call-template name="RepeatingType"/>
        <xsl:choose>
            <xsl:when test="sdc:ListField">
                <xsl:apply-templates select="sdc:ListField">
                    <xsl:with-param name="parentId" select="@ID"/>
                    <xsl:with-param name="title" select="@title"/>
                </xsl:apply-templates>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="sdc:ResponseField">
                    <xsl:with-param name="parentId" select="@ID"/>
                </xsl:apply-templates>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="sdc:Section">
        <xsl:call-template name="SectionItemType"/>
        <xsl:apply-templates/>
        <div style="clear:both"></div>
    </xsl:template>

    <xsl:template name="SectionItemType">
        <xsl:call-template name="SectionBasedType"/>
    </xsl:template>

    <xsl:template name="SectionBasedType">
        <xsl:call-template name="RepeatingType"/>
        <!-- Todo Attribute "ordered" -->
    </xsl:template>

    <xsl:template name="RepeatingType">
        <xsl:call-template name="DisplayedType"/>
        <!-- Todo Attribute "minCard" & "maxCard" -->
        <!-- Todo attributeGroup ref="ResponseReportingAttributes" -->
    </xsl:template>

    <xsl:template name="DisplayedType">
        <xsl:call-template name="IdentifiedExtensionType"/>
        <xsl:if test="not (@visible) or (@visible='true')">
            <xsl:if test="@title">
                <xsl:variable name="title_style" select="@styleClass"/>
                <xsl:variable name="title_id" select="@ID"/>
                <div ID="{$title_id}" class="{$title_style}">
                    <xsl:value-of select="@title"/>
                </div>
            </xsl:if>
        </xsl:if>
    </xsl:template>

    <xsl:template match="sdc:ListField">
        <xsl:param name="parentId"/>
        <xsl:param name="title"/>
        <xsl:call-template name="ListFieldType"/>
        <xsl:choose>
            <xsl:when test="@maxSelections=0">
                <xsl:apply-templates>
                    <xsl:with-param name="parentId" select="$parentId"/>
                    <xsl:with-param name="title" select="$title"/>
                    <xsl:with-param name="type" select="'checkbox'"/>
                </xsl:apply-templates>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates>
                    <xsl:with-param name="parentId" select="$parentId"/>
                    <xsl:with-param name="title" select="$title"/>
                    <xsl:with-param name="type" select="'radio'"/>
                </xsl:apply-templates>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="ListFieldType">
        <xsl:call-template name="ExtensionBaseType"/>
    </xsl:template>

    <xsl:template match="sdc:List">
        <xsl:param name="parentId"/>
        <xsl:param name="title"/>
        <xsl:param name="type"/>
        <xsl:call-template name="ListType"/>
        <xsl:apply-templates> <!-- DisplayedItem, ListItem -->
            <xsl:with-param name="parentId" select="$parentId"/>
            <xsl:with-param name="title" select="$title"/>
            <xsl:with-param name="type" select="$type"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template name="ListType">
        <xsl:call-template name="ExtensionBaseType"/>
    </xsl:template>

    <xsl:template match="sdc:ListItem">
        <xsl:param name="parentId"/>
        <xsl:param name="title"/>
        <xsl:param name="type"/>
        <div class="Answer">
            <input type="{$type}" style="float:left" name="{$parentId}">
                <xsl:if test="@selected='true'">
                    <xsl:attribute name="checked">
                    </xsl:attribute>
                    <xsl:attribute name="value">
                        <xsl:value-of select="$parentId"/>,<xsl:value-of select="$title"/>
                    </xsl:attribute>
                </xsl:if>
            </input>
            <xsl:call-template name="ListItemType"/>
        </div>
        <div style="clear:both"/>
        <xsl:apply-templates/> <!-- ListItemResponseField, Onselect, OnDeselect.... -->
    </xsl:template>

    <xsl:template name="ListItemType">
        <xsl:call-template name="ListItemBaseType"/>
    </xsl:template>

    <xsl:template name="ListItemBaseType">
        <xsl:call-template name="DisplayedType"/>
    </xsl:template>

    <xsl:template match="sdc:ListItemResponseField">
        <xsl:call-template name="ResponseFieldType"/>
        <xsl:apply-templates> <!-- Response, Extension, TextAfterResponse, ResponseUnits..... -->
            <xsl:with-param name="parentId" select="@ID"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template name="ResponseFieldType">
        <xsl:call-template name="ExtensionBaseType"/>
    </xsl:template>

    <xsl:template match="sdc:Response">
        <xsl:param name="parentId"/>
        <xsl:variable name="name" select="sdc:string/@name"/>

        <input type="text" class="TextBox" name="{$parentId}">
            <xsl:attribute name="value">
                <xsl:value-of select="$name"/>
            </xsl:attribute>
        </input>
    </xsl:template>

    <xsl:template match="sdc:TextAfterResponse">
        <xsl:value-of select="@val"/>
    </xsl:template>

    <xsl:template match="sdc:ResponseUnits">
    </xsl:template>

    <xsl:template match="sdc:ResponseField">
        <xsl:param name="parentId"/>
        <xsl:call-template name="ResponseFieldType"/>
        <xsl:apply-templates> <!-- Response, Extension, TextAfterResponse, ResponseUnits..... -->
            <xsl:with-param name="parentId" select="$parentId"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="sdc:DisplayedItem">
        <xsl:call-template name="DisplayedType"/>
    </xsl:template>

    <xsl:template match="sdc:OtherText">
        <xsl:if test="not (@type='reportText')">
            <xsl:variable name="textstyle" select="@styleClass"/>
            <div class="{$textstyle}">
                <xsl:value-of select="@name"/>:<xsl:value-of select="@val"/>
            </div>
            <div style="clear:both"/>
        </xsl:if>
    </xsl:template>

    <xsl:template name="IdentifiedExtensionType">
    </xsl:template>

    <xsl:template name="ExtensionBaseType">
        <!-- Todo -->
    </xsl:template>

</xsl:stylesheet>
