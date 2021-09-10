package cn.inkroom.json.reader;

import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.*;

public class SingleLineTokenReaderTest extends TokenReaderTest {

    @Test
    public void readNumber() {
        super.readNumber(SingleLineTokenReader::new);
    }

    @Test
    public void readBoolean() {
        super.readBoolean(SingleLineTokenReader::new);
    }

    @Test
    public void readNull() {
        super.readNull(SingleLineTokenReader::new);
    }
}