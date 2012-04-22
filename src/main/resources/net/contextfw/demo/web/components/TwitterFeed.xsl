<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" 
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   xmlns:i18n="http://www.contextfw.net/i18n" >

<xsl:template name="TwitterFeed.legend">
<p>
 This component is a simple twitter feed using asynchronous background updates. The idea is simple. 
 Accessing twitter service takes some time, sometimes seconds. Also tweet updates may even fail at some
 points. This may bind the request thread for unnecessary many seconds.
</p>
<p>
 So, what the component does, it creates a backround process to fetch new tweets and when tweets have
 been received, process updates the page scope and adds the tweets in it. The received tweets are
 polled regurarly by web client and new tweets are shown to user.
</p>
<p>
 Each update spawns a new tweet update and the cycle continues.
</p>
</xsl:template>

<xsl:template match="TwitterFeed">
 
 <div id="{@id}" class="TwitterFeed ui-widget ui-widget-content ui-helper-clearfix ui-corner-all">
  <div class="widget-header ui-widget-header ui-helper-clearfix ui-corner-all">Twitter Feed</div>
  <form id="{@id}_form">  
    <input type="text" id="{@id}_search" class="searchInput ui-widget ui-widget-content ui-corner-all" />
    <button id="{@id}_searchButton" type="submit" class="searchButton ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only">
      <span class="ui-button-text">Search</span>
    </button>
  </form>
  <div id="{@id}_searching" class="searching">
   <i><i18n:TwitterFeedLoading /></i>
  </div>
  <div id="{@id}_tweets" class="feed">
    <xsl:call-template name="TwitterFeed" />
  </div>
 </div>
</xsl:template>

<xsl:template match="TwitterFeed.update">
<xsl:if test="tweets/Tweet">
<prepend id="{@id}_tweets">
 <div style="display: none">
  <xsl:apply-templates select="tweets/Tweet" mode="TwitterFeed" />
 </div>
 </prepend>
</xsl:if>
</xsl:template>

<xsl:template match="TwitterFeed.tweetsUpdate">
 <prepend id="{@id}_tweets">
 <div style="display: none">
  <xsl:apply-templates select="tweets/Tweet" mode="TwitterFeed" />
 </div>
 </prepend>
</xsl:template>

<xsl:template name="TwitterFeed">
 <xsl:choose>
  <xsl:when test="@tweetsVisible='true'">
   <xsl:apply-templates select="tweets/Tweet" mode="TwitterFeed" />
  </xsl:when>
 </xsl:choose>
</xsl:template>

<xsl:template match="Tweet" mode="TwitterFeed">
 <div class="tweet ui-corner-all" style="background-image: url({@profileImageUrl})">
  <h4><xsl:value-of select="@user" /></h4>
  <p>
     <xsl:value-of select="@text" />
  </p>
 </div>
</xsl:template>

</xsl:stylesheet>