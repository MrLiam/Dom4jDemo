package com.ldl.test.dom4j.xml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;

public class Demo01 {

	public static void main(String[] args) {

	}

	@Test
	public void m01() {
		String xml = "<assetcontent><assettype>1001</assettype><assetdesc></assetdesc><assetoperation>0</assetoperation><content><special_id >专题ID</special_id ><asset_id> //媒资id </asset_id>"
				+ "<sort>//排序字段</sort><reserve1> //保留字段,类型:varchar(100) </reserve1><reserve2> //保留字段,类型:varchar(100) </reserve2></content><info><pushcode>出库编码</pushcode><queuename>队列名称</queuename>"
				+ "<contentprovider><![CDATA[测试]]></contentprovider></info></assetcontent>";

		/*
		 * //运行时间 39 long begin = System.currentTimeMillis(); HashMap<String,
		 * String> map = parseXml2MapByList(string2XMLDoc(xml)); if(null != map
		 * && !map.isEmpty()){ Iterator<Map.Entry<String, String>> it =
		 * map.entrySet().iterator();
		 * 
		 * System.out.println(
		 * "==========================map data BEGIN=========================="
		 * ); while (it.hasNext()) { Map.Entry<String, String> entry =
		 * it.next(); System.out.println(entry.getKey() + "=" +
		 * entry.getValue()); } System.out.println(
		 * "==========================map data END==========================");
		 * } System.out.println("耗时：   "
		 * +String.valueOf(System.currentTimeMillis() -begin));
		 */

		// 运行时间 40ms
		/*
		 * long begin = System.currentTimeMillis(); HashMap<String, String> map
		 * = new HashMap<>(); map =
		 * parserNodeByList(string2XMLDoc(xml).getRootElement(), map); if (null
		 * != map && !map.isEmpty()) { Iterator<Map.Entry<String, String>> it =
		 * map.entrySet().iterator(); System.out.println(
		 * "==========================map data BEGIN=========================="
		 * ); while (it.hasNext()) { Map.Entry<String, String> entry =
		 * it.next(); System.out.println(entry.getKey() + "=" +
		 * entry.getValue()); } System.out.println(
		 * "==========================map data END==========================");
		 * } System.out.println("耗时：   " +
		 * String.valueOf(System.currentTimeMillis() - begin));
		 */

		// 运行时间 ms
		long begin = System.currentTimeMillis();
		HashMap<String, String> map = new HashMap<>();
		map = parseXml2MapByIterator(string2XMLDoc(xml).getRootElement(), map);
		if (null != map && !map.isEmpty()) {
			Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
			System.out.println("==========================map data iterator BEGIN==========================");
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				System.out.println(entry.getKey() + "=" + entry.getValue());
			}
			System.out.println("==========================map data iterator END==========================");
		}
		System.out.println("耗时：   " + String.valueOf(System.currentTimeMillis() - begin));

	}

	public Document string2XMLDoc(String xmlSrc) {
		if (null != xmlSrc && !"".equals(xmlSrc)) {
			try {
				Document document = DocumentHelper.parseText(xmlSrc);
				return document;
			} catch (DocumentException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

	public HashMap<String, String> parseXml2MapByList(Document document) {
		if (null != document) {
			HashMap<String, String> map = new HashMap<>();
			Element root = document.getRootElement();
			map.put(root.getName(), root.getText());
			List<Element> childElements = root.elements();
			for (Element child : childElements) {
				// 未知属性名情况下
				List<Attribute> attributeList = child.attributes();
				for (Attribute attr : attributeList) {
					map.put(attr.getName(), attr.getValue());
				}

				// 已知属性名情况下
				// System.out.println("attrName: " +
				// child.attributeValue("attrName"));

				// 未知子元素名情况下
				map.put(child.getName(), child.getText());
				// 如果有多层，需要递归处理
				List<Element> elementList = child.elements();
				for (Element ele : elementList) {
					System.out.println(ele.getName() + ": " + ele.getText());
					map.put(ele.getName(), ele.getText());
				}
				// 已知子元素名的情况下
				/*
				 * System.out.println("eleName" + child.elementText("eleName"));
				 */
			}
			return map;
		} else {
			return null;
		}

	}

	public HashMap<String, String> parserNodeByList(Element ele, HashMap<String, String> map) {
		// 如果当前节点不为空，则输出
		if (null != ele) {
			if (null != ele.getName() && !"".equals(ele.getName())) {
				map.put(ele.getName(), ele.getTextTrim());
			}
			List<Attribute> attrList = ele.attributes();
			for (Attribute attr : attrList) {
				// 不为空存入map中
				if (null != attr.getName() && !"".equals(attr.getName())) {
					map.put(attr.getName(), attr.getValue());
				}
			}

			List<Element> eleList = ele.elements();
			// 递归遍历父节点下的所有子节点
			for (Element e : eleList) {
				parserNodeByList(e, map);
			}
		}
		return map;

	}

	public HashMap<String, String> parseXml2MapByIterator(Element ele, HashMap<String, String> map) {
		// 如果当前节点不为空，则输出
		if (null != ele) {
			if (null != ele.getName() && !"".equals(ele.getName())) {
				map.put(ele.getName(), ele.getTextTrim());
			}
			// 未知属性名称情况下
			Iterator attrIt = ele.attributeIterator();
			while (attrIt.hasNext()) {
				Attribute a = (Attribute) attrIt.next();
				if (null != a.getName() && !"".equals(a.getName())) {
					map.put(a.getName(), a.getValue());
				}
			}

			// 已知属性名称情况下
			/*
			 * System.out.println("attrName: " +
			 * element.attributeValue("attrName"));
			 */

			// 未知元素名情况下
			Iterator eleIt = ele.elementIterator();
			while (eleIt.hasNext()) {
				Element e = (Element) eleIt.next();
				parseXml2MapByIterator(e, map);
			}

			// 已知元素名情况下
			/*
			 * System.out.println("EleName: " +element.elementText("EleName"));
			 */
		}
		return map;
	}
}
