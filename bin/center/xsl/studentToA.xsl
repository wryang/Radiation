<?xml version="1.0" encoding = "gb2312"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" encoding="gb2312" />
	<xsl:template match="Students">
		<xsl:apply-templates />
		<Students>
			<xsl:for-each select="student">
				<student>
					<studentId>
						<xsl:value-of select="id" />
					</studentId>
					<name>
						<xsl:value-of select="name" />
					</name>
					<gender>
						<xsl:value-of select="sex" />
					</gender>
					<institution>
						<xsl:value-of select="major" />
					</institution>
				</student>
			</xsl:for-each>
		</Students>
	</xsl:template>
</xsl:stylesheet>