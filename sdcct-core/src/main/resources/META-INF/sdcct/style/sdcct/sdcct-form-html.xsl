<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    version="2.0"
    xmlns:sdc="http://healthIT.gov/sdc"
    xmlns:sdcct="urn:sdcct:core"
    xmlns:sdcct-xml="urn:sdcct:xml"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <!--====================================================================================================
    = TEMPLATES: NAMED
    =====================================================================================================-->
    <xsl:template name="script">
        <xsl:param name="content"/>
        <xsl:param name="lang" select="'javascript'"/>
        <xsl:param name="type" select="'text/javascript'"/>
        <xsl:element name="script">
            <xsl:attribute name="language" select="$lang"/>
            <xsl:attribute name="type" select="$type"/>
            <xsl:value-of disable-output-escaping="yes" select="$content"/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template name="style">
        <xsl:param name="content"/>
        <xsl:param name="type" select="'text/css'"/>
        <xsl:element name="style">
            <xsl:attribute name="type" select="$type"/>
            <xsl:value-of disable-output-escaping="yes" select="$content"/>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>