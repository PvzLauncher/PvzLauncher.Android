using PvzLauncher.Android.JsonConfigs;

namespace PvzLauncher.Android.Pages;

public partial class PageManangeDetail : ContentPage
{
    public required GameConfig Config { get; set; }

    public PageManangeDetail()
	{
		InitializeComponent();
	}

    private void SetLaunchButton_Clicked(object sender, EventArgs e)
    {
        Shell.Current.SendBackButtonPressed();
    }
}