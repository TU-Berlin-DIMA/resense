package de.tuberlin.dima.common.config;

import java.nio.ByteBuffer;

public enum DataType {

    SHORT(2),
    INTEGER(4),
    DOUBLE(8),
    BOOLEAN(1),
    BYTE(1);

    private final int length;

    DataType(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public int[] transform(String inputString) {
        int[] result = new int[this.getLength()];
        switch (this) {
            case BOOLEAN:
                boolean inputBoolean = Boolean.parseBoolean(inputString);
                result[0] = ((inputBoolean) ? 1 : 0);
                break;
            case BYTE:
                byte inputByte = Byte.parseByte(inputString);
                result = toUnsignedIntArray(new byte[]{inputByte});
                break;
            case SHORT:
                short inputShort = Short.parseShort(inputString);
                result = toUnsignedIntArray(ByteBuffer.allocate(2).putShort(inputShort).array());
                break;
            case INTEGER:
                int inputInteger = Integer.parseInt(inputString);
                result = toUnsignedIntArray(ByteBuffer.allocate(4).putInt(inputInteger).array());
                break;
            case DOUBLE:
                double inputDouble = Double.parseDouble(inputString);
                result = toUnsignedIntArray(ByteBuffer.allocate(8).putDouble(inputDouble).array());
                break;
        }
        return result;
    }

    public Object transform(int[] input) {
        byte[] inputBytes = toByteArray(input);
        switch (this) {
            case BOOLEAN:
                return input[0] == 1;
            case BYTE:
                return inputBytes[0];
            case SHORT:
                return ByteBuffer.wrap(inputBytes).getShort();
            case INTEGER:
                return ByteBuffer.wrap(inputBytes).getInt();
            case DOUBLE:
                return ByteBuffer.wrap(inputBytes).getDouble();
            default:
                throw new RuntimeException("We've got problems...");
        }
    }

    private int[] toUnsignedIntArray(byte[] input) {
        int[] result = new int[input.length];
        for (int i = 0; i < input.length; i++) {
            result[i] = input[i] & 0xFF;
        }
        return result;
    }

    private byte[] toByteArray(int[] input) {
        byte[] result = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            result[i] = (byte) input[i];
        }
        return result;
    }

    public String getDescription()
    {
        return this.name();
    }

}
