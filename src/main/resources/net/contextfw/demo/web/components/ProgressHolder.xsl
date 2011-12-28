<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" 
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   xmlns:i18n="http://www.contextfw.net/i18n">

<xsl:template name="ProgressHolder.legend">
<p>
 This another component demonstrating asyncronous executions started in the page scope. 
 In this example user is able to create jobes with name and duration (1-30 seconds).
 When a new job is started an asynchronous execution is spawned and it is run
 in background thread until the exeution finishes. During the exeucution it is possible to go back
 to the page scope on server and modify its state.
</p>
<p>
 Also the web client is able to check the progress or result of the execution at regular 
 intervals and notify user of it. Multiple jobs can run concurrently. Maximum 10 executions can
 be created in one page.
</p>
<p>
 In this demonstration, the progress is doing nothing, it simply updates the progress. But it could 
 be generating PDF or some other resource behind the scenes and when execution is finished, simply
 offer a link to it.
</p>
</xsl:template>

<xsl:template match="ProgressHolder">
 <div class="ProgressHolder ui-widget ui-widget-content ui-helper-clearfix ui-corner-all" id="{@id}">
  <xsl:call-template name="ProgressHolder" />
 </div>
</xsl:template>

<xsl:template match="ProgressHolder.update">
 <replaceInner id="{@id}">
  <xsl:call-template name="ProgressHolder" />
 </replaceInner>
</xsl:template>

<xsl:template name="ProgressHolder">
<div class="widget-header ui-widget-header ui-helper-clearfix ui-corner-all">Progress</div>
<div class="content">
 name <input type="text" id="{@id}_name" class="ui-widget ui-widget-content ui-corner-all" />
 duration (sec) <input maxsize="2" size="2" type="number" id="{@id}_duration" class="ui-widget ui-widget-content ui-corner-all" />
 <button id="{@id}_add" type="button" class="addButton ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only">
   <span class="ui-button-text">Add</span>
 </button>
<xsl:apply-templates select="indicators" />
</div>
</xsl:template>

<xsl:template match="ProgressIndicator">
 <div id="{@id}" class="ProgressIndicator">
  <xsl:call-template name="ProgressIndicator" />
 </div>
</xsl:template>

<xsl:template match="ProgressIndicator.update">
 <replaceInner id="{@id}">
  <xsl:call-template name="ProgressIndicator" />
 </replaceInner>
</xsl:template>

<xsl:template name="ProgressIndicator">
 <div class="name">
  <xsl:value-of select="@name" />
 </div>
 <div id="{@id}_progressbar" class="ui-progressbar ui-widget ui-widget-content ui-corner-all" />
</xsl:template>

</xsl:stylesheet>