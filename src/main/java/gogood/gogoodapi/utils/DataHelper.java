package gogood.gogoodapi.utils;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;

public class DataHelper {
    private static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static Date converterData(String data) {
        try{
            return format.parse(data);
        }catch (ParseException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(500), "Houve um erro no servidor");
        }

    }

}
