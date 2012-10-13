<?xml version="1.0" encoding = "gb2312"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" encoding="gb2312" />
	<xsl:template match="Choices">
		<xsl:apply-templates />
		<Choices>
			<xsl:for-each select="choice">
				<choice>
					<cid>
						<xsl:value-of select="classId" />
						<xsl:value-of select="courseId" />
						<xsl:value-of select="Cno" />
					</cid>
					<sid>
						<xsl:value-of select="studentId" />
						<xsl:value-of select="studentNumber" />
						<xsl:value-of select="Sno" />
					</sid>
					<score>
						<xsl:value-of select="score" />
						<xsl:value-of select="Grd" />
					</score>
				</choice>
			</xsl:for-each>
		</Choices>
	</xsl:template>
</xsl:stylesheet>