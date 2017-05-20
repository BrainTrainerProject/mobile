﻿using Quobject.SocketIoClientDotNet.Client;
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
		public MainPage()
		{
			InitializeComponent();
            ConnectToSocket();
		}

        // https://github.com/Quobject/SocketIoClientDotNet/
        private void ConnectToSocket()
        {
            var socket = IO.Socket("ws://localhost:8080");

            socket.On(Socket.EVENT_CONNECT, () =>
            {
                Debug.WriteLine("Connect");
            });

            socket.On(Socket.EVENT_MESSAGE, (data) =>
            {
                Debug.WriteLine(data);
            });
        }
	}
}
