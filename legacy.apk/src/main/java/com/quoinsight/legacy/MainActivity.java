package com.quoinsight.legacy;
/*
  https://abhiandroid.com/ui/calendarview
  https://developer.android.com/reference/android/widget/CalendarView
  https://developer.android.com/reference/android/widget/DatePicker
*/

public class MainActivity extends android.app.Activity {

  //////////////////////////////////////////////////////////////////////

/*
  static final public boolean isLeapMonth(android.icu.util.Calendar chineseCalendar) {
    android.icu.util.Calendar chineseCalendar0 = android.icu.util.Calendar.getInstance(
      new android.icu.util.ULocale("zh_CN@calendar=chinese")
    );  chineseCalendar0.setTimeInMillis(chineseCalendar.getTimeInMillis() - (long)(30.0*24.0*60.0*60000.0));
       // chineseCalendar0.add(java.util.Calendar.DAY_OF_MONTH, -30);
    int lastMonth = chineseCalendar0.get(java.util.Calendar.MONTH);
    int thisMonth = chineseCalendar.get(java.util.Calendar.MONTH);
    return (thisMonth==lastMonth);
  }
*/

  static final public String getChineseDateStr1(java.util.Date date) {
    final String[] dayArr = new String[] { "初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十", "十一", "十二", "十三", "十四",
       "十五", "十六", "十七", "十八", "十九", "二十", "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十", "卅一"
      }, monthArr = new String[] { "正", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"
      // }, hourArr = new String[] { "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥", "子" }
      }, hourArr = new String[] { "子zǐ", "丑chǒu", "寅yín", "卯mǎo", "辰chén", "巳sì", "午wǔ", "未wèi", "申shēn", "酉yǒu", "戌xū", "亥hài", "子zǐ" }
    ;
    String debug = "";
    try {
      java.util.Calendar cal = java.util.Calendar.getInstance();  cal.setTime(date);
      ChineseCalendar c = new ChineseCalendar();
        c.setGregorian(cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH)+1, cal.get(java.util.Calendar.DAY_OF_MONTH));
        c.computeChineseFields();
        c.computeSolarTerms();
      String dateStr = ( (c.chineseMonth<0) ? "闰" : "" )
        + monthArr[Math.abs(c.chineseMonth)-1] + "月" // MONTH==0..11
        + dayArr[c.chineseDate-1] // DAY_OF_MONTH==1..31
        + "⁄" + c.daysInChineseMonth(c.chineseYear, c.chineseMonth)
        // + hourArr[(int)(cal.get(java.util.Calendar.HOUR_OF_DAY)+1)/2] + "时" // HOUR_OF_DAY==0..23
        ;
      return dateStr;
    } catch(Exception e) {
      // some devices or versions may not support this
      // commonGui.writeMessage(MainActivity.this, "MainActivity.getChineseDateStr1", debug + "::" + e.getMessage() );
    }
    return "<ChineseDateUnavailable/>";
  }

  static final public String getChineseDateStr(java.util.Date date) {
    /*
      [BUG?] https://www.v2ex.com/t/505601
      原生安卓的农历显示居然是错的？
      2018-11-07 == 农历九月【大】三十?? [android=>]十月【大】初一?? 
      2018-12-06 == 农历十月【小】廿九?? [android=>] 十月【大】三十??
      农历有时可能出现两个大月，也可以连续出现两个小月 

      节气和朔望的时间计算以东经120度，中国标准时间为准
      清代《癸卯元历》 --> 《中华民国历书》 --> 《新法天文夏历》 使用北京地方(东经116°23')平太阳时进行推算
        --> 《译天历》 改用东经120°平时为历算子午线 --> 西元1969年至今开始使用《紫金历》

      [android.icu.util.ChineseCalendar]
      * <p>All astronomical computations are performed with respect to a time
      * zone of GMT+8:00 and a longitude of 120 degrees east.  Although some
      * calendars implement a historically more accurate convention of using
      * Beijing's local longitude (116 degrees 25 minutes east) and time zone
      * (GMT+7:45:40) for dates before 1929, we do not implement this here.

    */
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
      Class<?> ChineseCalendar = Class.forName("android.icu.util.ChineseCalendar");
      Object chineseCalendar = ChineseCalendar.getConstructor(java.util.Date.class).newInstance(date);

      java.lang.reflect.Method set = chineseCalendar.getClass().getMethod("set", int.class, int.class, int.class);
      java.lang.reflect.Method get = chineseCalendar.getClass().getMethod("get", int.class);
      java.lang.reflect.Method getActualMaximum = chineseCalendar.getClass().getMethod("getActualMaximum", int.class);
      //java.lang.reflect.Field IS_LEAP_MONTH = chineseCalendar.getClass().getField("IS_LEAP_MONTH");
        // notApplicable for IS_LEAP_MONTH ==> .getDeclaredField(<notForInheritedFields>);  <field>.setAccessible(true);

      // set.invoke(chineseCalendar, year, month, dayOfMonth); // year, month, dayOfMonth 此皆为农历的年月日
      String dateStr = ( ((Integer)get.invoke(chineseCalendar, Calendar.getDeclaredField("IS_LEAP_MONTH").get(null))==1) ? "闰" : "" )
        + monthArr[(Integer)get.invoke(chineseCalendar, java.util.Calendar.MONTH)] + "月" // MONTH==0..11
        + dayArr[(Integer)get.invoke(chineseCalendar, java.util.Calendar.DAY_OF_MONTH)-1] // DAY_OF_MONTH==1..31
        + "⁄" + (Integer)getActualMaximum.invoke(chineseCalendar, java.util.Calendar.DAY_OF_MONTH)
        ;

      //! above is to avoid the java.lang.NoClassDefFoundError at runtime !

    /*
      //android.icu.util.Calendar chineseCalendar = android.icu.util.Calendar.getInstance(
      //  new android.icu.util.ULocale("zh_CN@calendar=chinese")  // android.icu.util.ChineseCalendar.getInstance();
      //);  chineseCalendar.set(year, month, dayOfMonth); // year, month, dayOfMonth 此皆为农历的年月日

      android.icu.util.Calendar chineseCalendar = android.icu.util.Calendar.getInstance(
        new android.icu.util.ULocale("zh_CN@calendar=chinese")
      );  chineseCalendar.setTimeInMillis(date.getTime());  // System.currentTimeMillis()

      // C:\Data\adm\mobile\Android\apk\_src\icu-a7aed8f-android_icu4j-src-main-java-android-icu-util

      String dateStr = ( chineseCalendar.get(android.icu.util.Calendar.IS_LEAP_MONTH)==1 ? "闰" : "" )
        + monthArr[chineseCalendar.get(java.util.Calendar.MONTH)] + "月" // MONTH==0..11
        + dayArr[chineseCalendar.get(java.util.Calendar.DAY_OF_MONTH)-1] // DAY_OF_MONTH==1..31
        + "⁄" + chineseCalendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH)
        // + hourArr[(int)(chineseCalendar.get(java.util.Calendar.HOUR_OF_DAY)+1)/2] + "时" // HOUR_OF_DAY==0..23
        ; // https://www.ntu.edu.sg/home/ehchua/programming/java/DateTimeCalendar.html

      // https://developer.android.com/reference/android/icu/text/SimpleDateFormat
      //android.icu.text.SimpleDateFormat simpleDateFormat
      //  = new android.icu.text.SimpleDateFormat("UU年 MMM", new android.icu.util.ULocale("zh_CN@calendar=chinese"));
      //dateStr = simpleDateFormat.format(chineseCalendar)
      //  + dayArr[chineseCalendar.get(java.util.Calendar.DAY_OF_MONTH)-1]
      //  + "⁄" + chineseCalendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH)
      //  + " #" + String.valueOf(chineseCalendar.get(android.icu.util.Calendar.IS_LEAP_MONTH)) // Integer.toString(i)
      //  ;
    */

      return dateStr;
    } catch(Exception e) {
      // some devices or versions may not support this
      // return debug + "::" + e.getMessage();
    }
    return "<ChineseDateUnavailable/>";
  }

  //////////////////////////////////////////////////////////////////////

  static final public void quit(final android.app.Activity parentActivity) {
    android.app.AlertDialog.Builder alrt = new android.app.AlertDialog.Builder((android.content.Context)parentActivity);
    alrt.setMessage("Are you sure?").setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener() {
      @Override public void onClick(android.content.DialogInterface dialog, int which) {
        /*
        switch (which) {
          case android.content.DialogInterface.BUTTON_POSITIVE:
            //Yes button clicked
            break;
        }
        */

        parentActivity.finishAffinity();
        //finishAndRemoveTask();

        /*
          https://stackoverflow.com/questions/22166282/close-application-and-remove-from-recent-apps
          Note: this won't address the availability of "force stop" in the application info.
            Android allows you to force stop an application even if it does not have any processes running.
            Force stop puts the package into a specific stopped state, where it can't receive broadcast events.
        */

        // System.exit(0);
      }
    }).setNegativeButton("No", null).show();
  }

  //////////////////////////////////////////////////////////////////////

  // ⋮OptionsMenu vs. ≡NavigationDrawer
  private static final int NEW_MENU_ID=android.view.Menu.FIRST+1;
  @Override public boolean onCreateOptionsMenu(android.view.Menu menu) {
    super.onCreateOptionsMenu(menu);
    menu.add(0, 88, 0, "\uD83D\uDD90 About");
    menu.add(0, 99, 0, "\uD83D\uDEAA Quit");
    return true;
  }
  @Override public boolean onOptionsItemSelected(android.view.MenuItem item) {
    try {
      switch (item.getItemId()) {
        case 88:
          startActivity(new android.content.Intent(this.getApplicationContext(), WebViewActivity.class));
          return true;
        case 99:
          quit(this);
          return true;
        default:
          break;
      }
    } catch(Exception e) {
      commonGui.writeMessage(this, "onOptionsItemSelected", e.getMessage());
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////

  @Override protected void onCreate(android.os.Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //if ( android.os.Build.VERSION.SDK_INT > 19 ) {
    //  commonGui.writeMessage(this, "MainActivity.setContentView", "android.os.Build.VERSION.SDK_INT=" + String.valueOf(android.os.Build.VERSION.SDK_INT));
    //  return;
    //}

    setContentView(R.layout.mainactivity);

    try {

      android.widget.TextView txt1 = (android.widget.TextView) findViewById(R.id.txt1);;
        txt1.setGravity(android.view.Gravity.CENTER_HORIZONTAL);  // txt1.setText("Hello world!\n[" + commonUtil.getDateStr("yyyy-MM-dd HH:mm:ss") + "]");
        txt1.setText(android.text.Html.fromHtml(
          "Current Date: <br><small><small>[" + commonUtil.getDateStr("yyyy-MM-dd HH:mm:ss") + "]</small></small>"
            + "<br><font size='1.75em'>" + commonUtil.getChineseDateStr1() + "</font>"
        )); // Current Date:\n[2020-01-09 十二月十五⁄30巳时 09:06:21] --> Hello world\n[2020-01-09 09:06:21]\n十二月十五⁄30巳时

      android.widget.CalendarView simpleCalendar = (android.widget.CalendarView) findViewById(R.id.simpleCalendar);
        simpleCalendar.setOnDateChangeListener(new android.widget.CalendarView.OnDateChangeListener() {
          @Override public void onSelectedDayChange(android.widget.CalendarView view, int year, int month, int dayOfMonth) {
      //android.widget.DatePicker simpleCalendar = (android.widget.DatePicker) findViewById(R.id.simpleCalendar);
        //java.util.Calendar cal = java.util.Calendar.getInstance();
        //simpleCalendar.init(cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH), cal.get(java.util.Calendar.DAY_OF_MONTH),
        //  new DatePicker.OnDateChangedListener() {
        //    @Override public void onDateChanged(DatePicker view, int year, int month, int dayOfMonth) {
              String dateString = dayOfMonth + "/" + month + "/" + year;
              // java.util.Date date = new java.util.Date(year, month, dayOfMonth); // deprecated: As of JDK version 1.1, replaced by Calendar.set()

              java.util.Calendar cal = java.util.Calendar.getInstance(); cal.set(year, month, dayOfMonth);
              java.util.Date date = cal.getTime();
              dateString = (new java.text.SimpleDateFormat(
                "dd-MMM-yyyy", java.util.Locale.getDefault()
              )).format(date) +  " " + getChineseDateStr1(date);

              // display the selected date by using a toast
              android.widget.Toast.makeText(getApplicationContext(), dateString, android.widget.Toast.LENGTH_SHORT).show();
            }
          }
        );

      findViewById(R.id.button9).setOnClickListener( // --> .\src\main\res\layout\otheractivity.xml
        new android.view.View.OnClickListener() {
          public void onClick(android.view.View v) {
            MainActivity.this.finishAffinity();
            //finishAndRemoveTask();
          }
        }
      );

     /*
      android.widget.TextView txt2 = (android.widget.TextView) findViewById(R.id.txt2);  // --> .\src\main\res\layout\otheractivity.xml
        txt2.setLinksClickable(true);  // do not setAutoLinkMask !! txt2.setAutoLinkMask(android.text.util.Linkify.ALL);
        txt2.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
        txt2.setText(android.text.Html.fromHtml(txt2.getText().toString()));
     */

      android.widget.TextView txt9 = (android.widget.TextView) findViewById(R.id.txt9);  // --> .\src\main\res\layout\otheractivity.xml
        txt9.setLinksClickable(true);  // do not setAutoLinkMask !! txt9.setAutoLinkMask(android.text.util.Linkify.ALL);
        txt9.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
        txt9.setText(android.text.Html.fromHtml(txt9.getText().toString()));
        txt9.setText(android.text.Html.fromHtml(
          " [ <A href='https://github.com/QuoInsight/legacy.apk'>src</A> ] "
           + " [ <A href='https://github.com/QuoInsight/legacy.apk/raw/master/bin/quoinsight.apk'>apk</A> ] "
             + " [ <A href='https://play.google.com/store/apps/details?id=com.quoinsight.legacy'>store</A> ] "
            + " [ <A href='https://sites.google.com/site/quoinsight/home/legacy-apk'>about</A> ] "
        ));


    } catch(Exception e) {

      commonGui.writeMessage(this, "CalendarActivity.findViewById", e.getMessage());
      return;

    }

  }

}
