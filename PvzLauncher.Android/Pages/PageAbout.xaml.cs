using PvzLauncher.Android.Utils;

namespace PvzLauncher.Android.Pages;

public partial class PageAbout : ContentPage
{
	public PageAbout()
	{
		InitializeComponent();
		VersionBlock.Text = Versioning.AppVersion;
	}
}