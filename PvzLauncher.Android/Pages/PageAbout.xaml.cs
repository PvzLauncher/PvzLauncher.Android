using Microsoft.Maui.Controls.Internals;
using PvzLauncher.Android.Classes;
using PvzLauncher.Android.Utils;

namespace PvzLauncher.Android.Pages;

public partial class PageAbout : ContentPage
{
	public PageAbout()
	{
		InitializeComponent();
		VersionBlock.Text = AppGlobals.Version;
	}

    private async Task Button_Clicked(object sender, EventArgs e)
    {
		var index = await Updater.GetUpdateIndex();

		if (index.Version == AppGlobals.Version)//无需更新
		{
			await DisplayAlertAsync("无需更新", $"你使用的是最新版 \"{AppGlobals.Version}\"", "确定");
			return;
		}



    }
}