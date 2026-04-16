using PvzLauncher.Android.JsonConfigs;

namespace PvzLauncher.Android.Pages;

public partial class PageDownloadDetail : ContentPage
{
    public required DownloadConfig Config { get; set; }



    public PageDownloadDetail()
	{
		InitializeComponent();
	}

    private void DownloadButton_Clicked(object sender, EventArgs e)
    {
		Shell.Current.GoToAsync("task");
    }
}