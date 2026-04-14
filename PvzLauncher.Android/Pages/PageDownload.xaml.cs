using PvzLauncher.Android.Controls;

namespace PvzLauncher.Android.Pages;

public partial class PageDownload : ContentPage
{
	public PageDownload()
	{
		InitializeComponent();
		Loaded += ((s, e) =>
		{
			RootList.Add(new GameDetail
			{
				Icon="icon.png",
				Title="Test",
				Notes="Test"
			});
		});
	}

    private void TaskButton_Clicked(object sender, EventArgs e)
    {
		Shell.Current.GoToAsync("task");
    }
}