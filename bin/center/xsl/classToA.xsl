<?xml version="1.0" encoding = "gb2312"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method = "xml" encoding="gb2312"/>
	<xsl:template match ="Classes">
		<xsl:apply-templates />
		<Classes>
			<xsl:for-each select="class">
				<class>
					<id>
						<xsl:value-of select="id"/>
					</id>
					<className>
						<xsl:value-of select="name"/>
					</className>
					<point>
						<xsl:value-of select="score"/>
					</point>
					<tea>
						<xsl:value-of select="teacher"/>
					</tea>
					<place>
						<xsl:value-of select="location"/>
					</place>
				</class>
			</xsl:for-each>
		</Classes>
	</xsl:template>
</xsl:stylesheet>