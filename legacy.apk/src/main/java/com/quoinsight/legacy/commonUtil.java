package com.quoinsight.legacy;

public class commonUtil {

  //////////////////////////////////////////////////////////////////////

  static final public String getChineseDateStr1() {
    final String[] dayArr = new String[] { "初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十", "十一", "十二", "十三", "十四",
      "十五", "十六", "十七", "十八", "十九", "二十", "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十", "卅一"
     }, monthArr = new String[] { "正", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"
     // }, hourArr = new String[] { "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥", "子" }
     }, hourArr = new String[] { "子zǐ", "丑chǒu", "寅yín", "卯mǎo", "辰chén", "巳sì", "午wǔ", "未wèi", "申shēn", "酉yǒu", "戌xū", "亥hài", "子zǐ" }
    ;

    java.util.Calendar cal = java.util.Calendar.getInstance();
    ChineseCalendar c = new ChineseCalendar();
      c.setGregorian(cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH)+1, cal.get(java.util.Calendar.DAY_OF_MONTH));
      c.computeChineseFields();
      c.computeSolarTerms();
    int chineseMonth = c.chineseMonth;
    int chineseDate = c.chineseDate;

    String dateStr = ( (c.chineseMonth<0) ? "闰" : "" )
      + monthArr[Math.abs(c.chineseMonth)-1] + "月" // MONTH==0..11
      + dayArr[c.chineseDate-1] // DAY_OF_MONTH==1..31
      + "⁄" + c.daysInChineseMonth(c.chineseYear, c.chineseMonth)
      + hourArr[(int)(cal.get(java.util.Calendar.HOUR_OF_DAY)+1)/2] + "时" // HOUR_OF_DAY==0..23
      ;
    return dateStr;
  }

  static final public String getChineseDateStr() {
    final String[] dayArr = new String[] { "初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十", "十一", "十二", "十三", "十四",
      "十五", "十六", "十七", "十八", "十九", "二十", "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十", "卅一"
     }, monthArr = new String[] { "正", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"
     // }, hourArr = new String[] { "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥", "子" }
     }, hourArr = new String[] { "子zǐ", "丑chǒu", "寅yín", "卯mǎo", "辰chén", "巳sì", "午wǔ", "未wèi", "申shēn", "酉yǒu", "戌xū", "亥hài", "子zǐ" }
    ;

    String debug = "";
    try {
      Class<?> ULocale = Class.forName("android.icu.util.ULocale");  // some devices or versions may not support this!
      Class<?> Calendar = Class.forName("android.icu.util.Calendar");
      java.lang.reflect.Method getInstance = Calendar.getDeclaredMethod("getInstance", ULocale);
      Object chineseCalendar = getInstance.invoke(
        null, ULocale.getConstructor(String.class).newInstance("zh_CN@calendar=chinese")
      );
      java.lang.reflect.Method get = chineseCalendar.getClass().getMethod("get", int.class);
      java.lang.reflect.Method getActualMaximum = chineseCalendar.getClass().getMethod("getActualMaximum", int.class);
      //java.lang.reflect.Field IS_LEAP_MONTH = chineseCalendar.getClass().getField("IS_LEAP_MONTH");
        // notApplicable for IS_LEAP_MONTH ==> .getDeclaredField(<notForInheritedFields>);  <field>.setAccessible(true);

      String dateStr = ( ((Integer)get.invoke(chineseCalendar, Calendar.getDeclaredField("IS_LEAP_MONTH").get(null))==1) ? "闰" : "" )
        + monthArr[(Integer)get.invoke(chineseCalendar, java.util.Calendar.MONTH)] + "月" // MONTH==0..11
        + dayArr[(Integer)get.invoke(chineseCalendar, java.util.Calendar.DAY_OF_MONTH)-1] // DAY_OF_MONTH==1..31
        + "⁄" + (Integer)getActualMaximum.invoke(chineseCalendar, java.util.Calendar.DAY_OF_MONTH)
        + hourArr[(int)((Integer)get.invoke(chineseCalendar, java.util.Calendar.HOUR_OF_DAY)+1)/2] + "时" // HOUR_OF_DAY==0..23
        ;

      //! above is to avoid the java.lang.NoClassDefFoundError at runtime !
      /*
        android.icu.util.Calendar chineseCalendar = android.icu.util.Calendar.getInstance(
          new android.icu.util.ULocale("zh_CN@calendar=chinese")  // android.icu.util.ChineseCalendar.getInstance();
        );
      String dateStr = ( chineseCalendar.get(android.icu.util.Calendar.IS_LEAP_MONTH)==1 ? "闰" : "" )
          + monthArr[chineseCalendar.get(java.util.Calendar.MONTH)] + "月" // MONTH==0..11
          + dayArr[chineseCalendar.get(java.util.Calendar.DAY_OF_MONTH)-1] // DAY_OF_MONTH==1..31
          + "⁄" + chineseCalendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH)
          + hourArr[(int)(chineseCalendar.get(java.util.Calendar.HOUR_OF_DAY)+1)/2] + "时" // HOUR_OF_DAY==0..23
          ; // https://www.ntu.edu.sg/home/ehchua/programming/java/DateTimeCalendar.html
      */
      return dateStr;
    } catch(Exception e) {
      // some devices or versions may not support this
      // return debug + "::" + e.getMessage();
    }
    return "<ChineseDateUnavailable/>";
  }

  static final public String getDateStr(String format) {
    java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat(
      format, java.util.Locale.getDefault()
    );
    return simpleDateFormat.format(new java.util.Date());
  }

  //////////////////////////////////////////////////////////////////////

  static final public String joinStringList(String sep, java.util.List<String> lst) {
    // String.join() is not support by some versions --> use our local function joinStringList() instead
    String str = "";
    if (lst.size() > 0) {
      for (String s : lst) str = str + s + sep;
      str = str.substring(0, str.length()-sep.length()); // remove the last separator
    }
    return str;
  }

  static final public String[] getKeysetStringArr(java.util.LinkedHashMap<String, String> hashMap) {
    return hashMap.keySet().toArray(new String[hashMap.size()]);
  }

  //////////////////////////////////////////////////////////////////////

  public static String wget(String s_url) {
    String responseText = "";
    try {
      java.net.URL url = new java.net.URL(s_url);
      java.net.HttpURLConnection urlConnection
        = (java.net.HttpURLConnection) url.openConnection();
      try {
        // !! below will throw exception if running in Android without AsyncTask !!
        java.io.InputStream in = new java.io.BufferedInputStream(urlConnection.getInputStream());
        byte[] contents = new byte[1024];  int bytesRead = 0;
        while((bytesRead = in.read(contents)) != -1) { 
          responseText += new String(contents, 0, bytesRead);              
        }
      } catch (Exception e) {
        responseText = "ERROR: " + e.getMessage() + " \n " + responseText;
      } finally {
        urlConnection.disconnect();
      }
    } catch (Exception e) {
      responseText = "ERROR: " + e.getMessage() + " \n " + responseText;
    }
    return responseText;
  }

  /*
    <application android:label="QuoInsight☸Minimal"
      android:usesCleartextTraffic="true" >> need this to support http://
 
    https://stackoverflow.com/questions/32153991/urlconnection-connect-works-only-in-asynctask-android
    !! Network communications must be done in seprate Thread in android !!
  */

  public static java.util.List wgets(String s_url) {
    java.util.List lines = new java.util.ArrayList();
    int respCode = -1;  String dbgInf = "";

    java.net.URL url = null;
    java.net.HttpURLConnection urlConn = null;
    try {
      dbgInf += "#0";
      url = new java.net.URL(s_url);
      dbgInf += "#1";
      urlConn = (java.net.HttpURLConnection) url.openConnection();
    } catch (Exception e) {
      lines.add(0, "ERROR: " + dbgInf + " " + e.getMessage() + "; url=" + s_url);
      return lines;
    }
    dbgInf += "#2";

    java.io.InputStream in;
    try {
      urlConn.setRequestMethod("GET");
      urlConn.setConnectTimeout(3000); urlConn.setReadTimeout(5000);
      urlConn.setUseCaches(false); urlConn.setAllowUserInteraction(false);
      urlConn.setDoInput(true); //default
      dbgInf += "#3";
      // !! below will throw exception if running in Android without AsyncTask !!
      respCode = urlConn.getResponseCode();
      dbgInf += "#HTTP" + String.valueOf(respCode);
      in = urlConn.getInputStream();
      dbgInf += "#4";
      if (respCode != java.net.HttpURLConnection.HTTP_OK) {
        lines.add(0, "ERROR: HTTP " + String.valueOf(respCode) + "; " + dbgInf + "; url=" + urlConn.getURL());
        return lines;
      }
    } catch (Exception e) {
      lines.add(0, "ERROR: " + dbgInf + " " + e.getMessage() + "; url=" + urlConn.getURL());
      return lines;
    }

    dbgInf += "#6";
    try {
      java.io.BufferedReader r = new java.io.BufferedReader(new java.io.InputStreamReader(
        new java.io.BufferedInputStream(in), java.nio.charset.StandardCharsets.UTF_8
      ));
      dbgInf += "#8";
      String line=null;  while ((line=r.readLine())!=null) lines.add(line);
      dbgInf += "#9";
    } catch (java.io.IOException e) {
      lines.add(0, "IO-ERROR: " + e.getMessage() + "; " + dbgInf + " url=" + urlConn.getURL());
    } catch (Exception e) {
      lines.add(0, "ERROR: " + e.getMessage() + "; " + dbgInf + " src=" + urlConn.getURL());
    } finally {
      urlConn.disconnect();
    }

    return lines;
  }

  //////////////////////////////////////////////////////////////////////

  public static String getRootUrl(String s_url) {
    int p = s_url.indexOf("://");  p = s_url.indexOf("/", p+3);
    // --** never include the first "/" **-- //
    return ( p > 0) ? s_url.substring(0, p) : s_url;
  }

  public static String getBaseUrl(String s_url) {
    String url = s_url.substring(0, s_url.lastIndexOf("/")+1);
    // --** always ends with "/" **-- //
    return (url.endsWith("://")) ? s_url + "/" : url;
  }

  //////////////////////////////////////////////////////////////////////

  public static String getNextSeq(String thisSeq) {
    int nextValue = 1 + Integer.parseInt(thisSeq);
    return String.format("%0"+String.valueOf(thisSeq.length())+"d", new Object[]{new Integer(nextValue)});
  }

  public static String getNextUrl(String s_url) {
    String url = s_url.trim();
    java.util.regex.Pattern re = java.util.regex.Pattern.compile("^(.*)(\\D)(\\d+?)(\\.\\S+)$");
    java.util.regex.Matcher m = re.matcher(url);
    if ( m.find() ) {
      url = m.group(1) + m.group(2) + getNextSeq(m.group(3)) + m.group(4);
    } else {
      re = java.util.regex.Pattern.compile("^(.*)(\\D)(\\d{4,}?)(\\D+?)(\\S+)$");
      m = re.matcher(url);
      if ( m.find() ) {
        url = m.group(1) + m.group(2) + getNextSeq(m.group(3)) + m.group(4) + m.group(5);
      }
    }
    return url;
  }

  public static boolean urlEndsWithM3u(String s_url) {
    String url = s_url.trim().toLowerCase();
    return url.endsWith(".m3u")||url.endsWith(".m3u8")
      ||url.contains(".m3u?")||url.contains(".m3u8?");
  }

 /*
  public static String getMediaUrl(String s_url) {
    String url = s_url;
    if ( urlEndsWithM3u(url) ) {
      java.util.List lines = wgets(url);
      String firstLine = null;
      for (int i=0; i<lines.size(); i++) {
        // System.out.println(lines.get(i));
        String thisLine = lines.get(i).toString().trim();
        if ( thisLine==null || thisLine.length()==0 || thisLine.equals("null") ) {
          continue;
        } else if (firstLine==null) {
          firstLine = thisLine;
          if ( !firstLine.equals("#EXTM3U") ) {
            //System.out.println("invalid m3u8");
            url += "#invalid m3u8 [" + thisLine + "]";
            break;
          }
        } else if ( !thisLine.startsWith("#") ) {
          //System.out.println("OK");
          url = thisLine;
          if ( ! ( url.startsWith("http://")||url.startsWith("https://") ) )
            url = getBaseUrl(s_url) + url;
          if ( urlEndsWithM3u(thisLine) )
            url = getMediaUrl(url);
          break;
        }
      }
    }
    return url;
  }
 */

  public static String getMediaUrl2(String s_url) {
    String url = s_url;  // if ( !urlEndsWithM3u(url) ) return url;

    try {
      java.net.HttpURLConnection urlConn = (java.net.HttpURLConnection) (new java.net.URL(s_url)).openConnection();

      int respCode = urlConn.getResponseCode();
      if (respCode != java.net.HttpURLConnection.HTTP_OK) {
        return "ERROR: HTTP " + String.valueOf(respCode) + "; url=" + urlConn.getURL();
      }

      String contentType = urlConn.getContentType().toLowerCase();
      // https://en.wikipedia.org/wiki/M3U
      if ( ! (
        contentType.equals("application/vnd.apple.mpegurl") || contentType.equals("application/vnd.apple.mpegurl.audio")
         || contentType.equals("audio/mpegurl") || contentType.equals("audio/x-mpegurl")
          || contentType.equals("application/mpegurl") || contentType.equals("application/x-mpegurl")
      ) ) {
        //return "ERROR: Invalid ContentType [" + contentType + "]; url=" + urlConn.getURL();
        return url;
      }

      try {
        java.io.InputStream in = new java.io.BufferedInputStream(urlConn.getInputStream());
        java.io.BufferedReader r = new java.io.BufferedReader(new java.io.InputStreamReader(in, java.nio.charset.StandardCharsets.UTF_8));
        String thisLine=null, firstLine=null;
        while ((thisLine=r.readLine())!=null) {
          thisLine = thisLine.trim();
          if ( thisLine.length()==0 ) {
            continue;
          } else if (firstLine==null) {
            firstLine = thisLine;
            if ( !firstLine.equals("#EXTM3U") ) {
              //System.out.println("invalid m3u8");
              url += "#invalid m3u8 [" + thisLine + "]";
              break;
            }
          } else if ( !thisLine.startsWith("#") ) {
            url = thisLine;
            if ( url.startsWith("http://")||url.startsWith("https://") ) {
              // OK: take the url as-is
            } else if ( url.startsWith("/") ) {
              url = getRootUrl(s_url) + url;
            } else {
              url = getBaseUrl(s_url) + url;
            }
            if ( urlEndsWithM3u(thisLine) )
              url = getMediaUrl2(url);
            break;
          }
        }
      } catch (Exception e) {
        return "ERROR: " + e.getMessage();
      }

      urlConn.disconnect();

    } catch (Exception e) {

      return "ERROR: " + e.getMessage();

    }

    return url;
  }

  //////////////////////////////////////////////////////////////////////

  public static java.util.HashMap<String, String> getIcyMetaData(String s_url) {
    java.util.HashMap<String, String> icyData = new java.util.HashMap<String, String>();
    // Hashtable does not allow null keys or values

    // https://cast.readme.io/docs/icy
    // https://people.kth.se/~johanmon/dse/casty.pdf
    // http://ample.sourceforge.net/developers.shtml
    // https://stackoverflow.com/questions/47388541/value-type-of-icy-metaint
    // https://stackoverflow.com/questions/34267353/get-metadata-from-shoutcast-stream
    // https://github.com/saschpe/android-exoplayer2-ext-icy/blob/master/exoplayer2-ext-icy/src/main/java/saschpe/exoplayer2/ext/icy/IcyHttpDataSource.java

    // [xxx bytes audio] [metadata] [xxx bytes audio] [metadata] …

    try {
      java.net.HttpURLConnection urlConn = (java.net.HttpURLConnection) (new java.net.URL(s_url)).openConnection();
      urlConn.setRequestProperty("icy-metadata", "1");  // check for support and accepts ICY Metadata

      int respCode = urlConn.getResponseCode();
      if (respCode != java.net.HttpURLConnection.HTTP_OK) {
        icyData.put("ERROR", "HTTP " + String.valueOf(respCode) + "; url=" + urlConn.getURL());
        return icyData;
      }

      icyData.put("content-type", urlConn.getContentType().toLowerCase());
      icyData.put("icy-metaint", urlConn.getHeaderField("icy-metaint"));

      String icyMetaInt = icyData.get("icy-metaint");
      if (icyMetaInt==null) {
        return icyData;
      }
      
      icyData.put("icy-name", urlConn.getHeaderField("icy-name"));
      icyData.put("icy-url", urlConn.getHeaderField("icy-url"));

      try {
        int metadataOffset = Integer.parseInt(icyData.get("icy-metaint"));
        int metadataEnd = metadataOffset + 4080 + 1; // 4080 is the max length

        int b = -1, c = 0;
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        java.io.InputStream in = urlConn.getInputStream();
        //int count=-1; while ( (b=in.read()) != -1 ) { count++;
        int count=0; while ( count <= metadataEnd ) {
          if ( count < metadataOffset ) {
            c = in.read(new byte[metadataOffset-count]);
            if (c==0) break; 
            count += c;
            continue;
          } else if ( count == metadataOffset ) {
            if (b==-1) {
              b = in.read();
              if (b <= 0) break; 
              count += 1;
            }
            int metadataLength = b * 16;
            metadataEnd = metadataOffset + metadataLength + 1;
            byte[] buffer = new byte[metadataLength];
            c = in.read(buffer);
            if (c==0) break; 
            count += c;
            //buffer = (new String(buffer)).trim().getBytes();
            out.write(buffer);
            break;
          } else if ( count < metadataEnd ) {
            if (b==0) break;
            out.write(b);
          } else {
            break;
          }
        }
        in.close();
        out.close();

        String metadata = out.toString("UTF-8");  // StreamTitle='....';StreamURL='...';
        icyData.put("icy-metadata", metadata);

        java.util.regex.Pattern re = java.util.regex.Pattern.compile("^([a-zA-Z]+)=\\'([^\\']*)\\'$");
        for (String str: metadata.split(";")) {
          java.util.regex.Matcher m = re.matcher(str.trim());
          if ( m.find() ) icyData.put(m.group(1), new String(m.group(2).getBytes(),"UTF-8"));
        }

      } catch (Exception e) {
        icyData.put("ERROR", e.getMessage());
      }

      urlConn.disconnect();

    } catch (Exception e) {
      icyData.put("ERROR", e.getMessage());
    }

    return icyData;
  }

  //////////////////////////////////////////////////////////////////////

}
