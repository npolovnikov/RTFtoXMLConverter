package com.techinfocom.utils.tablesm;

import com.techinfocom.utils.model.FormatedChar;
import com.techinfocom.utils.model.RtfCommand;
import com.techinfocom.utils.model.TextFormat;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public interface TableParser {

    void processChar(FormatedChar fc);

    void processCommand(RtfCommand rtfCommand, TextFormat textFormat);

    //void processingDocEvent(DocEvent docEvent);

}
