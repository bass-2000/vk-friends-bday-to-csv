package bass2000;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

class FileHelper {
    static void printCSV(List<Friend> list) {

        StringBuilder sb = new StringBuilder();
        sb.append("Subject,");
        sb.append("Start Date,");
        sb.append("Start Time,");
        sb.append("End Date,");
        sb.append("End Time,");
        sb.append("Description,");
        sb.append("Private");
        sb.append('\n');
        for (Friend friend : list) {
            if (friend.getBday().length() < 3) {
                continue;
            }
            sb.append("День рождения ");
            sb.append(friend.getLastName());
            sb.append(" ");
            sb.append(friend.getFirstName());
            sb.append(",");
            String date = getBdateInGoogleFormat(friend.getBday());
            sb.append(date);
            sb.append(",");
            sb.append("09:00 AM,");
            sb.append(date);
            sb.append(",");
            sb.append("11:00 AM,");
            sb.append("Поздравить можно тут '");
            sb.append(friend.getUrl());
            sb.append("',");
            sb.append("False");
            sb.append("\n");
        }
        FileHelper.printToFile("friends.csv", sb.toString());

    }

    private static String getBdateInGoogleFormat(String date) {

        String[] split = date.split("\\.");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            sb.append(split[i].length() < 2 ? "0" + split[i] : split[i]);
            sb.append("/");
        }
        sb.append("2020");
        return sb.toString();
    }

    static void printToFile(String fileName, String value) {
        try (PrintStream out = new PrintStream(new FileOutputStream(fileName))) {
            out.println(value);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
