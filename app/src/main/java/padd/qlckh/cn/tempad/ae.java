package padd.qlckh.cn.tempad;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ae
{
  private static MessageDigest a;
  private static StringBuilder b;
  
  static
  {
    try
    {
      a = MessageDigest.getInstance("MD5");
      b = new StringBuilder();
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      for (;;) {}
    }
  }
  
  public static String a(String paramString)
  {
    a.reset();
    a.update(paramString.getBytes());
    byte[] digest = a.digest();
//    paramString = a.digest();
    StringBuilder localStringBuilder = b;
    int i = 0;
    localStringBuilder.setLength(0);
    while (i < digest.length)
    {
      int j = digest[i] & 0xFF;
      if (j < 16) {
        b.append('0');
      }
      b.append(Integer.toHexString(j));
      i += 1;
    }
    return b.toString();
  }
}
