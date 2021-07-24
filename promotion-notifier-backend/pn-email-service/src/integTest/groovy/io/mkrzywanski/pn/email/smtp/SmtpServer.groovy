package io.mkrzywanski.pn.email.smtp

import com.icegreen.greenmail.util.GreenMail
import com.icegreen.greenmail.util.ServerSetup

import javax.mail.internet.MimeMessage

class SmtpServer implements AutoCloseable {

    private final GreenMail greenMail

    private SmtpServer(GreenMail greenMail) {
        this.greenMail = greenMail
    }

    static Builder builder() {
        return new Builder()
    }

    void start() {
        greenMail.start()
    }

    List<MimeMessage> getReceivedMessages() {
        return greenMail.getReceivedMessages()
    }

    void stop() {
        greenMail.stop()
    }

    int port() {
        return greenMail.getSmtp().port
    }

    @Override
    void close() throws Exception {
        stop()
    }

    static class Builder {
        private String userName
        private String password
        private String email
        private int port

        Builder() {
            this.userName = "test"
            this.password = "test"
            this.email = "test@test.pl"
            this.port = 0
        }

        Builder email(final String email) {
            this.email = email
            return this
        }

        Builder userName(final String userName) {
            this.userName = userName
            this
        }

        Builder password(final String password) {
            this.password = password
            this
        }

        Builder port(final int port) {
            this.port = port
            this
        }

        SmtpServer build() {
            GreenMail greenMail1 = new GreenMail(new ServerSetup(port, "localhost", "smtp"))
            greenMail1.setUser(email, userName, password)
            return new SmtpServer(greenMail1)
        }
    }
}
