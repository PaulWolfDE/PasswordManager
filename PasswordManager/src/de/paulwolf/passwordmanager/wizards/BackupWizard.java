package de.paulwolf.passwordmanager.wizards;

import com.jcraft.jsch.*;
import de.paulwolf.passwordmanager.Configuration;
import de.paulwolf.passwordmanager.information.Database;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;

/**
 * SFTP AUTOMATED BACKUP CONCEPT
 * -----------------------------
 *
 * special reserved entry for sftp/ssh credentials;
 * backup to remote server every safe (later maybe user-defined);
 * inform user about unsuccessful saves
 *
 * reserved entry structure:
 * TITLE: sftp-automated-backup (reserved)
 * USERNAME: (sftp/ssh user name)
 * EMAIL -> HOSTNAME: (sftp/ssh server name)
 * PASSWORD: (sftp/ssh secret)
 * LAST MODIFIED: [N/A or ignored]
 * NOTES: [N/A or ignored]
 */

public class BackupWizard {

    public static void createBackup(String username, String hostname, byte[] password, Database database) throws SftpException, JSchException {

        Session session = null;
        ChannelSftp sftp = null;

        try {
            session = new JSch().getSession(username, hostname);
            session.setPassword(password);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            config.put("PreferredAuthentications", "password");
            session.setConfig(config);
            session.connect(3000);

            sftp = (ChannelSftp) session.openChannel("sftp");
            sftp.connect();
            try {
                sftp.mkdir("database_backup");
            } catch (SftpException ignored) {}
            sftp.cd("database_backup");
            String localDatabasePath = database.getPath().getAbsolutePath();
            String localDatabaseName = localDatabasePath.substring(localDatabasePath.lastIndexOf('/') + 1);
            sftp.put(localDatabasePath, String.format("%s-%s.pmdtb", localDatabaseName.substring(0, localDatabaseName.length() - 6), Configuration.DATE_FORMAT.format(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))));

        } finally {
            if (session != null)
                session.disconnect();
            if (sftp != null)
                sftp.disconnect();
        }
    }
}
