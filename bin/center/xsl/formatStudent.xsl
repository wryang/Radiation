<?xml version="1.0" encoding = "gb2312"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method = "xml" encoding = "gb2312"/>
	<xsl:template match= "Students">
		<xsl:apply-templates/>
		<Students>
			<xsl:for-each select="student">
				<student>
					<id>
						<xsl:value-of select = "studentNumber"/>
						<xsl:value-of select = "studentId"/>
						<xsl:value-of select = "Sno"/>
					</id>
					<name>
						<xsl:value-of select = "name"/>
						<xsl:value-of select = "Snm"/>
					</name>
					<sex>
						<xsl:value-of select = "sex"/>
						<xsl:value-of select = "gender"/>
						<xsl:value-of select = "Sex"/>
					</sex>
					<major>
						<xsl:value-of select = "speciality"/>
						<xsl:value-of select = "institution"/>
						<xsl:value-of select = "Sde"/>
					</major>
				</student>
			</xsl:for-each>
		</Students>
	</xsl:template>
</xsl:stylesheet>