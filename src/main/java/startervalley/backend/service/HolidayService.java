package startervalley.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import startervalley.backend.entity.Holiday;
import startervalley.backend.repository.HolidayRepository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class HolidayService {

    private static final DateTimeFormatter XML_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");;
    @Value("${open-api.holiday-key}")
    private String KEY;
    private final HolidayRepository holidayRepository;

    public void saveHolidays(LocalDate localDate) {
        try {
            List<Holiday> holidayList = getHolidays(localDate.getYear());
            holidayRepository.saveAll(holidayList);
        } catch (Exception e) {
            throw new RuntimeException("HolidayException");
        }
    }

    public List<Holiday> getHolidays(int year) throws IOException, SAXException, ParserConfigurationException {
        String requiredYear = String.valueOf(year);
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + KEY); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("100", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("solYear", "UTF-8") + "=" + URLEncoder.encode(requiredYear, "UTF-8")); /*연*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        String xml = sb.toString();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        Document document = documentBuilder.parse(is);

        Element root = document.getDocumentElement();

        Node header = root.getFirstChild(); // xml firstChild 갖고옴 - <header>

        Node body = header.getNextSibling(); // sibling 갖고옴 - <body>

        Node items = body.getFirstChild(); // body의 첫번째 - <items>

        NodeList itemList = items.getChildNodes(); // <items>의 <item> 리스트 갖고오기

        List<Holiday> holidayList = new ArrayList<>();
        for (int i = 0; i < itemList.getLength(); i++) {
            Node item = itemList.item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE) { // 노드의 타입이 Element일 경우(공백이 아닌 경우)
                NodeList childNodes = item.getChildNodes();
                Node holidayNameNode = childNodes.item(1);
                Node holidayDateNode = childNodes.item(3);
                String holidayName = holidayNameNode.getTextContent();
                String holidayDateString = holidayDateNode.getTextContent();
                LocalDate holidayDate = LocalDate.parse(holidayDateString, XML_FORMATTER);

                holidayList.add(new Holiday(holidayName, holidayDate));
            }
        }
        return holidayList;
    }
}
