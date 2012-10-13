<?xml version="1.0" encoding = "gb2312"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" encoding="gb2312" />
	<xsl:template match="Choices">
		<xsl:apply-templates />
		<Choices>
			<xsl:for-each select="choice">
				<choice>
					<classId>
						<xsl:value-of select="cid" />
					</classId>
					<studentId>
						<xsl:value-of select="sid" />
					</studentId>
					<score>
						<xsl:value-of select="score" />
					</score>
				</choice>
			</xsl:for-each>
		</Choices>
	</xsl:template>
</xsl:stylesheet>