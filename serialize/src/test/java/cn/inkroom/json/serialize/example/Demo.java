/*
 * Copyright <2021> <enpassPixiv@protonmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package cn.inkroom.json.serialize.example;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Demo {

    private int v1 = 12;
    private double v2 = 32.4;
    private String v3 = "v3";
    private float v4 = 3.0f;
    private String v5 = null;
    private char v6 = '6';
    private BigInteger v7 = new BigInteger("433");
    private BigDecimal v8 = new BigDecimal("3493.349");

    private List<Demo> v9;
    private Map<String, Demo> v10;

    private Integer v13Integer;
    private boolean v14 = true;

    private int[] v15;
    private double[] v16;
    private Float[] v17 = new Float[]{21f, 32f, Float.valueOf(12f)};
    private boolean[] v18 = new boolean[]{false, true, Boolean.FALSE, Boolean.TRUE};
    private byte[] v19 = new byte[]{12, 43, -12, 21, -43};
    private char[] v20 = new char[]{'3', '5', '7', '9', 'w'};
    private StringBuilder v21 = new StringBuilder("v21");

    public char[] getV20() {
        return v20;
    }

    public double[] getV16() {
        return v16;
    }

    public void setV16(double[] v16) {
        this.v16 = v16;
    }

    public Float[] getV17() {
        return v17;
    }

    public void setV17(Float[] v17) {
        this.v17 = v17;
    }

    public boolean[] getV18() {
        return v18;
    }

    public void setV18(boolean[] v18) {
        this.v18 = v18;
    }

    public byte[] getV19() {
        return v19;
    }

    public void setV19(byte[] v19) {
        this.v19 = v19;
    }

    public void setV20(char[] v20) {
        this.v20 = v20;
    }

    public int[] getV15() {
        return v15;
    }

    public void setV15(int[] v15) {
        this.v15 = v15;
    }

    public boolean isV14() {
        return v14;
    }

    public void setV14(boolean v14) {
        this.v14 = v14;
    }

    public Integer getV13Integer() {
        return v13Integer;
    }

    public void setV13Integer(Integer v13Integer) {
        this.v13Integer = v13Integer;
    }

    public int getV1() {
        return v1;
    }

    public void setV1(int v1) {
        this.v1 = v1;
    }

    public double getV2() {
        return v2;
    }

    public void setV2(double v2) {
        this.v2 = v2;
    }

    public String getV3() {
        return v3;
    }

    public void setV3(String v3) {
        this.v3 = v3;
    }

    public float getV4() {
        return v4;
    }

    public void setV4(float v4) {
        this.v4 = v4;
    }

    public String getV5() {
        return v5;
    }

    public void setV5(String v5) {
        this.v5 = v5;
    }

    public char getV6() {
        return v6;
    }

    public void setV6(char v6) {
        this.v6 = v6;
    }

    public BigInteger getV7() {
        return v7;
    }

    public void setV7(BigInteger v7) {
        this.v7 = v7;
    }

    public BigDecimal getV8() {
        return v8;
    }

    public void setV8(BigDecimal v8) {
        this.v8 = v8;
    }

    public List<Demo> getV9() {
        return v9;
    }

    public void setV9(List<Demo> v9) {
        this.v9 = v9;
    }

    public Map<String, Demo> getV10() {
        return v10;
    }

    public void setV10(Map<String, Demo> v10) {
        this.v10 = v10;
    }

    @Override
    public String toString() {
        return "Demo{" +
                "v1=" + v1 +
                ", v2=" + v2 +
                ", v3='" + v3 + '\'' +
                ", v4=" + v4 +
                ", v5='" + v5 + '\'' +
                ", v6=" + v6 +
                ", v7=" + v7 +
                ", v8=" + v8 +
                ", v9=" + v9 +
                ", v10=" + v10 +
                ", v13Integer=" + v13Integer +
                ", v14=" + v14 +
                ", v15=" + Arrays.toString(v15) +
                '}';
    }
}
