package ca.mcgill.ecse321.SportsSchedulePlus.beans;

/**
 * Class to contain the information for an email account and its mail configuration. Contains information
 * for the email client to work with mail systems other than GMail.
 *
 * @author Dania Bouhmidi
 *
 */
public class MailConfigBean {

    private String imapUrl;
    private String smtpUrl;
    private String userName;
    private String userEmailAddress;
    private String password;
    private String databaseURL;
    private String databaseName;
    private int databasePortNumber;
    private String databaseUserName;
    private String databasePassword;
    private int imapPortNumber;
    private int smtpPortNumber;

    /**
     * Default Constructor Sets the host, user email address and emailPassword
     * to empty strings.
     */
    public MailConfigBean() {
        this.imapUrl = "";
        this.smtpUrl = "";
        this.userEmailAddress = "";
        this.password = "";
        this.userName = "";
        this.databasePortNumber = 3306;
        this.imapPortNumber = 993;
        this.smtpPortNumber = 465;
        this.databaseName = "";
        this.databasePassword = "";
    }

    /**
     * Non-default constructor.Sets all the parameters.
     *
     * @param imapUrl
     * @param smtpUrl
     * @param userName
     * @param userEmailAddress
     * @param password
     * @param databaseURL
     * @param databaseName
     * @param databasePortNumber
     * @param databaseUserName
     * @param databasePassword
     * @param imapPortNumber
     * @param smtpPortNumber
     */
    public MailConfigBean(final String imapUrl, final String smtpUrl, final String userName, final String userEmailAddress, final String password,
            final String databaseURL, final String databaseName, final int databasePortNumber, final String databaseUserName,
            final String databasePassword, final int imapPortNumber, final int smtpPortNumber) {
        this.imapUrl = imapUrl;
        this.smtpUrl = smtpUrl;
        this.userName = userName;
        this.userEmailAddress = userEmailAddress;
        this.password = password;
        this.databaseURL = databaseURL;
        this.databaseName = databaseName;
        this.databasePortNumber = databasePortNumber;
        this.databaseUserName = databaseUserName;
        this.databasePassword = databasePassword;
        this.imapPortNumber = imapPortNumber;
        this.smtpPortNumber = smtpPortNumber;
    }

    /**
     * Non-default constructor Only sets the host, the userEmailAddress and the
     * emailPassword.
     *
     * @param imapUrl
     * @param smtpUrl
     * @param userEmailAddress
     * @param password
     */
    public MailConfigBean(final String imapUrl, final String smtpUrl, final String userEmailAddress, final String password) {
        this.imapUrl = imapUrl;
        this.smtpUrl = smtpUrl;
        this.userEmailAddress = userEmailAddress;
        this.password = password;
    }

    /**
     * Returns the userEmailAddress.
     *
     * @return the userEmailAddress
     */
    public final String getUserEmailAddress() {
        return userEmailAddress;
    }

    /**
     * Sets the userEmailAddress.
     *
     * @param userEmailAddress
     */
    public final void setUserEmailAddress(final String userEmailAddress) {
        this.userEmailAddress = userEmailAddress;
    }

    /**
     * Returns the emailPassword.
     *
     * @return the emailPassword.
     */
    public final String getEmailPassword() {
        return password;
    }

    /**
     * Sets the emailPassword
     *
     * @param emailPassword the emailPassword to set
     */
    public final void setEmailPassword(final String emailPassword) {
        this.password = emailPassword;
    }

    /**
     * Gets the database URL.
     *
     * @return databaseUrl.
     */
    public final String getDatabaseURL() {
        return databaseURL;
    }

    /**
     * Sets the database URL.
     *
     * @param databaseURL to set.
     */
    public final void setDatabaseURL(final String databaseURL) {
        this.databaseURL = databaseURL;
    }

    /**
     * Gets the database name.
     *
     * @return databaseName
     */
    public final String getDatabaseName() {
        return databaseName;
    }

    /**
     * Sets the database name.
     *
     * @param databaseName to set.
     */
    public final void setDatabaseName(final String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * Gets the database port number.
     *
     * @return databasePortNumber
     */
    public final int getDatabasePortNumber() {
        return databasePortNumber;
    }

    /**
     * Sets the database port number.
     *
     * @param databasePortNumber to set.
     */
    public final void setDatabasePortNumber(final int databasePortNumber) {
        this.databasePortNumber = databasePortNumber;
    }

    /**
     * Gets the database username.
     *
     * @return the database username.
     */
    public final String getDatabaseUserName() {
        return databaseUserName;
    }

    /**
     * Sets the database username.
     *
     * @param databaseUserName to set.
     */
    public final void setDatabaseUserName(final String databaseUserName) {
        this.databaseUserName = databaseUserName;
    }

    /**
     * Gets the database emailPassword.
     *
     * @return the database emailPassword.
     */
    public final String getDatabasePassword() {
        return databasePassword;
    }

    /**
     * Sets the database emailPassword.
     *
     * @param databasePassword to set
     */
    public final void setDatabasePassword(final String databasePassword) {
        this.databasePassword = databasePassword;
    }

    /**
     * Gets the email account's username.
     *
     * @return userName
     */
    public final String getUserName() {
        return userName;
    }

    /**
     * Sets the email account's username.
     *
     * @param userName
     */
    public final void setUserName(final String userName) {
        this.userName = userName;
    }

    /**
     * Gets the imapPortNumber
     *
     * @return imapPortNumber.
     */
    public final int getImapPortNumber() {
        return imapPortNumber;
    }

    /**
     * Sets the imapPortNumber.
     *
     * @param imapPortNumber to set.
     */
    public final void setImapPortNumber(final int imapPortNumber) {
        this.imapPortNumber = imapPortNumber;
    }

    /**
     * Gets the smptPortNumber.
     *
     * @return smptPortNumber.
     */
    public final int getSmtpPortNumber() {
        return smtpPortNumber;
    }

    /**
     * Sets the smptPortNumber
     *
     * @param smtpPortNumber to set.
     */
    public final void setSmtpPortNumber(final int smtpPortNumber) {
        this.smtpPortNumber = smtpPortNumber;
    }

    /**
     * Gets the imap url
     *
     * @return imap url
     */
    public String getImapUrl() {
        return imapUrl;
    }

    /**
     * Sets the imap url
     *
     * @param imapUrl
     */
    public void setImapUrl(final String imapUrl) {
        this.imapUrl = imapUrl;
    }

    /**
     * Gets the smtp url
     *
     * @return smtp url
     */
    public String getSmtpUrl() {
        return smtpUrl;
    }

    /**
     * Sets the smtp url
     *
     * @param smtpUrl
     */
    public void setSmtpUrl(final String smtpUrl) {
        this.smtpUrl = smtpUrl;
    }

}
