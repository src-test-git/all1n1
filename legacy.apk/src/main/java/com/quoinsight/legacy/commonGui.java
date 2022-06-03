package com.quoinsight.legacy;

public class commonGui {

  //////////////////////////////////////////////////////////////////////

  static final public void writeMessage(android.content.Context parentContext, String tag, String msg, String...args) {  // varargs
    android.widget.Toast.makeText(
      parentContext, tag + ": " +  msg,
        android.widget.Toast.LENGTH_LONG
    ).show();  // .setDuration(int duration)
    //android.util.Log.e(tag, msg);
    return;
  }

  static final public void msgBox(android.content.Context parentContext, String title, String msg) {
    android.app.AlertDialog.Builder alrt = new android.app.AlertDialog.Builder(parentContext);
    alrt.setTitle(title).setMessage(msg).setCancelable(false).setPositiveButton("OK", null).show();
  }

  //////////////////////////////////////////////////////////////////////

}
