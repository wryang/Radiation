<?xml version="1.0" encoding = "gb2312"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" encoding="gb2312" />
	<xsl:template match="Students">
		<xsl:apply-templates />
		<Students>
			<xsl:for-each select="student">
				<student>
					<Sno>
						<xsl:value-of select="id" />
					</Sno>
					<Snm>
						<xsl:value-of select="name" />
					</Snm>
					<Sex>
						<xsl:value-of select="sex" />
					</Sex>
					<Sde>
						<xsl:value-of select="major" />
					</Sde>
				</student>
			</xsl:for-each>
		</Students>
	</xsl:template>
</xsl:stylesheet>