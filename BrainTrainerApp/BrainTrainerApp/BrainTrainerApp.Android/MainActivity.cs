using System;

using Android.App;
using Android.Content.PM;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.OS;
using Plugin.LocalNotifications;
using Android.Content;

using Android.Gms.Common;

using Firebase.Messaging;
using Firebase.Iid;
using Android.Util;
using Android.Webkit;

namespace BrainTrainerApp.Droid
{
	[Activity (Label = "BrainTrainerApp", Icon = "@drawable/braintrainer_mobile_icon", Theme="@style/MainTheme", MainLauncher = true, ConfigurationChanges = ConfigChanges.ScreenSize | ConfigChanges.Orientation)]
	public class MainActivity : global::Xamarin.Forms.Platform.Android.FormsAppCompatActivity
	{
        const string TAG = "MainActivity";

        private WebView webview;

        protected override void OnCreate(Bundle bundle)
        {
            TabLayoutResource = Resource.Layout.Tabbar;
            ToolbarResource = Resource.Layout.Toolbar;

            base.OnCreate(bundle);

            global::Xamarin.Forms.Forms.Init(this, bundle);
            //LoadApplication(new TestApp.App());
            SetContentView(Resource.Layout.Main);

            webview = FindViewById<WebView>(Resource.Id.webview);
            webview.Settings.JavaScriptEnabled = true;
            webview.LoadUrl("https://braintrainer.herokuapp.com");

            if (Intent.Extras != null)
            {
                foreach (var key in Intent.Extras.KeySet())
                {
                    var value = Intent.Extras.GetString(key);
                    Log.Debug(TAG, "Key: {0} Value: {1}", key, value);
                }
            }
            IsPlayServicesAvailable();
        }

        public bool IsPlayServicesAvailable()
        {
            int resultCode = GoogleApiAvailability.Instance.IsGooglePlayServicesAvailable(this);
            if (resultCode != ConnectionResult.Success)
            {
                if (GoogleApiAvailability.Instance.IsUserResolvableError(resultCode))
                {
                    Log.Debug(TAG, GoogleApiAvailability.Instance.GetErrorString(resultCode));
                }
                else
                {
                    Log.Debug(TAG, "This device is not supported");
                    Finish();
                }
                return false;
            }
            else
            {
                Log.Debug(TAG, "Google Play Services is available.");
                return true;
            }
        }
    }
}

