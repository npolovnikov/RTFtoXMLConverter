package com.techinfocom.nvis.agendartftoxml;

import com.techinfocom.nvis.agendartftoxml.report.ReportMessage;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import static org.testng.Assert.*;

/**
 * Created by volkov_kv on 13.06.2016.
 */
@Test(testName = "Тестирование компонента RTFtoXMLConverter")
public class RtfAgendaConverterTest {

    @Test(description = "Проверка соответствия результата импорта 17 документов-образцов предопределенным соответствующим XML")
    // TODO: 30.06.2016 натравить на XML
    public void testSuccessConvert() throws Exception {

        File[] files = new File(getClass().getClassLoader().getResource("right/").toURI()).listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                File[] rtfFiles = f.listFiles((dir, name) -> name.endsWith(".rtf"));
                File[] xmlFiles = f.listFiles((dir, name) -> name.endsWith(".xml"));
                if (rtfFiles.length > 0) {
                    InputStream is = new FileInputStream(rtfFiles[0]);
                    RtfAgendaConverter converter = new RtfAgendaConverter();
                    AgendaConverterResponse agendaConverterResponse = converter.convert(is);

                    assertTrue(agendaConverterResponse.getXmlBytes().length > 10);
                    String xml = new String(agendaConverterResponse.getXmlBytes());
                    String report = agendaConverterResponse.printReport("ERROR", "WARNING");
                    assertTrue(report.isEmpty(), "Возник ERROR или WARNING при импорте корректного документа");
                    assertNotNull(xml, "результат импорта - null");
                    assertTrue(xml.length() > 0, "результат импорта - пустая строка");
                }
            }
        }

//        try(DirectoryStream<Path> rightStream = Files.newDirectoryStream(rightPath)) {
//            for(Path p: rightStream){
//
//            }
//
//        }
//
//        File file = new File(getClass().getClassLoader().getResource("right/1005/10-05.rtf").getFile());
//        InputStream is = new FileInputStream(file);
//
//        RtfAgendaConverter converter = new RtfAgendaConverter();
//        AgendaConverterResponse agendaConverterResponse = converter.convert(is);
//
//        String xml = new String(agendaConverterResponse.getXmlBytes());
//
//        assertNotNull(xml);


        // TODO: 16.06.2016 добавить упоминание имени схемы в документ

    }

    @Test(description = "Проверка обработки фатальных ошибок при импорте")
    public void testFailedConvert() throws Exception {

        //case 1
        File file = new File(getClass().getClassLoader().getResource("failed/1005wo_table/10-05_wo_table.rtf").getFile());
        InputStream is = new FileInputStream(file);

        RtfAgendaConverter converter = new RtfAgendaConverter();
        AgendaConverterResponse agendaConverterResponse = converter.convert(is);

        assertTrue(agendaConverterResponse.getXmlBytes().length == 0);
        String report = agendaConverterResponse.printReport("ERROR", "WARNING");
        assertFalse(report.isEmpty());

        //case 2
        file = null;
        agendaConverterResponse = null;
        report = null;
        file = new File(getClass().getClassLoader().getResource("failed/1005wo_date/10-05_wo_date.rtf").getFile());
        is = new FileInputStream(file);

        converter = new RtfAgendaConverter();
        agendaConverterResponse = converter.convert(is);

        assertTrue(agendaConverterResponse.getXmlBytes().length > 0);
        String xml = new String(agendaConverterResponse.getXmlBytes());
        report = agendaConverterResponse.printReport("ERROR", "WARNING");
        assertFalse(report.isEmpty());


    }

    @Test(description = "Проверка функционирования валидаторов")
    public void testOfValidators() throws Exception {

        //длина номера пункта работы
        {
            File file = new File(getClass().getClassLoader().getResource("validators/number/number_wrong.rtf").getFile());
            InputStream is = new FileInputStream(file);

            RtfAgendaConverter converter = new RtfAgendaConverter();
            AgendaConverterResponse agendaConverterResponse = converter.convert(is);

            assertTrue(agendaConverterResponse.getXmlBytes().length > 0);
            assertFalse(agendaConverterResponse.hasMessage("ERROR"), "отчет об импорте содержит ERROR");
            assertTrue(agendaConverterResponse.hasMessage("WARNING"), "отчет об импорте НЕ содержит WARNING");
            List<ReportMessage> messageList = agendaConverterResponse.getConversionReport().getMessages();
            assertTrue(messageList.size() == 1, "Кол-во сообщений в отчете более одного");
            String report = agendaConverterResponse.printReport("WARNING");
            //WARNING: В пункте 700.10.2 длина строки превышает максимальную - 8; Примерное положение: 700.10.23"
            assertTrue(report.contains("длина строки превышает максимальную") &&
                    report.contains("700.10.23"), "Сообщение валидатора не содержит ожидаемых фрагментов сообщения");
        }

    }


}