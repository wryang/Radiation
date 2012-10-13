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
	 * �����ַ����ݵ�֪ͨ�� ��DOM�� ch[begin:end] �൱��Text�ڵ�Ľڵ�ֵ��nodeValue��
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
	 * �����ĵ��Ľ�β��֪ͨ��
	 */
	@Override
	public void endDocument() throws SAXException {
		System.out.println(this.toBlankString(--this.frontBlankCount)
				+ ">>> end document");
	}

	/*
	 * �����ĵ��Ľ�β��֪ͨ�� �����������£� uri ��Ԫ�ص������ռ� localName ��Ԫ�صı������ƣ�����ǰ׺�� qName
	 * ��Ԫ�ص��޶�������ǰ׺��
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		System.out.println(this.toBlankString(--this.frontBlankCount)
				+ ">>> end element : " + qName + "(" + uri + ")");
	}

	/*
	 * ����ǰ׺ URI ��Χ��ӳ�䡣
	 */
	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		System.out.println(this.toBlankString(--this.frontBlankCount)
				+ ">>> end prefix_mapping : " + prefix);
	}

	/*
	 * ����Ԫ�������пɺ��ԵĿհ׵�֪ͨ�� �����������£� ch : ���� XML �ĵ����ַ� start : �����еĿ�ʼλ�� length :
	 * �������ж�ȡ���ַ��ĸ���
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
	 * ���մ���ָ���֪ͨ�� �����������£� target : ����ָ��Ŀ�� data : ����ָ�����ݣ����δ�ṩ����Ϊ null��
	 */
	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
		System.out.println(this.toBlankString(this.frontBlankCount)
				+ ">>> process instruction : (target = \"" + target
				+ "\",data = \"" + data + "\")");
	}

	/*
	 * ������������ SAX �ĵ��¼���Դ�Ķ��� �����������£� locator : ���Է����κ� SAX �ĵ��¼�λ�õĶ���
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
	 * ����������ʵ���֪ͨ�� �����������£� name : ��������ʵ������ơ�������ǲ���ʵ�壬�����ƽ��� '%' ��ͷ�� ��������ⲿ DTD
	 * �Ӽ��������ַ��� "[dtd]"
	 */
	@Override
	public void skippedEntity(String name) throws SAXException {
		System.out.println(this.toBlankString(this.frontBlankCount)
				+ ">>> skipped_entity : " + name);
	}

	/*
	 * �����ĵ��Ŀ�ʼ��֪ͨ��
	 */
	@Override
	public void startDocument() throws SAXException {
		System.out.println(this.toBlankString(this.frontBlankCount++)
				+ ">>> start document ");
	}

	/*
	 * ����Ԫ�ؿ�ʼ��֪ͨ�� �����������£� uri ��Ԫ�ص������ռ� localName ��Ԫ�صı������ƣ�����ǰ׺�� qName
	 * ��Ԫ�ص��޶�������ǰ׺�� atts ��Ԫ�ص����Լ���
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		System.out.println(this.toBlankString(this.frontBlankCount++)
				+ ">>> start element : " + qName + "(" + uri + ")");
	}

	/*
	 * ��ʼǰ׺ URI ���ƿռ䷶Χӳ�䡣 ���¼�����Ϣ���ڳ���������ռ䴦���Ǳ��裺 ��
	 * http://xml.org/sax/features/namespaces ����Ϊ true��Ĭ�ϣ�ʱ�� SAX XML
	 * ��ȡ�����Զ��滻Ԫ�غ��������Ƶ�ǰ׺�� �����������£� prefix ��ǰ׺ uri �������ռ�
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
