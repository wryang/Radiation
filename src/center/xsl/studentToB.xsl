<?xml version="1.0" encoding = "gb2312"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method = "xml" encoding = "gb2312"/>
	<xsl:template match= "Students">
		<xsl:apply-templates/>
		<Students>
			<xsl:for-each select="student">
				<student>
					<studentNumber>
						<xsl:value-of select = "id"/>
					</studentNumber>
					<name>
						<xsl:value-of select = "name"/>
					</name>
					<sex>
						<xsl:value-of select = "sex"/>
					</sex>
					<speciality>
						<xsl:value-of select = "major"/>
					</speciality>
				</student>
			</xsl:for-each>
		</Students>
	</xsl:template>
</xsl:stylesheet>