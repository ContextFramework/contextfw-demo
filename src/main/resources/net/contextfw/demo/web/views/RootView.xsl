<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:i18n="http://www.contextfw.net/i18n">
	<xsl:template match="RootView">
	<html>
	<head>
		<title><i18n:PageTitle /></title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<script type="text/javascript" src="{$contextPath}/scripts/jquery.js"></script>
		<script type="text/javascript" src="{$contextPath}/scripts/contextfw.js"></script>
		<script type="text/javascript" src="{$contextPath}/scripts/json.js"></script>
		<script type="text/javascript" src="{$contextPath}/resources.js"></script>
		<script type="text/javascript" src="{$contextPath}/jqueryui/js/jquery-ui-1.8.16.custom.min.js"></script>
		<link rel="stylesheet" type="text/css" href="{$contextPath}/jqueryui/css/redmond/jquery-ui-1.8.16.custom.css"></link>
		<link rel="stylesheet" type="text/css" href="{$contextPath}/resources.css"></link>
		<link rel="stylesheet" type="text/css" href="{$contextPath}/main.css"></link>
		<script type="text/javascript"><![CDATA[
jQuery(document).ready(function() {
contextfw.init("]]><xsl:value-of select="$contextPath" /><![CDATA[", "]]><xsl:value-of select="$webApplicationHandle" /><![CDATA[");
]]><xsl:apply-templates select="//Script" mode="script" /><![CDATA[
});]]>
        </script>
	</head>
 <body id="body">
  <a target="_blank" href="https://github.com/contextfw/contextfw-demo"><img style="position: absolute; top: 0; right: 0; border: 0;" src="https://a248.e.akamai.net/assets.github.com/img/7afbc8b248c68eb468279e8c17986ad46549fb71/687474703a2f2f73332e616d617a6f6e6177732e636f6d2f6769746875622f726962626f6e732f666f726b6d655f72696768745f6461726b626c75655f3132313632312e706e67" alt="Fork me on GitHub" /></a>
  <div class="pageHeader">
    <a href="/"><i18n:PageTitle /></a>
  </div>
  <div class="pageContent">
    <xsl:apply-templates select="child" />
  </div>
</body>
</html>
	</xsl:template>
</xsl:stylesheet>