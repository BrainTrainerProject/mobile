using Plugin.LocalNotifications;
using Quobject.SocketIoClientDotNet.Client;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Xamarin.Forms;

namespace BrainTrainerApp
{
	public partial class MainPage : ContentPage
	{
        private WebView webview;
        private string token;

		public MainPage()
		{
			InitializeComponent();
            webview = this.FindByName<WebView>("WebView");
            webview.HorizontalOptions = LayoutOptions.Fill;
            webview.VerticalOptions = LayoutOptions.Fill;
            //ConnectToSocket();
		}

        protected override bool OnBackButtonPressed()
        {
            Debug.WriteLine(webview.Source);
            //webview.Eval("console.log(document.body.innerHTML)");
            if (webview.CanGoBack)
            {
                webview.GoBack();
                return true;
            }
            return base.OnBackButtonPressed();
        }

        // https://github.com/Quobject/SocketIoClientDotNet/
        private void ConnectToSocket()
        {
            var socket = IO.Socket("https://braintrainer.herokuapp.com/");
            var option = new IO.Options();
            var headers = new Dictionary<string, string>();
            headers.Add("Authorization", "Bearer " + token);
            option.ExtraHeaders = headers;
            IO.Socket("https://braintrainer.herokuapp.com/");

            socket.On(Socket.EVENT_CONNECT, () =>
            {
                Debug.WriteLine("Connect");
            });

            socket.On(Socket.EVENT_MESSAGE, (data) =>
            {
                LocalNotification();
                Debug.WriteLine(data);
            });
        }

        // https://github.com/edsnider/LocalNotificationsPlugin
        private void LocalNotification()
        {
            CrossLocalNotifications.Current.Show("title", "body");
            Debug.WriteLine("Done");
        }
	}
}
