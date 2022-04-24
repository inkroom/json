package cn.inkroom.json.core.reader;

import cn.inkroom.json.core.Token;
import cn.inkroom.json.core.annotation.JsonConfig;
import cn.inkroom.json.core.exception.JsonParseException;

public class StringTokenReader extends AbstractTokenReader {

    private final char[] data;//数据窗口，每次读取这么多数据，把数据窗口的数据使用完之后再读取
    private int cursor = -1;//数据指针，指向即将读取的数据位置，包括换行符之类的空白字符
    private int row = 0;//读取到第几行
    private int col = -1;//读取到第几列

    public StringTokenReader(String data, JsonConfig config) {
        super(config);
        this.data = data.toCharArray();
    }


    @Override
    public int index() {
        return cursor;
    }

    @Override
    public char peek() {
        if (!hasMore()) throwError(null);
        return data[cursor];
    }

    @Override
    public boolean hasMore() {
        return cursor < data.length;
    }

    @Override
    public void move() {
        cursor++;
    }

    @Override
    public char next() {

        if (!hasMore()) throwError(null);
        char datum = data[cursor++];
        col++;
        switch (datum) {
            case '\r':
            case '\n':
                row++;
                col = 0;
        }
        return datum;
    }

    @Override
    public char now() {
        return data[cursor - 1];
    }

    @Override
    public void throwError(Token token) throws JsonParseException {
        throw new JsonParseException("Unexpected character [" + now() + "] row: " + row + ", col: " + col);

    }
}
