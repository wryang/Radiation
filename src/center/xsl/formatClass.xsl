<?xml version="1.0" encoding = "gb2312"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" encoding="gb2312" />
	<xsl:template match="Classes">
		<xsl:apply-templates>
			<Classes>
				<xsl:for-each select="class">
					<class>
						<id>
							<xsl:value-of select="id" />
							<xsl:value-of select="ID" />
							<xsl:value-of select="Cno" />
						</id>
						<name>
							<xsl:value-of select="className" />
							<xsl:value-of select="courseName" />
							<xsl:value-of select="Cnm" />
						</name>
						<time>
							<xsl:value-of select="time" />
							<xsl:value-of select="Ctm" />
						</time>
						<score>
							<xsl:value-of select="point" />
							<xsl:value-of select="Cpt" />
						</score>
						<teacher>
							<xsl:value-of select="tea" />
							<xsl:value-of select="teacher" />
							<xsl:value-of select="Tec" />
						</teacher>
						<location>
							<xsl:value-of select="place" />
							<xsl:value-of select="Pla" />
						</location>
					</class>
				</xsl:for-each>
			</Classes>
		</xsl:apply-templates>
	</xsl:template>
</xsl:stylesheet>