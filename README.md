Radiation
========

- 场景：
--------

- 现有院系A、院系B和院系C的教学管理系统基于不同的数据库系统。其中院系A的数据库系统使用***，院系B的数据库系统使用***，院系C的数据库系统使用***实现；其中每个数据库系统都包含了学生、课程以及选课的信息。
- 院系A、B、C的学生互不覆盖，但课程信息有所重叠。
- 院系A、B和C的学生、课程信息的数据库结构有所差异（包括表结构、字段名称、字段数据类型和数据意义等）。

对单个院系教务系统，用户可以包括学生，系统管理员，用户需求如下：

![Usercase](http://dl.dropbox.com/u/91146904/weibao/Radiation_Usercase.png)

- 对学生用户：

登陆验证
个人信息查询更新、本院系课程查看等基本操作

课程查看，选择和更新
- 对管理员用户：

登陆验证
查看本院系学生、课程、课程被选情况等统计信息
对课程，学生，选课等数据的CRUD操作

集成教务系统的目标是满足课程共享的需求，使得各院系的学生能够跨院系选课。
具体操作就是消除各系统数据异构性的影响，建立中间集成服务器，对不同院系数据库的异构数据进行数据集成，分析和处理，而同时对原有系统不需要对本地数据或代码做很大改变。

![Architecture](http://dl.dropbox.com/u/91146904/weibao/Radiation_Architecture.png)

- 集成服务器
-----------

编写集成端，集成端可以划分成如下几个部分（例）：
UI，业务逻辑处理（如处理跨院系选课，课程共享事件），XML验证转换
建议能够在集成端查看各学校统计信息，显示集成服务器正在处理的命令消息，或者正在进行的转换工作等。

业务逻辑处理模块可能的工作流程如下：

1. 集成端Server接受各个教务系统Server发出的请求或其他消息命令

2. 集成端Server和院系教务系统Server通信，请求与接收 XML文件

3. 进行数据处理，通过XSLT对接收过来的源特定格式的XML进行转化，生成标准格式的XML文件。

4. 对标准格式的XML文件进行针对目标院系特定格式的转换，并将之回发

- 教务系统（A、B、C服务器）
-------------------------

可以划分成如下两块（例）：
* LocalServer端处理与本院系客户端的通信，如登录验证，课程信息传送等消息，通信方式不限，可以自定义文本命令消息使用socket传输。
* XMLServer端处理数据过程如下：通过读取本地数据库数据，将数据转化为XML文件。并将在与集成服务器的通信中使用XML文件。在收到XML文件时，用相应的schema进行验证。

- 客户端（A、B、C客户端）
------------------------

各个院系的客户端应当提供具有登陆，课程查看，课程选择等其他个人管理功能。且只与相应院系的服务器端通信。

- 数据库
--------

数据库结构设计，可以由学生，账户，课程，选课四个关系表组成。
账户表中包括管理员账户。其中课程数据表中可以添加其他字段用以表示该课程是否共享。

![Database](http://dl.dropbox.com/u/91146904/weibao/Radiation_Database.png)

- XML
------
* 学生.xml

```````` XML
<?xml version="1.0" encoding="GB2312"?>
<Students>
  <student>
    <Sno>171251201</Sno>
    <Snm>洪城</Snm>
    <Sex>男</Sex>
    <SDe>软件学院</SDe>
  </student>
</Students>
```````
* 学生.xsd

``````XML
<?xml version="1.0" encoding="GB2312"?>
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema">
<xs:element name="Students">
        	<xs:complexType>
   		 	<xs:sequence>
      		<xs:element name="student" maxOccurs="unbounded">
        		<xs:complexType>
          	<xs:sequence>
			<xs:element name="Sno" type="xs:string"/>
			<xs:element name="Snm" type="xs:string"/>
			<xs:element name="Sex" type="xs:string"/>
			<xs:element name="SDe" type="xs:string"/>
        		<xs:element name="Pwd" type="xs:string" minOccurs="0"/>           
          	</xs:sequence>
        		</xs:complexType>
      		</xs:element>
    			</xs:sequence>
  			</xs:complexType>
</xs:element>
</xs:schema>
~~~~~~~~

* 特定格式到统一格式.xsl

``````````` XML
<?xml   version= "1.0"   encoding= "gb2312"?> 
<xsl:stylesheet   version= "1.0"   xmlns:xsl= "http://www.w3.org/1999/XSL/Transform"> 
    <xsl:output   method= "xml"   encoding= "gb2312"/> 
        <xsl:template match="Students">
            <xsl:apply-templates/> 
            <Students>
               <xsl:for-each select="student">
                   <student>
                    <xsl:choose>
                        <xsl:when test="A条件">
                            <id>
                                <xsl:value-of select="*****"/>
                            </id>
                        </xsl:when>
                        <xsl:when test=" B条件'">
                            <id>
                                <xsl:value-of select="*****"/>
                            </id>
                        </xsl:when>
                        <xsl:when test="C条件">
                            <id>
                                <xsl:value-of select="*****"/>
                            </id>
                        </xsl:when>
                    </xsl:choose>
                    <name>
                        <xsl:value-of select="姓名"/>
                        <xsl:value-of select="Snm"/>
                    </name>
                    <sex>
                        <xsl:value-of select="性别"/>
                        <xsl:value-of select="Sex"/>
                    </sex>
                    <xsl:choose>
                        <major>
                            <xsl:value-of select="专业"/>
                        </major>
                    </xsl:choose>
                   </student>
                </xsl:for-each>
            </Students>
        </xsl:template> 
</xsl:stylesheet>
`````````

* 统一格式到特定格式.xsl

```````` XML
<?xml   version= "1.0"   encoding= "gb2312"?> 
<xsl:stylesheet   version= "1.0"   xmlns:xsl= "http://www.w3.org/1999/XSL/Transform"> 
<xsl:output   method= "xml"   encoding= "gb2312"/> 
<xsl:template match="Classes">
<xsl:apply-templates/> 
<Classes>
    <xsl:for-each select="class">
       <class>
            <课程编号>
                <xsl:value-of select="id"/>
            </课程编号>
            <课程名称>
                <xsl:value-of select="name"/>
            </课程名称> 
            <学分>
                <xsl:value-of select="score"/>
            </学分>
            <授课老师>
               <xsl:value-of select="teacher"/>
            </授课老师>
            <授课地点>
                <xsl:value-of select="location"/>
            </授课地点>
        </class>
    </xsl:for-each>
</Classes>
</xsl:template> 
</xsl:stylesheet>
`````````

- XML验证
---------

利用本地的XSD文件对发送过来的XML文件进行验证，sax部分使用xercesImpl.jar包。

````````` Java
SAXReader saxReader = new SAXReader();    	
saxReader.setValidation(true);   
saxReader.setFeature("http://xml.org/sax/features/validation", true);   
saxReader.setFeature("http://apache.org/xml/features/validation/schema", true);   
saxReader.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation",  "*.xsd");   

XMLErrorHandler errorHandler = new XMLErrorHandler();   
saxReader.setErrorHandler(errorHandler);   
               
Document document = saxReader.read(new FileReader(new File("D:\\"+ fileName + ".xml")));
         		
XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint());   
if (errorHandler.getErrors().hasContent()) {   
    writer.write(errorHandler.getErrors());   
} else {   
    System.out.println("validate success.");
}
````````````

- XML转换
--------

利用本地的XSL文件对发送过来的XML文件转换成目标格式，部分示例代码如下

```````Java
SAXReader saxReader = new SAXReader();    	
Document document = saxReader.read(new FileReader(new File("*.xml")));
TransformerFactory factory = TransformerFactory.newInstance();
Transformer transformer = factory.newTransformer(new StreamSource(styleSheet));
DocumentSource source = new DocumentSource(document);
DocumentResult result = new DocumentResult();
========XML转换开始========
transformer.transform(source, result);
Document transformedDoc = result.getDocument();
Writer w = new FileWriter("**.xml");
OutputFormat opf = OutputFormat.createPrettyPrint();
opf.setEncoding("GB2312");
XMLWriter xw = new XMLWriter(w, opf);
xw.write(transformedDoc);
// 关闭文件流
xw.close();
w.close();
`````````




