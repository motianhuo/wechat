/**
 * Copyright 2015 Anthony Restaino

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 either express or implied. See the License for the specific language governing
 permissions and limitations under the License.
 */
package com.allenjuns.wechat.chatuidemo.runtimepermissions;

import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This abstract class should be used to create an if/else action that the PermissionsManager
 * can execute when the permissions you request are granted or denied. Simple use involves
 * creating an anonymous instance of it and passing that instance to the
 * requestPermissionsIfNecessaryForResult method. The result will be sent back to you as
 * either onGranted (all permissions have been granted), or onDenied (a required permission
 * has been denied). Ideally you put your functionality in the onGranted method and notify
 * the user what won't work in the onDenied method.
 */
public abstract class PermissionsResultAction {

  private static final String TAG = PermissionsResultAction.class.getSimpleName();
  private final Set<String> mPermissions = new HashSet<String>(1);
  private Looper mLooper = Looper.getMainLooper();

  /**
   * Default Constructor
   */
  public PermissionsResultAction() {}

  /**
   * Alternate Constructor. Pass the looper you wish the PermissionsResultAction
   * callbacks to be executed on if it is not the current Looper. For instance,
   * if you are making a permissions request from a background thread but wish the
   * callback to be on the UI thread, use this constructor to specify the UI Looper.
   *
   * @param looper the looper that the callbacks will be called using.
   */
  @SuppressWarnings("unused")
  public PermissionsResultAction(@NonNull Looper looper) {mLooper = looper;}

  /**
   * This method is called when ALL permissions that have been
   * requested have been granted by the user. In this method
   * you should put all your permissions sensitive code that can
   * only be executed with the required permissions.
   */
  public abstract void onGranted();

  /**
   * This method is called when a permission has been denied by
   * the user. It provides you with the permission that was denied
   * and will be executed on the Looper you pass to the constructor
   * of this class, or the Looper that this object was created on.
   *
   * @param permission the permission that was denied.
   */
  public abstract void onDenied(String permission);

  /**
   * This method is used to determine if a permission not
   * being present on the current Android platform should
   * affect whether the PermissionsResultAction should continue
   * listening for events. By default, it returns true and will
   * simply ignore the permission that did not exist. Usually this will
   * work fine since most new permissions are introduced to
   * restrict what was previously allowed without permission.
   * If that is not the case for your particular permission you
   * request, override this method and return false to result in the
   * Action being denied.
   *
   * @param permission the permission that doesn't exist on this
   *                   Android version
   * @return return true if the PermissionsResultAction should
   * ignore the lack of the permission and proceed with exection
   * or false if the PermissionsResultAction should treat the
   * absence of the permission on the API level as a denial.
   */
  @SuppressWarnings({"WeakerAccess", "SameReturnValue"})
  public synchronized boolean shouldIgnorePermissionNotFound(String permission) {
    Log.d(TAG, "Permission not found: " + permission);
    return true;
  }

  @SuppressWarnings("WeakerAccess")
  @CallSuper
  protected synchronized final boolean onResult(final @NonNull String permission, int result) {
    if (result == PackageManager.PERMISSION_GRANTED) {
      return onResult(permission, Permissions.GRANTED);
    } else {
      return onResult(permission, Permissions.DENIED);
    }

  }

  /**
   * This method is called when a particular permission has changed.
   * This method will be called for all permissions, so this method determines
   * if the permission affects the state or not and whether it can proceed with
   * calling onGranted or if onDenied should be called.
   *
   * @param permission the permission that changed.
   * @param result     the result for that permission.
   * @return this method returns true if its primary action has been completed
   * and it should be removed from the data structure holding a reference to it.
   */
  @SuppressWarnings("WeakerAccess")
  @CallSuper
  protected synchronized final boolean onResult(final @NonNull String permission, Permissions result) {
    mPermissions.remove(permission);
    if (result == Permissions.GRANTED) {
      if (mPermissions.isEmpty()) {
        new Handler(mLooper).post(new Runnable() {
          @Override
          public void run() {
            onGranted();
          }
        });
        return true;
      }
    } else if (result == Permissions.DENIED) {
      new Handler(mLooper).post(new Runnable() {
        @Override
        public void run() {
          onDenied(permission);
        }
      });
      return true;
    } else if (result == Permissions.NOT_FOUND) {
      if (shouldIgnorePermissionNotFound(permission)) {
        if (mPermissions.isEmpty()) {
          new Handler(mLooper).post(new Runnable() {
            @Override
            public void run() {
              onGranted();
            }
          });
          return true;
        }
      } else {
        new Handler(mLooper).post(new Runnable() {
          @Override
          public void run() {
            onDenied(permission);
          }
        });
        return true;
      }
    }
    return false;
  }

  /**
   * This method registers the PermissionsResultAction object for the specified permissions
   * so that it will know which permissions to look for changes to. The PermissionsResultAction
   * will then know to look out for changes to these permissions.
   *
   * @param perms the permissions to listen for
   */
  @SuppressWarnings("WeakerAccess")
  @CallSuper
  protected synchronized final void registerPermissions(@NonNull String[] perms) {
    Collections.addAll(mPermissions, perms);
  }
}