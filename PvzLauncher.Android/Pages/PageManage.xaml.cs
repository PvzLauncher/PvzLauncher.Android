using PvzLauncher.Android.Controls;

namespace PvzLauncher.Android.Pages;

public partial class PageManage : ContentPage
{
	public PageManage()
	{
		InitializeComponent();
        Loaded += ((s, e) =>
        {
            RootList.Clear();
            RootList.Add(new GameDetail
            {
                Icon = "icon.png",
                Title = "Test",
                Notes = "Test",
                TargetPage = "mdt"
            });
        });
    }


    private void ImportButton_Clicked(object sender, EventArgs e)
    {
		Shell.Current.GoToAsync("import");
    }
}