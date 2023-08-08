package my.slack.common.log;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CachedServletInputStream extends ServletInputStream {

    private final static Logger log = LoggerFactory.getLogger(CachedServletInputStream.class);
    private InputStream cachedInputStream;

    public CachedServletInputStream(byte[] cachedPayload) {
        this.cachedInputStream = new ByteArrayInputStream(cachedPayload);
    }

    @Override
    public boolean isFinished() {
        try {
            return cachedInputStream.available() == 0;
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int read() throws IOException {
        return cachedInputStream.read();
    }
}
