package startervalley.backend.util;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.stereotype.Component;
import startervalley.backend.dto.response.AttendanceDto;
import startervalley.backend.entity.AttendanceStatus;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class GoogleSpreadSheet {
    private static final String APPLICATION_NAME = "google spread sheet";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = GoogleSpreadSheet.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public List<AttendanceDto> makeAttendanceList(String name) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1glNhKddXOoaCyZOZMHrxdgRCWjho4cMbXSj0QZPM9Lw";
        final String range = "sheet1!C2:BY19";
        Sheets service =
                new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(APPLICATION_NAME)
                        .build();

        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();

        List<AttendanceDto> attendanceList = new ArrayList<>();
        List<List<Object>> values = response.getValues();
        List<Object> dateList = values.get(0);
        for (int i = 1; i < values.size(); i++) {
            List<Object> row = values.get(i);
            if (!name.equals(row.get(0))) {
                continue;
            }
            for (int j = 2; j < row.size(); j++) {
                String date = (String) dateList.get(j);
                String[] split = date.split("/");
                int year = 2022;
                int month = Integer.parseInt(split[0]);
                int day = Integer.parseInt(split[1]);
                LocalDate localDate = LocalDate.of(year, month, day);

                String status = (String) row.get(j);
                AttendanceStatus attendanceStatus = null;
                if (status.startsWith("출석")) {
                    attendanceStatus = AttendanceStatus.PRESENT;
                } else if (status.startsWith("지각")) {
                    attendanceStatus = AttendanceStatus.LATE;
                } else if (status.startsWith("결석")) {
                    attendanceStatus = AttendanceStatus.ABSENT;
                }
                AttendanceDto attendanceDto = new AttendanceDto(localDate, attendanceStatus);
                attendanceList.add(attendanceDto);
            }
        }
        return attendanceList;
    }
}
