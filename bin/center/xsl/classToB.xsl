<?xml version="1.0" encoding = "gb2312"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" encoding="gb2312" />
	<xsl:template match="Classes">
		<xsl:apply-templates>
			<Classes>
				<xsl:for-each select="class">
					<class>
						<ID>
							<xsl:value-of select="id" />
						</ID>
						<courseName>
							<xsl:value-of select="name" />
						</courseName>
						<time>
							<xsl:value-of select="time" />
						</time>
						<point>
							<xsl:value-of select="score" />
						</point>
						<teacher>
							<xsl:value-of select="teacher" />
						</teacher>
						<place>
							<xsl:value-of select="location" />
						</place>
					</class>
				</xsl:for-each>
			</Classes>
		</xsl:apply-templates>
	</xsl:template>
</xsl:stylesheet>