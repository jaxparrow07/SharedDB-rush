package com.jaxparrow.shareddb;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.errors.YailRuntimeError;
import com.google.appinventor.components.runtime.util.TextViewUtil;

import android.content.Context;
import android.app.Activity;
import android.content.SharedPreferences;

import android.content.pm.PackageManager.NameNotFoundException;

public class SharedDB extends AndroidNonvisibleComponent {

  private Activity activity;
  public Context con;
  private Context context;
  public String nmspace = "SharedDB";

  public SharedDB(ComponentContainer container) {
    super(container.$form());
    this.context=container.$context();
  }

  @SimpleProperty(description = "Set the namespace for current app")
  public void Namespace(String str){
        this.nmspace = str;
  }

  @SimpleProperty(description = "Returns the namespace for current app")
    public String Namespace(){
        return this.nmspace;
  }

  @SimpleEvent
  public void ErrorOccured(String packagename,String namespace){
    EventDispatcher.dispatchEvent(this,"ErrorOccured",packagename,namespace);
  }


  @SimpleFunction(description = "Stores the value using the tag by the Namespace")
  public void StoreSharedValue(String tag, String value) {


    SharedPreferences prefs = context.getSharedPreferences(nmspace,Context.MODE_WORLD_READABLE);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putString(tag,value);
    editor.commit();

  }

  @SimpleFunction(description = "Returns the value using the tag by the Namespace")
  public String GetSharedValue(String tag, String valueIfTagNotThere) {

    SharedPreferences pref = context.getSharedPreferences(nmspace, Context.MODE_PRIVATE);
    String data = pref.getString(tag, valueIfTagNotThere);
    return data;
    
  }

  @SimpleFunction(description = "Returns the value in another package using the tag by the Namespace." +
                                "Fires Error Occured event if any error occurs like if the Package is not found"+
                                "or Namespace not found")
  public String GetSharedValueByPackage(String packagename,String namespace,String tag, String valueIfTagNotThere) {




    try {
        con = context.createPackageContext(packagename, 0);
        SharedPreferences pref = con.getSharedPreferences(namespace, Context.MODE_PRIVATE);
        String data = pref.getString(tag, valueIfTagNotThere);
        return data;

    } catch (NameNotFoundException e) {
        ErrorOccured(packagename,namespace);
        return null;
       
    }
    
  }



}
