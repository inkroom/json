/*
 * Copyright <2021> <enpassPixiv@protonmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package cn.inkroom.json.core.reader;

import cn.inkroom.json.core.annotation.JsonConfig;
import org.junit.Test;

import java.io.Serializable;

public class AbstractTokenReaderTest extends TokenReaderTest implements Serializable {

    @Test
    public void readNumber() {
        super.readNumber(s -> new StringTokenReader(s, new JsonConfig()));
    }

    @Test
    public void readBoolean() {
        super.readBoolean(s -> new StringTokenReader(s, new JsonConfig()));
    }

    @Test
    public void readNull() {
        super.readNull(s -> new StringTokenReader(s, new JsonConfig()));
    }

    @Test
    public void readString() {
        super.readString(s -> new StringTokenReader(s, new JsonConfig()));


    }
}