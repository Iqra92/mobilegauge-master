package base;

import org.apache.commons.io.IOUtils;

import javax.mail.*;
import javax.mail.internet.MimeUtility;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmailUtils {
    private Folder folder;
    private Message message;
    private String authCode;
    public EmailUtils(String host, String storeType, String user, String password) throws MessagingException, IOException {
        try {

            // create properties
            Properties properties = new Properties();

            properties.put("mail.imap.host", host);
            properties.put("mail.imap.port", "993");
            properties.put("mail.imap.starttls.enable", "true");
            properties.put("mail.imap.ssl.trust", host);

            Session emailSession = Session.getDefaultInstance(properties);

            // create the imap store object and connect to the imap server
            Store store = emailSession.getStore("imaps");

            store.connect(host, user, password);

            // create the inbox object and open it
            Folder inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_WRITE);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            System.out.println("messages.length---" + messages.length);

            //for (int i = 0, n = messages.length; i < n; i++) {
            Message message = messages[messages.length-1];
            message.setFlag(Flags.Flag.SEEN, true);
            System.out.println("---------------------------------");
            String body = IOUtils.toString(
                    MimeUtility.decode(message.getInputStream(), "quoted-printable"),
                    "UTF-8"
            );
            final String regex = "text-align:center\\\">\\s*(.*?)\\s*<\\/div>";
            final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
            final Matcher matcher = pattern.matcher(body);
            //}
            while (matcher.find()) {
                System.out.println("Auth code bulundu: " + matcher.group(1));
                setAuthCode(matcher.group(1));
            }
            inbox.close(false);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
}

