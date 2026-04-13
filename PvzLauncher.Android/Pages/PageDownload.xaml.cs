namespace PvzLauncher.Android.Pages;

public partial class PageDownload : ContentPage
{
	public PageDownload()
	{
		InitializeComponent();
	}

    private void TaskButton_Clicked(object sender, EventArgs e)
    {
		Shell.Current.GoToAsync("task");
    }
}