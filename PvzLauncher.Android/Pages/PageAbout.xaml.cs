using Microsoft.Maui.Controls.Internals;
using PvzLauncher.Android.Classes;
using PvzLauncher.Android.Utils;
using System.Windows.Input;

namespace PvzLauncher.Android.Pages;

public partial class PageAbout : ContentPage
{
    public ICommand TapCommand => new Command<string>(async (url) => await Launcher.OpenAsync(url));
    public PageAbout()
	{
		InitializeComponent();
        BindingContext = this;
        VersionBlock.Text = AppGlobals.Version;
        EULAView.MarkdownText = AppGlobals.EULAString;
	}

    private async void Button_Clicked(object sender, EventArgs e)
    {

        button_CheckUpdate.IsEnabled = false;
        var index = await Updater.GetUpdateIndex();

        if (index.Version == AppGlobals.Version)//无需更新
        {
            await DisplayAlertAsync("无需更新", $"你使用的是最新版 \"{AppGlobals.Version}\"", "确定");
            button_CheckUpdate.IsEnabled = true;
            return;
        }

        var result = await DisplayAlertAsync($"你可以更新至: {index.Version}", "你现在可以更新", "取消", "更新");

        if(result == true)
        {

        }






        button_CheckUpdate.IsEnabled = true;



    }
}