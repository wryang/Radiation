package sax;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class SaxUtil extends DefaultHandler {
	StringBuffer jsonStringBuffer;
	int frontBlankCount = 0;

	public SaxUtil() {
		jsonStringBuffer = new StringBuffer();
	}

	/*
	 * 接收字符数据的通知。 在DOM中 ch[begin:end] 相当于Text节点的节点值（nodeValue）
	 */
	@Override
	public void characters(char[] ch, int begin, int length)
			throws SAXException {
		StringBuffer buffer = new StringBuffer();
		for (int i = begin; i < begin + length; i++) {
			switch (ch[i]) {
			case '\\':
				buffer.append("\\\\");
				break;
			case '\r':
				buffer.append("\\r");
				break;
			case '\n':
				buffer.append("\\n");
				break;
			case '\t':
				buffer.append("\\t");
				break;
			case '\"':
				buffer.append("\\\"");
				break;
			default:
				buffer.append(ch[i]);
			}
		}
		System.out.println(this.toBlankString(this.frontBlankCount)
				+ ">>> characters(" + length + "): " + buffer.toString());
	}

	/*
	 * 接收文档的结尾的通知。
	 */
	@Override
	public void endDocument() throws SAXException {
		System.out.println(this.toBlankString(--this.frontBlankCount)
				+ ">>> end document");
	}

	/*
	 * 接收文档的结尾的通知。 参数意义如下： uri ：元素的命名空间 localName ：元素的本地名称（不带前缀） qName
	 * ：元素的限定名（带前缀）
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		System.out.println(this.toBlankString(--this.frontBlankCount)
				+ ">>> end element : " + qName + "(" + uri + ")");
	}

	/*
	 * 结束前缀 URI 范围的映射。
	 */
	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		System.out.println(this.toBlankString(--this.frontBlankCount)
				+ ">>> end prefix_mapping : " + prefix);
	}

	/*
	 * 接收元素内容中可忽略的空白的通知。 参数意义如下： ch : 来自 XML 文档的字符 start : 数组中的开始位置 length :
	 * 从数组中读取的字符的个数
	 */
	@Override
	public void ignorableWhitespace(char[] ch, int begin, int length)
			throws SAXException {
		StringBuffer buffer = new StringBuffer();
		for (int i = begin; i < begin + length; i++) {
			switch (ch[i]) {
			case '\\':
				buffer.append("\\\\");
				break;
			case '\r':
				buffer.append("\\r");
				break;
			case '\n':
				buffer.append("\\n");
				break;
			case '\t':
				buffer.append("\\t");
				break;
			case '\"':
				buffer.append("\\\"");
				break;
			default:
				buffer.append(ch[i]);
			}
		}
		System.out.println(this.toBlankString(this.frontBlankCount)
				+ ">>> ignorable whitespace(" + length + "): "
				+ buffer.toString());
	}

	/*
	 * 接收处理指令的通知。 参数意义如下： target : 处理指令目标 data : 处理指令数据，如果未提供，则为 null。
	 */
	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
		System.out.println(this.toBlankString(this.frontBlankCount)
				+ ">>> process instruction : (target = \"" + target
				+ "\",data = \"" + data + "\")");
	}

	/*
	 * 接收用来查找 SAX 文档事件起源的对象。 参数意义如下： locator : 可以返回任何 SAX 文档事件位置的对象
	 */
	@Override
	public void setDocumentLocator(Locator locator) {
		System.out.println(this.toBlankString(this.frontBlankCount)
				+ ">>> set document_locator : (lineNumber = "
				+ locator.getLineNumber() + ",columnNumber = "
				+ locator.getColumnNumber() + ",systemId = "
				+ locator.getSystemId() + ",publicId = "
				+ locator.getPublicId() + ")");

	}

	/*
	 * 接收跳过的实体的通知。 参数意义如下： name : 所跳过的实体的名称。如果它是参数实体，则名称将以 '%' 开头， 如果它是外部 DTD
	 * 子集，则将是字符串 "[dtd]"
	 */
	@Override
	public void skippedEntity(String name) throws SAXException {
		System.out.println(this.toBlankString(this.frontBlankCount)
				+ ">>> skipped_entity : " + name);
	}

	/*
	 * 接收文档的开始的通知。
	 */
	@Override
	public void startDocument() throws SAXException {
		System.out.println(this.toBlankString(this.frontBlankCount++)
				+ ">>> start document ");
	}

	/*
	 * 接收元素开始的通知。 参数意义如下： uri ：元素的命名空间 localName ：元素的本地名称（不带前缀） qName
	 * ：元素的限定名（带前缀） atts ：元素的属性集合
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		System.out.println(this.toBlankString(this.frontBlankCount++)
				+ ">>> start element : " + qName + "(" + uri + ")");
	}

	/*
	 * 开始前缀 URI 名称空间范围映射。 此事件的信息对于常规的命名空间处理并非必需： 当
	 * http://xml.org/sax/features/namespaces 功能为 true（默认）时， SAX XML
	 * 读取器将自动替换元素和属性名称的前缀。 参数意义如下： prefix ：前缀 uri ：命名空间
	 */
	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		System.out.println(this.toBlankString(this.frontBlankCount++)
				+ ">>> start prefix_mapping : xmlns:" + prefix + " = " + "\""
				+ uri + "\"");

	}

	private String toBlankString(int count) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < count; i++)
			buffer.append("    ");
		return buffer.toString();
	}

	public static void main(String[] args) throws SAXException, FileNotFoundException, IOException {
		SaxUtil sax = new SaxUtil();
		XMLReader reader = XMLReaderFactory.createXMLReader();
		reader.setFeature("http://xml.org/sax/features/validation",true);
		reader.setFeature("http://xml.org/sax/features/namespaces",true);
		reader.setContentHandler(sax);
		reader.setDTDHandler(sax);
		reader.setEntityResolver(sax);
		reader.setErrorHandler(sax);
		reader.parse(new InputSource(new FileReader("./books.xml")));
	}
}
