package com.cupshe.authorization.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    /**
     * 使用gzip进行压缩
     */
    public static String gzip(String primStr) {
        if (primStr == null || primStr.length() == 0) {
            return primStr;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(primStr.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (gzip != null) {
                try {
                    gzip.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return new sun.misc.BASE64Encoder().encode(out.toByteArray());
    }

    /**
     * <p>Description:使用gzip进行解压缩</p>
     *
     * @param compressedStr
     * @return
     */
    public static String gunzip(String compressedStr) {
        if (compressedStr == null) {
            return null;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = null;
        GZIPInputStream ginzip = null;
        byte[] compressed = null;
        String decompressed = null;
        try {
            compressed = new sun.misc.BASE64Decoder().decodeBuffer(compressedStr);
            in = new ByteArrayInputStream(compressed);
            ginzip = new GZIPInputStream(in);

            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = ginzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ginzip != null) {
                try {
                    ginzip.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }

        return decompressed;
    }

    /**
     * 使用zip进行压缩
     *
     * @param str 压缩前的文本
     * @return 返回压缩后的文本
     */
    public static final String zip(String str) {
        if (str == null)
            return null;
        byte[] compressed;
        ByteArrayOutputStream out = null;
        ZipOutputStream zout = null;
        String compressedStr = null;
        try {
            out = new ByteArrayOutputStream();
            zout = new ZipOutputStream(out);
            zout.putNextEntry(new ZipEntry("0"));
            zout.write(str.getBytes());
            zout.closeEntry();
            compressed = out.toByteArray();
            compressedStr = new sun.misc.BASE64Encoder().encodeBuffer(compressed);
        } catch (IOException e) {
            compressed = null;
        } finally {
            if (zout != null) {
                try {
                    zout.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        return compressedStr;
    }

    /**
     * 使用zip进行解压缩
     *
     * @param compressedStr 压缩后的文本
     * @return 解压后的字符串
     */
    public static final String unzip(String compressedStr) {
        if (compressedStr == null) {
            return null;
        }

        ByteArrayOutputStream out = null;
        ByteArrayInputStream in = null;
        ZipInputStream zin = null;
        String decompressed = null;
        try {
            byte[] compressed = new sun.misc.BASE64Decoder().decodeBuffer(compressedStr);
            out = new ByteArrayOutputStream();
            in = new ByteArrayInputStream(compressed);
            zin = new ZipInputStream(in);
            zin.getNextEntry();
            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = zin.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed = out.toString();
        } catch (IOException e) {
            decompressed = null;
        } finally {
            if (zin != null) {
                try {
                    zin.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        return decompressed;
    }

    public static void main(String[] args) throws IOException {
       String strOld="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJBVVRIT1JJWkFUSU9OSU5GTyI6InJPMEFCWE55QUM1dmNtY3VZWEJoWTJobExuTm9hWEp2TG1GMWRHaDZMbE5wYlhCc1pVRjFkR2h2Y21sNllYUnBiMjVKYm1adldldEsxbDdRTFZRQ0FBTk1BQkZ2WW1wbFkzUlFaWEp0YVhOemFXOXVjM1FBRDB4cVlYWmhMM1YwYVd3dlUyVjBPMHdBQlhKdmJHVnpjUUIrQUFGTUFCRnpkSEpwYm1kUVpYSnRhWE56YVc5dWMzRUFmZ0FCZUhCd2MzSUFFV3BoZG1FdWRYUnBiQzVJWVhOb1UyVjB1a1NGbFphNHR6UURBQUI0Y0hjTUFBQUFFRDlBQUFBQUFBQUJkQUFEWVd4c2VITnhBSDRBQTNjTUFBQUFRRDlBQUFBQUFBQWdkQUFHVDNKa1pYSnpkQUFRUW1GemFXTkVZWFJoTFcxaGJtRm5aWFFBQ0VOaGRHVm5iM0o1ZEFBT1pIbHVZVzFwWXkxa1pYUmhhV3gwQUFsd1oyTnlaWFpwWlhkMEFBZFFSME5JYjIxbGRBQU1aSGx1WVcxcFl5MXNhWE4wZEFBTFlXUmtMV0Z5ZEdsamJHVjBBQk5rZVc1aGJXbGpMWFpwWlhjdFpHVjBZV2xzZEFBTlQzQmxjbUYwYVc5dVpHRjBZWFFBQ1hWelpYSXRiR2x6ZEhRQURHWmhjMmhwYjI0dGJHbHpkSFFBREdSNWJtRnRhV010ZG1sbGQzUUFCRWh2YldWMEFBdGhaR1F0WkhsdVlXMXBZM1FBQ1hWblkzSmxkbWxsZDNRQURHTnZiblJsYm5RdGJHbHpkSFFBRG1GeWRHbGpiR1V0WkdWMFlXbHNkQUFJUTI5dGJXVnVkSE4wQUF4MWMyVnlMV1I1Ym1GdGFXTjBBQVpOWVc1aFoyVjBBQVZNYjJkcGJuUUFDM1Z6WlhJdGJXRnVZV2RsZEFBTFJIbHVZVzFwWXkxVWIzQjBBQWxqYjIxdGRXNXBkSGwwQUF4aGNuUnBZMnhsTFd4cGMzUjBBQVpUZDJsMFkyaDBBQXQxYzJWeUxXUmxkR0ZwYkhRQUZHRnlkR2xqYkdVdFpHVjBZV2xzTFdOb1pXTnJkQUFKYjNCbGNtRjBhVzl1ZEFBS2RHOXdhV010YkdsemRIUUFESFJ2Y0dsakxXUmxkR0ZwYkhnPSIsIlNFU1NJT04iOiJyTzBBQlhOeUFDcHZjbWN1WVhCaFkyaGxMbk5vYVhKdkxuTmxjM05wYjI0dWJXZDBMbE5wYlhCc1pWTmxjM05wYjI2ZEhLRzQxWXhpYmdNQUFIaHdkd0lBMjNRQUpESXdNVGRoTTJVMUxXTm1ZemN0TkROa1lTMWhOREkyTFdKa016VTRObVpoTURreVkzTnlBQTVxWVhaaExuVjBhV3d1UkdGMFpXaHFnUUZMV1hRWkF3QUFlSEIzQ0FBQUFYVnFKa3FHZUhOeEFINEFBM2NJQUFBQmRXem5qVVo0ZHhNQUFBQUFCU1pjQUFBSk1USTNMakF1TUM0eGMzSUFFV3BoZG1FdWRYUnBiQzVJWVhOb1RXRndCUWZhd2NNV1lORURBQUpHQUFwc2IyRmtSbUZqZEc5eVNRQUpkR2h5WlhOb2IyeGtlSEEvUUFBQUFBQUFESGNJQUFBQUVBQUFBQUowQUZCdmNtY3VZWEJoWTJobExuTm9hWEp2TG5OMVltcGxZM1F1YzNWd2NHOXlkQzVFWldaaGRXeDBVM1ZpYW1WamRFTnZiblJsZUhSZlFWVlVTRVZPVkVsRFFWUkZSRjlUUlZOVFNVOU9YMHRGV1hOeUFCRnFZWFpoTG14aGJtY3VRbTl2YkdWaGJzMGdjb0RWblBydUFnQUJXZ0FGZG1Gc2RXVjRjQUYwQUUxdmNtY3VZWEJoWTJobExuTm9hWEp2TG5OMVltcGxZM1F1YzNWd2NHOXlkQzVFWldaaGRXeDBVM1ZpYW1WamRFTnZiblJsZUhSZlVGSkpUa05KVUVGTVUxOVRSVk5UU1U5T1gwdEZXWE55QURKdmNtY3VZWEJoWTJobExuTm9hWEp2TG5OMVltcGxZM1F1VTJsdGNHeGxVSEpwYm1OcGNHRnNRMjlzYkdWamRHbHZicWgvV0NYR293aEtBd0FCVEFBUGNtVmhiRzFRY21sdVkybHdZV3h6ZEFBUFRHcGhkbUV2ZFhScGJDOU5ZWEE3ZUhCemNnQVhhbUYyWVM1MWRHbHNMa3hwYm10bFpFaGhjMmhOWVhBMHdFNWNFR3pBK3dJQUFWb0FDMkZqWTJWemMwOXlaR1Z5ZUhFQWZnQUdQMEFBQUFBQUFBeDNDQUFBQUJBQUFBQUJkQUFQWTNNdFltMTNPbnBvWVc1bmMyRnVjM0lBRjJwaGRtRXVkWFJwYkM1TWFXNXJaV1JJWVhOb1UyVjAyR3pYV3BYZEtoNENBQUI0Y2dBUmFtRjJZUzUxZEdsc0xraGhjMmhUWlhTNlJJV1ZscmkzTkFNQUFIaHdkd3dBQUFBQ1AwQUFBQUFBQUFGemNnQXdZMjl0TG1OMWNITm9aUzVrWXk1elpYSjJhV05sTG1SdmJXRnBiaTVrZEc4dVJHbHVaMVZ6WlhKSmJtWnZSRlJQVUF6aEk0cDJnZ2tDQUE1TUFBcGtaWEJoY25STWFYTjBkQUFTVEdwaGRtRXZiR0Z1Wnk5VGRISnBibWM3VEFBSmFHbHlaV1JFWVhSbGRBQVFUR3BoZG1FdmRYUnBiQzlFWVhSbE8wd0FBbWxrZEFBUVRHcGhkbUV2YkdGdVp5OU1iMjVuTzB3QUNXcHZZbTUxYldKbGNuRUFmZ0FXVEFBR2JXOWlhV3hsY1FCK0FCWk1BQVJ1WVcxbGNRQitBQlpNQUFodmNtZEZiV0ZwYkhFQWZnQVdUQUFJY0dGemMzZHZjbVJ4QUg0QUZrd0FDSEJ2YzJsMGFXOXVjUUIrQUJaTUFBcHplWE4wWlcxRGIyUmxjUUIrQUJaTUFBVjBiMnRsYm5FQWZnQVdUQUFIZFc1cGIyNUpaSEVBZmdBV1RBQUdkWE5sY2tsa2NRQitBQlpNQUFoMWMyVnlibUZ0WlhFQWZnQVdlSEJ3Y0hOeUFBNXFZWFpoTG14aGJtY3VURzl1Wnp1TDVKRE1qeVBmQWdBQlNnQUZkbUZzZFdWNGNnQVFhbUYyWVM1c1lXNW5MazUxYldKbGNvYXNsUjBMbE9DTEFnQUFlSEFBQUFBQUFBQUFBWEJ3ZEFBUFFtRnliMjR0NlptSTVidTY1TGljZEFBWmFtbGhibVJ2Ym1jdVkyaGxia0JyWVhCbGFYaHBMbU52YlhRQUlFVXhNRUZFUXpNNU5EbENRVFU1UVVKQ1JUVTJSVEExTjBZeU1FWTRPRE5GY0hRQUJtTnpMV0p0ZDNCd2RBQVNNREV3TnpFMU5UY3hOak0zTnpRMU16SXlkQUFJZW1oaGJtZHpZVzU0ZUFCM0FRRnhBSDRBRUhoNGVBPT0iLCJleHAiOjE2MDM5MzYzMTZ9.sCk3P3PsWdEHt06KLFSgok_0OI7Ts-M9EHiZpfGOjP4";
        System.out.println(strOld.length());
        String gzip = gzip(strOld);
        System.out.println(gzip.length());
    }
}