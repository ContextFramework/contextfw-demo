<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:i18n="http://www.contextfw.net/i18n">

<xsl:include href="../components/NotePad.xsl" />
<xsl:include href="../components/TwitterFeed.xsl" />
<xsl:include href="../components/ProgressHolder.xsl" />

<xsl:template match="FrontView">
 <h1>
    <i18n:WelcomeText />
 </h1>
 
<table class="demos">
 <tr>
  <td class="demo">
   <xsl:apply-templates select="twitterFeed" />
  </td>
  <td class="legend">
   <xsl:call-template name="TwitterFeed.legend" />
  </td>
 </tr>
 <tr>
  <td class="demo">
   <xsl:apply-templates select="progressHolder" />
  </td>
  <td class="legend">
   <xsl:call-template name="ProgressHolder.legend" />
  </td>
 </tr>
 <tr>
  <td class="demo">
   <xsl:apply-templates select="notePad" />
  </td>
  <td class="legend">
   <xsl:call-template name="NotePad.legend" />
  </td>
 </tr>
</table>
</xsl:template>
	
</xsl:stylesheet>