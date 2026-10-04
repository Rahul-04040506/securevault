package com.securevault.util;

import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class FileMergeUtil {

    public byte[] merge(List<byte[]> chunks) throws IOException {

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        for (byte[] chunk : chunks) {

            outputStream.write(chunk);

        }

        return outputStream.toByteArray();

    }

}
