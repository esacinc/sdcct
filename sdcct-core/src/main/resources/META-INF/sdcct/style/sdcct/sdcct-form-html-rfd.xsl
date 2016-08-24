<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    exclude-result-prefixes="#all"
    version="2.0"
    xmlns:sdc="http://healthIT.gov/sdc"
    xmlns:sdcct="urn:sdcct:core"
    xmlns:sdcct-xml="urn:sdcct:xml"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <!--====================================================================================================
    = IMPORTS
    =====================================================================================================-->
    <xsl:import href="sdcct-form-html.xsl"/>
    
    <!--====================================================================================================
    = TEMPLATES: NAMED
    =====================================================================================================-->
    <xsl:template name="QuestionItemBaseType">
        <xsl:param name="base_classes" select="''"/>
        <xsl:call-template name="RepeatingType">
            <xsl:with-param name="base_classes" select="$base_classes"/>
        </xsl:call-template>
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
    
    <xsl:template name="SectionItemType">
        <xsl:param name="base_classes" select="''"/>
        <xsl:call-template name="SectionBasedType">
            <xsl:with-param name="base_classes" select="$base_classes"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="SectionBasedType">
        <xsl:param name="base_classes" select="''"/>
        <xsl:call-template name="RepeatingType">
            <xsl:with-param name="base_classes" select="$base_classes"/>
        </xsl:call-template>
        <!-- TODO: Attribute "ordered" -->
    </xsl:template>

    <xsl:template name="RepeatingType">
        <xsl:param name="base_classes" select="''"/>
        <xsl:call-template name="DisplayedType">
            <xsl:with-param name="base_classes" select="$base_classes"/>
        </xsl:call-template>
        <!-- TODO: Attribute "minCard" & "maxCard" -->
        <!-- TODO: attributeGroup ref="ResponseReportingAttributes" -->
    </xsl:template>

    <xsl:template name="DisplayedType">
        <xsl:param name="base_classes" select="''"/>
        <xsl:call-template name="IdentifiedExtensionType"/>
        <xsl:if test="(count(@visible) eq 0) or xsd:boolean(@visible)">
            <xsl:if test="@title">
                <xsl:variable name="classes" select="@styleClass"/>
                <xsl:variable name="id" select="@ID"/>
                <div id="{$id}" class="{string-join(($base_classes, $classes), ' ')}">
                    <xsl:value-of select="@title"/>
                </div>
            </xsl:if>
        </xsl:if>
    </xsl:template>
    
    <xsl:template name="ListFieldType">
        <xsl:call-template name="ExtensionBaseType"/>
    </xsl:template>
    
    <xsl:template name="ListType">
        <xsl:call-template name="ExtensionBaseType"/>
    </xsl:template>
    
    <xsl:template name="ListItemType">
        <xsl:call-template name="ListItemBaseType"/>
    </xsl:template>

    <xsl:template name="ListItemBaseType">
        <xsl:call-template name="DisplayedType"/>
    </xsl:template>

    <xsl:template name="ResponseFieldType">
        <xsl:call-template name="ExtensionBaseType"/>
    </xsl:template>
    
    <xsl:template name="IdentifiedExtensionType">
    </xsl:template>

    <xsl:template name="ExtensionBaseType">
        <!-- TODO: implement -->
    </xsl:template>
    
    <!--====================================================================================================
    = TEMPLATES: MATCHED
    =====================================================================================================-->
    <xsl:template match="/sdc:FormDesign">
        <xsl:text disable-output-escaping="yes">&lt;!DOCTYPE html&gt;</xsl:text>
        <html lang="en">
            <head>
                <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
                <meta name="viewport" content="width=device-width, initial-scale=1"/>
                <xsl:call-template name="style">
                    <xsl:with-param name="content"
                        select="sdcct-xml:resource-content('classpath*:${sdcct.static.webjars.bootstrap.dir.path}/css/bootstrap.min.css')"/>
                </xsl:call-template>
                <xsl:call-template name="style">
                    <xsl:with-param name="content"
                        select="sdcct-xml:resource-content('classpath*:${sdcct.static.webjars.font-awesome.dir.path}/css/font-awesome.min.css')"/>
                </xsl:call-template>
                <xsl:call-template name="style">
                    <xsl:with-param name="content" select="sdcct-xml:resource-content('classpath*:${sdcct.static.styles.dir.path}/sdcct.css')"/>
                </xsl:call-template>
                <xsl:call-template name="style">
                    <xsl:with-param name="content" select="sdcct-xml:resource-content('classpath*:${sdcct.static.styles.dir.path}/sdcct-form.css')"/>
                </xsl:call-template>
                <xsl:call-template name="style">
                    <xsl:with-param name="content" select="sdcct-xml:resource-content('classpath*:${sdcct.static.styles.dir.path}/sdcct-form-rfd.css')"/>
                </xsl:call-template>
                <xsl:call-template name="script">
                    <xsl:with-param name="content"
                        select="sdcct-xml:resource-content('classpath*:${sdcct.static.webjars.jquery.dir.path}/dist/jquery.min.js')"/>
                </xsl:call-template>
                <xsl:call-template name="script">
                    <xsl:with-param name="content"
                        select="sdcct-xml:resource-content('classpath*:${sdcct.static.webjars.bootstrap.dir.path}/js/bootstrap.min.js')"/>
                </xsl:call-template>
                <xsl:call-template name="script">
                    <xsl:with-param name="content" select="sdcct-xml:resource-content('classpath*:${sdcct.static.scripts.dir.path}/sdcct.js')"/>
                </xsl:call-template>
                <xsl:call-template name="script">
                    <xsl:with-param name="content" select="sdcct-xml:resource-content('classpath*:${sdcct.static.scripts.dir.path}/sdcct-form.js')"/>
                </xsl:call-template>
                <xsl:call-template name="script">
                    <xsl:with-param name="content"
                        select="sdcct-xml:resource-content('classpath*:${sdcct.static.scripts.dir.path}/sdcct-form-rfd.js')"/>
                </xsl:call-template>
            </head>
            <body>
                <div class="container">
                    <h1><xsl:value-of select="sdcct-xml:default-if-blank(sdc:Header/@title, sdc:Body/@title)"/></h1>
                    <div>
                        <h2>Header Data</h2>
                        <table class="table">
                            <thead>
                                <th>Name</th>
                                <th>Type</th>
                                <th>Value</th>
                            </thead>
                            <tbody>
                                <xsl:apply-templates select="sdc:Header"/>
                            </tbody>
                        </table>
                    </div>
                    <form class="form-inline" id="Form" method="POST" name="Form">
                        <xsl:apply-templates select="sdc:Body"/>
                    </form>
                </div>
            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="sdc:Header">
        <xsl:for-each select="sdc:OtherText">
            <tr>
                <td><xsl:value-of select="current()/@name"/></td>
                <td><xsl:value-of select="current()/@type"/></td>
                <td><xsl:value-of select="current()/@val"/></td>
            </tr>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="sdc:Body">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="sdc:ChildItems" name="ChildItemsType">
        <xsl:variable name="styleClass" select="@styleClass"/>
        <div class="{$styleClass}">
            <xsl:call-template name="ExtensionBaseType"/>
            <xsl:apply-templates/><!-- Section, Question -->
        </div>
    </xsl:template>

    <xsl:template match="sdc:Question" name="QuestionItemType">
        <xsl:call-template name="QuestionItemBaseType">
            <xsl:with-param name="base_classes">
                <xsl:choose>
                    <xsl:when test="local-name(ancestor::sdc:*[2]) eq 'Section'">
                        <xsl:value-of select="'QuestionTitle QuestionInSection'"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="'QuestionTitle'"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="sdc:Section">
        <xsl:call-template name="SectionItemType">
            <xsl:with-param name="base_classes">
                <xsl:choose>
                    <xsl:when test="local-name(ancestor::sdc:*[2]) eq 'Body'">
                        <xsl:value-of select="'form-group TopHeader'"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="'form-group'"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:with-param>
        </xsl:call-template>
        <xsl:apply-templates/>
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

    <xsl:template match="sdc:List">
        <xsl:param name="parentId"/>
        <xsl:param name="title"/>
        <xsl:param name="type"/>
        <xsl:call-template name="ListType"/>
        <xsl:apply-templates><!-- DisplayedItem, ListItem -->
            <xsl:with-param name="parentId" select="$parentId"/>
            <xsl:with-param name="title" select="$title"/>
            <xsl:with-param name="type" select="$type"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="sdc:ListItem">
        <xsl:param name="parentId"/>
        <xsl:param name="title"/>
        <xsl:param name="type"/>
        <xsl:variable name="id" select="string-join(($parentId, @ID), ',')"/>
        <div class="Answer">
            <input class="form-control" id="{$id}" name="{$id}" type="{$type}" value="{$id}">
                <xsl:if test="xsd:boolean(@selected)">
                    <xsl:attribute name="checked">
                        <xsl:value-of select="'checked'"/>
                    </xsl:attribute>
                </xsl:if>
            </input>
            <label class="control-label" for="{$id}" name="{$id}"><xsl:value-of select="sdcct-xml:default-if-blank(@title, $title)"/></label>
        </div>
        <xsl:apply-templates/><!-- ListItemResponseField, Onselect, OnDeselect.... -->
    </xsl:template>

    <xsl:template match="sdc:ListItemResponseField">
        <xsl:call-template name="ResponseFieldType"/>
        <xsl:apply-templates><!-- Response, Extension, TextAfterResponse, ResponseUnits..... -->
            <xsl:with-param name="parentId" select="@ID"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="sdc:Response">
        <xsl:param name="parentId"/>
        <xsl:variable name="name" select="sdc:string/@name"/>
        <input type="text" class="form-control TextBox" name="{$parentId}">
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
        <xsl:apply-templates><!-- Response, Extension, TextAfterResponse, ResponseUnits..... -->
            <xsl:with-param name="parentId" select="$parentId"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="sdc:DisplayedItem">
        <xsl:call-template name="DisplayedType"/>
    </xsl:template>

    <xsl:template match="sdc:OtherText">
        <xsl:if test="not(@type='reportText')">
            <xsl:variable name="classes" select="@styleClass"/>
            <div class="{$classes}">
                <xsl:value-of select="@name"/>:<xsl:value-of select="@val"/>
            </div>
        </xsl:if>
    </xsl:template>
</xsl:stylesheet>