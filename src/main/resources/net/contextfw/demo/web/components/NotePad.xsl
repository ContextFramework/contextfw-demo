<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" 
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   xmlns:i18n="http://www.contextfw.net/i18n">

<xsl:template name="NotePad.legend">
<p>
 This component demonstrates <code>CloudSession</code>-usage. Each user have their own notepad
 which content is saved to session temporarily. The notepad can be opened in to multiple windows 
 or tabs and the changes made in different pages are synced regularly.
</p>
<p>
 Multiple notes can be inserted. To make things more complex I created a locking mechanism
 to the document handling. That is, if certain document is open in one page, it cannot be opened
 in other pages. The lock is released in few minutes if nothing happens, so you cannot accidentally
 lock document forever.
</p>
<p> 
 The notepad demonstrates how the server side page scope enforces what the client is actually
 able to do.
</p>
</xsl:template>

<xsl:template match="NotePad">
 <div id="{@id}" class="NotePad ui-widget ui-widget-content ui-helper-clearfix ui-corner-all">
  <div class="ui-widget-header ui-helper-clearfix ui-corner-all">
    <i18n:NotePad />
  </div>
  <xsl:apply-templates select="noteList" />
  <xsl:apply-templates select="noteEditor" />
 </div>
</xsl:template>

<xsl:template match="NoteList">
 <div class="NoteList" id="{@id}">
  <xsl:call-template name="NoteList" />
 </div>
</xsl:template>

<xsl:template match="NoteList.update">
 <replaceInner id="{@id}">
  <xsl:call-template name="NoteList" />
 </replaceInner>
</xsl:template>

<xsl:template name="NoteList">
<ol>
 <xsl:for-each select="notes/NoteHeader">
  <li>
    <xsl:choose>
        <xsl:when test="../../@selectedId=@id">
          <xsl:attribute name="class">active ui-widget ui-widget-content ui-corner-all</xsl:attribute>
        </xsl:when>
        <xsl:when test="@isLocked='true'">
            <xsl:attribute name="class">locked ui-widget ui-widget-content ui-corner-all</xsl:attribute>
        </xsl:when>
        <xsl:otherwise>
            <xsl:attribute name="dataid"><xsl:value-of select="@id" /></xsl:attribute>
            <xsl:attribute name="class">selectableui-widget ui-widget-content ui-corner-all</xsl:attribute>
        </xsl:otherwise>
    </xsl:choose>
    <xsl:value-of select="@title" />
  </li>
 </xsl:for-each>
</ol>
</xsl:template>



<xsl:template match="NoteEditor">
 <div class="NoteEditor" id="{@id}">
  <xsl:call-template name="NoteEditor" />
 </div>
</xsl:template>

<xsl:template match="NoteEditor.update">
 <replaceInner id="{@id}">
  <xsl:call-template name="NoteEditor" />
 </replaceInner>
</xsl:template>

<xsl:template name="NoteEditor">
 <input type="text" id="{@id}_title" class="ui-widget ui-widget-content ui-corner-all" value="{header/NoteHeader/@title}" /><br/>
 <textarea class="ui-widget ui-widget-content ui-corner-all" id="{@id}_content"><xsl:value-of select="content" /></textarea><br/>
 <div class="buttons">
 <xsl:choose>
  <xsl:when test="header/NoteHeader">
    <button type="button" id="{@id}_store" class=" ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only">
     <span class="ui-button-text"><i18n:store /> &amp; close</span>
    </button>
    <button type="button" id="{@id}_close" class=" ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only">
     <span class="ui-button-text"><i18n:cancel /> &amp; close</span>
    </button>
    <button type="button" id="{@id}_remove" class=" ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only">
     <span class="ui-button-text">Remove</span>
    </button>
  </xsl:when>
  <xsl:otherwise>
    <button type="button" id="{@id}_store" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only">
     <span class="ui-button-text"><i18n:add /></span>
    </button>
  </xsl:otherwise>
 </xsl:choose>
 </div>
</xsl:template>



</xsl:stylesheet>